/**
 * Min heap implementation
 * @author Alaaddin Eren Namlı
 * @since Date: 19.12.2024
 */
public class MinHeap {

    public Node[] innerArray;
    public int capacity = 100;
    public int size;

    public MinHeap() {
        this.innerArray = new Node[capacity];
        this.size = 0;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public void enlarge(int newSize) { // Create bigger array and copy the elements
        Node[] newArray = new Node[newSize];

        for (int i = 0; i < innerArray.length; i++) {
            if (innerArray[i] != null) {
                Node node = innerArray[i];
                newArray[i] = node;
            }
        }
        this.innerArray = newArray;
    }

    public Node findMin() { // Return the minimum element
        return innerArray[1];
    }

    public void insert(Node node) { // Insert to the heap by comparing distance

        if (size == innerArray.length-1) {
            enlarge(innerArray.length*2 + 1);
        }

        int hole = ++size;
        double distance = node.distance;
        for (innerArray[0]=node; distance < innerArray[hole/2].distance; hole = hole/2) {
            innerArray[hole] = innerArray[hole/2];
        }
        innerArray[hole] = node;
    }

    public Node deleteMin() { // Delete the minimum element
        if (isEmpty()) {
            System.out.println("It is empty");
            return null;
        }

        Node minElement = findMin();
        innerArray[1] = innerArray[size--];
        percolateDown(1);
        return minElement;
    }

    public void percolateDown(int hole) { // Percolate down method. It compares distance values

        int child;
        Node tmp = innerArray[hole];

        for (; hole*2 <= size ; hole = child) {
            child = hole*2;
            if (child != size && innerArray[child+1].distance < innerArray[child].distance) {
                child++;
            }
            if (innerArray[child].distance < tmp.distance ) {
                innerArray[hole] = innerArray[child];
            } else {
                break;
            }
        }
        innerArray[hole] = tmp;
    }

    public void heapPrint() { // Print the heap
        int i = 0;
        for (Node node: innerArray) {
            if (node != null) {
                if (i <= size) {
                    System.out.println(node.distance + " " +i);
                }
            }
            i++;
        }
    }

}
