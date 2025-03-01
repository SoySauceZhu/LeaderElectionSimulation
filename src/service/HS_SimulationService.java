package service;

import util.GenerateNodes;

public class HS_SimulationService extends SimulationService {

    public HS_SimulationService() {
    }

    public static void main(String[] args) throws CloneNotSupportedException {
        SimulationService service = new HS_SimulationService();
        GenerateNodes generateNodes = new GenerateNodes(42);
        service.setNodes(generateNodes.generateHSNodes(10));
        service.startSimulation();

//        System.out.println("\n\n");

//        for (Node node : service.nodes) {
//            System.out.println(node);
//        }

//        System.out.println("\n\n");
//
//        for (MessageLog messageLog : service.messageLogs) {
//            System.out.println(messageLog);
//        }
//
//        System.out.println("\n\n");
//
//        for (NodeLog nodeLog: service.nodeLogs) {
//            System.out.println(nodeLog);
//        }
    }
}
