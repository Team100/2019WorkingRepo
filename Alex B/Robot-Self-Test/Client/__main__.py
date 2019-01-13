
import threading
from networktables import NetworkTables
print("Team 100 Self-Test Client")
print("Configuring Environment")
class ipPreset:
    ROBOT_IP = '10.1.0.2'
    LOCALHOST = 'localhost'
    X240_LAN = '169.254.234.20'


class robotConfig:
    ROBOT_IP = ipPreset.X240_LAN


cond = threading.Condition()
notified = [False]

def connectionListener(connected, info):
    print(info, '; Connected=%s' % connected)
    with cond:
        notified[0] = True
        cond.notify()


NetworkTables.initialize(server=robotConfig.ROBOT_IP)
NetworkTables.addConnectionListener(connectionListener, immediateNotify=True)

with cond:
    print("Waiting")
    if not notified[0]:
        cond.wait()


table = NetworkTables.getTable('')

print("Connected!")
print("Getting data from NT")

runStatus = True
while runStatus:
    st = table.getSubTable('selftest')

    test1 = st.getBoolean('test1',False)
    if(test1):
        runStatus = False
print("DONE")
