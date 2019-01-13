
import threading
from networktables import NetworkTables
print("Team 100 Self-Test Client")
print("Configuring Environment")
class ipPreset:
    ROBOT_IP = '10.1.0.2'
    LOCALHOST = 'localhost'
    X240_LAN = '169.254.234.20'
    ROBOT_ETHERNET = '169.254.1.252'
    USB = '172.22.11.2'

class robotConfig:
    ROBOT_IP = ipPreset.USB


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
wait = input("Press Enter to Generate Report")
st = table.getSubTable('selftest')
testRunning = st.getBoolean('running', False)

test1 = st.getString('test1', '')
test2 = st.getString('test2', '')
print("="*25)
print("Report")
print("Running: " + str(testRunning))
print("Test 1 : " + test1)
print("Test 2 : " + test2)