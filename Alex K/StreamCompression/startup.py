import argparse
import subprocess
import json

parser = argparse.ArgumentParser(description="Start all of the vision processes based on the number of cameras")
parser.add_argument("--link-local", help="Change to using IPv6 for link-local support", action="store_true")
parser.add_argument("--disable-vision", help="Stop vision process from running", action="store_true")
parser.add_argument("--disable-driver", help="Stop driver process from running", action="store_true")
args = parser.parse_args()

device_info = {dev: {property.split("=")[0][3:].lower(): property.split("=")[1] for property in subprocess.run("udevadm info -n {device} -q property".format(device=dev), stdout=subprocess.PIPE, shell=True).stdout.decode("utf-8").strip().split("\n") if "ID_MODEL_ID" in property or "ID_VENDOR_ID" in property} for dev in subprocess.run("ls /dev/video*", stdout=subprocess.PIPE, shell=True).stdout.decode("utf-8").strip().split("\n")}

camera_assignments = {"vision": "", "driver": []}
for camera in device_info:
	if device_info[camera]["model_id"] == "0810":
		if len(camera_assignments["driver"]) != 2:
			camera_assignments["driver"].append(camera)
		else:
			raise ValueError("too many driver cameras connected")
	elif device_info[camera]["model_id"] == "0779":
		if camera_assignments["vision"] == "":
			camera_assignments["vision"] = camera
		else:
			raise ValueError("too many vision cameras connected")

if camera_assignments["vision"] != "":
	print("Initializing Vision...")
	if not args.disable_vision:
		subprocess.Popen("cd /home/pi/akrantz/2019Vision; python3 main.py", shell=True)

if len(camera_assignments["driver"]) == 1:
	print("Initializing Monocular Driver...")
	if not args.disable_driver:
		subprocess.Popen("cd /home/pi/akrantz; python3 stream.py --host {host} --port 5805 {ipv6} --camera1 ".format(host="::" if args.link_local else "0.0.0.0", ipv6="--ipv6" if args.link_local else "") + camera_assignments["driver"][0][-1], shell=True)

elif len(camera_assignments["driver"]) == 2:
	print("Initializing Binocular Driver...")
	if not args.disable_driver:
		subprocess.Popen("cd /home/pi/akrantz; python3 stream.py --host {host} --port 5805 {ipv6} --camera1 ".format(host="::" if args.link_local else "0.0.0.0", ipv6="--ipv6" if args.link_local else "") + camera_assignments["driver"][0][-1] + " --camera2 " + camera_assignments["driver"][1][-1] + " --dual", shell=True)

