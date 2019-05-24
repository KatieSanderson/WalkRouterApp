import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.*;

/**
 * Required arguments: valid file and two valid OSM nodes
 * System output: shortest walking distance between input OSM nodes
 *
 * Valid file will consist of the format
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
 * Input is parsed into {@link Node} and {@link Edge} and computes the shortest walking distance in meters between two given OSM nodes in the graph (assuming all edges are walkable)
 *
 *
 */

public class WalkRouter {

    private final BufferedReader bufferedReader;
    private final Map<Long, Node> nodeMap;
    private final Set<Node> visitedNodes;
    private final PriorityQueue<Node> queue;

    private WalkRouter(BufferedReader bufferedReader) {
        this.bufferedReader = bufferedReader;
        nodeMap = new HashMap<>();
        visitedNodes = new HashSet<>();
        queue = new PriorityQueue<>();
    }

    public static void main(String[] args) throws Exception {
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new URL(args[0]).openStream()))) {
            WalkRouter walkRouter = new WalkRouter(bufferedReader);
            // input parsing
            walkRouter.parseURL();

            System.out.println(walkRouter.findShortestPathBetweenNodes(
                    walkRouter.nodeMap.get(Long.parseLong(args[1])), walkRouter.nodeMap.get(Long.parseLong(args[2]))));
        }
    }

    private long findShortestPathBetweenNodes(Node startNode, Node endNode) {
        startNode.setDistance(0);
        queue.add(startNode);
        try {
            while (queue.peek() != endNode) {
                if (queue.peek() == null) {
                    throw new NullPointerException();
                }
                Node currentNode = queue.poll();
                // possible for several references to same node in queue due to optimizing distance without removing old reference due to time complexity
                if (!visitedNodes.contains(currentNode)) {
                    visitedNodes.add(currentNode);
                    checkNeighbours(currentNode);
                }
            }
            if (queue.peek() == null) {
                throw new NullPointerException();
            }
            return queue.poll().getDistance();
        } catch (NoSuchElementException | NullPointerException e) {
            throw new IllegalArgumentException("Invalid input node(s). No valid route between provided nodes with id's: [" + startNode.getId() + "] and [" + endNode.getId() + "].");
        }
    }

    private void checkNeighbours(Node currentNode) {
        for (Edge edge : currentNode.getEdges()) {
            // checks other (not currentNode) node in edge pair (currentNode)
            Node otherNodeInEdge = edge.getOtherNode(currentNode);
            if (!visitedNodes.contains(otherNodeInEdge)) {
                optimizeDistance(currentNode, edge, otherNodeInEdge);
            }
        }
    }

    private void optimizeDistance(Node currentNode, Edge edge, Node nextNode) {
        long calculatedDistance = currentNode.getDistance() + edge.getDistance();
        if (calculatedDistance < nextNode.getDistance()) {
            nextNode.setDistance(calculatedDistance);
            // node is added to queue again to ensure priority queue has node with optimized distance
            queue.add(nextNode);
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
