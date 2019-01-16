from watchdog.observers import Observer
from watchdog.events import PatternMatchingEventHandler


# Watch all json files for updates
class FileWatcher(PatternMatchingEventHandler):
    def __init__(self, callback):
        PatternMatchingEventHandler.__init__(self, patterns=['*.json'],
                                             ignore_directories=True, case_sensitive=False)
        self.callback = callback

    # Call the specified function
    def on_modified(self, event):
        self.callback(event)


if __name__ == "__main__":
    import time
    UPDATED = 0

    def event_handler(_):
        global UPDATED
        UPDATED += 1

    observer = Observer()
    observer.schedule(FileWatcher(event_handler), ".", recursive=True)
    observer.start()

    try:
        while True:
            print("Updated {0} times".format(UPDATED))
            time.sleep(1)
    except KeyboardInterrupt:
        observer.stop()
    observer.join()
