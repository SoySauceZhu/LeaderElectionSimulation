package util;

import entity.common.Node;
import entity.common.Port;
import entity.hs.HSNode;
import entity.lcr.LCRNode;

import java.util.ArrayList;
import java.util.List;

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
            currentNode.setNeighbor(Port.RIGHT, nextNode);
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
            Node node = new LCRNode(i);
            nodes.add(node);
        }

        for (int i = 0; i < numberOfNodes; i++) {
            Node currentNode = nodes.get(i);
            Node nextNode = nodes.get((i + 1) % nodes.size());
            currentNode.setNeighbor(Port.RIGHT, nextNode);
        }

        return nodes;
    }

    public static void main(String[] args) {
        List<Node> nodes = GenerateNodes.generateLCRNodes(10);
        for (Node node : nodes) {
            System.out.println(node);
        }

        nodes.get(2).start();
    }
}
















