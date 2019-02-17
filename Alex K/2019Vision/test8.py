import cv2
import numpy as np
import math


class Target:
    def __init__(self, pos, size, angle, poly):
        (self.x, self.y) = pos  # center point position (pixels)
        (self.w, self.h) = size  # width/height (pixels)
        self.angle = angle  # angle (degrees)
        self.poly = poly


class TargetPair:
    def __init__(self, target1, target2):
        self.target1 = target1
        self.target2 = target2


class ShapeDetector:
    def __init__(self):
        pass

    def detect(self, c):
        # initialize the shape name and approximate the contour
        peri = cv2.arcLength(c, True)
        approx = cv2.approxPolyDP(c, peri * 0.005, True)
        # if the shape has 4 vertices, it is either a square or
        # a rectangle
        # if len(approx) == 4:
        # compute the bounding box of the contour and use the
        # bounding box to compute the aspect ratio

        return (cv2.minAreaRect(approx), approx)
        # return False


# def adjust_gamma(image, gamma=1.0):
#    # build a lookup table mapping the pixel values [0, 255] to
#    # their adjusted gamma values
#    invGamma = 1.0 / gamma
#    table = np.array([((i / 255.0) ** invGamma) * 255
#                      for i in np.arange(0, 256)]).astype("uint8")
#
#    # apply gamma correction using the lookup table
#    return cv2.LUT(image, table)


# lastSelectedTargetPair = False

cap = cv2.VideoCapture(1)
while (cap.isOpened()):
    ret, frame = cap.read()
    try:
        frame = cv2.resize(frame, None, fx=0.4, fy=0.4, interpolation=cv2.INTER_LINEAR)
        # frame = cv2.blur(frame,(5,5))
    except:
        break
    # frame = adjust_gamma(frame, 0.3)
    gray = np.int16(frame.copy())

    gray = gray[:, :, 1] - gray[:, :, 2]
    thresh = gray < 50
    gray[thresh] = 0
    thresh = gray > 80
    gray[thresh] = 255
    gray = np.uint8(gray)

    #gray = cv2.blur(gray, (3, 3))

    cnts = cv2.findContours(gray.copy(), cv2.RETR_EXTERNAL, cv2.CHAIN_APPROX_SIMPLE)[1]
    sd = ShapeDetector()

    targets = []

    # loop over the contours
    for c in cnts:
        # compute the center of the contour, then detect the name of the
        # shape using only the contour
        rectangle = sd.detect(c)
        if rectangle == False:
            continue

        ((pos, size, angle), poly) = rectangle
        (w, h) = size

        if w * h < 200:
            continue

        # print(shape)

        # multiply the contour (x, y)-coordinates by the resize ratio,
        # then draw the contours and the name of the shape on the image
        # c = c.astype("float")
        # c = c.astype("int")
        # cv2.drawContours(frame, [c], -1, (255, 0, 0), 1)
        # cv2.putText(frame, "rectangle", (cX, cY), cv2.FONT_HERSHEY_SIMPLEX, 0.5, (255, 255, 255), 2)
        targets.append(Target(pos, size, angle, poly))

    yError = 30
    targetPairs = []
    for i in range(len(targets)):
        for j in range(i + 1, len(targets)):
            # print(abs(targets[i].y - targets[j].y))
            if abs(targets[i].y - targets[j].y) < yError:
                targetPairs.append(TargetPair(targets[i], targets[j]))

    angleError = 30
    correctTargetPairs = []
    for i in targetPairs:
        if i.target1.x < i.target2.x:
            if i.target1.angle > -80 - angleError and i.target1.angle < -80 + angleError:
                if i.target2.angle > -10 - angleError and i.target2.angle < -10 + angleError:
                    avgWidth = (i.target1.w + i.target2.w) / 2
                    ratio = abs(i.target1.x - i.target2.x) / avgWidth
                    if ratio < 4:
                        correctTargetPairs.append(i)
        else:
            if i.target2.angle > -80 - angleError and i.target2.angle < -80 + angleError:
                if i.target1.angle > -10 - angleError and i.target1.angle < -10 + angleError:
                    avgWidth = (i.target1.w + i.target2.w) / 2
                    ratio = abs(i.target1.x - i.target2.x) / avgWidth
                    if ratio < 3.5:
                        correctTargetPairs.append(i)
        # if lastSelectedTargetPair == False:
        #    middleTargetPair = correctTargetPairs[0]
        #    for i in range(len(correctTargetPairs) - 1):
        #        midPoint1 = (correctTargetPairs[i + 1].target1.x + correctTargetPairs[i + 1].target2.x) / 2
        #        midPoint2 = (middleTargetPair.target1.x + middleTargetPair.target2.x) / 2
        #        if abs(midPoint1 - 511 / 2) < abs(midPoint2 - 511 / 2):
        #            middleTargetPair = correctTargetPairs[i + 1]
        # lastSelectedTargetPair = middleTargetPair
    for i in correctTargetPairs:
        box = cv2.boxPoints(((i.target1.x, i.target1.y), (i.target1.w, i.target1.h), i.target1.angle))
        box = np.int0(box)
        cv2.drawContours(frame, [box], 0, (0, 0, 255), 2)
        box = cv2.boxPoints(((i.target2.x, i.target2.y), (i.target2.w, i.target2.h), i.target2.angle))
        box = np.int0(box)
        cv2.drawContours(frame, [box], 0, (0, 0, 255), 2)

        bounding_box = cv2.boxPoints(((i.target1.x, i.target1.y), (i.target1.w, i.target1.h), i.target1.angle))
        bounding_box = np.int0(bounding_box)

        # Calculate tilt of retroreflective strip
        points1 = np.asarray(bounding_box).tolist()  # type: list

        bounding_box2 = cv2.boxPoints(((i.target2.x, i.target2.y), (i.target2.w, i.target2.h), i.target2.angle))
        bounding_box2 = np.int0(bounding_box2)

        # Calculate tilt of retroreflective strip
        points2 = np.asarray(bounding_box2).tolist()  # type: list

        # Get angle
        att = round(((i.target1.x + i.target2.x) / 2 - frame.shape[1]) * 0.1962, 4)

        # Get distance
        rad_height1t = (frame.shape[0]/2 - i.target1.y + i.target1.h / 2) / 6 / 57.3
        rad_height1b = (frame.shape[0]/2.5 - i.target1.y - i.target1.h / 2) / 6 / 57.3
        rad_height2t = (frame.shape[0]/2.5 - i.target2.y + i.target2.h / 2) / 6 / 57.3
        rad_height2b = (frame.shape[0]/2.5 - i.target2.y - i.target2.h / 2) / 6 / 57.3

        dt1 = (5 + 5 / 8) * (rad_height1t / (rad_height1t - rad_height1b))
        dt2 = (5 + 5 / 8) * (rad_height2t / (rad_height2t - rad_height2b))

        d1 = dt1 / math.tan(rad_height1t)
        d2 = dt2 / math.tan(rad_height2t)

        dtt = round(((d1 + d2) / 2) / 20.25 * 39 / 62.1 * 60, 4)

        # Fix division by zero errors
        try:
            # ##
            # ##  Begin calculating angle of plane
            # ##
            if points1[3][1] - points1[0][1] > points1[1][1] - points1[0][1]:
                if points1[3][0] > points1[0][0]:
                    facing = 1
                else:
                    facing = -1
            else:
                if points1[2][0] > points1[0][0]:
                    facing = 1
                else:
                    facing = -1

            if facing == -1:
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

            my -= 239.5
            norm_theta = my / 6 / 57.3

            om1 = 0
            om2 = math.cos(norm_theta)
            om3 = math.sin(norm_theta)

            n11 = v2 * om3 - v3 * om2
            n12 = v3 * om1 - v1 * om3
            n13 = v1 * om2 - v2 * om1

            # For conversions
            RAD2DEG = 180 / math.pi

            if facing == -1:
                inner1 = max(max(points1[0][0], points1[1][0]), max(points1[2][0], points1[3][0]))
                inner2 = min(min(points2[0][0], points2[1][0]), min(points2[2][0], points2[3][0]))
                (nx1, ny1) = points1[0]
                (nx2, ny2) = points2[0]

                na1 = 90 - abs(math.atan((points1[0][1] - points1[3][1]) / (points1[0][0] - points1[3][0])) * RAD2DEG)
                na2 = 90 - abs(math.atan((points2[0][1] - points2[1][1]) / (points2[0][0] - points2[1][0])) * RAD2DEG)
            else:
                inner1 = min(min(points1[0][0], points1[1][0]), min(points1[2][0], points1[3][0]))
                inner2 = max(max(points2[0][0], points2[1][0]), max(points2[2][0], points2[3][0]))
                (nx1, ny1) = points1[0]
                (nx2, ny2) = points2[0]

                na1 = 90 - abs(math.atan((points1[0][1] - points1[1][1]) / (points1[0][0] - points1[1][0])) * RAD2DEG)
                na2 = 90 - abs(math.atan((points2[0][1] - points2[3][1]) / (points2[0][0] - points2[3][0])) * RAD2DEG)
            slope = (ny1 - ny2) / (nx1 - nx2)

            my = ny1
            v1 = (nx1 - nx2)
            v2 = (ny1 - ny2)
            v3 = 0
            if slope != 0:
                my = (slope * (319.5 + slope * 239.5) + (-slope * nx1 + ny1)) / (slope * slope + 1) - 2

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

            aop = (90 - abs(math.atan(ab1 / (ab2 + 0.000001)) / 3.1415 * 180)) * 2 * 4 / 3
            # ##
            # ## End calculating angle of plane
            # ##
        except:
            aop = 0
        print(dtt/2.9/0.9375/1.08, att, aop)

    cv2.imshow('dsta', frame)
    # img = np.uint8(img)
    cv2.imshow('dst0', gray)
    # cv2.imshow('dst1', img[:, :, 1])
    # cv2.imshow('dst2', img[:, :, 2])
    if cv2.waitKey(1) & 0xFF == ord('q'):
        break

cap.release()
cv2.destroyAllWindows()
