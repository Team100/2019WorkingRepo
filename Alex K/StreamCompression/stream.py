from argparse import ArgumentParser
import cv2
import gzip
import socket

parser = ArgumentParser(description="Stream gzipped camera frames to driver station")
parser.add_argument("--host", help="Host to send on", default="localhost")
parser.add_argument("--port", help="Port to send to", type=int, default=5802)
args = parser.parse_args()

camera = cv2.VideoCapture(0)
if not camera.isOpened():
    raise ValueError("Camera is not attached or is in use")

s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
s.connect((args.host, args.port))


while True:
    try:
        _, frame = camera.read()

        _, buffer = cv2.imencode(".jpg", frame, [int(cv2.IMWRITE_JPEG_QUALITY), 25])
        compressed = gzip.compress(buffer)

        for i in range(0, len(compressed), 1024):
            s.send(compressed[i:i+1024])

        s.send(b"rst")

    except KeyboardInterrupt:
        s.close()
        camera.release()
        break
    except ConnectionResetError:
        s.close()
        camera.release()
        break
    except ConnectionAbortedError:
        s.close()
        camera.release()
        break
