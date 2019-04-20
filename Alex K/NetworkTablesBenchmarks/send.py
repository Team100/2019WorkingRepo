import socket
import time

s = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
s.connect(("127.0.0.1", 8080))

s.sendall(int(time.time() * 1000).to_bytes(8, "big"))
