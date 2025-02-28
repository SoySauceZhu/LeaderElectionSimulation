package service;

import entity.common.Node;
import util.GenerateNodes;

public class HS_SimulationService extends SimulationService {

    public HS_SimulationService(int num) {
        nodes = GenerateNodes.generateHSNodes(num);
    }

    public static void main(String[] args) {
        SimulationService service = new HS_SimulationService(10);
        service.startSimulation();

        for (Node node : service.nodes) {
            System.out.println(node);
        }
    }
}
