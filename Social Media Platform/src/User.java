/**
 * User class
 * @author Alaaddin Eren Namlı
 * @since Date: 21.11.2024
 */
public class User {
    public String ID;
    public HashTableUsers followedUsers;
    public HashTablePosts seenPosts;
    public HashTablePosts ownPosts;
    public HashTablePosts likedPosts;

    public User(String ID) { // Constructor
        this.ID = ID;
        this.followedUsers = new HashTableUsers();
        this.seenPosts = new HashTablePosts();
        this.ownPosts = new HashTablePosts();
        this.likedPosts = new HashTablePosts();
    }

    @Override
    public String toString() {
        return "User{" +
                "ID='" + ID + '\'' +
                ", followedUsers=" + followedUsers +
                ", seenPosts=" + seenPosts +
                ", ownPosts=" + ownPosts +
                ", likedPosts=" + likedPosts +
                '}';
    }
}
