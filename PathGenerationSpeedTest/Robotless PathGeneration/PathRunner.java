public class PathRunner
{
    public static void main(String args[]){
        double distance = 20; //feet
        double angle = 30;
        double approachAngle = -90;
        double endPosX = Math.cos(angle)*distance;
        double endPosY = Math.sin(angle)*distance;
        //create waypoint path
        double[][] waypoints = new double[][]{
            {0.0, 0.0},
            {endPosX/5, 0.0},
            // {Math.cos(approachAngle)*(distance/2), -Math.sin(approachAngle)*(distance/2)},
            {endPosX, endPosY*3/4},
            {endPosX, endPosY}
        }; 
        // double[][] waypoints = new double[][]{
        //     {0.0, 0.0},
        //     {endPosX/4, 0.0},
        //     {1.5, -2.0},
        //     {2.2, -6.0},
        //     {2.7, -10.0},
        //     {endPosX, endPosY-endPosY/4},
        //     {endPosX, endPosY}
        // }; 

        double totalTime = 16; //max seconds we want to drive the path
        double timeStep = 0.02; //period of control loop on Rio, seconds
        double robotTrackWidth = 2; //distance between left and right wheels, feet
        
        FalconPathPlanner path = new FalconPathPlanner(waypoints);
        path.calculate(totalTime, timeStep, robotTrackWidth);
        double[][] leftVel = path.smoothLeftVelocity;
        double[][] rightVel = path.smoothRightVelocity;
        double[][] smoothPath = path.smoothPath;
        double[][] output = new double[leftVel.length][4];
        String out = "";
        for(int i = 0; i < output.length; i++){
            output[i][0] = leftVel[i][1];
            output[i][1] = rightVel[i][1];
            output[i][2] = smoothPath[i][0];
            output[i][3] = smoothPath[i][1];
            
            out += (output[i][0] + "," + output[i][1] + "," + output[i][2] + "," + output[i][3] + "\n");
        }
        System.out.println(out);
    }
}
