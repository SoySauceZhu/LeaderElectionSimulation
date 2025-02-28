package service;

import entity.common.Node;
import util.GenerateNodes;

public class LCR_SimulationService extends SimulationService {

    public LCR_SimulationService(int num) {
        nodes = GenerateNodes.generateLCRNodes(num);
    }

    public static void main(String[] args) {
        SimulationService service = new LCR_SimulationService(10);
        service.startSimulation();

        for (Node node : service.nodes) {
            System.out.println(node);
        }
    }
}
