import json
from time import sleep
from tkinter import Tk, DoubleVar, BooleanVar, Scale, Checkbutton, Label, Button, messagebox, HORIZONTAL, CENTER


# Update the configuration file
def update_config(*_):
    j = json.load(open("config.json"))
    j["conversion_factors"]["distance"] = dist
    j["conversion_factors"]["angle"] = ang
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
dist = DoubleVar()
ang = DoubleVar()

# Create display value stores
display_window = BooleanVar()
display_bounding = BooleanVar()
display_rotation = BooleanVar()
display_debug = BooleanVar()

# Initialize value stores
data = json.load(open("config.json"))
dist.set(data["conversion_factors"]["distance"])
ang.set(data["conversion_factors"]["angle"])
display_window.set(data["display"]["window"])
display_bounding.set(data["display"]["bounding"])
display_debug.set(data["display"]["debug"])

# Scale for hue min
Label(root, text="Dist:").pack(anchor="w")
Scale(root, variable=dist, from_=0, to=2, orient=HORIZONTAL,
      length=255, resolution=0.01, command=update_config).pack(anchor=CENTER)

# Scale for hue max
Label(root, text="ang:").pack(anchor="w")
Scale(root, variable=ang, from_=0, to=3, orient=HORIZONTAL, length=255, resolution=0.01, command=update_config).pack(anchor=CENTER)

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
