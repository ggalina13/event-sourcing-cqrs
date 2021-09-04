import event.Event;

import java.util.Map;

public class ReportService {
    private final LocalStorage localStorage;

    public ReportService(LocalStorage localStorage) {
        this.localStorage = localStorage;
    }

    public Map<String, Integer> getVisitCountByDay() {
        return localStorage.getVisitCountByDay();
    }

    public long getAverageDurationMinutes() {
        return Math.round(localStorage.getAverageDurationMinutes());
    }

    public long getAverageDayVisitsCount() {
        return Math.round(localStorage.getAverageDayVisitsCount());
    }

    public void handleTurnstileEvent(Event event) throws TurnstileActionException {
        localStorage.handleTurnstileEvent(event);
    }
}
