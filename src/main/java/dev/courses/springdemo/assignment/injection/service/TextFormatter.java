package dev.courses.springdemo.assignment.injection.service;

public class TextFormatter {

    private final DateTimeService dateTimeService;

    public TextFormatter(DateTimeService dateTimeService) {
        this.dateTimeService = dateTimeService;
    }

    public String addDateTime(String input) {
        // Should return concatenation of input + time
        // return input + " - " + DateTimeService.getDateTimeNow();
        return input + " - " + dateTimeService.getDateTimeNow();
    }

}
