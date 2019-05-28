import java.util.Comparator;
import java.util.PriorityQueue;

public class Test {

    public static void main(String[] args) {
        Test test = new Test();
        Node[] nodes = new Node[6];
        PriorityQueue<Node> pq = new PriorityQueue<>(Comparator.comparingInt(o -> o.value));
        for (int i = 0; i < 6; i++) {
            nodes[i] = test.new Node(i + 10);
            pq.add(nodes[i]);
        }
        // pq contains 10, 11, 12, 13, 14, 15
        nodes[3].value -= 10;
        // value 13 decreased to 3
        pq.add(nodes[3]); // and re-added to the priority queue.
        // pq contains two 3 instances, and 10, 11, 12, 14, 15.
        // The second 3 is not that important
        // we will ignore when we poll the second 3.

        // add other values, larger than 3 and smaller than others.
        pq.add(test.new Node(7));
        pq.add(test.new Node(8));
        pq.add(test.new Node(16));
        // now we have two 3 instances, 10, 11, 12, 14, 15, 7, 8, and 16.

        // We poll 3 times. We expect to pick 3, 7 and 8.
        // The second 3 may appear or not, we do not care.
        System.out.println(pq.poll().value);
        System.out.println(pq.poll().value);
        System.out.println(pq.poll().value);
    }

    class Node {
        int value;

        public Node(int value) {
            this.value = value;
        }
    }

}