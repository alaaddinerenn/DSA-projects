/**
 * Node class
 * @author Alaaddin Eren Namlı
 * @since Date: 19.12.2024
 */
public class Node {
    public int type;
    public int xCoordinate;
    public int yCoordinate;
    public Node left;
    public Node right;
    public Node up;
    public Node down;
    public double leftTime;
    public double rightTime;
    public double upTime;
    public double downTime;
    public double distance;

    public Node(int xCoordinate, int yCoordinate, int type) { // Constructor
        this.xCoordinate = xCoordinate;
        this.yCoordinate = yCoordinate;
        this.type = type;
        this.leftTime = Double.MAX_VALUE;
        this.rightTime = Double.MAX_VALUE;
        this.upTime = Double.MAX_VALUE;
        this.downTime = Double.MAX_VALUE;
    }

    // Second constructor for deep copy
    public Node(int xCoordinate, int yCoordinate, int type, Node left, Node right, Node up, Node down, double leftTime, double rightTime, double upTime, double downTime, double distance) {
        this.xCoordinate = xCoordinate;
        this.yCoordinate = yCoordinate;
        this.type = type;
        this.leftTime = leftTime;
        this.rightTime = rightTime;
        this.upTime = upTime;
        this.downTime = downTime;
        this.distance = distance;
        this.left = left;
        this.right = right;
        this.up = up;
        this.down = down;
    }

    @Override
    public String toString() {
        return String.valueOf(type);
    }
}
