import java.util.ArrayList;
import java.util.List;

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

    public static List<Node> copyPath(List<Node> path) {
        List<Node> copyPath = new ArrayList<>();
        for (Node node : path) {
            copyPath.add(node);
        }
        return copyPath;
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
}
