#!/bin/bash
source /usr/local/bin/virtualenvwrapper.sh
workon cv3
echo "Starting process based off number of cameras..."
python3 /home/pi/akrantz/startup.py
sleep infinity
