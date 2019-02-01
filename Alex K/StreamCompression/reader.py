from argparse import ArgumentParser
import cv2
from gzip import decompress
import numpy as np
import socket
import threading
import tkinter

RUNNING = True
CONTROL = None


def control_window(c: socket.socket):
    global RUNNING, CONTROL

    def callback():
        c.send(b"switch")

    def stop():
        global RUNNING
        CONTROL.quit()
        RUNNING = False

    CONTROL = tkinter.Tk()

    CONTROL.geometry("275x50")
    CONTROL.title("Camera Control")

    tkinter.Button(CONTROL, text="Switch Cameras", command=callback).place(anchor="center", x=175, y=25)
    tkinter.Button(CONTROL, text="Quit", command=stop).place(anchor="center", x=75, y=25)

    CONTROL.mainloop()


parser = ArgumentParser(description="Decode gzip compressed video frames")
parser.add_argument("--host", help="Host for server to run on", default="localhost")
parser.add_argument("--port", help="Port for server to listen on", type=int, default=5802)
args = parser.parse_args()

s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
s.bind((args.host, args.port))
s.listen(1)

buffer = []
t = None

try:
    conn, _ = s.accept()

    t = threading.Thread(target=control_window, args=(conn,))
    t.start()
    while RUNNING:
        data = conn.recv(1024)

        if b"rst" in data:
            try:
                buffer.append(data[:data.index(b"rst")])
                assembled = b''.join(buffer)

                decomp_buffer = decompress(assembled)

                frame = np.frombuffer(decomp_buffer, dtype=np.uint8)
                frame = cv2.imdecode(frame, 1)

                cv2.imshow("Stream", frame)
                cv2.waitKey(1)
            except:
                pass

            buffer.clear()
            continue

        buffer.append(data)

except KeyboardInterrupt:
    cv2.destroyAllWindows()
    s.close()
    t.join()
    CONTROL.quit()
except ConnectionResetError:
    cv2.destroyAllWindows()
    s.close()
    t.join()
    CONTROL.quit()
except ConnectionAbortedError:
    cv2.destroyAllWindows()
    s.close()
    t.join()
    CONTROL.quit()
