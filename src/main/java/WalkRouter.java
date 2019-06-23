import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
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

public class WalkRouter implements AutoCloseable {

    private final BufferedReader bufferedReader;
    private final Scanner scanner;
    private final Map<Long, Node> nodeMap;

    private WalkRouter(BufferedReader bufferedReader) {
        this.bufferedReader = bufferedReader;
        this.scanner = new Scanner(System.in);
        nodeMap = new HashMap<>();
    }

    public static void main(String[] args) throws Exception {
        // todo use with open maps data
        try (WalkRouter walkRouter = new WalkRouter(new BufferedReader(new FileReader(new File(args[0]))))) {
            walkRouter.parseURL();

            // calculate and output routes
            if (args.length > 1) {
                List<Node> nodesInPath = new ArrayList<>();
                for (int i = 1; i < args.length; i++) {
                    nodesInPath.add(walkRouter.parseInputNode(args[i]));
                }
                walkRouter.findShortestDistanceInRoute(nodesInPath);
            }
            walkRouter.processMoreShortestPaths();
        }
    }

    private void findShortestDistanceInRoute(List<Node> nodesInPath) {
        Route route = new Route(nodesInPath);
        // last element not included in for loop due to findShortestDistanceBetweenNodes call including next element
        for (int i = 0; i < nodesInPath.size() - 1; i++) {
            // find subroute between two nodes in nodesInPath and add to route
            Route subroute = findShortestDistanceBetweenNodes(nodesInPath.subList(i, i + 2));
            route.addDistance(subroute.getDistance());

            // remove the last node from path (to prevent duplicated node in list), if present
            if (route.getPath().size() > 0) {
                route.getPath().remove(route.getPath().size() - 1);
            }
            route.getPath().addAll(subroute.getPath());
        }
        route.printRoute();
    }

    private Route findShortestDistanceBetweenNodes(List<Node> nodesInPath) {
        Route route = new Route(nodesInPath);
        PriorityQueue<RouteNode> priorityQueue = initRoutePriorityQueue(route);
        Set<Node> visitedNodes = new HashSet<>();

        try {
            while (!Objects.equals(priorityQueue.peek().getNode(), route.getEndNode())) {
                RouteNode currentRouteNode = priorityQueue.poll();
                Node currentNode = currentRouteNode.getNode();
                if (!visitedNodes.contains(currentNode)) {
                    visitedNodes.add(currentNode);
                    evaluateAdjacentNodes(visitedNodes, priorityQueue, currentRouteNode);
                }
            }
            RouteNode endRouteNode = priorityQueue.poll();
            route.setDistance(endRouteNode.getDistance());
            route.setPath(endRouteNode.getPath());
            return route;
        } catch (NullPointerException e) {
            throw new IllegalArgumentException("Invalid input node(s). No valid route between provided nodes with id's: [" +
                    route.getStartNode().getId() + "] and [" + route.getEndNode().getId() + "].");
        }
    }

    private PriorityQueue<RouteNode> initRoutePriorityQueue(Route route) {
        PriorityQueue<RouteNode> priorityQueue = new PriorityQueue<>();
        RouteNode startingRouteNode = new RouteNode(route.getStartNode());
        startingRouteNode.setDistance(0);
        priorityQueue.add(startingRouteNode);
        return priorityQueue;
    }

    private void evaluateAdjacentNodes(Set<Node> visitedNodes, PriorityQueue<RouteNode> priorityQueue, RouteNode currentRouteNode) {
        Node currentNode = currentRouteNode.getNode();
        for (Edge edge : currentNode.getEdges()) {
            Node otherNodeInEdge = edge.getOtherNode(currentNode);
            if (!visitedNodes.contains(otherNodeInEdge)) {
                updateNodeDistance(priorityQueue, currentRouteNode, edge, new RouteNode(otherNodeInEdge));
            }
        }
    }

    private void updateNodeDistance(PriorityQueue<RouteNode> priorityQueue, RouteNode currentRouteNode, Edge edge, RouteNode nextNode) {
        long calculatedDistance = currentRouteNode.getDistance() + edge.getDistance();
        // if calculatedDistance is less than nextNode's current distance, optimize it and re-add to priority queue
        // note: cannot simply update nextNode's RouteNode in priority queue - will violate invariants of priority queue, return incorrect results
        if (calculatedDistance < nextNode.getDistance()) {
            nextNode.setDistance(calculatedDistance);
            nextNode.setPath(new ArrayList<>(currentRouteNode.getPath()));
            // optimized nextNode added to priorityQueue
            priorityQueue.add(nextNode);
        }
    }

    private void processMoreShortestPaths() {
        String exitString = "*";
        String continueString = "To calculate shortest path between two nodes, input comma-separated node ids. To end, enter \"" + exitString + "\"";
        System.out.println(continueString);
        String line;
        while (scanner.hasNext() && !(line = scanner.nextLine()).equals(exitString)) {
            String[] scannerInput = line.split("[\\s]*,[\\s]*");
            List<Node> nodesInPath = new ArrayList<>();
            for (String str : scannerInput) {
                nodesInPath.add(parseInputNode(str));
            }
            findShortestDistanceInRoute(nodesInPath);
            System.out.println(continueString);
        }
    }

    private Node parseInputNode(String inputNodeId) {
        Node node = nodeMap.get(Long.parseLong(inputNodeId));
        if (node != null) {
            return node;
        } else {
            throw new IllegalArgumentException("Invalid input node. No node with id [" + inputNodeId + "] in file.");
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

    private Edge parseEdge(String edgeData) {
        String[] edgeDataSplit = edgeData.split(" ");
        Node node1 = nodeMap.get(Long.parseLong(edgeDataSplit[0]));
        Node node2 = nodeMap.get(Long.parseLong(edgeDataSplit[1]));
        int distance = Integer.parseInt(edgeDataSplit[2]);
        return new Edge(node1, node2, distance);
    }

    private Node parseNode(String nodeData) {
        return new Node(Long.parseLong(nodeData));
    }

    private void addEdgeToNode(Edge edge, Node node) {
        node.getEdges().add(edge);
    }

    @Override
    public void close() throws Exception {
        bufferedReader.close();
        scanner.close();
    }
}
