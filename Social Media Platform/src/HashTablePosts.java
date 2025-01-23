/**
 * HashTablePosts class, hash table implementation to store post objects
 * Separate chaining
 * @author Alaaddin Eren Namlı
 * @since Date: 21.11.2024
 */
public class HashTablePosts {

    public HashNodePosts[] array;
    public int tableSize;
    public int numOfelements;

    public HashTablePosts() { // Constructor
        this.array = new HashNodePosts[100];
        this.tableSize = 100;
        this.numOfelements = 0;
    }

    public int hash(String string) { // Hashcode method, returns the index
        return Math.abs(string.hashCode()) % tableSize;
    }

    public HashNodePosts get(String ID) { // Get the post node according to ID
        int code = hash(ID);
        HashNodePosts node = array[code];

        while (node != null) {
            if (node.ID.equals(ID)) {
                return node;
            }
            node = node.next;
        }
        return null;
    }

    public void insert(String userID, String ID, String content) { // Insert to the hash table, if load factor exceeds 0.70, rehash
        if ((double) numOfelements / tableSize > 0.70) { // If load factor exceeds 0.70, rehash
            rehash();
        }

        int code = hash(ID);
        HashNodePosts newNode = new HashNodePosts(userID,ID,content);

        if (array[code] == null) {
            array[code] = newNode;
        } else {
            newNode.next = array[code];
            array[code] = newNode;
        }
        numOfelements++;
    }

    public int search(String ID) { // Search in the hash table. If the node does not exist, returns -1
        int code = hash(ID);
        if (array[code] == null) {
            return -1;
        } else {
            HashNodePosts node = array[code];
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
            HashNodePosts node = array[code];
            HashNodePosts previous = null;
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
        return false; // Key is not found
    }

    public void rehash() { // Rehashing method. Hash every element of the old table into the bigger hash table

        HashNodePosts[] newArray = new HashNodePosts[tableSize*2];

        for (HashNodePosts hashNode : array) {
            if (hashNode==null){
                continue; // If there is no node, continue
            }
            while (hashNode != null) {
                String ID = hashNode.ID;
                int code = Math.abs(ID.hashCode()) % (tableSize*2); // Hash according to the new table size
                HashNodePosts newNode = new HashNodePosts(hashNode.ID, hashNode.postObject);

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
        for (HashNodePosts hashNodePosts : array) {
            if (hashNodePosts != null) {
                if (hashNodePosts.next == null) {
                    System.out.println(hashNodePosts + " " + counter);
                    counter++;
                    continue;
                }
                while (hashNodePosts.next != null) {
                    System.out.print(hashNodePosts+ " -> " + hashNodePosts.next);
                    hashNodePosts = hashNodePosts.next;
                }
                System.out.println(" " +counter);
            }
            counter++;
        }
    }

    public void copyHashTable(HashTablePosts target) { // Copy the elements of the hash table to the target table
        for (HashNodePosts hashNodePosts: this.array) {  // Traverse the table
            while (hashNodePosts != null) {
                Post post = hashNodePosts.postObject;
                target.insert(post.userID, post.postID, post.content);
                hashNodePosts = hashNodePosts.next;
            }
        }
    }

    /**
     * This method creates the hash table consisting posts of the followed users
     * @param hashTableUsers Hash table consisting followed users of the user
     * @param allUsers Hash table storing all users
     * @return Hash table consisting posts of the followed users
     */
    public HashTablePosts createFollowedPostsTable(HashTableUsers hashTableUsers, HashTableUsers allUsers) { // Get followed users' posts
        HashNodeUsers[] userArray = hashTableUsers.array;
        HashTablePosts followedPosts = new HashTablePosts();
        for (HashNodeUsers node: userArray) { // Traverse the table
            while (node != null) {
                User user = allUsers.get(node.userObject.ID).userObject;
                HashTablePosts usersPosts = user.ownPosts;
                usersPosts.copyHashTable(followedPosts);
                node = node.next;
            }
        }
        return followedPosts;
    }

    /**
     * This method creates the max heap consisting posts of the followed users of the user
     * @param followedPosts Hash table consisting posts of the followed users
     * @param realUser The user
     * @param allPosts Hash table storing all posts
     * @return The max heap consisting posts of the followed users of the user
     */
    public MaxHeap createFollowedPostsHeap(HashTablePosts followedPosts, User realUser, HashTablePosts allPosts) { // Build heap from the followed users' posts
        HashNodePosts[] followedPostsArray = followedPosts.array;
        MaxHeap followedPostsHeap = new MaxHeap();
        for (HashNodePosts node: followedPostsArray) {  // Traverse the table
            while (node != null) {
                Post post = allPosts.get(node.postObject.postID).postObject;
                if (realUser.ownPosts.search(post.postID) == -1 && realUser.seenPosts.search(post.postID) == -1) {
                    followedPostsHeap.insert(post);
                }
                node = node.next;
            }
        }
        return followedPostsHeap;
    }

}
