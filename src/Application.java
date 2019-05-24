import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.*;

public class Application implements AutoCloseable {

    private final BufferedReader bufferedReader;
    private final Map<Long, Node> nodeMap;
    private final Set<Node> visitedNodes;
    private final Queue<Node> queue;

    private long inputNodeStart;
    private long inputNodeEnd;


    private Application(BufferedReader bufferedReader) {
        this.bufferedReader = bufferedReader;
        nodeMap = new HashMap<>();
        visitedNodes = new HashSet<>();
        queue = new PriorityQueue<>();
    }

    public static void main(String[] args) throws Exception {
        URL url = new URL(args[0]);
        try (Application application = new Application(new BufferedReader(new InputStreamReader(url.openStream())))) {
            // input parsing
            application.inputNodeStart = Long.parseLong(args[1]);
            application.inputNodeEnd = Long.parseLong(args[2]);
            application.parseURL();

            System.out.println(application.findShortestPathToNode().getMinDistance());
        }
    }

    private Node findShortestPathToNode() {
        Node nodeStart = nodeMap.get(inputNodeStart);
        nodeStart.setMinDistance(0);
        queue.add(nodeStart);
        while (queue.peek().getId() != inputNodeEnd) {
            Node currentNode = queue.poll();
            // possible for several instances of same node in queue due to optimizing distance (without removing old queue instance due to time complexity)
            if (!visitedNodes.contains(currentNode)) {
                visitedNodes.add(currentNode);
                checkNeighbours(currentNode);
            }
        }
        return queue.poll();
    }

    private void checkNeighbours(Node currentNode) {
        for (Edge edge : currentNode.getEdges()) {
            // checks other (not currentNode) node in edge pair (currentNode
            for (Node nextNode : edge.getNodes()) {
                if (!visitedNodes.contains(nextNode)) {
                    optimizeDistance(currentNode, edge, nextNode);
                }
            }
        }
    }

    private void optimizeDistance(Node currentNode, Edge edge, Node nextNode) {
        long calculatedDistance = currentNode.getMinDistance() + edge.getDistance();
        if (calculatedDistance < nextNode.getMinDistance()) {
            nextNode.setMinDistance(calculatedDistance);
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
            for (Node node : edge.getNodes()) {
                node.getEdges().add(edge);
            }
        }
    }

    private Edge parseEdge(String edgeData) {
        String[] edgeMetaDataSplit = edgeData.split(" ");
        List<Node> edgeNodes = new ArrayList<>();
        // all array members except last index are expected to be node ids
        for (int i = 0; i < edgeMetaDataSplit.length - 1; i++) {
            edgeNodes.add(nodeMap.get(Long.parseLong(edgeMetaDataSplit[i])));
        }
        int distance = Integer.parseInt(edgeMetaDataSplit[2]);
        return new Edge(edgeNodes, distance);
    }

    private Node parseNode(String readLine) {
        return new Node(Long.parseLong(readLine));
    }

    @Override
    public void close() throws Exception {
        bufferedReader.close();
    }

}
