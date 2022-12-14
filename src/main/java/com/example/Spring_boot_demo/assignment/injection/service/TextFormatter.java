package com.example.Spring_boot_demo.assignment.injection.service;

import org.springframework.stereotype.Component;

import javax.management.MXBean;

@Component
public class TextFormatter {

    // inject DateTimeService dependency
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
