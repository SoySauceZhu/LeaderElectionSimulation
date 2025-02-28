package service;

import entity.common.MessageLog;
import entity.common.NodeState;
import entity.hs.Node;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class HS_SimulationService {
    private final Collection<Node> nodes;
    private final List<Map<Integer, NodeState>> nodeStatusHistory = new ArrayList<>();
    private final List<MessageLog> messageLogs = new ArrayList<>();

    public HS_SimulationService(Collection<Node> nodes) {
        this.nodes = nodes;
    }
}
