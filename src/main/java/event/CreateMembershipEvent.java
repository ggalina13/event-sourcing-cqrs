package event;

public class CreateMembershipEvent extends Event {
    private final int clientId;
    private final long expiresAtMillis;

    public CreateMembershipEvent(int membershipId, long timeMillis, int clientId, long expiresAtMillis) {
        super(membershipId, timeMillis);
        this.expiresAtMillis = expiresAtMillis;
        this.clientId = clientId;
    }

    public long getExpiresAtMillis() {
        return expiresAtMillis;
    }

    public int getClientId() {
        return clientId;
    }
}
