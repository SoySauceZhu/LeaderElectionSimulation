package service;

import entity.common.MessageLog;
import entity.common.Node;
import entity.common.NodeLog;
import util.GenerateNodes;

public class HS_SimulationService extends SimulationService {

    public HS_SimulationService(int num) {
        nodes = GenerateNodes.generateHSNodes(num);
    }

    public static void main(String[] args) throws CloneNotSupportedException {
        SimulationService service = new HS_SimulationService(10);
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
