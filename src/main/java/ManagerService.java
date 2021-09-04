import event.CreateMembershipEvent;
import event.Event;
import event.ExtendMembershipEvent;
import model.Membership;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

public class ManagerService {
    private final EventStore eventStore;

    public ManagerService(EventStore eventStore) {
        this.eventStore = eventStore;
    }

    public boolean createMembershipForDuration(int membershipId, int clientId, int durationDays) {
        if (getMembershipById(membershipId) != null) {
            return false;
        }
        long currentTimeMillis = System.currentTimeMillis();
        Event event = new CreateMembershipEvent(membershipId, currentTimeMillis, clientId,
                currentTimeMillis + TimeUnit.DAYS.toMillis(durationDays));
        eventStore.handleMembershipEvent(event);
        return true;
    }

    public boolean createMembershipUntil(int membershipId, int clientId, long timeMillis) {
        if (getMembershipById(membershipId) != null) {
            return false;
        }
        Event event = new CreateMembershipEvent(membershipId, System.currentTimeMillis(), clientId,
                timeMillis);
        eventStore.handleMembershipEvent(event);
        return true;
    }

    public void extendMembership(int membershipId, int dayCount) {
        Event event = new ExtendMembershipEvent(membershipId, System.currentTimeMillis(), dayCount);
        eventStore.handleMembershipEvent(event);
    }

    public Membership getMembershipById(int membershipId) {
        Optional<Event> creationEvent = eventStore
                .getEvents()
                .stream()
                .filter(event -> (event instanceof CreateMembershipEvent) && event.getMembershipId() == membershipId)
                .findAny();
        if (creationEvent.isEmpty()) {
            return null;
        }
        CreateMembershipEvent createMembershipEvent = (CreateMembershipEvent) creationEvent.get();
        int clientId = createMembershipEvent.getClientId();
        long expiredAtMillis = createMembershipEvent.getExpiresAtMillis();
        int extendedOnDays = eventStore
                .getEvents()
                .stream()
                .filter(event -> (event instanceof ExtendMembershipEvent) && event.getMembershipId() == membershipId)
                .map(event -> ((ExtendMembershipEvent) event).getExtendOnDays())
                .mapToInt(a -> a)
                .sum();
        expiredAtMillis += TimeUnit.DAYS.toMillis(extendedOnDays);
        return new Membership(clientId, membershipId, expiredAtMillis);
    }
}
