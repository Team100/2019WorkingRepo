import cv2
import numpy as np
import math


class Target:
    def __init__(self, pos, size, angle, poly, cnt):
        (self.x, self.y) = pos  # center point position (pixels)
        (self.w, self.h) = size  # width/height (pixels)
        self.angle = angle  # angle (degrees)
        self.rect = (pos, size, angle)
        self.poly = poly
        self.cnt = cnt


class TargetPair:
    def __init__(self, target1, target2):
        self.target1 = target1
        self.target2 = target2


class ShapeDetector:
    def __init__(self):
        pass

    def detect(self, c):
        peri = cv2.arcLength(c, True)
        approx = cv2.approxPolyDP(c, peri * 0.005, True)
        return (cv2.minAreaRect(approx), approx)


def process(frame, config):
    gray = np.int16(frame.copy())

    gray = gray[:, :, 1] - gray[:, :, 2]
    thresh = gray < 50
    gray[thresh] = 0
    thresh = gray > 80
    gray[thresh] = 255
    gray = np.uint8(gray)

    cnts = cv2.findContours(gray.copy(), cv2.RETR_EXTERNAL, cv2.CHAIN_APPROX_SIMPLE)[0]
    sd = ShapeDetector()

    targets = []

    # loop over the contours
    if cnts is None:
        return []
    for c in cnts:
        rectangle = sd.detect(c)
        if rectangle == False:
            continue

        ((pos, size, angle), poly) = rectangle
        (w, h) = size

        if w * h < 200:
            continue

        targets.append(Target(pos, size, angle, poly, c))

    yError = 30
    targetPairs = []
    for i in range(len(targets)):
        for j in range(i + 1, len(targets)):
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
                    if ratio < 3.5:
                        correctTargetPairs.append(i)
        else:
            if i.target2.angle > -80 - angleError and i.target2.angle < -80 + angleError:
                if i.target1.angle > -10 - angleError and i.target1.angle < -10 + angleError:
                    avgWidth = (i.target1.w + i.target2.w) / 2
                    ratio = abs(i.target1.x - i.target2.x) / avgWidth
                    if ratio < 3.5:
                        correctTargetPairs.append(i)
    targets = []
    for i in correctTargetPairs:
        targets.append(i.target1)
        targets.append(i.target2)
    return targets
