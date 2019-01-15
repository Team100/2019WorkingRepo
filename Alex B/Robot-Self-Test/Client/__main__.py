import os
import sys
import threading
from networktables import NetworkTables



def cls():
    os.system('cls' if os.name=='nt' else 'clear')


def hr():
    try:
        columns, rows = os.get_terminal_size(0)
    except OSError:
        columns, rows = os.get_terminal_size(1)
    print("=" * columns)


cls()
hr()
print("Team 100 Self-Test Client")
print("Configuring Environment")


class IPPreset:
    ROBOT_IP = '10.1.0.2'
    LOCALHOST = 'localhost'
    X240_LAN = '169.254.234.20'
    ROBOT_ETHERNET = '169.254.1.252'
    USB = '172.22.11.2'

class RobotConfig:
    ROBOT_IP = IPPreset.USB
    TEAM = '100'
    NAME = 'WILDHATS GENERAL'



cond = threading.Condition()
notified = [False]
connection = ''
def connectionListener(connected, info):
    print(info, '; Connected=%s' % connected)
    connection=connected
    with cond:
        notified[0] = True
        cond.notify()


NetworkTables.initialize(server=RobotConfig.ROBOT_IP)
NetworkTables.addConnectionListener(connectionListener, immediateNotify=True)

table = NetworkTables.getTable('')
with cond:
    print("Waiting")
    if not notified[0]:
        cond.wait()


def clearTable():
    stt = table.getSubTable('selftest')

    stt.putString('test1', '')
    stt.putString('test2', '')
    stt.putString('test3', '')


print("Connected!")
clearTable()
print("NetworkTable Cleared")
hr()
wait = input("Press Enter to Generate Report")

cls()
hr()
st = table.getSubTable('selftest')
testRunning = st.getBoolean('running', False)
test1 = st.getString('test1','UNKNOWN')
test2 = st.getString('test2','UNKNOWN')
test3 = st.getString('test3','UNKNOWN')

print("Report")
print("Team   : " + RobotConfig.TEAM)
print("Name   : " + RobotConfig.NAME)
print("Running: " + str(testRunning))
print("Test 1 : " + test1)
print("Test 2 : " + test2)
print("Test 3 : " + test3)
hr()