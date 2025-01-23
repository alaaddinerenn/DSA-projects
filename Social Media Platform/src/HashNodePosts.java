/**
 * HashNodePosts class for nodes in hash table
 * @author Alaaddin Eren Namlı
 * @since Date: 21.11.2024
 */
public class HashNodePosts {

    public HashNodePosts next;
    public String ID;
    public Post postObject;

    public HashNodePosts(String userID, String ID, String content) { // Constructor
        this.ID = ID;
        this.postObject = new Post(userID,ID,content);
    }

    public HashNodePosts(String ID, Post postObject) { // Constructor
        this.ID = ID;
        this.postObject = postObject;
    }

    @Override
    public String toString() {
        return "HashNodePosts{" +
                "next=" + next +
                ", ID='" + ID + '\'' +
                ", postObject=" + postObject +
                '}';
    }
}
