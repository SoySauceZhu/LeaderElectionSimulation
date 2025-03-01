package util;

import entity.common.Node;
import entity.common.Port;
import entity.hs.HSNode;
import entity.lcr.LCRNode;

import java.util.*;

import static entity.common.Port.RIGHT;

public class GenerateNodes {
    private Random rand;

    public GenerateNodes(int num) {
        this.rand = new Random(num);
    }

    public GenerateNodes() {
        this.rand = new Random();
    }

    public List<Node> generateHSNodes(int numberOfNodes) {
        List<Node> nodes = new ArrayList<>();
        int[] ids = new int[numberOfNodes];

        for (int i = 0; i < numberOfNodes; i++) {
            ids[i] = i;
        }

        for (int i = 0; i < numberOfNodes; i++) {
            Node node = new HSNode(i);
            nodes.add(node);
        }

        for (int i = 0; i < numberOfNodes; i++) {
            Node currentNode = nodes.get(i);
            Node nextNode = nodes.get((i + 1) % nodes.size());
            Node prevNode = nodes.get((i - 1 + nodes.size()) % nodes.size());
            currentNode.setNeighbor(RIGHT, nextNode);
            currentNode.setNeighbor(Port.LEFT, prevNode);
        }

        return nodes;
    }

    public List<Node> generateLCRNodes(int numberOfNodes) {
        List<Node> nodes = new ArrayList<>();
        int[] ids = new int[numberOfNodes];

        for (int i = 0; i < numberOfNodes; i++) {
            ids[i] = i;
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
        Node last = new LCRNode(0);
        Node sentinel = last;
        nodes.add(last);

        Set<Integer> uniqueIds = new HashSet<>();
        while (uniqueIds.size() < numberOfNodes - 1) {
            uniqueIds.add(rand.nextInt(1, numberOfNodes * 3));
        }
        Iterator<Integer> idIterator = uniqueIds.iterator();

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
        Node last = new HSNode(0);
        Node sentinel = last;
        nodes.add(last);

        Set<Integer> uniqueIds = new HashSet<>();
        while (uniqueIds.size() < numberOfNodes - 1) {
            uniqueIds.add(rand.nextInt(1, numberOfNodes * 3));
        }
        Iterator<Integer> idIterator = uniqueIds.iterator();

        while (idIterator.hasNext()) {
            int id = idIterator.next();
            Node nextNode = new HSNode(id);
            nodes.add(nextNode);
            last.setNeighbor(RIGHT, nextNode);
            nextNode.setNeighbor(Port.LEFT, last);

            if (!idIterator.hasNext()) {
                nextNode.setNeighbor(RIGHT, sentinel);
                sentinel.setNeighbor(Port.LEFT, nextNode);
            }
            last = nextNode;
        }

        return nodes;
    }

    public static void main(String[] args) {
        GenerateNodes generateNodes = new GenerateNodes(42);
//        List<Node> nodes = GenerateNodes.generateLCRNodes(10);
        List<Node> nodes = generateNodes.generateRandomHSNodes(10);
//        for (Node node : nodes) {
//            System.out.println(node);
//        }

        Node ptr = nodes.get(0);
        System.out.println(ptr);
        ptr = ptr.getNeighbors().get(RIGHT);
        while (ptr != null && !ptr.equals(nodes.get(0))) {
            System.out.println(ptr);
            ptr = ptr.getNeighbors().get(RIGHT);
        }
    }
}
















