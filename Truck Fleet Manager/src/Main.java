import java.io.*;
/**
 * Program to manage fleet of trucks with varying capacities.
 * @author Alaaddin Eren Namlı
 * @since Date: 07.11.2024
 */
public class Main {

    public static void main(String[] args) throws IOException {

        BufferedReader reader = new BufferedReader(new FileReader(args[0]));
        BufferedWriter writer = new BufferedWriter(new FileWriter(args[1], true));

        // Use 3 AVL trees.
        AVLTreeClass parkingLotIDs = new AVLTreeClass(); // For all parking lots
        AVLTreeClass lotsWaiting = new AVLTreeClass(); // For lots that have truck in WQ
        AVLTreeClass lotsReady = new AVLTreeClass(); // For lots that have truck in RQ
        HashingArray parkingLotsHashing = new HashingArray(); // Hashing array for all parking lots

        String line; // To read line by line
        while ((line = reader.readLine()) != null) { // This loop reads the input file and calls the relevant methods
            String[] words = line.split(" ");
            if (words[0].equals("create_parking_lot")) {
                createNewParkingLot(Integer.parseInt(words[1]), Integer.parseInt(words[2]), parkingLotIDs, parkingLotsHashing);
            } else if (words[0].equals("add_truck")) {
                addTruck(Integer.parseInt(words[1]), Integer.parseInt(words[2]), parkingLotIDs, lotsWaiting,parkingLotsHashing, writer);
            } else if (words[0].equals("ready")) {
                ready(Integer.parseInt(words[1]), parkingLotIDs, lotsWaiting, lotsReady,parkingLotsHashing, writer);
            } else if (words[0].equals("load")) {
                load(Integer.parseInt(words[1]), Integer.parseInt(words[2]), parkingLotIDs, lotsWaiting,lotsReady,parkingLotsHashing, writer);
            } else if (words[0].equals("delete_parking_lot")) {
                deleteLot(Integer.parseInt(words[1]), parkingLotIDs, lotsWaiting, lotsReady,parkingLotsHashing, writer);
            } else if (words[0].equals("count")) {
                countTrucks(Integer.parseInt(words[1]), parkingLotIDs, parkingLotsHashing, writer);
            }
        }

        // Close files
        reader.close();
        writer.close();
    }
    
    /**
     * Creates new parking lots.
     * @param capacityConstraint Capacity constraint of the parking lot
     * @param truckLimit Truck limit of the parking lot
     * @param parkingLotIDs AVL Tree contains all parking lots, this ID is the same as capacity constraint of that parking lot
     * @param parkingLotsHashing Array containing parking lots
     */
    public static void createNewParkingLot(int capacityConstraint, int truckLimit, AVLTreeClass parkingLotIDs, HashingArray parkingLotsHashing) {
        ParkingLot parkingLot = new ParkingLot(capacityConstraint, truckLimit);
        parkingLotIDs.insert(parkingLot.capacityConstraint);
        parkingLotsHashing.insert(capacityConstraint, parkingLot);
    }

    /**
     * Adds trucks to the parking lot specified in the input file. If the parking lot is full or does not exist, it adds the truck to the predecessor of that parking lot.
     * @param truckID ID of the truck
     * @param maxCapacity Maximum load capacity of the truck
     * @param parkingLotIDs AVL Tree which contains all parking lots, this ID is the same as capacity constraint of that parking lot
     * @param lotsWaiting AVL Tree which contains the parking lots that have truck in their waiting queues
     * @param parkingLotsHashing Array containing parking lots
     * @param writer Buffered Writer object
     */
    public static void addTruck(int truckID, int maxCapacity, AVLTreeClass parkingLotIDs, AVLTreeClass lotsWaiting, HashingArray parkingLotsHashing, BufferedWriter writer) throws IOException {
        Truck truck = new Truck(truckID, maxCapacity);
        boolean isAdded = false;

        // If that parking lots exists, insert the truck in its waiting queue.
        // Also add that parking lot to the lotsWaiting AVL Tree
        if (parkingLotsHashing.get(maxCapacity) != null) {
            ParkingLot parkingLot = parkingLotsHashing.get(maxCapacity);
            if (!parkingLot.isFull()) { // If the parking lot is not full, insert the truck.
                parkingLot.addTruckWQ(truck); // Add the truck into the waiting queue
                lotsWaiting.insert(parkingLot.capacityConstraint); // Add the parking lot to the lotsWaiting AVL Tree
                isAdded = true;
                writer.write(String.valueOf(maxCapacity)); // Write to the file
                writer.newLine();
            }
        }

        while (!isAdded) { // If the truck is not added, search for a predecessor.

            // Search for a predecessor in the AVL Tree
            if (parkingLotIDs.findGreatestMin(maxCapacity) == null) { // No such parking lot exists, write -1
                writer.write("-1");
                writer.newLine();
                break;
            } else {
                int greatestMinID = parkingLotIDs.findGreatestMin(maxCapacity).parkingLotCapacityConstraint; // Get the predecessor parking lot ID
                ParkingLot greatestMinLot = parkingLotsHashing.get(greatestMinID);
                if (!greatestMinLot.isFull()) { // If the parking lot is not full, insert the truck.
                    greatestMinLot.addTruckWQ(truck); // Add the truck into the waiting queue
                    isAdded = true;
                    lotsWaiting.insert(greatestMinLot.capacityConstraint); // Add the parking lot to the lotsWaiting AVL Tree
                    writer.write(String.valueOf(greatestMinID)); // Write to the file
                    writer.newLine();
                }
                maxCapacity = greatestMinID; // update the max capacity
            }
        }
    }

    /**
     * This method moves the trucks from waiting queue to the ready queue.
     * If there is no truck in the specified parking lot's waiting queue or there is no such parking lot, it searches for successor parking lot.
     * @param capacityConstraint Capacity constraint of the parking lot
     * @param parkingLotIDs AVL Tree which contains all parking lots, this ID is the same as capacity constraint of that parking lot
     * @param lotsWaiting AVL Tree which contains the parking lots that have truck in their waiting queues
     * @param lotsReady AVL Tree which contains the parking lots that have truck in their ready queues
     * @param parkingLotsHashing Array containing parking lots
     * @param writer Buffered Writer object
     */
    public static void ready(int capacityConstraint, AVLTreeClass parkingLotIDs, AVLTreeClass lotsWaiting, AVLTreeClass lotsReady,HashingArray parkingLotsHashing, BufferedWriter writer) throws IOException {

        boolean isAdded = false;

        // If that parking lots exists, move the truck in its ready queue.
        // Also add that parking lot to the lotsReady AVL Tree
        if (parkingLotsHashing.get(capacityConstraint) != null) { // If the parking lot is not full, move the truck.
            ParkingLot parkingLot = parkingLotsHashing.get(capacityConstraint);
            if (!parkingLot.waitingQueue.isEmpty()) { // If the waiting queue is not empty, move the truck to the ready queue
                Truck truck = parkingLot.readyTruck();
                lotsReady.insert(parkingLot.capacityConstraint); // Add the parking lot to the lotsReady AVL Tree
                isAdded = true;

                // Check if the parking lot is in lostWaiting and is there any other truck in its waiting queue
                // If there is no truck left, remove the parking lot from lotsWaiting
                if (lotsWaiting.contains(parkingLot.capacityConstraint)) {
                    if (parkingLot.waitingQueue.isEmpty()) {
                        lotsWaiting.remove(parkingLot.capacityConstraint);
                    }
                }

                // Write to the file
                writer.write(String.valueOf(truck.ID) + " " + String.valueOf(capacityConstraint));
                writer.newLine();
            }
        }

        // There is no truck in the parking lot's waiting queue, so search for a successor parking lot

        while (!isAdded) { // Search in the lotsWaiting AVL Tree

            if (lotsWaiting.findLowestMax(capacityConstraint) == null) { // No such node exists write -1
                writer.write("-1");
                writer.newLine();
                break;
            } else {
                int lowestMaxID = lotsWaiting.findLowestMax(capacityConstraint).parkingLotCapacityConstraint;
                ParkingLot lowestMaxLot = parkingLotsHashing.get(lowestMaxID);
                if (!lowestMaxLot.waitingQueue.isEmpty()) { // If the waiting queue is not empty, move the truck to the ready queue
                    Truck truck = lowestMaxLot.readyTruck();
                    lotsReady.insert(lowestMaxLot.capacityConstraint); // Add the parking lot to the lotsReady AVL Tree
                    isAdded = true;

                    // Check if the parking lot is in lostWaiting and is there any other truck in its waiting queue
                    // If there is no truck left, remove the parking lot from lotsWaiting
                    if (lotsWaiting.contains(lowestMaxLot.capacityConstraint)) {
                        if (lowestMaxLot.waitingQueue.isEmpty()) {
                            lotsWaiting.remove(lowestMaxLot.capacityConstraint);
                        }
                    }

                    // Write to the file
                    writer.write(String.valueOf(truck.ID) + " " + String.valueOf(lowestMaxID));
                    writer.newLine();
                }
                capacityConstraint = lowestMaxID; // update the capacity constraint
            }
        }
    }

    /**
     * Loads the trucks and relocates them.
     * @param capacity Capacity constraint of trucks
     * @param totalAmount The total amount of load that is going to be distributed
     * @param parkingLotIDs AVL Tree which contains all parking lots, this ID is the same as capacity constraint of that parking lot
     * @param lotsWaiting AVL Tree which contains the parking lots that have truck in their waiting queues
     * @param lotsReady AVL Tree which contains the parking lots that have truck in their ready queues
     * @param parkingLotsHashing Array containing parking lots
     * @param writer Buffered Writer object
     */
    public static void load(int capacity, int totalAmount, AVLTreeClass parkingLotIDs, AVLTreeClass lotsWaiting,AVLTreeClass lotsReady, HashingArray parkingLotsHashing, BufferedWriter writer) throws IOException  {

        int counter = 0; // When a truck is loaded, increase the counter.

        // If that parking lot exists and there is a truck inside its ready queue, load the trucks.
        if (parkingLotsHashing.get(capacity) != null) {
            ParkingLot parkingLot = parkingLotsHashing.get(capacity);
            while (!parkingLot.readyQueue.isEmpty() && totalAmount > 0) {
                Truck truck = parkingLot.removeTruckRQ(); // Remove the truck from the ready queue of the parking lot

                // Check if the parking lot is in lotsReady and is there any other truck in its ready queue.
                // If there is no truck left, remove the parking lot from lotsReady.
                if (lotsReady.contains(parkingLot.capacityConstraint)) {
                    if (parkingLot.readyQueue.isEmpty()) {
                        lotsReady.remove(parkingLot.capacityConstraint);
                    }
                }

                int loadedAmount = truck.load(totalAmount, parkingLot.capacityConstraint); // Load the truck
                totalAmount = totalAmount - loadedAmount; // Truck is loaded, next relocate it

                // If the truck is full, reset the load and relocate it.
                // If the truck is not full, relocate it according to its capacity constraint.
                // If there is no such parking lot, search for a predecessor parking lot.
                // Check counter if there is any truck loaded for output writing purposes.
                if (truck.isFull()) {
                    truck.load = 0;
                    if (counter == 0) {
                        relocateTruck(false,truck, parkingLotIDs, lotsWaiting,parkingLotsHashing, writer);
                    } else {
                        relocateTruck(true,truck, parkingLotIDs, lotsWaiting,parkingLotsHashing, writer);
                    }
                    counter++;
                } else {
                    if (counter == 0) {
                        relocateTruck(false,truck, parkingLotIDs, lotsWaiting,parkingLotsHashing, writer);
                    } else {
                        relocateTruck(true,truck, parkingLotIDs, lotsWaiting,parkingLotsHashing, writer);
                    }
                    counter++;
                }
            }
        }

        // The parking lot does not exist ,so search for a predecessor parking lot in lotsReady AVL Tree.
        while (totalAmount > 0) {
            if (lotsReady.findLowestMax(capacity) == null) { // No such node exists write -1 to the file
                if (counter == 0) {
                    writer.write("-1");
                    break;
                }
                break;
            } else {
                int lowestMaxID = lotsReady.findLowestMax(capacity).parkingLotCapacityConstraint;
                ParkingLot lowestMaxLot = parkingLotsHashing.get(lowestMaxID);

                while (!lowestMaxLot.readyQueue.isEmpty() && totalAmount > 0) {
                    Truck truck = lowestMaxLot.removeTruckRQ(); // Remove the truck from the ready queue of the parking lot

                    // Check if the parking lot is in lotsReady and is there any other truck in its ready queue.
                    // If there is no truck left, remove the parking lot from lotsReady.
                    if (lotsReady.contains(lowestMaxLot.capacityConstraint)) {
                        if (lowestMaxLot.readyQueue.isEmpty()) {
                            lotsReady.remove(lowestMaxLot.capacityConstraint);
                        }
                    }

                    int loadedAmount = truck.load(totalAmount, lowestMaxLot.capacityConstraint); // Load the truck
                    totalAmount = totalAmount - loadedAmount; // Truck is loaded, next relocate it

                    // If the truck is full, reset the load and relocate it.
                    // If the truck is not full, relocate it according to its capacity constraint.
                    // If there is no such parking lot, search for a predecessor parking lot.
                    // Check counter if there is any truck loaded for output writing purposes.
                    if (truck.isFull()) {
                        truck.load = 0;
                        if (counter == 0) {
                            relocateTruck(false,truck, parkingLotIDs,lotsWaiting, parkingLotsHashing, writer);

                        } else {
                            relocateTruck(true,truck, parkingLotIDs, lotsWaiting,parkingLotsHashing, writer);
                        }
                        counter++;
                    } else {
                        if (counter == 0) {
                            relocateTruck(false,truck, parkingLotIDs, lotsWaiting,parkingLotsHashing, writer);

                        } else {
                            relocateTruck(true,truck, parkingLotIDs, lotsWaiting,parkingLotsHashing, writer);
                        }
                        counter++;
                    }
                }
                capacity = lowestMaxID; // update the capacity
            }
        }
        writer.newLine();
    }

    /**
     * Method for relocating trucks according to their capacity constraints
     * @param isWritten If there is any truck loaded during the method execution, it is true.
     * @param truck Truck object
     * @param parkingLotIDs AVL Tree which contains all parking lots, this ID is the same as capacity constraint of that parking lot
     * @param lotsWaiting AVL Tree which contains the parking lots that have truck in their waiting queues
     * @param parkingLotsHashing Array containing parking lots
     * @param writer Buffered Writer object
     */
    public static void relocateTruck(boolean isWritten, Truck truck, AVLTreeClass parkingLotIDs, AVLTreeClass lotsWaiting, HashingArray parkingLotsHashing, BufferedWriter writer) throws IOException {

        int capacityConstraint = truck.maximumCapacity - truck.load;
        boolean isAdded = false;

        // If the parking lot exists and it is not full add the truck.
        if (parkingLotsHashing.get(capacityConstraint) != null) {
            ParkingLot parkingLot = parkingLotsHashing.get(capacityConstraint);
            if (!parkingLot.isFull()) {
                parkingLot.addTruckWQ(truck); // Add the truck into the waiting queue of the parking lot
                lotsWaiting.insert(parkingLot.capacityConstraint); // Add the parking lot to the lotsWaiting AVL Tree
                isAdded = true;

                // Write to the file
                if (isWritten) {
                    writer.write(" - " + truck.ID + " " + String.valueOf(capacityConstraint));
                } else {
                    writer.write(truck.ID + " " + String.valueOf(capacityConstraint));
                }
            }
        }

        while (!isAdded) { // If there is no such parking lot, search for a predecessor in the AVL Tree.

            if (parkingLotIDs.findGreatestMin(capacityConstraint) == null) { // No such node exists write -1
                if (isWritten) {
                    writer.write(" - " + truck.ID + " " +  "-1");
                    break;
                } else {
                    writer.write(truck.ID + " " +  "-1");
                    break;
                }
            } else {
                int greatestMinID = parkingLotIDs.findGreatestMin(capacityConstraint).parkingLotCapacityConstraint;
                ParkingLot greatestMinLot = parkingLotsHashing.get(greatestMinID);
                if (!greatestMinLot.isFull()) {
                    greatestMinLot.addTruckWQ(truck); // Add the truck into the waiting queue of the parking lot
                    lotsWaiting.insert(greatestMinLot.capacityConstraint); // Add the parking lot to the lotsWaiting AVL Tree
                    isAdded = true;

                    // Write to the file
                    if (isWritten) {
                        writer.write(" - " + truck.ID + " " +  String.valueOf(greatestMinID));
                    } else {
                        writer.write(truck.ID + " " + String.valueOf(greatestMinID));
                    }
                }
                capacityConstraint = greatestMinID;
            }
        }
    }

    /**
     * Method for deleting parking lots
     * @param capacity Capacity constraint of the parking lot
     * @param parkingLotIDs AVL Tree which contains all parking lots, this ID is the same as capacity constraint of that parking lot
     * @param lotsWaiting AVL Tree which contains the parking lots that have truck in their waiting queues
     * @param lotsReady AVL Tree which contains the parking lots that have truck in their ready queues
     * @param parkingLotsHashing Array containing parking lots
     * @param writer Buffered Writer object
     */
    public static void deleteLot(int capacity, AVLTreeClass parkingLotIDs, AVLTreeClass lotsWaiting, AVLTreeClass lotsReady, HashingArray parkingLotsHashing, BufferedWriter writer) throws IOException  {
        // Remove the parking lot from all collections.
        if (parkingLotsHashing.get(capacity) == null) {
            return;
        } else {
            parkingLotsHashing.remove(capacity);
            parkingLotIDs.remove(capacity);
            lotsWaiting.remove(capacity);
            lotsReady.remove(capacity);
        }

    }

    /**
     * Method for counting the number of trucks that have larger capacity constraint than given capacity constraint
     * @param capacity Capacity constraint of the parking lot
     * @param parkingLotIDs AVL Tree which contains all parking lots, this ID is the same as capacity constraint of that parking lot
     * @param parkingLotsHashing Array containing parking lots
     * @param writer Buffered Writer object
     */
    public static void countTrucks(int capacity, AVLTreeClass parkingLotIDs, HashingArray parkingLotsHashing, BufferedWriter writer) throws IOException {

        int counter = 0; // Keep number of trucks

        while (true) { // Traverse on the AVL
            if (parkingLotIDs.findLowestMax(capacity) == null) {
                break;
            } else {
                int lowestMaxID = parkingLotIDs.findLowestMax(capacity).parkingLotCapacityConstraint;
                ParkingLot lowestMaxLot = parkingLotsHashing.get(lowestMaxID);
                counter = counter + lowestMaxLot.truckCount;
                capacity = lowestMaxID;
            }
        }
        // Write to the file
        writer.write(String.valueOf(counter));
        writer.newLine();
    }

}
