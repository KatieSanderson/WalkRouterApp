import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Each {@link Node} object represents an OSM node and the connecting {@link Edge}s of that node
 */

class Node {

    private final long id;
    private final List<Edge> edges;

    Node(long id) {
        this.id = id;
        edges = new ArrayList<>();
    }

    /**
     * {@link Node #Node(Node nextNode, long distance} is used to copy a node with a new distance
     */

    long getId() {
        return id;
    }

    List<Edge> getEdges() {
        return edges;
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
