import FalconPathPlanner;
public class PathRunner
{
    public static void main(String args[]){
        double distance = 120.0/12.0; //feet
        double angle = 15;
        double endPosX = Math.cos(angle)*distance;
        double endPosY = Math.sin(angle)*distance;
        double extrusionX = endPosX*4/5;
        double extrusionY = endPosY*4/5;
        //create waypoint path
        double[][] waypoints = new double[][]{
            {0.0, 0.0},
            //{extrusionY, 0.0},
            {0.0, endPosY},
            //{endPosY, extrusionX},
            {endPosX, endPosY}
        }; 

        double totalTime = distance/7 + 6; //max seconds we want to drive the path
        double timeStep = 0.02; //period of control loop on Rio, seconds
        double robotTrackWidth = 2; //distance between left and right wheels, feet
        
        FalconPathPlanner path = new FalconPathPlanner(waypoints);
        path.calculate(totalTime, timeStep, robotTrackWidth);
        double[][] leftVel = path.smoothLeftVelocity;
        double[][] rightVel = path.smoothRightVelocity;
        double[][] smoothPath = path.smoothPath;
        double[][] output = new double[leftVel.length][3];
        String out = "";
        for(int i = 0; i < output.length; i++){
            output[i][0] = leftVel[i][1];
            output[i][1] = rightVel[i][1];
            output[i][2] = path.heading[i][1];
            out += (output[i][0] + "," + output[i][1] + "," + output[i][2] + "," + path.smoothPath[i][0] + "," + path.smoothPath[i][1] + "\n");
        }
        System.out.println(out);
    }
}
