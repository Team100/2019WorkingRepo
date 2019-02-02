from argparse import ArgumentParser
import cv2
from gzip import decompress
import numpy as np
import socket
import threading
import tkinter
import sys

RUNNING = True
CONTROL = None


def control_window(c: socket.socket):
    global RUNNING, CONTROL

    def callback(_):
        c.send(b"switch")

    def stop(_):
        global RUNNING
        CONTROL.quit()
        RUNNING = False

    CONTROL = tkinter.Tk()

    CONTROL.geometry("275x50")
    CONTROL.title("Camera Control")

    tkinter.Button(CONTROL, text="Switch Cameras", command=callback).place(anchor="center", x=175, y=25)
    tkinter.Button(CONTROL, text="Quit", command=stop).place(anchor="center", x=75, y=25)

    CONTROL.bind("s", callback)
    CONTROL.bind("q", stop)

    CONTROL.mainloop()


parser = ArgumentParser(description="Decode gzip compressed video frames")
parser.add_argument("--host", help="Host to send to", default="localhost")
parser.add_argument("--port", help="Port to send to", type=int, default=5802)
args = parser.parse_args()

s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
try:
    s.connect((args.host, args.port))
except ConnectionRefusedError:
    print("Connection Refused: Please check the camera streamer...")
    sys.exit(1)

buffer = []
t = None

try:
    t = threading.Thread(target=control_window, args=(s,))
    t.start()
    while RUNNING:
        data = s.recv(1024)

        if b"rst" in data:
            try:
                buffer.append(data[:data.index(b"rst")])
                assembled = b''.join(buffer)

                decomp_buffer = decompress(assembled)

                frame = np.frombuffer(decomp_buffer, dtype=np.uint8)
                frame = cv2.imdecode(frame, 1)

                cv2.imshow("Stream", frame)
                cv2.waitKey(1)
            except EOFError:
                pass
            except OSError:
                pass

            buffer.clear()
            continue

        buffer.append(data)

except KeyboardInterrupt:
    cv2.destroyAllWindows()
    s.close()
    CONTROL.quit()
    t.join()
except ConnectionResetError:
    cv2.destroyAllWindows()
    s.close()
    CONTROL.quit()
    t.join()
except ConnectionAbortedError:
    cv2.destroyAllWindows()
    s.close()
    CONTROL.quit()
    t.join()
