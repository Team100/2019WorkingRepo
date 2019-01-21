from collections import namedtuple
from copy import deepcopy
import cv2
from json import dumps as stringify_json, load as parse_json, JSONDecodeError
import logging
from networktables import NetworkTables
import numpy as np
import math
import pipeline
from time import sleep as wait, time
from watchdog.observers import Observer
from watcher import FileWatcher


# Update the configuration
def update_config(_):
    global config
    temp_cfg = deepcopy(config)
    try:
        config = parse_json(open("config.json"), object_hook=lambda d: namedtuple("Config", d.keys())(*d.values()))
    except JSONDecodeError:
        config = temp_cfg


# For conversions
RAD2DEG = 180 / math.pi

# View logs from NetworkTables
logging.basicConfig(level=logging.DEBUG)

# Parse config file
config = parse_json(open("config.json"), object_hook=lambda d: namedtuple("Config", d.keys())(*d.values()))

# Initialize config file watcher
observer = Observer()
observer.schedule(FileWatcher(update_config), path=".", recursive=False)
observer.start()

# Connect to NetworkTables
NetworkTables.initialize(server=config.network_tables)
wait(3)
table = NetworkTables.getTable("camera")

# Start connection
camera = cv2.VideoCapture(config.camera_port)

try:
    while True:
        # Get frame
        _, frame = camera.read()

        # Check for cubes
        targets = pipeline.process(frame, config)

        facing = []
        json_representation = []

        # Compute centers, angle to cube & distance
        for i, target in enumerate(targets):
            m1 = cv2.moments(target)

            # Get center x and y
            cx1 = int(m1["m10"] / m1["m00"])
            cy1 = int(m1["m01"] / m1["m00"])

            # Get bounding box
            x1, y1, w1, h1 = cv2.boundingRect(target)

            # Calculate rotated bounding box
            rect = cv2.minAreaRect(target)
            bounding_box = cv2.boxPoints(rect)
            bounding_box = np.int0(bounding_box)

            # Calculate tilt of retroreflective strip
            points1 = np.asarray(bounding_box).tolist()  # type: list

            # Draw rotated bounding box
            if config.display.bounding:
                cv2.drawContours(frame, [bounding_box], 0, (0, 0, 255), 2)

            if points1[3][1] - points1[0][1] > points1[1][1] - points1[0][1]:
                if points1[3][0] > points1[0][0]:
                    facing.append(1)
                else:
                    facing.append(-1)
            else:
                if points1[2][0] > points1[0][0]:
                    facing.append(1)
                else:
                    facing.append(-1)

            if config.display.debug:
                cv2.putText(frame, str(facing[i]), (cx1, cy1), cv2.FONT_HERSHEY_SIMPLEX, 0.5, (255, 255, 255), 1)
                cv2.putText(frame, "1", tuple(points1[0]), cv2.FONT_HERSHEY_SIMPLEX, 0.5, (255, 255, 255), 1)
                cv2.putText(frame, "2", tuple(points1[1]), cv2.FONT_HERSHEY_SIMPLEX, 0.5, (255, 255, 255), 1)
                cv2.putText(frame, "3", tuple(points1[2]), cv2.FONT_HERSHEY_SIMPLEX, 0.5, (255, 255, 255), 1)
                cv2.putText(frame, "4", tuple(points1[3]), cv2.FONT_HERSHEY_SIMPLEX, 0.5, (255, 255, 255), 1)

            if i % 2 == 0:
                continue

            x2, y2, w2, h2 = cv2.boundingRect(targets[i - 1])

            m2 = cv2.moments(targets[i - 1])
            cx2 = int(m2["m10"] / m2["m00"])
            cy2 = int(m2["m01"] / m2["m00"])

            cx = int((cx1 + cx2) / 2)
            cy = int((cy1 + cy2) / 2)

            if config.display.debug:
                cv2.circle(frame, (cx, cy), 2, (255, 0, 0), -1)

            # Calculate rotated bounding box
            rect = cv2.minAreaRect(targets[i - 1])
            box = cv2.boxPoints(rect)
            box = np.int0(box)

            # Calculate tilt of retroreflective strip
            points2 = np.asarray(box).tolist()  # type: list

            # Get angle
            att = round((cx - config.image_center) * config.horizontal_degrees_per_pixel, 4)

            # Get distance
            rad_height1t = (239.5 - y1) / 6 / 57.3
            rad_height1b = (239.5 - y1 - h1) / 6 / 57.3
            rad_height2t = (239.5 - y2) / 6 / 57.3
            rad_height2b = (239.5 - y2 - h2) / 6 / 57.3

            dt1 = (5 + 5 / 8) * (rad_height1t / (rad_height1t - rad_height1b))
            dt2 = (5 + 5 / 8) * (rad_height2t / (rad_height2t - rad_height2b))

            d1 = dt1 / math.tan(rad_height1t)
            d2 = dt2 / math.tan(rad_height2t)

            dtt = round(((d1 + d2) / 2) / 20.25 * 39 / 62.1 * 60, 4)

            # ##
            # ##  Begin calculating angle of plane
            # ##
            if facing[i] == -1:
                (nx1, ny1) = points1[2]
                (nx2, ny2) = points2[2]
            else:
                (nx1, ny1) = points1[2]
                (nx2, ny2) = points2[2]

            slope = (ny1 - ny2) / (nx1 - nx2)

            my = ny1
            v1 = (nx1 - nx2)
            v2 = (ny1 - ny2)
            v3 = 0
            if slope != 0:
                my = (slope * (319.5 + slope * 239.5) + (-slope * nx1 + ny1)) / (slope * slope + 1) - 2

            # Display debugging parallel lines
            if config.display.debug:
                cv2.line(frame, (int(nx1 * 0), int(my)), (int(nx2 * 0 + 640), int(my)), (255, 0, 0), 2)
                cv2.line(frame, (int(nx1), int(ny1)), (int(nx2), int(ny2)), (255, 255, 0), 2)

            my -= 239.5
            norm_theta = my / 6 / 57.3

            om1 = 0
            om2 = math.cos(norm_theta)
            om3 = math.sin(norm_theta)

            n11 = v2 * om3 - v3 * om2
            n12 = v3 * om1 - v1 * om3
            n13 = v1 * om2 - v2 * om1

            if facing[i] == -1:
                inner1 = max(max(points1[0][0], points1[1][0]), max(points1[2][0], points1[3][0]))
                inner2 = min(min(points2[0][0], points2[1][0]), min(points2[2][0], points2[3][0]))
                (nx1, ny1) = points1[0]
                (nx2, ny2) = points2[0]
            else:
                inner1 = min(min(points1[0][0], points1[1][0]), min(points1[2][0], points1[3][0]))
                inner2 = max(max(points2[0][0], points2[1][0]), max(points2[2][0], points2[3][0]))
                (nx1, ny1) = points1[0]
                (nx2, ny2) = points2[0]

            slope = (ny1 - ny2) / (nx1 - nx2)

            my = ny1
            v1 = (nx1 - nx2)
            v2 = (ny1 - ny2)
            v3 = 0
            if slope != 0:
                my = (slope * (319.5 + slope * 239.5) + (-slope * nx1 + ny1)) / (slope * slope + 1) - 2

            if config.display.debug:
                cv2.line(frame, (int(nx1 * 0), int(my)), (int(nx2 * 0 + 640), int(my)), (255, 0, 0), 2)
                cv2.line(frame, (int(nx1), int(ny1)), (int(nx2), int(ny2)), (255, 255, 0), 2)

            my -= 239.5
            norm_theta = my / 6 / 57.3

            om1 = 0
            om2 = math.cos(norm_theta)
            om3 = math.sin(norm_theta)

            n21 = v2 * om3 - v3 * om2
            n22 = v3 * om1 - v1 * om3
            n23 = v1 * om2 - v2 * om1

            ab1 = n12 * n23 - n13 * n22
            ab2 = n13 * n21 - n11 * n23

            inner1 -= 50
            inner2 -= 50
            width = inner1 - inner2
            if width > 0:
                width += 10
            else:
                width *= -1

            aop = (90 - abs(math.atan(ab1 / (ab2 + 0.000001)) / 3.1415 * 180)) * 2
            # ##
            # ## End calculating angle of plane
            # ##

            if config.display.debug:
                cv2.putText(frame, "Angle of Plane: {0}".format(aop), (16, 420), cv2.FONT_HERSHEY_SIMPLEX, 0.5,
                            (255, 255, 255), 1)
                cv2.putText(frame, "Angle: {0}".format(att), (16, 440), cv2.FONT_HERSHEY_SIMPLEX, 0.5,
                            (255, 255, 255), 1)
                cv2.putText(frame, "Distance: {0}".format(dtt), (16, 460), cv2.FONT_HERSHEY_SIMPLEX, 0.5,
                            (255, 255, 255), 1)

            # Add to respective lists
            json_representation.append({
                "center": (cx, cy),
                "angle": att,
                "distance": dtt,
                "plane": aop,
                "bounding": bounding_box.tolist(),
                "timestamp": time()
            })

        # Post to NetworkTables
        table.putString("data", stringify_json(json_representation))

        # Check if should display
        if config.display.window:
            # Display stream
            cv2.imshow("Video Stream", frame)

            # Stop on 'q' press
            if cv2.waitKey(1) & 0xFF == ord('q'):
                cv2.destroyAllWindows()
                camera.release()
                observer.stop()
                break

# Stop on SIGINT/SIGKILL
except KeyboardInterrupt:
    cv2.destroyAllWindows()
    camera.release()
    observer.stop()

# Wait for observer to finish
observer.join()