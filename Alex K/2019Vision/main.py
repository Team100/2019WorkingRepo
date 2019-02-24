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
from os import system as execute
from sys import platform as os_platform

# Target size
TARGET_SIZE = 5 + 5 / 8


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
if config.network_tables:
    NetworkTables.initialize(server=config.network_tables)
    wait(3)
    table = NetworkTables.getTable("camera")

# Start connection
camera = cv2.VideoCapture(config.camera_port)
if not camera.isOpened():
    raise ValueError("No camera exists at port {camera}".format(camera=config.camera_port))


def draw(img, corners, imgpts):
    corner = tuple(corners[0].ravel())
    img = cv2.line(img, corner, tuple(imgpts[0].ravel()), (255, 0, 0), 5)
    img = cv2.line(img, corner, tuple(imgpts[1].ravel()), (0, 255, 0), 5)
    img = cv2.line(img, corner, tuple(imgpts[2].ravel()), (0, 0, 255), 5)
    return img


H = np.array([[5.00514201e-01, -9.13498146e-02, 1.63918922e+02],
              [-6.02599046e-02, 5.19652703e-01, 1.05971366e+02],
              [-1.48083400e-04, -2.88541810e-04, 1.00000000e+00]], dtype=np.double)

H = cv2.UMat(H)

H2 = np.array([[5.00514201e-01, -9.13498146e-02, 1.63918922e+02],
               [-6.02599046e-02, 5.19652703e-01, 1.05971366e+02],
               [-1.48083400e-04, -2.88541810e-04, 1.00000000e+00]], dtype=np.double)

HI = np.array([[2.20421542e+00, 1.80786366e-01, -3.88363174e+02],
               [1.83879137e-01, 2.09554202e+00, -2.56953105e+02],
               [3.92678880e-04, 6.35626237e-04, 1.00000000e+00]], dtype=np.double)

last3 = []

try:
    while True:
        # Get frame
        _, frame = camera.read()
        # 46, 11
        h, w = frame.shape[:2]

        # frame = cv2.warpPerspective(frame, H, (w, h)).get()

        # Check for target
        targets = pipeline.process(frame, config)

        frame = cv2.resize(frame, (int(1280 / 2), int(720 / 2)))
        h, w = frame.shape[:2]

        facing = []
        json_representation = []

        # Compute centers, angle to cube & distance
        for i, target in enumerate(targets):
            # Get center x and y
            cx1 = target.x
            cy1 = target.y

            # Get bounding box
            x1, y1, w1, h1 = cv2.boundingRect(target.poly)

            # Calculate rotated bounding box
            bounding_box = cv2.boxPoints(target.rect)
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
                cv2.putText(frame, str(facing[i]), (int(cx1), int(cy1)), cv2.FONT_HERSHEY_SIMPLEX, 0.5, (255, 255, 255),
                            1)
                cv2.putText(frame, "1", tuple(points1[0]), cv2.FONT_HERSHEY_SIMPLEX, 0.5, (255, 255, 255), 1)
                cv2.putText(frame, "2", tuple(points1[1]), cv2.FONT_HERSHEY_SIMPLEX, 0.5, (255, 255, 255), 1)
                cv2.putText(frame, "3", tuple(points1[2]), cv2.FONT_HERSHEY_SIMPLEX, 0.5, (255, 255, 255), 1)
                cv2.putText(frame, "4", tuple(points1[3]), cv2.FONT_HERSHEY_SIMPLEX, 0.5, (255, 255, 255), 1)

            if i % 2 == 0:
                continue

            x2, y2, w2, h2 = cv2.boundingRect(targets[i - 1].poly)

            cx2 = targets[i - 1].x
            cy2 = targets[i - 1].y

            cx = int((cx1 + cx2) / 2)
            cy = int((cy1 + cy2) / 2)

            if config.display.debug:
                cv2.circle(frame, (cx, cy), 2, (255, 0, 0), -1)

            # Calculate rotated bounding box
            box = cv2.boxPoints(targets[i - 1].rect)
            box = np.int0(box)

            # Calculate tilt of retroreflective strip
            points2 = np.asarray(box).tolist()  # type: list

            # Get angle
            pts1 = np.array([[cx, cy]], dtype='float32')
            pts1 = np.array([pts1])

            invtranpoints = cv2.perspectiveTransform(pts1, H2)

            cx = invtranpoints[0][0][0]
            cy = invtranpoints[0][0][1]
            att = round((cx - config.image_center.x) * config.degrees_per_pixel.horizontal, 4)

            # Get distance
            pts1 = np.array([[x1, y1]], dtype='float32')
            pts1 = np.array([pts1])

            invtranpoints = cv2.perspectiveTransform(pts1, H2)
            x1 = invtranpoints[0][0][0]
            y1 = invtranpoints[0][0][1]

            pts2 = np.array([[x2, y2]], dtype='float32')
            pts2 = np.array([pts2])

            invtranpoints = cv2.perspectiveTransform(pts2, H2)
            x2 = invtranpoints[0][0][0]
            y2 = invtranpoints[0][0][1]

            pts1 = np.array([[points1[0][0], points1[0][1]]], dtype='float32')
            pts1 = np.array([pts1])

            invtranpoints = cv2.perspectiveTransform(pts1, H2)
            tx1 = invtranpoints[0][0][0]
            ty1 = invtranpoints[0][0][1]

            pts2 = np.array([[points1[2][0], points1[2][1]]], dtype='float32')
            pts2 = np.array([pts2])

            invtranpoints = cv2.perspectiveTransform(pts2, H2)
            tx2 = invtranpoints[0][0][0]
            ty2 = invtranpoints[0][0][1]

            h1 = (tx1 - tx2) ** 2 + (ty1 - ty2) ** 2
            h1 = math.sqrt(h1 - 4)

            pts1 = np.array([[points2[0][0], points2[0][1]]], dtype='float32')
            pts1 = np.array([pts1])

            invtranpoints = cv2.perspectiveTransform(pts1, H2)
            tx1 = invtranpoints[0][0][0]
            ty1 = invtranpoints[0][0][1]

            pts2 = np.array([[points2[2][0], points2[2][1]]], dtype='float32')
            pts2 = np.array([pts2])

            invtranpoints = cv2.perspectiveTransform(pts2, H2)
            tx2 = invtranpoints[0][0][0]
            ty2 = invtranpoints[0][0][1]

            h2 = (tx1 - tx2) ** 2 + (ty1 - ty2) ** 2
            h2 = math.sqrt(h2 - 4)

            rad_height1t = (config.image_center.y - y1) * config.degrees_per_pixel.vertical
            rad_height1b = (config.image_center.y - y1 - h1) * config.degrees_per_pixel.vertical
            rad_height2t = (config.image_center.y - y2) * config.degrees_per_pixel.vertical
            rad_height2b = (config.image_center.y - y2 - h2) * config.degrees_per_pixel.vertical

            dt1 = TARGET_SIZE * (rad_height1t / (rad_height1t - rad_height1b))
            dt2 = TARGET_SIZE * (rad_height2t / (rad_height2t - rad_height2b))

            d1 = dt1 / math.tan(rad_height1t / RAD2DEG)
            d2 = abs(dt2 / math.tan(rad_height2t / RAD2DEG))

            dtt = round(((d1 + d2) / 2) * config.conversion_factors.distance, 4)

            # Fix division by zero errors
            try:
                # ##
                # ##  Begin calculating angle of plane
                # ##
                (nx1, ny1) = targets[i].top
                (nx2, ny2) = targets[i - 1].top

                # pts1 = np.array([[nx1, ny1]], dtype='float32')
                # pts1 = np.array([pts1])
                #
                ##print(pts1, HI)
                #
                # invtranpoints = cv2.perspectiveTransform(pts1, HI)
                # nx1 = invtranpoints[0][0][0]
                # ny1 = invtranpoints[0][0][1]
                #
                # pts2 = np.array([[nx2, ny2]], dtype='float32')
                # pts2 = np.array([pts2])
                #
                # invtranpoints = cv2.perspectiveTransform(pts2, HI)
                # nx2 = invtranpoints[0][0][0]
                # ny2 = invtranpoints[0][0][1]

                slope = (ny1 - ny2) / (nx1 - nx2)

                my = ny1
                v1 = (nx1 - nx2)
                v2 = (ny1 - ny2)
                v3 = 0

                if slope != 0:
                    my = (slope * (config.image_center.x + slope * config.image_center.y) + (-slope * nx1 + ny1)) / (
                            slope * slope + 1) - 2

                # Display debugging parallel lines
                if config.display.debug:
                    cv2.line(frame, (int(nx1 * 0), int(my)), (int(nx2 * 0 + 640), int(my)), (255, 0, 0), 2)
                    cv2.line(frame, (int(nx1), int(ny1)), (int(nx2), int(ny2)), (255, 255, 0), 2)

                my -= config.image_center.y
                norm_theta = my * config.degrees_per_pixel.vertical

                om1 = 0
                om2 = math.cos(norm_theta / RAD2DEG)
                om3 = math.sin(norm_theta / RAD2DEG)

                n11 = v2 * om3 - v3 * om2
                n12 = v3 * om1 - v1 * om3
                n13 = v1 * om2 - v2 * om1

                (nx1, ny1) = targets[i].bottom
                (nx2, ny2) = targets[i - 1].bottom

                # pts1 = np.array([[nx1, ny1]], dtype='float32')
                # pts1 = np.array([pts1])
                #
                ## print(pts1, HI)
                #
                # invtranpoints = cv2.perspectiveTransform(pts1, HI)
                # nx1 = invtranpoints[0][0][0]
                # ny1 = invtranpoints[0][0][1]
                #
                # pts2 = np.array([[nx2, ny2]], dtype='float32')
                # pts2 = np.array([pts2])
                #
                # invtranpoints = cv2.perspectiveTransform(pts2, HI)
                # nx2 = invtranpoints[0][0][0]
                # ny2 = invtranpoints[0][0][1]

                slope = (ny1 - ny2) / (nx1 - nx2)

                my = ny1
                v1 = (nx1 - nx2)
                v2 = (ny1 - ny2)
                v3 = 0
                if slope != 0:
                    my = (slope * (config.image_center.x + slope * config.image_center.y) + (-slope * nx1 + ny1)) / (
                            slope * slope + 1) - 2

                if config.display.debug:
                    cv2.line(frame, (int(nx1 * 0), int(my)), (int(nx2 * 0 + 640), int(my)), (255, 0, 0), 2)
                    cv2.line(frame, (int(nx1), int(ny1)), (int(nx2), int(ny2)), (255, 255, 0), 2)

                my -= config.image_center.y
                norm_theta = my * config.degrees_per_pixel.vertical

                om1 = 0
                om2 = math.cos(norm_theta / RAD2DEG)
                om3 = math.sin(norm_theta / RAD2DEG)

                n21 = v2 * om3 - v3 * om2
                n22 = v3 * om1 - v1 * om3
                n23 = v1 * om2 - v2 * om1

                ab1 = n12 * n23 - n13 * n22
                ab2 = n13 * n21 - n11 * n23

                temp_angle = math.atan(ab1 / (ab2 + 0.000001)) * RAD2DEG

                aop = (90 - abs(temp_angle)) * math.copysign(1, temp_angle)

                # ##
                # ## End calculating angle of plane
                # ##
            except:
                aop = 0
            if dtt > abs(config.camera_position_offset.height):
                dtt = math.sqrt(dtt ** 2 - config.camera_position_offset.height ** 2)

            x_1 = dtt * math.cos(att / RAD2DEG)
            y_1 = dtt * math.sin(att / RAD2DEG)

            x_1 += config.camera_position_offset.side

            att = math.atan2(y_1, x_1) * RAD2DEG
            if dtt > abs(config.camera_position_offset.side):
                dtt = math.sqrt(dtt ** 2 - config.camera_position_offset.side ** 2)

            avgaop = 0
            for i in last3:
                avgaop += i
            last3.append(aop)
            aop = avgaop / 4
            if len(last3) > 3:
                last3.pop(0)
            aop *= math.sqrt(dtt)
            aop += 23.5
            aop *= config.conversion_factors.angle

            # print(dtt, att, aop)

            if config.display.debug:
                cv2.putText(frame, "Angle of Plane: {0}".format(round(aop, 4)), (16, 310), cv2.FONT_HERSHEY_SIMPLEX,
                            0.5,
                            (255, 255, 255), 1)
                cv2.putText(frame, "Angle: {0}".format(round(att, 4)), (16, 330), cv2.FONT_HERSHEY_SIMPLEX, 0.5,
                            (255, 255, 255), 1)
                cv2.putText(frame, "Distance: {0}".format(round(dtt, 4)), (16, 350), cv2.FONT_HERSHEY_SIMPLEX, 0.5,
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
        # if config.network_tables:
        #   table.putString("data", stringify_json(json_representation))

        # Check if should display
        if config.display.window:
            # Display stream
            # frame = cv2.cvtColor(frame, cv2.COLOR_YUV2BGR)
            cv2.imshow("Video Stream", frame)

            if cv2.waitKey(1) & 0xFF == ord('e'):
                # Set exposure if MacOS
                if os_platform == "darwin":
                    # execute(
                    #    "uvcc --vendor 0x45e --product 0x779 set autoExposureMode 1 && uvcc --vendor 0x45e --product 0x779 "
                    #    "set absoluteExposureTime 5")
                    execute(
                        "uvcc --vendor 0x46d --product 0x843 set autoExposurePriority 1 && "
                        "uvcc --vendor 0x46d --product 0x843 set autoExposureMode 1 && "
                        "uvcc --vendor 0x46d --product 0x843 set absoluteExposureTime 10")

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
