package entity;

public class NodeState {
    private final int id;
    private final int curMaxId;
    private final entity.Status status;
    private final Integer leaderId;
    private final boolean terminated;

    public NodeState(int id, int curMaxId, entity.Status status, Integer leaderId, boolean terminated) {
        this.id = id;
        this.curMaxId = curMaxId;
        this.status = status;
        this.leaderId = leaderId;
        this.terminated = terminated;
    }

    public int getId() {
        return id;
    }

    public int getCurMaxId() {
        return curMaxId;
    }

    public Status getStatus() {
        return status;
    }

    public Integer getLeaderId() {
        return leaderId;
    }

    public boolean isTerminated() {
        return terminated;
    }
}