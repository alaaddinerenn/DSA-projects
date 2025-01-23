/**
 * ParkingLot class
 * @author Alaaddin Eren Namlı
 * @since Date: 07.11.2024
 */
public class ParkingLot {

    public int capacityConstraint;
    public int truckLimit;
    public QueueClass waitingQueue;
    public QueueClass readyQueue;
    public int truckCount;

    public ParkingLot(int capacityConstraint, int truckLimit) { // Constructor
        this.capacityConstraint = capacityConstraint;
        this.truckLimit = truckLimit;
        this.waitingQueue = new QueueClass();
        this.readyQueue = new QueueClass();
    }

    public void addTruckWQ(Truck truck) { // Add truck to the waiting queue
        if (truckCount >= truckLimit) {
            System.out.printf("Truck capacity is full, %s is not added\n", truck.toString());
            return;
        }
        waitingQueue.enqueue(truck);
        truckCount++;
    }

    public void addTruckRQ(Truck truck) { // Add truck to the ready queue
        if (truckCount >= truckLimit) {
            System.out.printf("Truck capacity is full, %s is not added\n", truck.toString());
            return;
        }
        readyQueue.enqueue(truck);
        truckCount++;
    }

    public Truck getFirstWTruck() {
        return waitingQueue.peek();
    }

    public Truck getFirstRTruck() {
        return readyQueue.peek();
    }

    public Truck readyTruck() { // Move the first truck in the waiting queue to the ready queue
        Truck truck = waitingQueue.dequeue(); // Remove the truck from waiting queue
        readyQueue.enqueue(truck); // Add the truck to the ready queue
        return truck;
    }

    public Truck removeTruckRQ() { // Remove the first truck in the ready queue
        Truck truck = readyQueue.dequeue();
        truckCount--;
        return truck;
    }

    public boolean isFull() {
        return truckCount >= truckLimit;
    }

    @Override
    public String toString() {
        return "ParkingLot{" +
                "capacityConstraint=" + capacityConstraint +
                ", truckLimit=" + truckLimit +
                ", waitingQueue=" + waitingQueue +
                ", readyQueue=" + readyQueue +
                ", truckCount=" + truckCount +
                '}';
    }

}
