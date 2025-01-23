/**
 * Truck class
 * @author Alaaddin Eren Namlı
 * @since Date: 07.11.2024
 */
public class Truck {

    public int ID;
    public int maximumCapacity;
    public int load;

    public Truck(int ID, int maximumCapacity) { // Constructor
        this.ID = ID;
        this.maximumCapacity = maximumCapacity;
        this.load = 0;
    }

    public int load(int totalAmount, int parkingLotConstraint) { // Load function
        if (isFull()) return 0; // If the truck is full, return 0

        // Load the truck according to the parking lot's constraint and its capacity.
        if (totalAmount <= parkingLotConstraint) {
            int loadedAmount = totalAmount;
            load = load + loadedAmount;
            return loadedAmount;
        } else {
            int loadedAmount = parkingLotConstraint;
            load = load + loadedAmount;
            return loadedAmount;
        }
    }

    public boolean isFull() {
        return load == maximumCapacity;
    }

    @Override
    public String toString() {
        return "Truck{" +
                "ID=" + ID +
                ", maximumCapacity=" + maximumCapacity +
                ", load=" + load +
                '}';
    }

}
