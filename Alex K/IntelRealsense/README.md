# Intel RealSense Demo
Integrates 2018's vision code with the [Intel RealSense D435](https://software.intel.com/en-us/realsense/d400/get-started) camera for the Intel demo.

## Config Parameters
**Config Path**|**Type**|**Purpose**
-----|-----|-----
`hsv.hue`|array of floats|Upper and lower bounds of hue filter
`hsv.saturation`|array of floats|Upper and lower bounds of saturation filter
`hsv.value`|array of floats|Upper and lower bounds of value filter
`contours.min_area`|integer|Minimum area for a contour
`contours.min_perimeter`|integer|Minimum perimeter for a contour
`contours.min_width`|integer|Minimum width for a contour
`contours.max_width`|integer|Maximum width for a contour
`contours.min_height`|integer|Minimum height for a contour
`contours.max_height`|integer|Maximum height for a contour
`contours.solidity`|array of integers|Upper and lower bounds for how solid a contour is
`contours.min_vertices`|integer|Minimum vertices for a contour
`contours.max_vertices`|integer|Maximum vertices for a contour
`contours.min_ratio`|integer|Minimum width to height ratio for a contour
`contours.max_ratio`|integer|Maximum width to height ratio for a contour
`contours.external_contours`|boolean|Find only external contours (true) or not (false)
`camera.width`|integer|Width of the camera stream
`camera.height`|integer|Height of the camera stream
`camera.frame_rate`|integer|Frame rate of the camera
`image_center`|float|Center line of the image (x-value)
`horizontal_degrees_per_pixel`|float|Number of horizontal degrees per pixel 
`distance_offset`|float|Offset for correcting distance
`network_tables`|string|IP or URL for the NetworkTables server
