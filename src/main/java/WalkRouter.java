import java.io.*;
import java.util.*;

/**
 * Required arguments: valid file and two valid OSM nodes
 * Printed to console: shortest walking path and distance between input OSM nodes
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

public class WalkRouter implements AutoCloseable{

    private final BufferedReader bufferedReader;
    private final Scanner scanner;
    private final Map<Long, Node> nodeMap;

    private Set<Node> visitedNodes;
    private PriorityQueue<Node> priorityQueue;

    private WalkRouter(BufferedReader bufferedReader) {
        this.bufferedReader = bufferedReader;
        this.scanner = new Scanner(System.in);
        nodeMap = new HashMap<>();
        visitedNodes = new HashSet<>();
        priorityQueue = new PriorityQueue<>();
    }

    public static void main(String[] args) throws Exception {
        try (WalkRouter walkRouter = new WalkRouter(new BufferedReader(new FileReader(new File(args[0]))))) {
            walkRouter.parseURL();
            if (args.length > 1) {
                List<Node> nodesInPath = new ArrayList<>();
                for (int i = 1; i < args.length; i++) {
                    nodesInPath.add(walkRouter.parseInputNodes(args[i]));
                }
                walkRouter.findShortestDistanceBetweenNodes(nodesInPath);
            }
            walkRouter.processMoreShortestPaths();
        }
    }

    private void processMoreShortestPaths() {
        String exitString = "*";
        String continueString = "To calculate shortest path between two nodes, input comma-separated node ids. To end, enter \"" + exitString + "\"";
        System.out.println(continueString);
        String line;
        while (scanner.hasNext() && !(line = scanner.nextLine()).equals(exitString)) {
            // todo verify user input is acceptable
            String[] scannerInput = line.split("[\\s]*,[\\s]*");
            List<Node> nodesInPath = new ArrayList<>();
            for (int i = 0; i < scannerInput.length; i++) {
                nodesInPath.add(parseInputNodes(scannerInput[i]));
            }
            findShortestDistanceBetweenNodes(nodesInPath);
            System.out.println(continueString);
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

    private void findShortestDistanceBetweenNodes(List<Node> nodesInPath) {
        // todo implement queue with duplicle
        List<Node> path = new ArrayList<>();
        long totalDistance = 0;
        Node startNode = nodesInPath.get(0);
        Node endNode = nodesInPath.get(nodesInPath.size() - 1);
        for (int i = 0; i < nodesInPath.size() - 1; i++) {
            Node innerStartNode = nodesInPath.get(i);
            Node innerEndNode = nodesInPath.get(i + 1);
            resetNodeDistances(innerStartNode);
            priorityQueue.add(innerStartNode);
            try {
                while (priorityQueue.peek() != null && !Objects.equals(priorityQueue.peek(), innerEndNode)) {
                    Node currentNode = priorityQueue.poll();
                    if (!visitedNodes.contains(currentNode)) {
                        visitedNodes.add(currentNode);
                        evaluateAdjacentNodes(currentNode);
                    }
                }
                if (priorityQueue.peek() == null) {
                    throw new NullPointerException();
                }
                Node endNodeAfterParse = priorityQueue.poll();
                totalDistance += endNodeAfterParse.getDistance();
                path.addAll(endNodeAfterParse.getPath());
            } catch (NullPointerException e) {
                throw new IllegalArgumentException("Invalid input node(s). No valid route between provided nodes with id's: [" + innerStartNode.getId() + "] and [" + innerEndNode.getId() + "].");
            }
        }
        System.out.println("Shortest distance between " +
                startNode.getId() + " and " + endNode.getId() +
                " is " + totalDistance +
                " achieved with path: \n" + path);
    }

    private void resetNodeDistances(Node startNode) {
        priorityQueue = new PriorityQueue<>();
        visitedNodes = new HashSet<>();
        for (Map.Entry<Long, Node> entry : nodeMap.entrySet()) {
            Node.reset(entry.getValue());
        }
        startNode.setDistance(0);
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
            nextNode.setPath(Node.copyPath(currentNode.getPath()));
            // copy of nextNode with new distance is added to priorityQueue
            priorityQueue.add(new Node(nextNode, calculatedDistance));
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

    @Override
    public void close() throws Exception {
        bufferedReader.close();
        scanner.close();
    }
}
