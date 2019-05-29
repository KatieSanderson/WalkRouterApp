import java.util.ArrayList;
import java.util.List;

public class Route {

    private final Node startNode;
    private final Node endNode;
    private long distance;
    private List<Node> path;

    public Route(Node startNode, Node endNode) {
        this.startNode = startNode;
        this.endNode = endNode;
        distance = 0;
        path = new ArrayList<>();
    }

    public Node getStartNode() {
        return startNode;
    }

    public Node getEndNode() {
        return endNode;
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
}
