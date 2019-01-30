from argparse import ArgumentParser
import cv2
from gzip import decompress
import numpy as np
import socket

parser = ArgumentParser(description="Decode gzip compressed video frames")
parser.add_argument("--host", help="Host for server to run on", default="localhost")
parser.add_argument("--port", help="Port for server to listen on", type=int, default=5802)
args = parser.parse_args()

s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
s.bind((args.host, args.port))
s.listen(1)

buffer = []

try:
    conn, addr = s.accept()
    while True:
        data = conn.recv(1024)

        if b"rst" in data:
            try:
                buffer.append(data[:data.index(b"rst")])
                assembled = b''.join(buffer)

                decomp_buffer = decompress(assembled)

                frame = np.frombuffer(decomp_buffer, dtype=np.uint8)
                frame = cv2.imdecode(frame, 1)

                cv2.imshow("Stream", frame)
                if cv2.waitKey(1) & 0xFF == ord('q'):
                    cv2.destroyAllWindows()
                    s.close()
                    break
            except:
                pass

            buffer.clear()
            continue

        buffer.append(data)

except KeyboardInterrupt:
    cv2.destroyAllWindows()
    s.close()
