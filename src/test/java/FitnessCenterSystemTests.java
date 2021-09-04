import event.EntranceEvent;
import event.Event;
import event.LeaveEvent;
import model.Membership;
import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class FitnessCenterSystemTests {
    @Test
    public void MembershipTest() {
        FitnessCenterSystem fitnessCenterSystem = new FitnessCenterSystem();
        assertTrue(fitnessCenterSystem.managerService.createMembershipForDuration(0, 0, 365));
        assertFalse(fitnessCenterSystem.managerService.createMembershipForDuration(0, 1, 100));

        Membership membership = fitnessCenterSystem.managerService.getMembershipById(0);
        assertNotNull(membership);
    }

    @Test
    public void TurnstileTest() {
        FitnessCenterSystem fitnessCenterSystem = new FitnessCenterSystem();
        fitnessCenterSystem.managerService.createMembershipForDuration(0, 0, 365);

        assertTrue(fitnessCenterSystem.turnstileService.passEntrance(0));
        assertTrue(fitnessCenterSystem.turnstileService.passLeave(0));
        assertFalse(fitnessCenterSystem.turnstileService.passLeave(0));
        assertFalse(fitnessCenterSystem.turnstileService.passEntrance(1));
        assertTrue(fitnessCenterSystem.turnstileService.passEntrance(0));
        assertFalse(fitnessCenterSystem.turnstileService.passEntrance(0));

        fitnessCenterSystem.managerService.createMembershipUntil(3, 1,
                System.currentTimeMillis() - 100);
        assertFalse(fitnessCenterSystem.turnstileService.passEntrance(3));
        fitnessCenterSystem.managerService.extendMembership(3, 3);
        assertTrue(fitnessCenterSystem.turnstileService.passEntrance(3));
    }

    @Test
    public void ReportTest() throws ParseException, TurnstileActionException {
        FitnessCenterSystem fitnessCenterSystem = new FitnessCenterSystem();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
        long time1 = sdf.parse("10-01-2021 10:30:45").getTime();
        long time1_ = sdf.parse("10-01-2021 11:35:40").getTime();

        long time2 = sdf.parse("10-01-2021 12:40:45").getTime();
        long time2_ = sdf.parse("10-01-2021 13:25:30").getTime();

        long time3 = sdf.parse("01-02-2021 10:40:45").getTime();
        long time3_ = sdf.parse("01-02-2021 12:35:45").getTime();

        long time4 = sdf.parse("01-02-2021 13:30:45").getTime();
        long time4_ = sdf.parse("01-02-2021 14:15:45").getTime();

        long time5 = sdf.parse("01-02-2021 20:30:45").getTime();
        long time5_ = sdf.parse("01-02-2021 22:00:10").getTime();

        Event event1 = new EntranceEvent(0, time1);
        Event event1_ = new LeaveEvent(0, time1_);

        Event event2 = new EntranceEvent(1, time2);
        Event event2_ = new LeaveEvent(1, time2_);

        Event event3 = new EntranceEvent(0, time3);
        Event event3_ = new LeaveEvent(0, time3_);

        Event event4 = new EntranceEvent(1, time4);
        Event event4_ = new LeaveEvent(1, time4_);

        Event event5 = new EntranceEvent(1, time5);
        Event event5_ = new LeaveEvent(1, time5_);

        fitnessCenterSystem.reportService.handleTurnstileEvent(event1);
        fitnessCenterSystem.reportService.handleTurnstileEvent(event1_);

        fitnessCenterSystem.reportService.handleTurnstileEvent(event2);
        fitnessCenterSystem.reportService.handleTurnstileEvent(event2_);

        fitnessCenterSystem.reportService.handleTurnstileEvent(event3);
        fitnessCenterSystem.reportService.handleTurnstileEvent(event3_);

        fitnessCenterSystem.reportService.handleTurnstileEvent(event4);
        fitnessCenterSystem.reportService.handleTurnstileEvent(event4_);

        fitnessCenterSystem.reportService.handleTurnstileEvent(event5);
        fitnessCenterSystem.reportService.handleTurnstileEvent(event5_);

        assertEquals(3, fitnessCenterSystem.reportService.getAverageDayVisitsCount());
        assertEquals(71, fitnessCenterSystem.reportService.getAverageDurationMinutes());
        Map<String, Integer> expected = new HashMap<>();

        expected.put("2021/01/10", 2);
        expected.put("2021/02/01", 3);
        Map<String, Integer> visitCountByDay = fitnessCenterSystem.reportService.getVisitCountByDay();
        assertEquals(visitCountByDay, expected);
    }
}
