public class VisionTarget {
    private double angle;
    private double distance;
    private double plane;
    private long timestamp;

    public double getAngle() {
        return angle;
    }

    public void setAngle(double angle) {
        this.angle = angle;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public double getPlane() {
        return plane;
    }

    public void setPlane(double plane) {
        this.plane = plane;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String toString() {
        return String.format("<VisionTarget angle: %s, distance: %s, plane: %s, timestamp: %s>", angle, distance, plane, timestamp);
    }
}