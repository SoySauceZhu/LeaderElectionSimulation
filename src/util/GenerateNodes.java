package util;

import entity.common.Node;
import entity.common.Port;
import entity.hs.HSNode;
import entity.lcr.LCRNode;

import java.util.*;

import static entity.common.Port.RIGHT;

public class GenerateNodes {

    public static List<Node> generateHSNodes(int numberOfNodes) {
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

    public static List<Node> generateLCRNodes(int numberOfNodes) {
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


    public static List<Node> generateRandomLCRNodes(int numberOfNodes) {
        List<Node> nodes = new ArrayList<>();
        Random random = new Random();
        Node last = new LCRNode(0);
        Node sentinel = last;
        nodes.add(last);

        Set<Integer> uniqueIds = new HashSet<>();
        while (uniqueIds.size() < numberOfNodes) {
            uniqueIds.add(random.nextInt(1, numberOfNodes * 3));
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

    public static List<Node> generateRandomHSNodes(int numberOfNodes) {
        List<Node> nodes = new ArrayList<>();
        Random random = new Random();
        Node last = new HSNode(0);
        Node sentinel = last;
        nodes.add(last);

        Set<Integer> uniqueIds = new HashSet<>();
        while (uniqueIds.size() < numberOfNodes) {
            uniqueIds.add(random.nextInt(1, numberOfNodes * 3));
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
//        List<Node> nodes = GenerateNodes.generateLCRNodes(10);
        List<Node> nodes = GenerateNodes.generateRandomHSNodes(10);
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
















