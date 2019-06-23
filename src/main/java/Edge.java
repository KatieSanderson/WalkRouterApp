import java.util.Objects;

/**
 * Each Edge object represents the distance between OSM nodes
 *
 * The connecting nodes are stored in a @List for maintainability
 */

class Edge {

    private final Node node1;
    private final Node node2;
    private int distance;

    Edge(Node node1, Node node2, int distance) {
        this.node1 = node1;
        this.node2 = node2;
        this.distance = distance;
    }

    Node getOtherNode(Node node) {
        if (Objects.equals(node, node1)) {
            return node2;
        } else if (Objects.equals(node, node2)) {
            return node1;
        } else {
            throw new IllegalArgumentException("Invalid Node [" + node.getId() + "] for indicated Edge.");
        }
    }

    Node getNode1() {
        return node1;
    }

    Node getNode2() {
        return node2;
    }

    int getDistance() {
        return distance;
    }
}
