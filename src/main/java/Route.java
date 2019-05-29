import java.util.ArrayList;
import java.util.List;

public class Route {

    private final List<Node> nodes;
    private long distance;
    private List<Node> path;

    public Route(List<Node> nodes) {
        this.nodes = nodes;
        distance = 0;
        path = new ArrayList<>();
    }

    public Node getStartNode() {
        return nodes.get(0);
    }

    public Node getEndNode() {
        return nodes.get(nodes.size() - 1);
    }

    public long getDistance() {
        return distance;
    }

    public void setDistance(long distance) {
        this.distance = distance;
    }

    public List<Node> getPath() {
        return path;
    }

    public void setPath(List<Node> path) {
        this.path = path;
    }

    public void addDistance(long distance) {
        this.distance += distance;
    }
}
