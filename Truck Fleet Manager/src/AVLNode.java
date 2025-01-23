/**
 * AVLNode class
 * @author Alaaddin Eren Namlı
 * @since Date: 07.11.2024
 */
public class AVLNode {

    public int parkingLotCapacityConstraint; // Value is the capacity constraint of the parking lot
    public AVLNode left;
    public AVLNode right;
    public int height;

    public AVLNode(int parkingLotCapacityConstraint) {
        this.parkingLotCapacityConstraint = parkingLotCapacityConstraint;
        this.height = 0;
    }

    @Override
    public String toString() {
        return "AVLNode{" +
                "parkingLotCapacityConstraint=" + parkingLotCapacityConstraint +
                '}';
    }

}
