import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Main {
    public static void main(String[] args) {
        DateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
        FitnessCenterSystem fitnessCenterSystem = new FitnessCenterSystem();
        boolean created = fitnessCenterSystem.managerService.createMembershipForDuration(0, 0, 365);
        System.out.println(formatter.format(new Date(
                (fitnessCenterSystem.managerService.getMembershipById(0).getExpiresAtMillis()))));
        boolean created2 = fitnessCenterSystem.managerService.createMembershipForDuration(0, 1, 10);
        fitnessCenterSystem.managerService.extendMembership(0, 10);
        System.out.println(formatter.format(new Date(
                (fitnessCenterSystem.managerService.getMembershipById(0).getExpiresAtMillis()))));

        System.out.println(fitnessCenterSystem.turnstileService.passEntrance(0));
        System.out.println(fitnessCenterSystem.turnstileService.passLeave(0));

        System.out.println(fitnessCenterSystem.turnstileService.passLeave(0));
        System.out.println(fitnessCenterSystem.turnstileService.passEntrance(2));

    }
}
