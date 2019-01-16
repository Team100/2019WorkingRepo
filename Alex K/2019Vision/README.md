# 2019 Vision System
Does all the vision processing for the robot. 

## Components
The things that make up the vision system

### Main
The thing that runs on the co-processor to write to NetworkTables. It is made up of: `main.py`, `watcher.py`, `pipeline.py`, and `config.json`. As a whole, this ends up doing all of the calculations for distance and angles and whatnot. It uses the `config.json` file to get all of its configuration. With `watcher.py`, you are able to update the configuration on the fly allowing for quick and easy debugging. In order to find the targets, we use `pipeline.py` to filter via HSV to find strips of retroreflective tape. This is then taken and put into `main.py` which calculates the distance, angle to turn, and angle of the plane of each pair of targets.

### Config Editor
Config editor is a nicer frontend for `config.json` which gives sliders and toggles for each of the variables.

## Configuration
```
    {
        "blur": {
            "radius": <amount of blur to apply>,
        },
        "camera": {
            "height": <physical height of camera>
        },
        "camera_port": "<camera to read from>",
        "contours": {
            "external_contours": <only show external contours>,
            "max_height": <maximum height of a contour>,
            "max_ratio": <maximum ratio of height to width>,
            "max_vertices": <maximum vertices a contour can have>,
            "max_width": <maximum width of a contour>,
            "min_area": <minimum area of a contour>,
            "min_height": <minimum height of a contour>,
            "min_perimeter": <minimum perimeter of a contour>,
            "min_ratio": <minimum ratio of height to width>,
            "min_vertices": <minimum vertices a contour can have>,
            "min_width": <minimum width of a contour>,
            "solidity": [
                <minumum solidity of a contour>,
                <maximum solidity of a contour>
            ]
        },
        "display": {
            "bounding": <enable bounding boxes on display>,
            "debug": <enable debug info on display>,
            "window": <display a window with the image>
        },
        "horizontal_degrees_per_pixel": <number of degrees per horizontal pixel from FOV>,
        "hsv": {
            "hue": [
                <minumum hue value>,
                <maximum hue value>
            ],
            "saturation": [
                <minumum saturation value>,
                <maximum saturation value>
            ],
            "value": [
                <minimum value value>,
                <maximum value value>
            ]
        },
        "image_center": <center of the image - 0.5px>,
        "network_tables": "<ip address of the NetworkTables server>"
    }
```
