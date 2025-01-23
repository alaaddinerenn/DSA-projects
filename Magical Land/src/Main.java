import java.io.*;
import java.util.ArrayList;
/**
 * Program to travel in the wizard's land.
 * @author Alaaddin Eren Namlı
 * @since Date: 19.12.2024
 */
public class Main {

    public static void main(String[] args) throws IOException {

        // Read the files
        BufferedReader reader1 = new BufferedReader(new FileReader(args[0]));
        BufferedReader reader2 = new BufferedReader(new FileReader(args[1]));
        BufferedReader reader3 = new BufferedReader(new FileReader(args[2]));
        BufferedWriter writer = new BufferedWriter(new FileWriter(args[3], true));

        String line1 = reader1.readLine(); // Read the first line of the nodes file
        String[] coords = line1.split(" ");
        int xGrid = Integer.parseInt(coords[0]); // Get the x size of the grid
        int yGrid = Integer.parseInt(coords[1]); // Get the y size of the grid
        Node[][] nodes = new Node[yGrid][xGrid]; // Initialize the nodes matrix
        boolean[][] isImpassable = new boolean[yGrid][xGrid]; // Initialize the matrix to store the seen nodes
        int radiusOfSight = 0;

        while ((line1 = reader1.readLine()) != null) { // This loop reads the nodes file and construct the nodes and isImpassable matrices
            String[] lines1 = line1.split(" ");
            int xCoordinate = Integer.parseInt(lines1[0]);
            int yCoordinate = Integer.parseInt(lines1[1]);
            int type = Integer.parseInt(lines1[2]);
            Node node = new Node(xCoordinate, yCoordinate, type);
            nodes[yCoordinate][xCoordinate] = node;
            if (node.type == 1) { // If the type is 1, the node is impassable
                isImpassable[yCoordinate][xCoordinate] = true;
            }
        }

        String line2;
        while ((line2 = reader2.readLine()) != null) { // This loop reads the edges file and update the nodes
            String[] lines2 = line2.split(" ");
            String coordPart = lines2[0];
            double time = Double.parseDouble(lines2[1]); // The time that takes to travel between the nodes
            String[] coordinates = coordPart.split(","); // Split the coordinates
            String[] firstNode = coordinates[0].split("-");
            String[] secondNode = coordinates[1].split("-");
            // Read the coordinates
            int xCoordinate1 = Integer.parseInt(firstNode[0]);
            int yCoordinate1 = Integer.parseInt(firstNode[1]);
            int xCoordinate2 = Integer.parseInt(secondNode[0]);
            int yCoordinate2 = Integer.parseInt(secondNode[1]);
            Node node1 = nodes[yCoordinate1][xCoordinate1];
            Node node2 = nodes[yCoordinate2][xCoordinate2];
            // Place the nodes according to their coordinates and store the times
            if (node1.type != 1 && node2.type != 1) { // If their types not 1
                if (xCoordinate1 == xCoordinate2) { // If x coordinates equal
                    if (yCoordinate1 < yCoordinate2) { // node2 is on top
                        node1.up = node2;
                        node1.upTime = time;
                        node2.down = node1;
                        node2.downTime = time;
                    } else { // node1 is on top
                        node2.up = node1;
                        node2.upTime = time;
                        node1.down = node2;
                        node1.downTime = time;
                    }
                } else if (yCoordinate1 == yCoordinate2) { // If y coordinates equal
                    if (xCoordinate1 < xCoordinate2) { // node2 is on the right sight of node1
                        node1.right = node2;
                        node1.rightTime = time;
                        node2.left = node1;
                        node2.leftTime = time;
                    } else { // node1 is on the right sight of node1
                        node2.right = node1;
                        node2.rightTime = time;
                        node1.left = node2;
                        node1.leftTime = time;
                    }
                }
            }
        }

        String line3 = reader3.readLine(); // Read the first line of the objective file
        radiusOfSight = Integer.parseInt(line3);
        String[] originCoordinates = reader3.readLine().split(" "); // Read the second line of the objective file
        int sourceX = Integer.parseInt(originCoordinates[0]);
        int sourceY = Integer.parseInt(originCoordinates[1]);
        int counter = 1; // Objective counter
        boolean isOptionsGiven = false; // Whether the wizard helps or not
        ArrayList<Integer> options = new ArrayList<>();
        while ((line3 = reader3.readLine()) != null) { // This loop reads the objective file and using Dijkstra's Algorithm traverses the map according to the objectives
            String[] parts = line3.split(" ");
            if (parts.length == 2) { // If there is no options
                int targetX = Integer.parseInt(parts[0]);
                int targetY = Integer.parseInt(parts[1]);
                if (isOptionsGiven) { // If options are given from the last objective node
                    // After objective reached, we need to choose a number
                    double minTime = Double.MAX_VALUE;
                    int minOption = -1;
                    // Choose the min option that gives the min time
                    // Consider all options and choose the best one
                    int c = 0;
                    for (int option: options) { // This loop tries all options and chooses the best option
                        // First copy the nodes matrix and ısImpassable matrix
                        Node[][] copyNodes = copyMatrix(nodes);
                        boolean[][] copyIsImpassable = new boolean[isImpassable.length][isImpassable[0].length];
                        for (int i = 0; i < isImpassable.length; i++) { // Copies the matrix
                            for (int j = 0; j < isImpassable[0].length; j++) {
                                copyIsImpassable[i][j] = isImpassable[i][j];
                            }
                        }
                        // Then try the options
                        for (Node[] vector: copyNodes) { // Change the type x to 0 for all x option
                            for (Node node: vector) {
                                if (node.type == option) {
                                    node.type = 0;
                                    copyIsImpassable[node.yCoordinate][node.xCoordinate] = false;
                                }
                            }
                        }
                        // For every option's map, execute the Dijkstra's Algorithm to get the fastest possible route
                        ArrayList<Node> optionalPath = dijkstra(copyNodes, copyIsImpassable,sourceX, sourceY, targetX, targetY);
                        double optionalTime = optionalPath.getLast().distance; // Get the time that takes to get the target node

                        if (optionalTime < minTime) { // If it is less from the previous ones, update the minTime and minOption
                            minTime = optionalTime;
                            minOption = option;
                        }
                    }
                    // Write to the output file
                    writer.write("Number " + minOption + " is chosen!");
                    writer.newLine();
                    // Update the nodes and isImpassable matrices according to the best option
                    for (Node[] vector: nodes) {
                        for (Node node: vector) {
                            if (node.type == minOption) {
                                node.type = 0;
                                isImpassable[node.yCoordinate][node.xCoordinate] = false;
                            }
                        }
                    }
                }
                isOptionsGiven = false; // Make it false since the new objective node has no options
                boolean isObjectiveReached = false;
                // Until the objective is reached, calculate the best route and move 1 by 1.
                // If there is impassable nodes, stop and recalculate the best route.
                while (!isObjectiveReached) {
                    // First reveal the nodes types in the radius of sight
                    ArrayList<Node> nodesInSight1 = nodesInSight(nodes, sourceX, sourceY, radiusOfSight); // Get the nodes in sight
                    for (Node node : nodesInSight1) {
                        if (node.type > 0) {
                            isImpassable[node.yCoordinate][node.xCoordinate] = true;
                        }
                    }
                    // Find the best route
                    ArrayList<Node> path = dijkstra(nodes, isImpassable, sourceX, sourceY, targetX, targetY);
                    // This loop moves on the path 1 by 1 and checks whether there are impassable nodes
                    for (int i = 0; i < path.size() - 1; i++) {
                        Node originNode = path.get(i);
                        // Reveal the nodes types in the radius of sight
                        ArrayList<Node> nodesInSight = nodesInSight(nodes, originNode.xCoordinate, originNode.yCoordinate, radiusOfSight);
                        for (Node node : nodesInSight) {
                            if (node.type > 0) {
                                isImpassable[node.yCoordinate][node.xCoordinate] = true;
                            }
                        }
                        Node nextNode = path.get(i + 1);
                        // If the path is passable, move to the next node
                        if (checkPath(isImpassable, path)) { // The path is passable
                            // Write to the output file
                            writer.write("Moving to " + nextNode.xCoordinate + "-" + nextNode.yCoordinate);
                            writer.newLine();
                            if (i == path.size() - 2) { // Objective node is reached
                                // Write to the output file
                                writer.write("Objective " + counter + " reached!");
                                writer.newLine();
                                isObjectiveReached = true;
                                // Update the source node
                                sourceX = targetX;
                                sourceY = targetY;
                            }
                        } else { // The path is impassable
                            // Write to the output file
                            writer.write("Path is impassable!");
                            writer.newLine();
                            // Update the source node
                            sourceX = originNode.xCoordinate;
                            sourceY = originNode.yCoordinate;
                            break;
                        }
                    }
                }
            } else if (parts.length > 2) {
                int targetX = Integer.parseInt(parts[0]);
                int targetY = Integer.parseInt(parts[1]);
                if (isOptionsGiven) { // If options are given from the last objective node
                    // After objective reached, we need to choose a number
                    double minTime = Double.MAX_VALUE;
                    int minOption = -1;
                    // Choose the min option that gives the min time
                    // Consider all options and choose the best one
                    int c = 0;
                    for (int option: options) { // This loop tries all options and chooses the best option
                        // First copy the nodes matrix and ısImpassable matrix
                        Node[][] copyNodes = copyMatrix(nodes);
                        boolean[][] copyIsImpassable = new boolean[isImpassable.length][isImpassable[0].length];
                        for (int i = 0; i < isImpassable.length; i++) { // Copies the matrix
                            for (int j = 0; j < isImpassable[0].length; j++) {
                                copyIsImpassable[i][j] = isImpassable[i][j];
                            }
                        }
                        // Then try the options
                        for (Node[] vector: copyNodes) { // Change the type x to 0 for all x option
                            for (Node node: vector) {
                                if (node.type == option) {
                                    node.type = 0;
                                    copyIsImpassable[node.yCoordinate][node.xCoordinate] = false;
                                }
                            }
                        }
                        // For every option's map, execute the Dijkstra's Algorithm to get the fastest possible route
                        ArrayList<Node> optionalPath = dijkstra(copyNodes, copyIsImpassable,sourceX, sourceY, targetX, targetY);
                        double optionalTime = optionalPath.getLast().distance; // Get the time that takes to get the target node
                        if (optionalTime < minTime) { // If it is less from the previous ones, update the minTime and minOption
                            minTime = optionalTime;
                            minOption = option;
                        }
                    }
                    // Write to the output file
                    writer.write("Number " + minOption + " is chosen!");
                    writer.newLine();
                    // Update the nodes and isImpassable matrices according to the best option
                    for (Node[] vector: nodes) {
                        for (Node node: vector) {
                            if (node.type == minOption) {
                                node.type = 0;
                                isImpassable[node.yCoordinate][node.xCoordinate] = false;
                            }
                        }
                    }
                }
                boolean isObjectiveReached = false;
                // Until the objective is reached, calculate the best route and move 1 by 1.
                // If there is impassable nodes, stop and recalculate the best route.
                while (!isObjectiveReached) {
                    isOptionsGiven = true; // Make it true since there are options
                    // Store options in an ArrayList
                    options = new ArrayList<>();
                    for (int i = 2; i < parts.length; i++) {
                        options.add(Integer.parseInt(parts[i]));
                    }
                    // First reveal the nodes types in the radius of sight
                    ArrayList<Node> nodesInSight1 = nodesInSight(nodes, sourceX, sourceY, radiusOfSight); // Get the nodes in sight
                    for (Node node : nodesInSight1) {
                        if (node.type > 0) {
                            isImpassable[node.yCoordinate][node.xCoordinate] = true;
                        }
                    }
                    // Find the best route
                    ArrayList<Node> path = dijkstra(nodes, isImpassable, sourceX, sourceY, targetX, targetY);
                    // This loop moves on the path 1 by 1 and checks whether there are impassable nodes
                    for (int i = 0; i < path.size() - 1; i++) {
                        Node originNode = path.get(i);
                        // Reveal the nodes types in the radius of sight
                        ArrayList<Node> nodesInSight = nodesInSight(nodes, originNode.xCoordinate, originNode.yCoordinate, radiusOfSight);
                        for (Node node : nodesInSight) {
                            if (node.type > 0) {
                                isImpassable[node.yCoordinate][node.xCoordinate] = true;
                            }
                        }
                        Node nextNode = path.get(i + 1);
                        // If the path is passable, move to the next node
                        if (checkPath(isImpassable, path)) { // The path is passable
                            // Write to the output file
                            writer.write("Moving to " + nextNode.xCoordinate + "-" + nextNode.yCoordinate);
                            writer.newLine();
                            if (i == path.size() - 2) { // Objective node is reached
                                // Write to the output file
                                writer.write("Objective " + counter + " reached!");
                                writer.newLine();
                                isObjectiveReached = true;
                                // Update the source node
                                sourceX = targetX;
                                sourceY = targetY;
                            }
                        } else { // The path is impassable
                            // Write to the output file
                            writer.write("Path is impassable!");
                            writer.newLine();
                            // Update the source node
                            sourceX = originNode.xCoordinate;
                            sourceY = originNode.yCoordinate;
                            break;
                        }
                    }
                }
            }
            counter++;
        }

        // Close the files
        reader1.close();
        reader2.close();
        reader3.close();
        writer.close();
    }

    /**
     * Deep copy the node matrix
     * @param nodes Matrix that stores nodes
     * @return Copy of the matrix
     */
    public static Node[][] copyMatrix(Node[][] nodes) {
        Node[][] copyNodes = new Node[nodes.length][nodes[0].length];
        for (int i = 0; i < nodes.length; i++) {
            for (int j = 0; j < nodes[0].length; j++) {
                copyNodes[i][j] = new Node(nodes[i][j].xCoordinate, nodes[i][j].yCoordinate, nodes[i][j].type, nodes[i][j].left, nodes[i][j].right,nodes[i][j].up,nodes[i][j].down,nodes[i][j].leftTime,nodes[i][j].rightTime,nodes[i][j].upTime,nodes[i][j].downTime,nodes[i][j].distance);
            }
        }
        return copyNodes;
    }

    /**
     * Checks for all nodes in the path whether they are impassable or not
     * @param isImpassable The matrix to store the seen nodes
     * @param path Arraylist of nodes that are on the path
     * @return
     */
    public static boolean checkPath(boolean[][] isImpassable,ArrayList<Node> path) {
        for (Node node: path) {
            if (isImpassable[node.yCoordinate][node.xCoordinate] == true) {
                return false; // The path is impassable
            }
        }
        return true; // The path is passable
    }

    /**
     * Calculates the distance between the two nodes
     * @param node1 Node1
     * @param node2 Node2
     */
    public static double calculateDistance(Node node1, Node node2) {
        return Math.sqrt(Math.pow(node1.xCoordinate - node2.xCoordinate,2) + Math.pow(node1.yCoordinate - node2.yCoordinate, 2));
    }

    /**
     * Returns the nodes that are in the radius of sight
     * @param nodes Matrix that stores nodes
     * @param currentX X coordinate of the current node
     * @param currentY Y coordinate of the current node
     * @param r Radius of sight
     */
    public static ArrayList<Node> nodesInSight(Node[][] nodes, int currentX, int currentY,int r) {
        ArrayList<Node> nodesSeen = new ArrayList<>();
        Node currentNode = nodes[currentY][currentX];
        // Check boundaries to avoid the out of bounds exception
        int minX = Math.max(currentX-r,0);
        int minY = Math.max(currentY-r,0);
        int maxX = Math.min(currentX+r,nodes[0].length-1);
        int maxY = Math.min(currentY+r,nodes.length-1);
        // For a circle with radius(radius of sight), check the nodes that are in the smallest rectangle that contains the circle
        for (int i = minY; i <= maxY ; i++) {
            for (int j = minX; j <=maxX ; j++) {
                Node node = nodes[i][j];
                if (calculateDistance(node,currentNode) <= r) {
                    nodesSeen.add(node);
                }
            }
        }
        return nodesSeen; // Return the nodes that are in the radius of sight
    }

    /**
     * Dijkstra's Algorithm to find the fastest route
     * @param nodes Matrix that stores nodes
     * @param isImpassable The matrix to store the seen nodes
     * @param sourceX X coordinate of the source node
     * @param sourceY Y coordinate of the source node
     * @param targetX X coordinate of the target node
     * @param targetY Y coordinate of the target node
     */
    public static ArrayList<Node> dijkstra(Node[][] nodes, boolean[][] isImpassable, int sourceX, int sourceY, int targetX, int targetY) {

        MinHeap distances = new MinHeap(); // Initialize the min heap
        for (int i = 0; i < nodes.length; i++) { // Make the distances infinity for all nodes
            for (int j = 0; j < nodes[0].length; j++) {
                Node node = nodes[i][j];
                node.distance = Double.MAX_VALUE;
            }
        }

        Node[][] parents = new Node[nodes.length][nodes[0].length]; // Initialize the parents matrix
        boolean[][] explored = new boolean[nodes.length][nodes[0].length]; // Initialize the explored matrix to store the nodes that are visited
        Node source = nodes[sourceY][sourceX];
        source.distance = 0; // Make distance of the source 0
        distances.insert(source); // Add source to the heap

        while (!distances.isEmpty()) { // Until the heap is empty, continue the process
            Node minNode = distances.deleteMin(); // Get the node with minimum distance value
            int minX = minNode.xCoordinate;
            int minY= minNode.yCoordinate;
            if (explored[minY][minX] == true) { // If it is visited before, continue
                continue;
            }
            explored[minY][minX] = true; // Make it visited
            // For every node visit its adjacent nodes and update the distances
            // Since every node maximum has 4 edges, we use this simple loop
            for (int i = 0; i < 4; i++) {
                if (i == 0) {
                    if (minX-1 < 0) { // If it is out of bounds, continue
                        continue;
                    }
                    Node leftNode = nodes[minY][minX-1]; // Get the left node
                    if (leftNode != null) {
                        if (isImpassable[leftNode.yCoordinate][leftNode.xCoordinate] == true) { // If it is impassable, continue
                            continue;
                        }
                        if (minNode.distance + minNode.leftTime < leftNode.distance) { // If its distance more than the new distance, update its distance
                            leftNode.distance = minNode.distance + minNode.leftTime;
                            nodes[minY][minX-1].distance = leftNode.distance;
                            distances.insert(leftNode); // Insert this node to the heap
                            parents[leftNode.yCoordinate][leftNode.xCoordinate] = minNode; // Set its parent node
                        }
                    }
                } else if (i == 1) {
                    if (minX+1 >= nodes[0].length) { // If it is out of bounds, continue
                        continue;
                    }
                    Node rightNode = nodes[minY][minX+1]; // Get the right node
                    if (rightNode != null) {
                        if (isImpassable[rightNode.yCoordinate][rightNode.xCoordinate] == true) { // If it is impassable, continue
                            continue;
                        }
                        if (minNode.distance + minNode.rightTime < rightNode.distance) { // If its distance more than the new distance, update its distance
                            rightNode.distance = minNode.distance + minNode.rightTime;
                            nodes[minY][minX+1].distance = rightNode.distance;
                            distances.insert(rightNode); // Insert this node to the heap
                            parents[rightNode.yCoordinate][rightNode.xCoordinate] = minNode; // Set its parent node
                        }
                    }
                } else if (i == 2) {
                    if (minY+1 >= nodes.length) { // If it is out of bounds, continue
                        continue;
                    }
                    Node upNode = nodes[minY+1][minX]; // Get the upper node
                    if (upNode != null) {
                        if (isImpassable[upNode.yCoordinate][upNode.xCoordinate] == true) { // If it is impassable, continue
                            continue;
                        }
                        if (minNode.distance + minNode.upTime < upNode.distance) { // If its distance more than the new distance, update its distance
                            upNode.distance = minNode.distance + minNode.upTime;
                            nodes[minY+1][minX].distance = upNode.distance;
                            distances.insert(upNode); // Insert this node to the heap
                            parents[upNode.yCoordinate][upNode.xCoordinate] = minNode; // Set its parent node
                        }
                    }
                } else if (i == 3) {
                    if (minY-1 < 0) { // If it is out of bounds, continue
                        continue;
                    }
                    Node downNode = nodes[minY-1][minX]; // Get the down node
                    if (downNode != null) {
                        if (isImpassable[downNode.yCoordinate][downNode.xCoordinate] == true) { // If it is impassable, continue
                            continue;
                        }
                        if (minNode.distance + minNode.downTime < downNode.distance) { // If its distance more than the new distance, update its distance
                            downNode.distance = minNode.distance + minNode.downTime;
                            nodes[minY-1][minX].distance = downNode.distance;
                            distances.insert(downNode); // Insert this node to the heap
                            parents[downNode.yCoordinate][downNode.xCoordinate] = minNode; // Set its parent node
                        }
                    }
                }
            }
        }
        // The time that takes to travel between the source and target nodes is the distance field of the target node
        double shortestTime = nodes[targetY][targetX].distance;
        ArrayList<Node> pathNodes = constructPath(nodes, parents, targetX, targetY); // Get the path
        return pathNodes; // Return the path
    }

    /**
     * Constructs the path from the parents matrix
     * @param nodes Matrix that stores nodes
     * @param parents Matrix that stores parent nodes
     * @param targetX X coordinate of the target node
     * @param targetY Y coordinate of the target node
     */
    public static ArrayList<Node> constructPath(Node[][] nodes, Node[][] parents, int targetX, int targetY) {
        ArrayList<Node> path = new ArrayList<>();
        Node node = nodes[targetY][targetX];
        Node parentNode = parents[node.yCoordinate][node.xCoordinate];
        while (parentNode != null) {
            path.addFirst(parentNode); // Add the nodes to the path
            parentNode = parents[parentNode.yCoordinate][parentNode.xCoordinate];
        }
        Node targetNode = nodes[targetY][targetX]; // At last, add the target node to the path
        path.add(targetNode);
        return path; // Return the path
    }

}
