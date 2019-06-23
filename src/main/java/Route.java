import java.util.ArrayList;
import java.util.List;

class Route {

    private final List<Node> requiredNodes;
    private long distance;
    private List<Node> path;

    Route(List<Node> requiredNodes) {
        this.requiredNodes = requiredNodes;
        distance = 0;
        path = new ArrayList<>();
    }

    Node getStartNode() {
        return requiredNodes.get(0);
    }

    Node getEndNode() {
        return requiredNodes.get(requiredNodes.size() - 1);
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

    void printRoute() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder
                .append("Shortest distance between ")
                .append(getStartNode().getId())
                .append(" and ")
                .append(getEndNode().getId())
                .append(" is ")
                .append(distance)
                .append(" achieved with path: ")
                .append("\n");
        for (int i = 0; i < path.size(); i++) {
            stringBuilder.append(path.get(i).getId()).append(" ");
            if (i % 5 == 4) {
                stringBuilder.append("\n");
            }
        }
        System.out.println(stringBuilder.toString());
    }
}
