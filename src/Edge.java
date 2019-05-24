import java.util.List;

class Edge {

    private final List<Node> nodes;
    private final int distance;

    Edge(List<Node> nodes, int distance) {
        this.nodes = nodes;
        this.distance = distance;
    }

    List<Node> getNodes() {
        return nodes;
    }

    int getDistance() {
        return distance;
    }
}
