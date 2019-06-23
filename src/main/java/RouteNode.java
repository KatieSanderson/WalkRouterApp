import java.util.ArrayList;
import java.util.List;

class RouteNode implements Comparable<RouteNode> {

    private Node node;
    private List<Node> path;
    private long distance;

    RouteNode(Node node) {
        this.node = node;
        setPath(new ArrayList<>());
        distance = Long.MAX_VALUE;
    }

    String printPath() {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < path.size(); i++) {
            stringBuilder.append(path.get(i).getId()).append(" ");
            if (i % 5 == 4) {
                stringBuilder.append("\n");
            }
        }
        return stringBuilder.toString();
    }

    @Override
    public int compareTo(RouteNode that) {
        return Long.compare(distance, that.distance);
    }

    Node getNode() {
        return node;
    }

    public void setNode(Node node) {
        this.node = node;
    }

    public List<Node> getPath() {
        return path;
    }

    public void setPath(List<Node> path) {
        this.path = path;
        path.add(this.node);
    }

    long getDistance() {
        return distance;
    }

    public void setDistance(long distance) {
        this.distance = distance;
    }
}
