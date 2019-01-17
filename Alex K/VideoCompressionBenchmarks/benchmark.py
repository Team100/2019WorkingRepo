import cv2
import base64
import time
import csv

# Import compression algorithms
import gzip
import lzma
import bz2
import brotli
import zlib

# Configuration Variables
CAMERA = 1


# Calculate the efficiency of a compression algorithm
def calculate_compression_percentage(c, u):
    return 1 - (len(c) / len(u))


# Take the picture
camera = cv2.VideoCapture(CAMERA)
_, image = camera.read()
camera.release()

# Check that image exists
if image is None:
    print("No image taken")
    exit(1)

# Convert to base64
_, buffer = cv2.imencode(".jpg", image)
uncomp = base64.b64encode(buffer)

# Store metrics
metrics = []

# Calculate GZIP efficiency
print("GZIP:")
for l in range(1, 10):
    start = time.time()
    compressed = gzip.compress(uncomp, l)
    total = time.time() - start

    ratio = calculate_compression_percentage(compressed, uncomp)
    print("\tLevel {0}:\n\t\tRatio: {1}\n\t\tTime: {2}".format(l, ratio, total))

    metrics.append([l, ratio, total, "gzip"])

print("\n")

# Calculate LZMA efficiency
print("LZMA:")
for l in range(0, 9):
    start = time.time()
    compressed = lzma.compress(uncomp, preset=l)
    total = time.time() - start

    ratio = calculate_compression_percentage(compressed, uncomp)
    print("\tLevel {0}:\n\t\tRatio: {1}\n\t\tTime: {2}".format(l, ratio, total))

    metrics.append([l, ratio, total, "lzma"])

print("\n")

# Calculate BZ2 efficiency
print("BZ2:")
for l in range(1, 10):
    start = time.time()
    compressed = bz2.compress(uncomp, l)
    total = time.time() - start

    ratio = calculate_compression_percentage(compressed, uncomp)
    print("\tLevel {0}:\n\t\tRatio: {1}\n\t\tTime: {2}".format(l, ratio, total))

    metrics.append([l, ratio, total, "bz2"])

# Calculate Brotli efficiency
print("Brotli:")
for l in range(0, 12):
    start = time.time()
    compressed = brotli.compress(uncomp, quality=l)
    total = time.time() - start

    ratio = calculate_compression_percentage(compressed, uncomp)
    print("\tLevel {0}:\n\t\tRatio: {1}\n\t\tTime: {2}".format(l, ratio, total))

    metrics.append([l, ratio, total, "brotli"])

# Calculate zlib efficiency
print("ZLIB:")
for l in range(0, 10):
    start = time.time()
    compressed = zlib.compress(uncomp, level=l)
    total = time.time() - start

    ratio = calculate_compression_percentage(compressed, uncomp)
    print("\tLevel: {0}:\n\t\tRatio: {1}\n\t\tTime: {2}".format(l, ratio, total))

    metrics.append([l, ratio, total, "zlib"])

# Write metrics to csv file
with open("metrics.csv", "w", newline="") as file:
    writer = csv.writer(file, delimiter=",", quotechar="|", quoting=csv.QUOTE_MINIMAL)
    writer.writerow(["Level", "Compression Ratio", "Time", "Algorithm"])

    for algorithm in metrics:
        writer.writerow(algorithm)

print("\nFINISHED")
