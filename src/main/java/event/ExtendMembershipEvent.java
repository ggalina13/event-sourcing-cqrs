package event;

public class ExtendMembershipEvent extends Event {
    private final int extendOnDays;

    public ExtendMembershipEvent(int membershipId, long timeMillis, int extendOnDays) {
        super(membershipId, timeMillis);
        this.extendOnDays = extendOnDays;
    }

    public int getExtendOnDays() {
        return extendOnDays;
    }
}
