package dev.courses.springdemo.assignment.injection.service;

public class OutputAggregator {

    private final AppLogger appLogger;
    private final TextFormatter textFormatter;

    public OutputAggregator(AppLogger appLogger, TextFormatter textFormatter) {
        this.appLogger = appLogger;
        this.textFormatter = textFormatter;
    }

    public void printInput(String input) {
        // call AppLogger.printLog(TextFormatter.addDateTime(input)) to print the result
        appLogger.printLog(textFormatter.addDateTime(input));
    }

}
