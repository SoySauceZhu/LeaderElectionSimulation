package service;

import entity.Node;

import java.util.*;

public class RandomNodes {
    public static Set<Node> generateRandomNodes(int numberOfNodes) {
        Set<Node> nodes = new HashSet<>();
        Random random = new Random();
        Node last = new Node(0);
        Node sentinel = last;
        nodes.add(last);

        for (int i = 0; i < numberOfNodes; i++) {
            int id = random.nextInt(1, numberOfNodes * 5); // Generate random ID between 0 and 999
            Node node = new Node(id);
            nodes.add(node);
            last.setNext(node);

            if (i == numberOfNodes - 1) {
                node.setNext(sentinel);
            }
            last = node;
        }


        return nodes;
    }

    public static List<Node> generateNodes(int numberOfNodes) {
        List<Node> nodes = new ArrayList<>();
        int[] ids = new int[numberOfNodes];

        for (int i = 0; i < numberOfNodes; i++) {
            ids[i] = i;
        }

        for (int i = 0; i < numberOfNodes; i++) {
            Node node = new Node(ids[i]);
            nodes.add(node);
        }

        for (int i = 0; i < numberOfNodes; i++) {
            Node node = nodes.get(i);
            node.setNext(nodes.get((i + 1) % numberOfNodes));
        }

        return nodes;
    }
}
