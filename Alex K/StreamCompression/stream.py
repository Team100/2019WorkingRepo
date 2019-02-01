from argparse import ArgumentParser
import cv2
import gzip
import socket
import threading

CURRENT = True


def worker(r: socket.socket):
    global CURRENT
    while True:
        try:
            data = r.recv(1024)
            if data == b"switch":
                CURRENT = not CURRENT
        except ConnectionResetError:
            break
        except ConnectionAbortedError:
            break


parser = ArgumentParser(description="Stream gzipped camera frames to driver station")
parser.add_argument("--host", help="Host to send on", default="localhost")
parser.add_argument("--port", help="Port to send to", type=int, default=5802)
parser.add_argument("--width", help="Width of the stream", type=int, default=320)
parser.add_argument("--height", help="Height of the stream", type=int, default=240)
parser.add_argument("--dual", help="Use two cameras", action="store_true")
args = parser.parse_args()

camera0 = cv2.VideoCapture(0)
if not camera0.isOpened():
    raise ValueError("Camera 0 is not attached or is in use")

camera1 = None
if args.dual:
    camera1 = cv2.VideoCapture(1)
    if not camera1.isOpened():
        raise ValueError("Camera 1 is not attached or is in use")

s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
s.connect((args.host, args.port))

t = threading.Thread(target=worker, args=(s,))
t.start()

while True:
    try:
        if args.dual and CURRENT:
            _, frame = camera0.read()
        elif args.dual and not CURRENT:
            _, frame = camera1.read()
        else:
            _, frame = camera0.read()

        frame = cv2.resize(frame, (args.width, args.height))

        _, buffer = cv2.imencode(".jpg", frame, [int(cv2.IMWRITE_JPEG_QUALITY), 25])
        compressed = gzip.compress(buffer)

        for i in range(0, len(compressed), 1024):
            s.send(compressed[i:i+1024])

        s.send(b"rst")

    except KeyboardInterrupt:
        s.close()

        camera0.release()
        if args.dual:
            camera1.release()

        t.join()
        break
    except ConnectionResetError:
        s.close()

        camera0.release()
        if args.dual:
            camera1.release()

        t.join()
        break
    except ConnectionAbortedError:
        s.close()

        camera0.release()
        if args.dual:
            camera1.release()

        t.join()
        break
