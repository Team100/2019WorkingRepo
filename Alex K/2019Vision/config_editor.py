import json
from time import sleep
from tkinter import Tk, DoubleVar, BooleanVar, Scale, Checkbutton, Label, Button, messagebox, HORIZONTAL, CENTER


# Update the configuration file
def update_config(*_):
    if hue_min.get() > hue_max.get():
        messagebox.showerror("Error", "Hue minimum cannot be larger than maximum")
        hue_min.set(hue_max.get() - 1)
        sleep(0.25)
    if sat_min.get() > sat_max.get():
        messagebox.showerror("Error", "Saturation minimum cannot be larger than maximum")
        sat_min.set(sat_max.get() - 1)
        sleep(0.25)
    if val_min.get() > val_max.get():
        messagebox.showerror("Error", "Value minimum cannot be larger than maximum")
        val_min.set(val_max.get() - 1)
        sleep(0.25)

    j = json.load(open("config.json"))
    j["hsv"]["hue"] = [hue_min.get(), hue_max.get()]
    j["hsv"]["saturation"] = [sat_min.get(), sat_max.get()]
    j["hsv"]["value"] = [val_min.get(), val_max.get()]
    j["display"]["window"] = display_window.get()
    j["display"]["bounding"] = display_bounding.get()
    j["display"]["debug"] = display_debug.get()

    json.dump(j, open("config.json", "w"), sort_keys=True, indent=2)


# Close the window
def close():
    root.destroy()


# Initialize root window
root = Tk()
root.title("Vision Config")

# Create HSV value stores
hue_max = DoubleVar()
hue_min = DoubleVar()
sat_max = DoubleVar()
sat_min = DoubleVar()
val_max = DoubleVar()
val_min = DoubleVar()

# Create display value stores
display_window = BooleanVar()
display_bounding = BooleanVar()
display_rotation = BooleanVar()
display_debug = BooleanVar()

# Initialize value stores
data = json.load(open("config.json"))
hue_min.set(data["hsv"]["hue"][0])
hue_max.set(data["hsv"]["hue"][1])
sat_min.set(data["hsv"]["saturation"][0])
sat_max.set(data["hsv"]["saturation"][1])
val_min.set(data["hsv"]["value"][0])
val_max.set(data["hsv"]["value"][1])
display_window.set(data["display"]["window"])
display_bounding.set(data["display"]["bounding"])
display_debug.set(data["display"]["debug"])

# Scale for hue min
Label(root, text="Hue Minimum:").pack(anchor="w")
Scale(root, variable=hue_min, from_=0, to=255, orient=HORIZONTAL,
      length=255, resolution=0.1, command=update_config).pack(anchor=CENTER)

# Scale for hue max
Label(root, text="Hue Maximum:").pack(anchor="w")
Scale(root, variable=hue_max, from_=0, to=255, orient=HORIZONTAL, length=255, resolution=0.1, command=update_config).pack(anchor=CENTER)

# Scale for saturation min
Label(root, text="Saturation Maximum:").pack(anchor="w")
Scale(root, variable=sat_min, from_=0, to=255, orient=HORIZONTAL, length=255, resolution=0.1, command=update_config).pack(anchor=CENTER)

# Scale for saturation max
Label(root, text="Saturation Maximum:").pack(anchor="w")
Scale(root, variable=sat_max, from_=0, to=255, orient=HORIZONTAL, length=255, resolution=0.1, command=update_config).pack(anchor=CENTER)

# Scale for value min
Label(root, text="Value Minimum:").pack(anchor="w")
Scale(root, variable=val_min, from_=0, to=255, orient=HORIZONTAL, length=255, resolution=0.1, command=update_config).pack(anchor=CENTER)

# Scale for value max
Label(root, text="Value Maximum:").pack(anchor="w")
Scale(root, variable=val_max, from_=0, to=255, orient=HORIZONTAL, length=255, resolution=0.1, command=update_config).pack(anchor=CENTER)

# Checkbox for display window
Checkbutton(root, text="Display Window?", variable=display_window, onvalue=True, offvalue=False, height=5, width=20, command=update_config).pack(anchor=CENTER)

# Checkbox for display bounding box
Checkbutton(root, text="Display Bounding Box?", variable=display_bounding, onvalue=True, offvalue=False, height=5, width=20, command=update_config).pack(anchor=CENTER)

# Checkbox for display debug info
Checkbutton(root, text="Display Debug?", variable=display_debug, onvalue=True, offvalue=False, height=5, width=20, command=update_config).pack(anchor=CENTER)

# Close the program
Button(root, text="Close", command=close).pack(anchor=CENTER)

# Run the entire thing
root.mainloop()
