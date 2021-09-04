package model;

public class Membership {
    private final int clientId;
    private final int membershipId;
    private final long expiresAtMillis;

    public Membership(int clientId, int membershipId, long expiresAtMillis) {
        this.clientId = clientId;
        this.membershipId = membershipId;
        this.expiresAtMillis = expiresAtMillis;
    }

    public int getClientId() {
        return clientId;
    }

    public int getMembershipId() {
        return membershipId;
    }

    public long getExpiresAtMillis() {
        return expiresAtMillis;
    }
}
