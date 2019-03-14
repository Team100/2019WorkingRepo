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
parser.add_argument("--host", help="Host for server to run on", default="localhost")
parser.add_argument("--port", help="Port for server to run on", type=int, default=5802)
parser.add_argument("--width", help="Width of the stream", type=int, default=320)
parser.add_argument("--height", help="Height of the stream", type=int, default=240)
parser.add_argument("--dual", help="Use two cameras", action="store_true")
parser.add_argument("--ipv6", help="Use IPv6 over IPv4", action="store_true")
parser.add_argument("--camera1", help="Port the first camera is on", type=int, default=0)
parser.add_argument("--camera2", help="Port the second camera is on", type=int, default=1)
args = parser.parse_args()

camera0 = cv2.VideoCapture(args.camera1)
if not camera0.isOpened():
    raise ValueError("Camera 0 is not attached or is in use")

camera1 = None
if args.dual:
    camera1 = cv2.VideoCapture(args.camera2)
    if not camera1.isOpened():
        raise ValueError("Camera 1 is not attached or is in use")

socket_info = set(socket.getaddrinfo(args.host, args.port))
socket_address = None
for res in socket_info:
    af, _, _, _, server_address = res
    if (af == socket.AF_INET and not args.ipv6) or (af == socket.AF_INET6 and args.ipv6):
        socket_address = (af, server_address)

if socket_address is None:
    raise ValueError("Invalid listen address for selected IP version")

s = socket.socket(socket_address[0], socket.SOCK_STREAM)
s.bind(socket_address[1])
s.listen(1)

t = None

try:
    while True:
        try:
            conn, _ = s.accept()

            t = threading.Thread(target=worker, args=(conn,))
            t.start()

            while True:
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
                    conn.send(compressed[i:i+1024])

                conn.send(b"rst")

        except (ConnectionResetError, ConnectionAbortedError, BrokenPipeError):
            pass
except KeyboardInterrupt:
    s.close()

    camera0.release()
    if args.dual:
        camera1.release()

    t.join()
