package controller;

import service.LCR_SimulationService;
import entity.Node;
import view.View;

import java.util.ArrayList;
import java.util.List;

public class Controller {
    public static void main(String[] args) {
        // Create nodes for the simulation
        List<Node> nodes = new ArrayList<>();
        int numNodes = 5; // Example node count

        for (int i = 0; i < numNodes; i++) {
            nodes.add(new Node(i + 1));
        }

        // Set up the circular topology
        for (int i = 0; i < numNodes; i++) {
            nodes.get(i).setNext(nodes.get((i + 1) % numNodes));
        }

        // Initialize and start the simulation
        LCR_SimulationService simulationService = new LCR_SimulationService(nodes);
        simulationService.startSimulation();

        // Visualize the results
        View visualizationService = new View(simulationService);
        visualizationService.displayNodeStatusHistory();
        visualizationService.displayMessageLogs();
    }
}
