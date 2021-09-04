package event;

public abstract class Event {
    private final int membershipId;
    private final long timeMillis;

    public Event(int membershipId, long timeMillis) {
        this.membershipId = membershipId;
        this.timeMillis = timeMillis;
    }

    public int getMembershipId() {
        return membershipId;
    }

    public long getTimeMillis() {
        return timeMillis;
    }
}