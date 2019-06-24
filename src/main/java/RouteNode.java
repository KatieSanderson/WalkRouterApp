import java.util.ArrayList;
import java.util.List;

/**
 * Each {@link RouteNode} object represents an OSM node, the connecting edges on that node, and the distance of this node from a given node (starting node in @WalkRouter)
 */

class RouteNode implements Comparable<RouteNode> {

    private Node node;
    private List<Node> path;
    private long distance;

    RouteNode(Node node) {
        this.node = node;
        setPath(new ArrayList<>());
        distance = Long.MAX_VALUE;
    }

    @Override
    public int compareTo(RouteNode that) {
        return Long.compare(distance, that.distance);
    }

    Node getNode() {
        return node;
    }

    List<Node> getPath() {
        return path;
    }

    void setPath(List<Node> path) {
        this.path = path;
        path.add(this.node);
    }

    long getDistance() {
        return distance;
    }

    void setDistance(long distance) {
        this.distance = distance;
    }
}
