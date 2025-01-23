/**
 * HashNodeUsers class for nodes in hash table
 * @author Alaaddin Eren Namlı
 * @since Date: 21.11.2024
 */
public class HashNodeUsers {

    public HashNodeUsers next;
    public String ID;
    public User userObject; // User

    public HashNodeUsers(String ID) { // Constructor
        this.ID = ID;
        this.userObject = new User(ID);
    }

    public HashNodeUsers(String ID, User userObject) { // Constructor
        this.ID = ID;
        this.userObject = userObject;
    }

    @Override
    public String toString() {
        return "HashNodeUsers{" +
                "next=" + next +
                ", ID='" + ID + '\'' +
                ", userObject=" + userObject +
                '}';
    }
}
