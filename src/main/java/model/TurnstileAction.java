package model;

public class TurnstileAction {
    private final int clientId;
    private final long timestamp;
    private final boolean entranceType;

    public TurnstileAction(int clientId, long timestamp, boolean entranceType) {
        this.clientId = clientId;
        this.timestamp = timestamp;
        this.entranceType = entranceType;
    }

    public int getClientId() {
        return clientId;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public boolean isEntranceType() {
        return entranceType;
    }
}
