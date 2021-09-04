import event.EntranceEvent;
import event.Event;
import event.LeaveEvent;
import model.Membership;

public class TurnstileService {
    private final EventStore eventStore;
    private final ManagerService managerService;

    public TurnstileService(EventStore eventStore, ManagerService managerService) {
        this.eventStore = eventStore;
        this.managerService = managerService;
    }

    public boolean passEntrance(int membershipId) {
        Event event = new EntranceEvent(membershipId, System.currentTimeMillis());
        return passThrough(event);
    }

    public boolean passLeave(int membershipId) {
        Event event = new LeaveEvent(membershipId, System.currentTimeMillis());
        return passThrough(event);
    }

    private boolean passThrough(Event event) {
        Membership membership = managerService.getMembershipById(event.getMembershipId());
        if (membership == null || membership.getExpiresAtMillis() <= event.getTimeMillis()) {
            return false;
        }

        try {
            eventStore.handleTurnstileEvent(event);
        } catch (TurnstileActionException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }
}
