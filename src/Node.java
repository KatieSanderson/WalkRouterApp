import java.util.ArrayList;
import java.util.List;

public class Node implements Comparable {

    private final long id;
    private final List<Edge> edges;
    private long minDistance;

    Node(long id) {
        this.id = id;
        edges = new ArrayList<>();
        minDistance = Long.MAX_VALUE;
    }

    public long getMinDistance() {
        return minDistance;
    }

    public void setMinDistance(long minDistance) {
        this.minDistance = minDistance;
    }

    public List<Edge> getEdges() {
        return edges;
    }

    public long getId() {
        return id;
    }

    @Override
    public int compareTo(Object that) {
        if (that instanceof Node) {
            return Long.compare(getMinDistance(), ((Node) that).getMinDistance());
        } else {
            throw new IllegalArgumentException("Invalid class: [" + that + "] is an instance of [" + that.getClass() + "]. Requires [" + this.getClass() + "]");
        }
    }
}
