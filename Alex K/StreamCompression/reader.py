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
args = parser.parse_args()

s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
try:
    s.connect((args.host, args.port))
except ConnectionRefusedError:
    print("Connection Refused: Please check the camera streamer...")
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
