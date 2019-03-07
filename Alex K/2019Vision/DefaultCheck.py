import cv2
import numpy as np

frame = cv2.imread("chessboard1.png")
frame = frame[0:360, 10:410]
#print(frame.shape[:2])
#frame = cv2.resize(frame, (int(640), int(480)))

gray = cv2.cvtColor(frame, cv2.COLOR_BGR2GRAY)

# Find the chess board corners
ret, corners = cv2.findChessboardCorners(gray, (7, 5), None)

objpoints = []  # 3d point in real world space
imgpoints = []  # 2d points in image plane.

# termination criteria
criteria = (cv2.TERM_CRITERIA_EPS + cv2.TERM_CRITERIA_MAX_ITER, 30, 0.001)

# prepare object points, like (0,0,0), (1,0,0), (2,0,0) ....,(6,5,0)
objp = np.zeros((6 * 7, 3), np.float32)
objp[:, :2] = np.mgrid[0:7, 0:6].T.reshape(-1, 2)
asdf = None

# If found, add object points, image points (after refining them)
if ret == True:
    objpoints.append(objp)

    corners2 = cv2.cornerSubPix(gray, corners, (11, 11), (-1, -1), criteria)
    imgpoints.append(corners2)
    objpoints.append(objp[0:corners2.shape[0]])
    asdf = imgpoints

    # h, status = cv2.findHomography(corners2, corners2)

    # Draw and display the corners
    frame = cv2.drawChessboardCorners(frame, (7, 6), corners2, ret)

    cv2.imwrite('calibresult.png', frame)

# Start connection
camera = cv2.VideoCapture(0)

while True:
    # Get frame
    _, frame = camera.read()
    #frame = cv2.imread("chessboard1.png")
    # 46, 11

    h, w = frame.shape[:2]

    gray = cv2.cvtColor(frame, cv2.COLOR_BGR2GRAY)

    # Find the chess board corners
    ret, corners = cv2.findChessboardCorners(gray, (7, 5), None)

    objpoints = []  # 3d point in real world space
    imgpoints = []  # 2d points in image plane.

    # termination criteria
    criteria = (cv2.TERM_CRITERIA_EPS + cv2.TERM_CRITERIA_MAX_ITER, 30, 0.001)

    # If found, add object points, image points (after refining them)
    if ret == True:
        # objpoints.append(objp)

        corners2 = cv2.cornerSubPix(gray, corners, (11, 11), (-1, -1), criteria)
        imgpoints.append(corners2)
        objpoints.append(objp[0:corners2.shape[0]])

        H, status = cv2.findHomography(imgpoints[0], asdf[0], cv2.RANSAC)
        HI, status = cv2.findHomography(asdf[0], imgpoints[0], cv2.RANSAC)
        #ret, mtx, dist, rvecs, tvecs = cv2.calibrateCamera(objpoints, imgpoints, gray.shape[::-1], None, None)

        # ret, mtx, dist, rvecs, tvecs = cv2.calibrateCamera(objpoints, imgpoints, gray.shape[::-1], None, None)
        # newcameramtx, roi = cv2.getOptimalNewCameraMatrix(mtx, dist, (w, h), 1, (w, h))
        # frame = cv2.undistort(frame, mtx, dist, None, newcameramtx)
        # Draw and display the corners
        frame = cv2.drawChessboardCorners(frame, (7, 6), corners2, ret)

        #h, w = frame.shape[:2]
        #newcameramtx, roi = cv2.getOptimalNewCameraMatrix(mtx, dist, (w, h), 1, (w, h))
        #print(mtx, dist)

        #mapx, mapy = cv2.initUndistortRectifyMap(mtx, dist, None, newcameramtx, (w, h), 5)
        #frame = cv2.remap(frame, mapx, mapy, cv2.INTER_LINEAR)

        # crop the image
        #x, y, w, h = roi
        #frame = frame[y:y + h, x:x + w]
        print(H, HI)

        frame = cv2.warpPerspective(frame, H, (w, h))
    frame = cv2.resize(frame, (int(1280 / 2), int(720 / 2)))

    cv2.imshow("Video Stream", frame)
    # Stop on 'q' press
    if cv2.waitKey(1) & 0xFF == ord('q'):
        cv2.destroyAllWindows()
        camera.release()
        break
