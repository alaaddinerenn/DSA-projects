import java.io.*;
import java.util.ArrayList;
/**
 * Program to manage social media platform.
 * @author Alaaddin Eren Namlı
 * @since Date: 21.11.2024
 */
public class Main {

    public static void main(String[] args) throws IOException {

        BufferedReader reader = new BufferedReader(new FileReader(args[0]));
        BufferedWriter writer = new BufferedWriter(new FileWriter(args[1], true));

        // Create hash tables and heap to store users and posts
        HashTableUsers allUsers = new HashTableUsers(); // Hash table for all users
        HashTablePosts allPosts = new HashTablePosts(); // Hash table for all posts
        MaxHeap allPostsHeap = new MaxHeap(); // Heap for all posts

        String line; // To read line by line
        while ((line = reader.readLine()) != null) { // This loop reads the input file and calls the relevant methods
            String[] words = line.split(" ");
            if (words[0].equals("create_user")) {
                createUser(words[1], allUsers, writer);
            } else if (words[0].equals("follow_user")) {
                followUser(words[1], words[2], allUsers, writer);
            } else if (words[0].equals("unfollow_user")) {
                unfollowUser(words[1], words[2], allUsers, writer);
            } else if (words[0].equals("create_post")) {
                createPost(words[1], words[2], words[3], allUsers, allPosts, allPostsHeap,writer);
            } else if (words[0].equals("see_post")) {
                seePost(words[1], words[2], allUsers, allPosts, writer);
            } else if (words[0].equals("see_all_posts_from_user")) {
                seeAllPosts(words[1], words[2], allUsers, allPosts, writer);
            } else if (words[0].equals("toggle_like")) {
                pressLikeButton(words[1], words[2], allUsers, allPosts, writer);
            } else if (words[0].equals("generate_feed")) {
                generateFeed(words[1], Integer.parseInt(words[2]), allUsers, allPosts, allPostsHeap, writer);
            } else if (words[0].equals("scroll_through_feed")) {
                ArrayList<Integer> numbers = new ArrayList<>();
                for (int i = 2; i < words.length; i++) {
                    numbers.add(Integer.parseInt(words[i]));
                }
                scrollThroughFeed(words[1], numbers, allUsers, allPosts, allPostsHeap, writer);
            } else if (words[0].equals("sort_posts")) {
                sortPosts(words[1], allUsers, allPosts, writer);
            }
        }

        // Close files
        reader.close();
        writer.close();
    }

    /**
     * Method that creates users
     * @param ID userID
     * @param allUsers Hash table storing all users
     * @param writer Buffered writer object
     */
    public static void createUser(String ID, HashTableUsers allUsers, BufferedWriter writer) throws IOException{

         if (allUsers.search(ID) == -1) { // If user does not exist, create it
             allUsers.insert(ID);
             writer.write("Created user with Id " + ID + ".");
             writer.newLine();
         } else { // If user already exists, give error
             writer.write("Some error occurred in create_user.");
             writer.newLine();
         }

    }

    /**
     * This method allows users to follow another user
     * @param userID1 UserID for user1
     * @param userID2 UserID for user2
     * @param allUsers Hash table storing all users
     * @param writer Buffered writer object
     */
    public static void followUser(String userID1, String userID2, HashTableUsers allUsers, BufferedWriter writer) throws IOException {

        if (allUsers.search(userID1) == -1 || allUsers.search(userID2) == -1) { // If one of the users does not exist, give error
            writer.write("Some error occurred in follow_user.");
            writer.newLine();
            return;
        }
        // If the code reaches here, we can say that both users exist

        if (userID1.equals(userID2)) { // If users are the same, give error
            writer.write("Some error occurred in follow_user.");
            writer.newLine();
            return;
        }
        // If the code reaches here, we can say that users are not the same

        User user1 = allUsers.get(userID1).userObject;

        if (user1.followedUsers.search(userID2) == -1) { // If user1 does not follow user2, insert user2 to the followed users hash table
            user1.followedUsers.insert(userID2);
            writer.write(userID1+ " followed " + userID2 + ".");
            writer.newLine();
        } else { // If user1 is already following user2, nothing changes
            writer.write("Some error occurred in follow_user.");
            writer.newLine();
        }
    }

    /**
     * This method allows users to unfollow another user
     * @param userID1 UserID for user1
     * @param userID2 UserID for user2
     * @param allUsers Hash table storing all users
     * @param writer Buffered writer object
     */
    public static void unfollowUser(String userID1, String userID2, HashTableUsers allUsers, BufferedWriter writer) throws IOException {

        if (allUsers.search(userID1) == -1 || allUsers.search(userID2) == -1) { // If one of the users does not exist, give error
            writer.write("Some error occurred in unfollow_user.");
            writer.newLine();
            return;
        }
        // If the code reaches here, we can say that both users exist

        if (userID1.equals(userID2)) { // If users are the same, give error
            writer.write("Some error occurred in unfollow_user.");
            writer.newLine();
            return;
        }
        // If the code reaches here, we can say that users are not the same

        User user1 = allUsers.get(userID1).userObject;

        if (user1.followedUsers.search(userID2) != -1) { // If user1 follows user2, delete user2 from the followed users hash table
            user1.followedUsers.delete(userID2);
            writer.write(userID1 + " unfollowed " + userID2 + ".");
            writer.newLine();
        } else { // If user1 is already unfollowing user2, nothing changes
            writer.write("Some error occurred in unfollow_user.");
            writer.newLine();
        }
    }

    /**
     * Method that creates posts
     * @param userID ID of the user
     * @param postID ID of the new post
     * @param content Content of the new post
     * @param allUsers Hash table storing all users
     * @param allPosts Hash table storing all posts
     * @param allPostsHeap Max Heap storing all posts
     * @param writer Buffered writer object
     */
    public static void createPost(String userID, String postID, String content, HashTableUsers allUsers, HashTablePosts allPosts, MaxHeap allPostsHeap, BufferedWriter writer) throws IOException {

        if (allUsers.search(userID) == -1) { // If the user does not exist, give error
            writer.write("Some error occurred in create_post.");
            writer.newLine();
            return;
        }
        // If the code reaches here, we can say that the user exists
        User user = allUsers.get(userID).userObject;

        if (allPosts.search(postID) == -1) { // If the post does not already exist, create the post
            allPosts.insert(userID,postID,content); // Add to the hash table
            allPostsHeap.insert(new Post(userID,postID,content)); // Add to the heap
            user.ownPosts.insert(userID,postID,content); // Add to the hash table for user's posts
            writer.write(userID + " created a post with Id " + postID + ".");
            writer.newLine();
        } else { // If the post already exists, nothing changes
            writer.write("Some error occurred in create_post.");
            writer.newLine();
        }
    }

    /**
     * This method allows users to see posts
     * @param userID ID of the user
     * @param postID ID of the post
     * @param allUsers Hash table storing all users
     * @param allPosts Hash table storing all posts
     * @param writer Buffered writer object
     */
    public static void seePost(String userID, String postID, HashTableUsers allUsers, HashTablePosts allPosts, BufferedWriter writer) throws IOException {

        if (allUsers.search(userID) == -1 || allPosts.search(postID) == -1) { // If the user or the post does not exist, give error
            writer.write("Some error occurred in see_post.");
            writer.newLine();
            return;
        }
        // If the code reaches here, we can say that both the user and the post exist

        User user = allUsers.get(userID).userObject;
        Post post = allPosts.get(postID).postObject;

        if (user.seenPosts.search(postID) == -1) { // If the user does not already see the post, insert the post into the seen posts hash table
            user.seenPosts.insert(post.userID,postID,post.content);
            writer.write(userID + " saw " + postID + ".");
            writer.newLine();
        } else { // If the user already see the post, just write to the file
            writer.write(userID + " saw " + postID + ".");
            writer.newLine();
        }
    }

    /**
     * This method allows users to see another user's all posts
     * @param user1ID UserID for user1
     * @param user2ID UserID for user2
     * @param allUsers Hash table storing all users
     * @param allPosts Hash table storing all posts
     * @param writer Buffered writer object
     */
    public static void seeAllPosts(String user1ID, String user2ID, HashTableUsers allUsers, HashTablePosts allPosts, BufferedWriter writer) throws IOException {

        if (allUsers.search(user1ID) == -1 || allUsers.search(user2ID) == -1) { // If one of the users does not exist, give error
            writer.write("Some error occurred in see_all_posts_from_user.");
            writer.newLine();
            return;
        }
        // If the code reaches here, we can say that both users exist
        User user1 = allUsers.get(user1ID).userObject;
        User user2 = allUsers.get(user2ID).userObject;

        user2.ownPosts.copyHashTable(user1.seenPosts); // Copy every post of user2 to the seenPosts of user1

        writer.write(user1ID + " saw all posts of " + user2ID + "."); // Print the message
        writer.newLine();
    }

    /**
     * This method allows users to like or unlike the posts
     * @param userID ID for the user
     * @param postID ID for the post
     * @param allUsers Hash table storing all users
     * @param allPosts Hash table storing all posts
     * @param writer Buffered writer object
     */
    public static void pressLikeButton(String userID, String postID, HashTableUsers allUsers, HashTablePosts allPosts, BufferedWriter writer) throws IOException {

        if (allUsers.search(userID) == -1 || allPosts.search(postID) == -1) { // If the user or the post does not exist, give error
            writer.write("Some error occurred in toggle_like.");
            writer.newLine();
            return;
        }
        // If the code reaches here, we can say that both the user and the post exist
        User user = allUsers.get(userID).userObject;
        Post post = allPosts.get(postID).postObject;

        if (user.likedPosts.search(postID) == -1) { // If the user does not already like the post, insert the post into the user's liked posts hash table
            user.likedPosts.insert(post.userID,postID,post.content); // insert the post object into the hash table
            post.likeCount++;
            writer.write(userID + " liked " + postID + ".");
            writer.newLine();
        } else { // If the user already like the post, delete the post from the user's liked posts hash table
            user.likedPosts.delete(postID); // delete the post object from the hash table
            post.likeCount--;
            writer.write(userID + " unliked " + postID + ".");
            writer.newLine();
        }

        // If the user does not see the post before, insert the post into the user's seen posts hash table
        if (user.seenPosts.search(postID) == -1) {
            user.seenPosts.insert(post.userID, postID, post.content);
        }

    }

    /**
     * This method generates feed for user's according to their interactions
     * @param userID ID for the user
     * @param num Number of posts in the feed
     * @param allUsers Hash table storing all users
     * @param allPosts Hash table storing all posts
     * @param allPostsHeap Max Heap storing all users
     * @param writer Buffered writer object
     */
    public static void generateFeed(String userID, int num, HashTableUsers allUsers, HashTablePosts allPosts, MaxHeap allPostsHeap, BufferedWriter writer) throws IOException {

        if (allUsers.search(userID) == -1) { // If the user does not exist, give error
            writer.write("Some error occurred in generate_feed.");
            writer.newLine();
            return;
        }
        // If the code reaches here, we can say that the user exists

        User user = allUsers.get(userID).userObject; // Get the user object
        HashTableUsers followedUsers = user.followedUsers; // Get the users that the user follows
        HashTablePosts followedPosts = new HashTablePosts();
        followedPosts = followedPosts.createFollowedPostsTable(followedUsers,allUsers); // Get the followed users' posts in a hash table
        MaxHeap followedPostsHeap = new MaxHeap();
        followedPostsHeap = followedPosts.createFollowedPostsHeap(followedPosts, user, allPosts); // Get the followed users' posts in a max heap

        writer.write("Feed for " + userID + ":");
        writer.newLine();

        int counter = 0;
        while (counter < num) { // Do the process for the given number times
            if (!followedPostsHeap.isEmpty()) { // If the heap is not empty, get the max post and write the message
                Post maxPost = followedPostsHeap.deleteMax();
                writer.write("Post ID: " + maxPost.postID + ", Author: " + maxPost.userID + ", Likes: " + maxPost.likeCount);
                writer.newLine();
            } else { // If the heap is empty, print the message and finish the process
                writer.write("No more posts available for " + userID + ".");
                writer.newLine();
                break;
            }
            counter++;
        }

    }

    /**
     * This method allows users to scroll through their feed
     * @param userID ID for the user
     * @param numbers Arraylist consisting input numbers
     * @param allUsers Hash table storing all users
     * @param allPosts Hash table storing all posts
     * @param allPostsHeap Max Heap storing all users
     * @param writer Buffered writer object
     */
    public static void scrollThroughFeed(String userID, ArrayList<Integer> numbers,HashTableUsers allUsers, HashTablePosts allPosts, MaxHeap allPostsHeap, BufferedWriter writer) throws IOException {

        if (allUsers.search(userID) == -1) { // If the user does not exist, give error
            writer.write("Some error occurred in scroll_through_feed.");
            writer.newLine();
            return;
        }
        // If the code reaches here, we can say that the user exists

        User user = allUsers.get(userID).userObject; // Get the user object
        HashTableUsers followedUsers = user.followedUsers; // Get the users that the user follows
        HashTablePosts followedPosts = new HashTablePosts();
        followedPosts = followedPosts.createFollowedPostsTable(followedUsers,allUsers); // Get the followed users' posts in a hash table
        MaxHeap followedPostsHeap = new MaxHeap();
        followedPostsHeap = followedPosts.createFollowedPostsHeap(followedPosts, user, allPosts); // Get the followed users' posts in a max heap

        writer.write(userID + " is scrolling through feed:");
        writer.newLine();

        int counter = 1;
        while (counter < numbers.size()) { // Do the process for the given number times
            if (!followedPostsHeap.isEmpty()) { // If the heap is not empty, get the max post and write the message according to input numbers
                Post maxPost = followedPostsHeap.deleteMax();
                if (numbers.get(counter) == 1) { // The number is 1, so the user likes the post
                    writer.write(userID + " saw " + maxPost.postID + " while scrolling and clicked the like button.");
                    writer.newLine();
                    if (user.likedPosts.search(maxPost.postID) == -1) { // Insert the post into the user's liked posts hash table
                        user.likedPosts.insert(maxPost.userID,maxPost.postID,maxPost.content);
                        maxPost.likeCount++;
                    }
                    if (user.seenPosts.search(maxPost.postID) == -1) { // Insert the post into the user's seen posts hash table
                        user.seenPosts.insert(maxPost.userID, maxPost.postID, maxPost.content);
                    }
                } else { // The number is 1, so the user just saw it.
                    writer.write(userID + " saw " + maxPost.postID + " while scrolling.");
                    writer.newLine();
                    if (user.seenPosts.search(maxPost.postID) == -1) { // Insert the post into the user's seen posts hash table
                        user.seenPosts.insert(maxPost.userID, maxPost.postID, maxPost.content);
                    }
                }
            } else { // If the heap is empty, print the message and finish the process
                writer.write("No more posts in feed.");
                writer.newLine();
                break;
            }
            counter++;
        }

    }

    /**
     * Method that stores the user's all posts according to their likes
     * @param userID ID for the user
     * @param allUsers Hash table storing all users
     * @param allPosts Hash table storing all posts
     * @param writer Buffered writer object
     */
    public static void sortPosts(String userID, HashTableUsers allUsers, HashTablePosts allPosts, BufferedWriter writer) throws IOException {

        if (allUsers.search(userID) == -1) { // If the user does not exist, give error
            writer.write("Some error occurred in sort_posts.");
            writer.newLine();
            return;
        }
        // If the code reaches here, we can say that the user exists

        User user = allUsers.get(userID).userObject;

        if (user.ownPosts.numOfelements == 0) { // If the user has no posts, give the error
            writer.write("No posts from " + userID + ".");
            writer.newLine();
            return;
        }
        // If the code reaches here, we can say that the user has at least one post

        writer.write("Sorting " + userID + "'s" + " posts:");
        writer.newLine();

        MaxHeap postsHeap = createPostsHeap(user, allPosts); // Insert the user's all posts into a max heap

        while (!postsHeap.isEmpty()) { // Until the heap is empty, delete the most liked post and write it to the file
            Post post = postsHeap.deleteMax();
            writer.write(post.postID + ", Likes: " + post.likeCount);
            writer.newLine();
        }

    }

    /**
     * This method creates a max heap to store a user's all posts
     * @param user User object
     * @param allPosts Hash table storing all posts
     * @return The max heap storing the user's all posts
     */
    public static MaxHeap createPostsHeap(User user, HashTablePosts allPosts) {

        MaxHeap postsHeap = new MaxHeap();
        HashNodePosts[] postsArray = user.ownPosts.array;

        for (HashNodePosts node: postsArray) { // Traverse the hash table and insert the posts to the heap
            while (node != null) {
                Post post = allPosts.get(node.postObject.postID).postObject;
                postsHeap.insert(post);
                node = node.next;
            }
        }
        return postsHeap;
    }

}
