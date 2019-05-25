import java.io.*;
import java.util.*;

/**
 * Required arguments: valid file and two valid OSM nodes
 * Printed to console: shortest walking distance between input OSM nodes
 *
 * Valid file will consist of the format:
 * <number of nodes>
 * <OSM id of node>
 * ...
 * <OSM id of node>
 * <number of edges>
 * <from node OSM id> <to node OSM id> <length in meters>
 * ...
 * <from node OSM id> <to node OSM id> <length in meters>
 *
 * Valid OSM Nodes are nodes with <OSM id of node> in file input and can be connected via edges (<from node OSM id> <to node OSM id> <length in meters>) in input
 *
 * Input is parsed into {@link Node} and {@link Edge}. The shortest walking distance in meters is computed between two given OSM nodes in the graph (assuming all edges are walkable)
 */

public class WalkRouter {

    private final BufferedReader bufferedReader;
    private final Map<Long, Node> nodeMap;
    private final Set<Node> visitedNodes;
    private final PriorityQueue<Node> priorityQueue;

    private WalkRouter(BufferedReader bufferedReader) {
        this.bufferedReader = bufferedReader;
        nodeMap = new HashMap<>();
        visitedNodes = new HashSet<>();
        priorityQueue = new PriorityQueue<>();
    }

    public static void main(String[] args) throws Exception {
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(new File(args[0])))) {
            WalkRouter walkRouter = new WalkRouter(bufferedReader);
            walkRouter.parseURL();

            Node startNode = walkRouter.parseInputNodes(args[1]);
            Node endNode = walkRouter.parseInputNodes(args[2]);
            System.out.println(walkRouter.findShortestDistanceBetweenNodes(startNode, endNode));
        }
    }

    private Node parseInputNodes(String inputNodeId) {
        Node node = nodeMap.get(Long.parseLong(inputNodeId));
        if (node != null) {
            return node;
        } else {
            throw new IllegalArgumentException("Invalid input node. No node with id [" + inputNodeId + "] in file.");
        }
    }

    private long findShortestDistanceBetweenNodes(Node startNode, Node endNode) {
        startNode.setDistance(0);
        priorityQueue.add(startNode);
        try {
            while (priorityQueue.peek() != null && priorityQueue.peek() != endNode) {
                Node currentNode = priorityQueue.poll();
                if (!visitedNodes.contains(currentNode)) {
                    visitedNodes.add(currentNode);
                    evaluateAdjacentNodes(currentNode);
                }
            }
            if (priorityQueue.peek() == null) {
                throw new NullPointerException();
            }
            return priorityQueue.poll().getDistance();
        } catch (NullPointerException e) {
            throw new IllegalArgumentException("Invalid input node(s). No valid route between provided nodes with id's: [" + startNode.getId() + "] and [" + endNode.getId() + "].");
        }
    }

    private void evaluateAdjacentNodes(Node currentNode) {
        for (Edge edge : currentNode.getEdges()) {
            Node otherNodeInEdge = edge.getOtherNode(currentNode);
            if (!visitedNodes.contains(otherNodeInEdge)) {
                updateNodeDistance(currentNode, edge, otherNodeInEdge);
            }
        }
    }

    private void updateNodeDistance(Node currentNode, Edge edge, Node nextNode) {
        long calculatedDistance = currentNode.getDistance() + edge.getDistance();
        if (calculatedDistance < nextNode.getDistance()) {
            nextNode.setDistance(calculatedDistance);
            // nextNode is added to priorityQueue again (required to ensure priorityQueue includes nextNode's new distance)
            priorityQueue.add(nextNode);
        }
    }

    private void parseURL() throws IOException {
        // parse and add nodes to map
        int numNodes = Integer.parseInt(bufferedReader.readLine());
        for (int i = 0; i < numNodes; i++) {
            Node node = parseNode(bufferedReader.readLine());
            nodeMap.put(node.getId(), node);
        }

        // parse and add edges to nodes
        int numEdges = Integer.parseInt(bufferedReader.readLine());
        for (int i = 0; i < numEdges; i++) {
            Edge edge = parseEdge(bufferedReader.readLine());
            addEdgeToNode(edge, edge.getNode1());
            addEdgeToNode(edge, edge.getNode2());
        }
    }

    private void addEdgeToNode(Edge edge, Node node) {
        node.getEdges().add(edge);
    }

    private Edge parseEdge(String edgeData) {
        String[] edgeDataSplit = edgeData.split(" ");
        Node node1 = nodeMap.get(Long.parseLong(edgeDataSplit[0]));
        Node node2 = nodeMap.get(Long.parseLong(edgeDataSplit[1]));
        int distance = Integer.parseInt(edgeDataSplit[2]);
        return new Edge(node1, node2, distance);
    }

    private Node parseNode(String readLine) {
        return new Node(Long.parseLong(readLine));
    }

}
