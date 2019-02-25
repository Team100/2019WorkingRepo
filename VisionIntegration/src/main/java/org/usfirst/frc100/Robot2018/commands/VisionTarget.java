package org.usfirst.frc100.Robot2018.commands;

public class VisionTarget {
    private int[] center;
    private double angle;
    private double distance;
    private double plane;
    private int[] bounding_box;
    private long timestamp;

    public VisionTarget() {}

    /**
     * @return the timestamp
     */
    public long getTimestamp() {
        return timestamp;
    }

    /**
     * @param timestamp the timestamp to set
     */
    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    /**
     * @return the bounding_box
     */
    public int[] getBounding_box() {
        return bounding_box;
    }

    /**
     * @param bounding_box the bounding_box to set
     */
    public void setBounding_box(int[] bounding_box) {
        this.bounding_box = bounding_box;
    }

    /**
     * @return the plane
     */
    public double getPlane() {
        return plane;
    }

    /**
     * @param plane the plane to set
     */
    public void setPlane(double plane) {
        this.plane = plane;
    }

    /**
     * @return the distance
     */
    public double getDistance() {
        return distance;
    }

    /**
     * @param distance the distance to set
     */
    public void setDistance(double distance) {
        this.distance = distance;
    }

    /**
     * @return the angle
     */
    public double getAngle() {
        return angle;
    }

    /**
     * @param angle the angle to set
     */
    public void setAngle(double angle) {
        this.angle = angle;
    }

    /**
     * @return the center
     */
    public int[] getCenter() {
        return center;
    }

    /**
     * @param center the center to set
     */
    public void setCenter(int[] center) {
        this.center = center;
    }
}
