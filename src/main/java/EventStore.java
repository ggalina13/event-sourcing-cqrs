import event.Event;

import java.util.LinkedList;
import java.util.List;

public class EventStore {
    private final List<Event> events;
    private final ReportService reportService;

    public EventStore(ReportService reportService) {
        this.events = new LinkedList<>();
        this.reportService = reportService;
    }

    public List<Event> getEvents() {
        return List.copyOf(events);
    }

    public void handleMembershipEvent(Event event) {
        events.add(event);
    }

    public void handleTurnstileEvent(Event event) throws TurnstileActionException {
        events.add(event);
        reportService.handleTurnstileEvent(event);
    }
}
