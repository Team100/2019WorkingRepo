import cv2


# Process the frame
def process(frame, config):
    # Filter image by HSV
    hsv_filtered = cv2.inRange(cv2.cvtColor(frame, cv2.COLOR_BGR2HSV),
                               (config.hsv.hue[0], config.hsv.saturation[0], config.hsv.value[0]),
                               (config.hsv.hue[1], config.hsv.saturation[1], config.hsv.value[1])
                               )

    # Calculate contours of HSV filtered image
    mode = cv2.RETR_EXTERNAL if config.contours.external_contours else cv2.RETR_LIST
    _, contours, _ = cv2.findContours(hsv_filtered, mode, method=cv2.CHAIN_APPROX_SIMPLE)

    # Filter and calculate convex hull of contours
    filtered_and_hulled = []
    for contour in contours:
        x, y, w, h = cv2.boundingRect(contour)

        # Check width
        if w < config.contours.min_width or w > config.contours.max_width:
            continue

        # Check height
        if h < config.contours.min_height or w > config.contours.max_height:
            continue

        # Check area
        area = cv2.contourArea(contour)
        if area < config.contours.min_area:
            continue

        # Check perimeter
        if cv2.arcLength(contour, True) < config.contours.min_perimeter:
            continue

        hull = cv2.convexHull(contour)

        # Check solidity
        solid = 100 * area / cv2.contourArea(hull)
        if solid < config.contours.solidity[0] or solid > config.contours.solidity[1]:
            continue

        # Check number of vertices
        if len(contour) < config.contours.min_vertices or len(contour) > config.contours.max_vertices:
            continue

        # Check ratio of width to height
        ratio = float(w) / h
        if ratio < config.contours.min_ratio or ratio > config.contours.max_ratio:
            continue

        filtered_and_hulled.append(hull)

    return filtered_and_hulled
