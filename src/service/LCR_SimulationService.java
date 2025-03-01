package service;

import entity.common.MessageLog;
import util.GenerateNodes;

public class LCR_SimulationService extends SimulationService {

    public LCR_SimulationService() {

    }

    public static void main(String[] args) throws CloneNotSupportedException {
        SimulationService service = new LCR_SimulationService();
        GenerateNodes generateNodes = new GenerateNodes(42);
//        service.setNodes(generateNodes.generateRandomLCRNodes(10));
        service.setNodes(generateNodes.generateLCRNodes(10));
        service.startSimulation();

//        System.out.println("\n\n");
//
//        for (Node node : service.nodes) {
//            System.out.println(node);
//        }


//
//        System.out.println("\n\n");
//
//        for (NodeLog nodeLog : service.nodeLogs) {
//            System.out.println(nodeLog);
//        }
    }
}
