import java.util.Arrays;
/**
 * HashingArray class for storing ParkingLot objects in an array.
 * Parking lots are hashed according to its capacity constraints.
 * @author Alaaddin Eren Namlı
 * @since Date: 07.11.2024
 */
public class HashingArray {

    private ParkingLot[] parkingLotArray; // Inner array

    public HashingArray() {
        this.parkingLotArray = new ParkingLot[500001];
    }

    public void insert(int capacityConstraint, ParkingLot parkingLot) {
        parkingLotArray[capacityConstraint] = parkingLot;
    }

    public ParkingLot remove(int capacityConstraint) {
        ParkingLot parkingLot = parkingLotArray[capacityConstraint];
        parkingLotArray[capacityConstraint] = null;
        return parkingLot;
    }

    public boolean contains(int capacityConstraint) {
        if (parkingLotArray[capacityConstraint] != null) return true;
        else return false;
    }

    public ParkingLot get(int capacityConstraint) {
        return parkingLotArray[capacityConstraint];
    }

    @Override
    public String toString() {
        return "HashingArray{" +
                "parkingLotArray=" + Arrays.toString(parkingLotArray) +
                '}';
    }

}
