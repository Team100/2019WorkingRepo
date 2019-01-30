public class PathRunner
{
    public static void main(String args[]){
        double distance = 15; //feet
        double angle = -45;
        double endPosX = Math.cos(angle)*distance;
        double endPosY = Math.sin(angle)*distance;
        double extrusionX = endPosX*2/5;
        double extrusionY = endPosY*3/5;
        //create waypoint path
        double[][] waypoints = new double[][]{
            {0.0, 0.0},
            {extrusionX, 0.0},
            {endPosX, 0.0},
            {endPosX, extrusionY},
            {endPosX, endPosY}
        }; 

        double totalTime = 12; //max seconds we want to drive the path
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
