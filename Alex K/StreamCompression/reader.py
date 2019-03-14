from argparse import ArgumentParser
import cv2
from gzip import decompress
import numpy as np
from pynput.keyboard import Listener, KeyCode
import socket
import threading
import tkinter
import sys

RUNNING = True
CONTROL = None


def control_window(c: socket.socket):
    global RUNNING, CONTROL

    def callback(_=None):
        c.send(b"switch")

    def stop(_=None):
        global RUNNING
        CONTROL.quit()
        RUNNING = False
        hotkey.stop()

    CONTROL = tkinter.Tk()

    CONTROL.geometry("275x50")
    CONTROL.title("Camera Control")

    tkinter.Button(CONTROL, text="Switch Cameras", command=callback).place(anchor="center", x=175, y=25)
    tkinter.Button(CONTROL, text="Quit", command=stop).place(anchor="center", x=75, y=25)

    CONTROL.mainloop()


def on_keypress(key: KeyCode):
    if type(key) != KeyCode:
        pass
    elif key.char == "s":
        try:
            s.send(b"switch")
        except ConnectionResetError:
            pass
    elif key.char == "q":
        global RUNNING
        RUNNING = False
        CONTROL.quit()
        hotkey.stop()


parser = ArgumentParser(description="Decode gzip compressed video frames")
parser.add_argument("--host", help="Host to send to", default="localhost")
parser.add_argument("--port", help="Port to send to", type=int, default=5802)
parser.add_argument("--upscale-width", help="Upscale the received stream - width", type=int, default=0)
parser.add_argument("--upscale-height", help="Upscale the received stream - height", type=int, default=0)
parser.add_argument("--rotate", help="Specify how to rotate the image", type=int, default=0, choices=[0, 90, 180, 270])
parser.add_argument("--ipv6", help="Use IPv6 over IPv4", action="store_true")
args = parser.parse_args()

socket_info = set(socket.getaddrinfo(args.host, args.port))
socket_address = None
for res in socket_info:
    af, _, _, _, server_address = res
    if (af == socket.AF_INET and not args.ipv6) or (af == socket.AF_INET6 and args.ipv6):
        socket_address = (af, server_address)

if socket_address is None:
    print("Listen address '{address}' is invalid for selected IP version {version}...".format(address=args.host, version=6 if args.ipv6 else 4))
    sys.exit(1)

s = socket.socket(socket_address[0], socket.SOCK_STREAM)
try:
    s.connect(socket_address[1])
except (ConnectionRefusedError, TimeoutError) as e:
    print("{error}: Please check the camera streamer...".format(error=e.__class__.__name__))
    sys.exit(1)

hotkey = Listener(on_press=on_keypress)
hotkey.start()

buffer = []
t = threading.Thread(target=control_window, args=(s,))
t.start()

while RUNNING:
    try:
        buffer = []
        while RUNNING:
            data = s.recv(1024)

            if b"rst" in data:
                try:
                    buffer.append(data[:data.index(b"rst")])
                    assembled = b''.join(buffer)

                    decompressed_buffer = decompress(assembled)

                    frame = np.frombuffer(decompressed_buffer, dtype=np.uint8)
                    frame = cv2.imdecode(frame, 1)

                    if args.upscale_height != 0 and args.upscale_width != 0:
                        frame = cv2.resize(frame, (args.upscale_width, args.upscale_height))

                    if args.rotate == 90:
                        frame = cv2.rotate(frame, cv2.ROTATE_90_CLOCKWISE)
                    elif args.rotate == 180:
                        frame = cv2.rotate(frame, cv2.ROTATE_180)
                    elif args.rotate == 270:
                        frame = cv2.rotate(frame, cv2.ROTATE_90_COUNTERCLOCKWISE)

                    cv2.imshow("Stream", frame)
                    cv2.waitKey(1)
                except (EOFError, OSError):
                    pass

                buffer.clear()
                continue

            buffer.append(data)

    except (KeyboardInterrupt, ConnectionResetError, ConnectionAbortedError):
        pass

cv2.destroyAllWindows()
s.close()
CONTROL.quit()
t.join()
hotkey.stop()
hotkey.join()
