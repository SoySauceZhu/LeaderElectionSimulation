package entity.common;

public class NodeLog {
    private final Integer id;
    private final NodeType nodeType;
    private final Integer leaderId;
    private final boolean terminated;

    public NodeLog(Integer id, NodeType nodeType, Integer leaderId, boolean terminated) {
        this.id = id;
        this.nodeType = nodeType;
        this.leaderId = leaderId;
        this.terminated = terminated;
    }

    public Integer getId() {
        return id;
    }

    public NodeType getStatus() {
        return nodeType;
    }

    public Integer getLeaderId() {
        return leaderId;
    }

    public boolean isTerminated() {
        return terminated;
    }
}
