/**
 * Post class
 * @author Alaaddin Eren Namlı
 * @since Date: 21.11.2024
 */
public class Post {
    public String userID;
    public String postID;
    public String content;
    public int likeCount = 0;

    public Post(String postID) { // Constructor
        this.postID = postID;
    }

    public Post(String userID, String postID, String content) { // Constructor
        this.userID = userID;
        this.postID = postID;
        this.content = content;
    }

    @Override
    public String toString() {
        return "Post{" +
                "userID='" + userID + '\'' +
                ", postID='" + postID + '\'' +
                ", content='" + content + '\'' +
                ", likeCount=" + likeCount +
                '}';
    }
}
