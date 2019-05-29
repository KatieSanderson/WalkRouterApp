import java.util.ArrayList;
import java.util.List;

class Route {

    private final List<Node> nodes;
    private long distance;
    private List<Node> path;

    Route(List<Node> nodes) {
        this.nodes = nodes;
        distance = 0;
        path = new ArrayList<>();
    }

    Node getStartNode() {
        return nodes.get(0);
    }

    Node getEndNode() {
        return nodes.get(nodes.size() - 1);
    }

    long getDistance() {
        return distance;
    }

    void setDistance(long distance) {
        this.distance = distance;
    }

    List<Node> getPath() {
        return path;
    }

    void setPath(List<Node> path) {
        this.path = path;
    }

    void addDistance(long distance) {
        this.distance += distance;
    }
}
