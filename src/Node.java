import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Each Node object represents an OSM node, the connecting edges on that node, and the distance of this node from a given node (starting node in @WalkRouter)
 */

class Node implements Comparable<Node> {

    private final long id;
    private final List<Edge> edges;
    private List<Node> path;
    private long distance;

    Node(long id) {
        this.id = id;
        edges = new ArrayList<>();
        path = new ArrayList<>();
        path.add(this);
        distance = Long.MAX_VALUE;
    }

    /**
     * {@link Node #Node(Node nextNode, long distance} is used to copy a node with a new distance
     * @param nextNode - node to be copied (except for distance parameter)
     * @param distance - distance to be used for new node
     */

    public Node(Node nextNode, long distance) {
        this.id = nextNode.id;
        this.edges = nextNode.edges;
        this.distance = distance;
    }

    public static List<Node> copyPath(List<Node> path) {
        List<Node> copyPath = new ArrayList<>();
        for (Node node : path) {
            copyPath.add(node);
        }
        return copyPath;
    }

    public static void reset(Node node) {
        node.setPath(new ArrayList<>());
        node.setDistance(Long.MAX_VALUE);
    }

    public String printPath() {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < path.size(); i++) {
            stringBuilder.append(path.get(i).getId()).append(" ");
            if (i % 5 == 4) {
                stringBuilder.append("\n");
            }
        }
        return stringBuilder.toString();
    }

    long getId() {
        return id;
    }

    List<Edge> getEdges() {
        return edges;
    }

    public List<Node> getPath() {
        return path;
    }

    public void setPath(List<Node> path) {
        this.path = path;
        path.add(this);
    }

    long getDistance() {
        return distance;
    }

    void setDistance(long distance) {
        this.distance = distance;
    }

    @Override
    public int compareTo(Node that) {
        return Long.compare(getDistance(), that.getDistance());
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Node node = (Node) obj;
        return id == node.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
