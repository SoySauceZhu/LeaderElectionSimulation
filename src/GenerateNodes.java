import java.util.*;

public class GenerateNodes {
    private final Port LEFT = Port.LEFT;
    private final Port RIGHT = Port.RIGHT;

    private Random rand;

    public GenerateNodes(int num) {
        this.rand = new Random(num);
    }

    public GenerateNodes() {
        this.rand = new Random();
    }

    public List<Node> generateHSNodes(int numberOfNodes, boolean clockwise) {
        List<Node> nodes = new ArrayList<>();
        int[] ids = new int[numberOfNodes];

        for (int i = 0; i < numberOfNodes; i++) {
            if (clockwise) {
                ids[i] = i;
            } else {
                ids[i] = numberOfNodes - 1 - i;
            }
        }

        for (int i = 0; i < numberOfNodes; i++) {
            Node node = new HSNode(ids[i]);
            nodes.add(node);
        }

        for (int i = 0; i < numberOfNodes; i++) {
            Node currentNode = nodes.get(i);
            Node nextNode = nodes.get((i + 1) % nodes.size());
            Node prevNode = nodes.get((i - 1 + nodes.size()) % nodes.size());
            currentNode.setNeighbor(RIGHT, nextNode);
            currentNode.setNeighbor(LEFT, prevNode);
        }

        return nodes;
    }

    public List<Node> generateLCRNodes(int numberOfNodes, boolean clockwise) {
        List<Node> nodes = new ArrayList<>();
        int[] ids = new int[numberOfNodes];

        for (int i = 0; i < numberOfNodes; i++) {
            if (clockwise) {
                ids[i] = i;
            } else {
                ids[i] = numberOfNodes - 1 - i;
            }
        }

        for (int i = 0; i < numberOfNodes; i++) {
            Node node = new LCRNode(ids[i]);
            nodes.add(node);
        }

        for (int i = 0; i < numberOfNodes; i++) {
            Node currentNode = nodes.get(i);
            Node nextNode = nodes.get((i + 1) % nodes.size());
            currentNode.setNeighbor(RIGHT, nextNode);
        }

        return nodes;
    }

    public List<Node> generateRandomLCRNodes(int numberOfNodes) {
        List<Node> nodes = new ArrayList<>();

        Set<Integer> uniqueIds = new HashSet<>();
        while (uniqueIds.size() < numberOfNodes) {
            uniqueIds.add(rand.nextInt(1, numberOfNodes * 3));
        }
        Iterator<Integer> idIterator = uniqueIds.iterator();

        Node last = null;
        if (idIterator.hasNext())
            last = new LCRNode(idIterator.next());
        Node sentinel = last;
        nodes.add(last);
//        for (int i = 0; i < numberOfNodes; i++) {
//            int id = random.nextInt(1, numberOfNodes * 3);

        while (idIterator.hasNext()) {
            int id = idIterator.next();
            Node nextNode = new LCRNode(id);
            nodes.add(nextNode);
            last.setNeighbor(RIGHT, nextNode);

            if (!idIterator.hasNext()) {
                nextNode.setNeighbor(RIGHT, sentinel);
            }
            last = nextNode;
        }


        return nodes;
    }

    public List<Node> generateRandomHSNodes(int numberOfNodes) {
        List<Node> nodes = new ArrayList<>();

        Set<Integer> uniqueIds = new HashSet<>();
        while (uniqueIds.size() < numberOfNodes) {
            uniqueIds.add(rand.nextInt(1, numberOfNodes * 3));
        }
        Iterator<Integer> idIterator = uniqueIds.iterator();

        Node last = null;
        if (idIterator.hasNext()) last = new HSNode(idIterator.next());
        Node sentinel = last;
        nodes.add(last);
        while (idIterator.hasNext()) {
            int id = idIterator.next();
            Node nextNode = new HSNode(id);
            nodes.add(nextNode);
            last.setNeighbor(RIGHT, nextNode);
            nextNode.setNeighbor(LEFT, last);

            if (!idIterator.hasNext()) {
                nextNode.setNeighbor(RIGHT, sentinel);
                sentinel.setNeighbor(LEFT, nextNode);
            }
            last = nextNode;
        }

        return nodes;
    }

}















