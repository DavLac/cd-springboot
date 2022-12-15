
public class TextFormatter {

    private final DateTimeService dateTimeService;

    public TextFormatter(DateTimeService dateTimeService) {
        this.dateTimeService = dateTimeService;
    }

    public String addDateTime(String input) {
        return input + " - " + dateTimeService.getDateTimeNow();
    }

}
