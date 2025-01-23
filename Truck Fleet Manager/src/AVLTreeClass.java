import java.util.ArrayList;
/**
 * AVLTree class for storing parking lots' capacity constraints.
 * @author Alaaddin Eren Namlı
 * @since Date: 07.11.2024
 */
public class AVLTreeClass {

    public AVLNode root;

    public AVLTreeClass() {
        this.root = null;
    }

    public int height(AVLNode node) {
        if (node == null) return -1;
        return node.height;
    }

    public void insert(int parkingLotCapacityConstraint) {
        root = insert(parkingLotCapacityConstraint,root);
    }

    public void remove(int parkingLotCapacityConstraint) {
        root = remove(parkingLotCapacityConstraint,root);
    }

    public boolean contains(int parkingLotCapacityConstraint) {
        return contains(parkingLotCapacityConstraint, root);
    }

    private boolean contains(int parkingLotCapacityConstraint, AVLNode node) {

        if (node == null) return false;

        // If the value of the node is greater, go to the left
        // If the value of the node is smaller, go to the right
        if ( parkingLotCapacityConstraint < node.parkingLotCapacityConstraint) {
            return contains(parkingLotCapacityConstraint,node.left);
        } else if (parkingLotCapacityConstraint > node.parkingLotCapacityConstraint) {
            return contains(parkingLotCapacityConstraint,node.right);
        } else {
            return true; // It is found return true
        }
    }

    private AVLNode insert(int parkingLotCapacityConstraint, AVLNode node) {

        if (node == null) {
            return new AVLNode(parkingLotCapacityConstraint);
        }

        // If the value of the node is greater, go to the left
        // If the value of the node is smaller, go to the right
        if (parkingLotCapacityConstraint < node.parkingLotCapacityConstraint) {
            node.left = insert(parkingLotCapacityConstraint,node.left);
        } else if (parkingLotCapacityConstraint > node.parkingLotCapacityConstraint) {
            node.right = insert(parkingLotCapacityConstraint,node.right);
        }

        return balance(node);
    }

    public AVLNode balance(AVLNode node) {
        if (node == null) {
            return node;
        }

        if (height(node.left) - height(node.right) > 1) {
            if (height(node.left.left) >= height(node.left.right)) {
                node = rotateWithLeftChild(node);
            } else {
                node = rotateDoubleLeft(node);
            }
        } else {
            if (height(node.right) - height(node.left) > 1) {
                if (height(node.right.right) >= height(node.right.left)) {
                    node = rotateWithRightChild(node);
                } else {
                    node = rotateDoubleRight(node);
                }
            }
        }

        node.height = Math.max(height(node.left), height(node.right)) + 1;
        return node;
    }

    public AVLNode rotateWithLeftChild(AVLNode node2) { // right rotation
        AVLNode node1 = node2.left;
        node2.left = node1.right;
        node1.right = node2;
        node2.height = Math.max(height(node2.left), height(node2.right)) + 1;
        node1.height = Math.max(height(node1.left), height(node1.right)) + 1;
        return node1;
    }

    public AVLNode rotateWithRightChild(AVLNode node1) { // left rotation
        AVLNode node2 = node1.right;
        node1.right = node2.left;
        node2.left = node1;
        node1.height = Math.max(height(node1.left), height(node1.right)) + 1;
        node2.height = Math.max(height(node2.left), height(node2.right)) + 1;
        return node2;
    }

    public AVLNode rotateDoubleLeft(AVLNode node) { // left-right double rotation
        node.left = rotateWithRightChild(node.left);
        return rotateWithLeftChild(node);
    }

    public AVLNode rotateDoubleRight(AVLNode node) { // right-left double rotation
        node.right = rotateWithLeftChild(node.right);
        return rotateWithRightChild(node);
    }

    private AVLNode remove(int parkingLotCapacityConstraint, AVLNode node) {

        if (node == null) {
            return node;
        }

        if (parkingLotCapacityConstraint < node.parkingLotCapacityConstraint) {
            node.left = remove(parkingLotCapacityConstraint,node.left);
        } else if (parkingLotCapacityConstraint > node.parkingLotCapacityConstraint) {
            node.right = remove(parkingLotCapacityConstraint,node.right);
        } else if (node.left != null && node.right != null) {
            node.parkingLotCapacityConstraint = findMin(node.right).parkingLotCapacityConstraint;
            node.right = remove(node.parkingLotCapacityConstraint, node.right);
        } else {
            if (node.left != null) {
                node = node.left;
            } else {
                node = node.right;
            }
        }
        return balance(node);
    }

    public void preOrder() {
        preOrder(root);
    }
    private void preOrder(AVLNode node) {
        if (node == null) return;
        System.out.println(node.parkingLotCapacityConstraint);
        preOrder(node.left);
        preOrder(node.right);
    }

    public AVLNode findMin(AVLNode node) { // Finds the minimum node in the subtree
        if (node == null) {
            return null;
        } else if (node.left == null) {
            return node;
        }
        return findMin(node.left);
    }

    public AVLNode findMax(AVLNode node) { // Finds the maximum node in the subtree
        if (node == null) {
            return null;
        } else if (node.right == null) {
            return node;
        }
        return findMax(node.right);
    }

    public AVLNode findGreatestMin(int capacityConstraint) { // Finds the predecessor of a node
        ArrayList<AVLNode> parents = new ArrayList<>();

        if (contains(capacityConstraint, root)) { // If the node exists, find the node.

            AVLNode current = root;
            while (current != null) {
                if (capacityConstraint < current.parkingLotCapacityConstraint) {
                    parents.add(current);
                    current = current.left;
                } else if (capacityConstraint > current.parkingLotCapacityConstraint) {
                    parents.add(current);
                    current = current.right;
                } else {
                    break;
                }
            }
            // The node is found = current

            if (current.left != null) { // If it has a left child, find the maximum node in its left subtree.
                current = current.left;
                return findMax(current);
            } else { // Else find minimum node from parent arraylist
                for (int i = parents.size()-1; i >=0 ; i--) {
                    if (parents.get(i).parkingLotCapacityConstraint < current.parkingLotCapacityConstraint) {
                        return parents.get(i);
                    }
                }
                return null; // There is no node in the tree which has a smaller value than the input value.
            }

        } else { // If the node does not exist, look into the parent nodes.

            AVLNode current = root;
            while (current != null) { // Locate the approximate place of the node
                if (capacityConstraint < current.parkingLotCapacityConstraint) {
                    parents.add(current);
                    current = current.left;
                } else if (capacityConstraint > current.parkingLotCapacityConstraint) {
                    parents.add(current);
                    current = current.right;
                } else {
                    break;
                }
            }
            // current == null because the node is not in the AVL Tree.

            if (parents.isEmpty()) { // Check if it is empty
                return null;
            }

            current = parents.getLast(); // Last node on the road

            if (current.parkingLotCapacityConstraint < capacityConstraint) {
                return current; // If last parent node is smaller than the input value, it is the predecessor.
            } else { // Search for the first node that is smaller than the input value
                for (int i = parents.size()-2; i >=0 ; i--) {
                    if (parents.get(i).parkingLotCapacityConstraint < current.parkingLotCapacityConstraint) {
                        return parents.get(i);
                    }
                }
                return null; // There is no node in the tree which has a smaller value than the input value.
            }

        }

    }

    public AVLNode findLowestMax(int capacityConstraint) { // Finds the successor of a node
        ArrayList<AVLNode> parents = new ArrayList<>();

        if (contains(capacityConstraint, root)) { // If the node exists, find the node.

            AVLNode current = root;
            while (current != null) {
                if (capacityConstraint < current.parkingLotCapacityConstraint) {
                    parents.add(current);
                    current = current.left;
                } else if (capacityConstraint > current.parkingLotCapacityConstraint) {
                    parents.add(current);
                    current = current.right;
                } else {
                    break;
                }
            }
            // The node is found = current

            if (current.right != null) { // If it has a right child, find the minimum node in its right subtree.
                current = current.right;
                return findMin(current);
            } else { // Else find maximum node from parent arraylist
                for (int i = parents.size()-1; i >=0 ; i--) {
                    if (parents.get(i).parkingLotCapacityConstraint > current.parkingLotCapacityConstraint) {
                        return parents.get(i);
                    }
                }
                return null; // There is no node in the tree which has a greater value than the input value.
            }

        } else { // If the node does not exist, look into the parent nodes.

            AVLNode current = root;
            while (current != null) { // Locate the approximate place of the node
                if (capacityConstraint < current.parkingLotCapacityConstraint) {
                    parents.add(current);
                    current = current.left;
                } else if (capacityConstraint > current.parkingLotCapacityConstraint) {
                    parents.add(current);
                    current = current.right;
                } else {
                    break;
                }
            }
            // current == null because the node is not in the AVL Tree.

            if (parents.isEmpty()) { // Check if it is empty
                return null;
            }

            current = parents.getLast(); // Last node on the road

            if (current.parkingLotCapacityConstraint > capacityConstraint) {
                return current; // If last parent node is greater than the input value, it is the successor.
            } else {
                for (int i = parents.size()-2; i >=0 ; i--) { // Search for the first node that is greater than the input value
                    if (parents.get(i).parkingLotCapacityConstraint > current.parkingLotCapacityConstraint) {
                        return parents.get(i);
                    }
                }
                return null; // There is no node in the tree which has a greater value than the input value.
            }
        }
    }

}
