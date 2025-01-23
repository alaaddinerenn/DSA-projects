/**
 * HashTableUsers class, hash table implementation to store user objects
 * Separate chaining
 * @author Alaaddin Eren Namlı
 * @since Date: 21.11.2024
 */
public class HashTableUsers {

    public HashNodeUsers[] array;
    public int tableSize;
    public int numOfelements;

    public HashTableUsers() { // Constructor
        this.array = new HashNodeUsers[100]; // 100 DÜ YİNE
        this.tableSize = 100; // 50 İDİ
        this.numOfelements = 0;
    }

    public int hash(String string) { // Hashcode method, returns the index
        return Math.abs(string.hashCode()) % tableSize;
    }

    public HashNodeUsers get(String ID) { // Get the user node according to ID
        int code = hash(ID);
        HashNodeUsers node = array[code];

        while (node != null) {
            if (node.ID.equals(ID)) {
                return node;
            }
            node = node.next;
        }
        return null;
    }

    public void insert(String ID) { // Insert to the hash table, if load factor exceeds 0.70, rehash
        if ((double) numOfelements / tableSize > 0.70) { // If load factor exceeds 0.70, rehash
            rehash();
        }

        int code = hash(ID);
        HashNodeUsers newNode = new HashNodeUsers(ID);

        if (array[code] == null) {
            array[code] = newNode;
        } else {
            newNode.next = array[code];
            array[code] = newNode;
        }
        numOfelements++;
    }

    public int search(String ID) { // Search in the hash table. If the node does not exists, returns -1
        int code = hash(ID);

        if (array[code] == null) {
            return -1;
        } else {
            HashNodeUsers node = array[code];
            while (node != null) {
                if (node.ID.equals(ID)) {
                    return hash(ID);
                }
                node = node.next;
            }
        }
        return -1;
    }

    public boolean delete(String ID) { // Delete the node from the hash table
        int code = hash(ID);
        if (array[code] == null) {
            return false; // There is no linked list, totally empty
        } else {
            HashNodeUsers node = array[code];
            HashNodeUsers previous = null;
            while (node != null) {
                if (node.ID.equals(ID)) { // Found the node
                    if (previous == null) {
                        array[code] = node.next; // There is only 1 node
                    } else {
                        previous.next = node.next;
                    }
                    numOfelements--;
                    return true;
                }
                previous = node;
                node = node.next;
            }
        }
        return false; // Key not found even there is a linked list
    }

    public void rehash() { // Rehashing method. Hash every element of the old table into the bigger hash table
        HashNodeUsers[] newArray = new HashNodeUsers[tableSize*2];

        for (HashNodeUsers hashNode : array) {
            if (hashNode==null){
                continue; // If there is no node, continue
            }
            while (hashNode != null) {
                String ID = hashNode.ID;
                int code = Math.abs(ID.hashCode()) % (tableSize*2); // Hash according to the new table size
                HashNodeUsers newNode = new HashNodeUsers(hashNode.ID, hashNode.userObject);

                if (newArray[code] == null) {
                    newArray[code] = newNode;
                } else {
                    newNode.next = newArray[code];
                    newArray[code] = newNode;
                }
                hashNode = hashNode.next;
            }
        }
        this.array = newArray;
        this.tableSize = tableSize*2;
    }

    public void hashPrint() { // Print the hash table
        int counter = 0;
        for (HashNodeUsers hashNodeUsers : array) {
            if (hashNodeUsers != null) {
                if (hashNodeUsers.next == null) {
                    System.out.println(hashNodeUsers + " " + counter);
                    counter++;
                    continue;
                }
                while (hashNodeUsers.next != null) {
                    System.out.print(hashNodeUsers+ " -> " + hashNodeUsers.next);
                    hashNodeUsers = hashNodeUsers.next;
                }
                System.out.println(" " +counter);
            }
            counter++;
        }
    }

}
