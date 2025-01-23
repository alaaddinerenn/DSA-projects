/**
 * Max heap implementation
 * @author Alaaddin Eren Namlı
 * @since Date: 21.11.2024
 */
public class MaxHeap {
    public Post[] innerArray;
    public int capacity = 5;
    public int size;

    public MaxHeap() {
        this.innerArray = new Post[capacity];
        this.size = 0;
    }

    public Post getLeftChild(int i) {
        if (2*i <= size) {
            return innerArray[2*i];
        }
        return null;
    }

    public Post getRightChild(int i) {
        if ((2*i) + 1 <= size) {
            return innerArray[(2*i) +1];
        }
        return null;
    }

    public Post getParent(int i) {
        if (i/2 <= size) {
            return innerArray[i/2];
        }
        return null;
    }

    public boolean isEmpty() {
        return size == 0;
    }


    public void enlarge(int newSize) { // Create bigger array and copy the elements
        Post[] newArray = new Post[newSize];

        for (int i = 0; i < innerArray.length; i++) {
            if (innerArray[i] != null) {
                Post post = innerArray[i];
                newArray[i] = post;
            }
        }
        this.innerArray = newArray;
    }

    public void heapPrint() { // Print the heap
        int i = 0;
        for (Post post: innerArray) {
            if (post != null) {
                if (i <= size) {
                    System.out.println(post.postID + " " + i);
                }
            }
            i++;
        }
    }

    public Post findMax() {
        return innerArray[1];
    }


    public void insert(Post post) { // Insert to the heap, if like counts are equal, consider lexicographical order

        if (size == innerArray.length-1) {
            enlarge(innerArray.length*2 + 1);
        }

        int hole = ++size;
        int x = post.likeCount;
        for (innerArray[0]=post; x >= innerArray[hole/2].likeCount; hole = hole/2) {
            if (x == innerArray[hole/2].likeCount) {
                if (post.postID.compareTo(innerArray[hole/2].postID) > 0) {
                    innerArray[hole] = innerArray[hole/2];
                } else {
                    break;
                }
            } else {
                innerArray[hole] = innerArray[hole/2];
            }
        }
        innerArray[hole] = post;

    }

    public Post deleteMax() { // Delete the maximum element
        if (isEmpty()) {
            System.out.println("It is empty");
            return null;
        }

        Post maxPost = findMax();
        innerArray[1] = innerArray[size--];
        percolateDown(1);
        return maxPost;
    }

    public void percolateDown(int hole) { // Percolate down on the heap. If like counts are equal, consider lexicographical order

        int child;
        Post tmp = innerArray[hole];

        for (; hole*2 <= size ; hole = child) {
            child = hole*2;
            if (child != size && innerArray[child+1].likeCount >= innerArray[child].likeCount) {
                if (innerArray[child+1].likeCount == innerArray[child].likeCount) {
                    if (innerArray[child+1].postID.compareTo(innerArray[child].postID) > 0) {
                        child++;
                    }
                } else {
                    child++;
                }
            }
            if (innerArray[child].likeCount >= tmp.likeCount ) {
                if (innerArray[child].likeCount == tmp.likeCount) {
                    if (innerArray[child].postID.compareTo(tmp.postID) > 0) {
                        innerArray[hole] = innerArray[child];
                    } else {
                        break;
                    }
                } else {
                    innerArray[hole] = innerArray[child];
                }
            } else {
                break;
            }
        }
        innerArray[hole] = tmp;
    }

}
