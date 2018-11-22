from collections import namedtuple
import cv2
from json import dumps as stringify_json, load as parse_json
import logging
from networktables import NetworkTables
import numpy as np
import pipeline
import pyrealsense2 as rs
from time import sleep as wait, time

# View logs from NetworkTables
logging.basicConfig(level=logging.DEBUG)

# Parse configuration json
config = parse_json(open("config.json"), object_hook=lambda d: namedtuple("Config", d.keys())(*d.values()))

# Configure Intel RealSense camera
camera_config = rs.config()
camera_config.enable_stream(rs.stream.depth, config.camera.height, config.camera.width,
                            rs.format.z16, config.camera.frame_rate)
camera_config.enable_stream(rs.stream.color, config.camera.height, config.camera.width,
                            rs.format.bgr8, config.camera.frame_rate)

# Start Intel RealSense camera
camera = rs.pipeline()
camera.start(camera_config)


# Connect to network tables
NetworkTables.initialize(server=config.network_tables)
wait(5)
table = NetworkTables.getTable("Camera")

while True:
    try:
        # Get depth & color frames
        frames = camera.wait_for_frames()
        depth = frames.get_depth_frame()
        color_frame = frames.get_color_frame()
        if not depth or not color_frame:
            continue

        # Convert to numpy array
        color = np.asanyarray(color_frame.get_data())

        # Check for cubes
        cubes = pipeline.process(color, config)
        cubes.sort(key=cv2.contourArea, reverse=True)

        bounding_boxes = []
        json_representation = []

        # Compute centers, angle to cube and distance
        for cube in cubes:
            m = cv2.moments(cube)

            # Get center x and y
            cx = int(m["m10"] / m["m00"])
            cy = int(m["m01"] / m["m00"])

            # Get bounding box
            x, y, w, h = cv2.boundingRect(cube)

            # Get angle and distance
            att = round((cx - config.image_center) * config.horizontal_distance_per_pixel, 4)
            dtt = depth.get_distance(cx, cy)

            # Add to respective lists
            bounding_boxes.append([x, y, w, h])
            json_representation.append({
                "center": (cx, cy),
                "bounding": (x, y, w, h),
                "angle": att,
                "distance": dtt,
                "timestamp": time()
            })

        # Post to NetworkTables
        table.putString("data", stringify_json(json_representation))

        # Display bounding boxes
        for box in bounding_boxes:
            cv2.rectangle(color, (box[0], box[1]),
                          (box[0] + box[2], box[1] + box[3]), (255, 0, 255), 2)

        # Display stream
        cv2.imshow("Video", color)

        # Stop on 'q' press
        if cv2.waitKey(1) & 0xFF == ord('q'):
            cv2.destroyAllWindows()
            camera.stop()
            break

    # Stop on SIGINT/SIGKILL
    except KeyboardInterrupt:
        cv2.destroyAllWindows()
        camera.stop()
        break
