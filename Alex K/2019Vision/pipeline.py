import cv2
import numpy as np
import math


class Target:
    def __init__(self, pos, size, angle, poly):
        (self.x, self.y) = pos  # center point position (pixels)
        (self.w, self.h) = size  # width/height (pixels)
        self.angle = angle  # angle (degrees)
        self.rect = (pos, size, angle)
        self.poly = poly


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
        return cv2.minAreaRect(approx), approx


def process(frame, config):
    grey = np.int16(frame.copy())

    grey = grey[:, :, 1] - grey[:, :, 2]
    thresh = grey < config.filtering.grey_threshold
    grey[thresh] = 0
    thresh = grey > config.filtering.grey_threshold
    grey[thresh] = 255
    grey = np.uint8(grey)

    if config.opencv_version == 4:
        cnts = cv2.findContours(grey.copy(), cv2.RETR_EXTERNAL, cv2.CHAIN_APPROX_SIMPLE)[0]
    else:
        cnts = cv2.findContours(grey.copy(), cv2.RETR_EXTERNAL, cv2.CHAIN_APPROX_SIMPLE)[1]
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

        if w * h < config.filtering.area:
            continue

        targets.append(Target(pos, size, angle, poly))
        
    target_pairs = []
    for i in range(len(targets)):
        for j in range(i + 1, len(targets)):
            if abs(targets[i].y - targets[j].y) < config.error.y:
                target_pairs.append(TargetPair(targets[i], targets[j]))

    correct_target_pairs = []
    for i in target_pairs:
        if i.target1.x < i.target2.x:
            if i.target1.angle > config.filtering.relative_angle.left - config.error.angle \
                    and i.target1.angle < config.filtering.relative_angle.left + config.error.angle:
                if i.target2.angle > config.filtering.relative_angle.right - config.error.angle \
                        and i.target2.angle < config.filtering.relative_angle.right + config.error.angle:
                    avg_width = (i.target1.w + i.target2.w) / 2
                    ratio = abs(i.target1.x - i.target2.x) / avg_width
                    if ratio < config.filtering.width_height_ratio:
                        correct_target_pairs.append(i)
        else:
            if i.target2.angle > config.filtering.relative_angle.left - config.error.angle \
                    and i.target2.angle < config.filtering.relative_angle.left + config.error.angle:
                if i.target1.angle > config.filtering.relative_angle.right - config.error.angle \
                        and i.target1.angle < config.filtering.relative_angle.right + config.error.angle:
                    avg_width = (i.target1.w + i.target2.w) / 2
                    ratio = abs(i.target1.x - i.target2.x) / avg_width
                    if ratio < config.filtering.width_height_ratio:
                        correct_target_pairs.append(i)
    targets = []
    for i in correct_target_pairs:
        targets.append(i.target1)
        targets.append(i.target2)
    return targets
