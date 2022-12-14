package com.example.Spring_boot_demo.assignment.injection.service;

import org.springframework.stereotype.Component;

@Component
public class OutputAggregator {

    private final AppLogger logger;
    private final TextFormatter formatter;
    // inject AppLogger dependency
    // inject TextFormatter dependency


    public OutputAggregator(AppLogger logger, TextFormatter formatter) {
        this.logger = logger;
        this.formatter = formatter;
    }

    public void printInput(String input) {
        // call AppLogger.printLog(TextFormatter.addDateTime(input)) to print the result
        logger.printLog(formatter.addDateTime(input));
    }

}
