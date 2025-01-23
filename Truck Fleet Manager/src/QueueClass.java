import java.util.ArrayList;
import java.util.Iterator;
/**
 * Queue class implemented with ArrayList
 * @author Alaaddin Eren Namlı
 * @since Date: 07.11.2024
 */
public class QueueClass implements Iterable<Truck> {

    private ArrayList<Truck> innerArrayList;

    public QueueClass() { // Constructor
        innerArrayList = new ArrayList<>();
    }

    public int size(){
        if(innerArrayList.isEmpty()) {
            return 0;
        } else {
            return innerArrayList.size();
        }
    }

    public boolean isEmpty() {
        return innerArrayList.isEmpty();
    }

    public void enqueue(Truck truck) {
        innerArrayList.addLast(truck);
    }

    public Truck dequeue() {
        if (isEmpty()) {
            System.out.println("Queue is already empty");
        }
        return innerArrayList.removeFirst();
    }

    public Truck peek() {
        if (isEmpty()) {
            System.out.println("Queue is empty");
        }
        return innerArrayList.get(0);
    }

    @Override
    public String toString() {
        return "QueueClass{" +
                "innerArrayList=" + innerArrayList +
                '}';
    }

    @Override
    public Iterator<Truck> iterator() {
        return innerArrayList.iterator();
    }

}
