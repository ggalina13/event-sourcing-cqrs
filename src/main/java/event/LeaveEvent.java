package event;

public class LeaveEvent extends Event {
    public LeaveEvent(int membershipId, long timeMillis) {
        super(membershipId, timeMillis);
    }
}
