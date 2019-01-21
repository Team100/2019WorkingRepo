import cv2

print("Acquiring camera...")
camera = cv2.VideoCapture("http://raspberrypi.local:5802/?action=stream")
print("Camera acquired, running...")

while True:
    _, frame = camera.read()

    height, width, _ = frame.shape

    cv2.line(frame, (width//2, height//4), (width//2, (height//4)*3), (0, 0, 255), 2)
    cv2.line(frame, (width//4, height//2), ((width//4)*3, height//2), (0, 0, 255), 2)

    cv2.imshow("Camera Stream", frame)
    if cv2.waitKey(1) & 0xFF == ord('q'):
        cv2.destroyAllWindows()
        camera.release()
        break
