import json
import random
import socket
import time

s = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
s.connect(("127.0.0.1", 8080))

for i in range(10):
    data = [{
        "angle": round(random.uniform(-31, 31), 4),
        "distance": round(random.uniform(0, 60), 4),
        "plane": round(random.uniform(-45, 45), 4),
        "timestamp": int(time.time() * 1000)
    } for x in range(random.randint(0, 5))]
    s.sendall(json.dumps(data).encode())
    time.sleep(1)

s.close()
