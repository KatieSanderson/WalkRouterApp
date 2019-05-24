import java.util.List;

public class Edge {

    private final List<Node> nodes;
    private final int distance;

    public Edge(List<Node> nodes, int distance) {
        this.nodes = nodes;
        this.distance = distance;
    }

    public List<Node> getNodes() {
        return nodes;
    }

    public int getDistance() {
        return distance;
    }
}
