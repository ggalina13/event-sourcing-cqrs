import event.EntranceEvent;
import event.Event;
import event.LeaveEvent;
import model.TurnstileAction;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class LocalStorage {
    private final Map<String, Integer> visitCountByDay;
    private double averageDuration;
    private final Map<Integer, TurnstileAction> lastTurnstileActionByUser;
    private int visitCount;
    private final DateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");

    public LocalStorage() {
        this.visitCountByDay = new HashMap<>();
        this.averageDuration = 0;
        this.lastTurnstileActionByUser = new HashMap<>();
        this.visitCount = 0;
    }

    public void handleTurnstileEvent(Event event) throws TurnstileActionException {
        String dateString = formatter.format(new Date(event.getTimeMillis()));
        int clientId = event.getMembershipId();
        if (event instanceof EntranceEvent) {
            TurnstileAction clientLastAction = lastTurnstileActionByUser.get(clientId);
            if (clientLastAction != null && clientLastAction.isEntranceType())
                throw new TurnstileActionException("Client " + clientId + " double entrance error");
            int curVisitCountByDay = visitCountByDay.get(dateString) != null ? visitCountByDay.get(dateString) : 0;
            visitCountByDay.put(dateString, curVisitCountByDay + 1);
            lastTurnstileActionByUser.put(event.getMembershipId(),
                    new TurnstileAction(clientId, event.getTimeMillis(), true));
        }
        if (event instanceof LeaveEvent) {
            TurnstileAction clientLastAction = lastTurnstileActionByUser.get(clientId);
            if (clientLastAction != null && !clientLastAction.isEntranceType())
                throw new TurnstileActionException("Client " + clientId + " double leave error");
            long entranceTime = clientLastAction.getTimestamp();
            long leaveTime = event.getTimeMillis();
            averageDuration = (averageDuration * visitCount + (leaveTime - entranceTime)) / (visitCount + 1);
            visitCount += 1;
            lastTurnstileActionByUser.put(event.getMembershipId(),
                    new TurnstileAction(clientId, event.getTimeMillis(), false));
        }
    }

    public Map<String, Integer> getVisitCountByDay() {
        Map<String, Integer> result = new HashMap<>();
        result.putAll(visitCountByDay);
        return result;
    }

    public double getAverageDurationMinutes() {
        return TimeUnit.MILLISECONDS.toMinutes(Math.round(averageDuration));
    }

    public double getAverageDayVisitsCount() {
        return visitCountByDay.values().stream().mapToInt(a -> a).average().orElse(0);
    }
}
