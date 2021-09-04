public class FitnessCenterSystem {
    public ManagerService managerService;
    public ReportService reportService;
    public TurnstileService turnstileService;

    public FitnessCenterSystem() {
        LocalStorage localStorage = new LocalStorage();
        this.reportService = new ReportService(localStorage);
        EventStore eventStore = new EventStore(reportService);

        this.managerService = new ManagerService(eventStore);
        this.turnstileService = new TurnstileService(eventStore, managerService);
    }
}
