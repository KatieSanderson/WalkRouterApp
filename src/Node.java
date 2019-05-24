import java.util.ArrayList;
import java.util.List;

/**
 * Each Node object represents an OSM node, the connecting edges on that node, and the distance of this node from a given node (starting node in @WalkRouter)
 */

class Node implements Comparable<Node> {

    private final long id;
    private final List<Edge> edges;
    private long distance;

    Node(long id) {
        this.id = id;
        edges = new ArrayList<>();
        distance = Long.MAX_VALUE;
    }

    long getDistance() {
        return distance;
    }

    void setDistance(long distance) {
        this.distance = distance;
    }

    List<Edge> getEdges() {
        return edges;
    }

    long getId() {
        return id;
    }

    @Override
    public int compareTo(Node that) {
        return Long.compare(getDistance(), that.getDistance());
    }
}
