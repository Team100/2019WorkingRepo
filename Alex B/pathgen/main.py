import pathfinder as pf
import math
import time
from pathfinder.followers import EncoderFollower
start = time.time()
points = [
    pf.Waypoint(-4, -1, math.radians(-45.0)),   # Waypoint @ x=-4, y=-1, exit angle=-45 degrees
    pf.Waypoint(-2, -2, 0),                     # Waypoint @ x=-2, y=-2, exit angle=0 radians
    pf.Waypoint(0, 0, 0),# Waypoint @ x=0, y=0,   exit angle=0 radians
    pf.Waypoint(15,15,0)
]

MAX_CYCLE_UPDATE = 0.05
MAX_VELOCITY = 1.7
MAX_ACCEL = 2.0
MAX_JERK = 60.0

info, trajectory = pf.generate(points, pf.FIT_HERMITE_CUBIC, pf.SAMPLES_HIGH,
                               dt=MAX_CYCLE_UPDATE,
                               max_velocity=MAX_VELOCITY,
                               max_acceleration=MAX_ACCEL,
                               max_jerk=MAX_JERK)


modifier = pf.modifiers.TankModifier(trajectory).modify(0.5)
left = EncoderFollower(modifier.getLeftTrajectory())
right = EncoderFollower(modifier.getRightTrajectory())
end = time.time()

print(end-start)
print(type(left.trajectory))
print(left.trajectory)
final_traj = []
for i in range(0,len(left.trajectory)):
    final_traj.append({left.trajectory[i].velocity,right.trajectory[i].velocity})

for x in final_traj:
    print(x)
