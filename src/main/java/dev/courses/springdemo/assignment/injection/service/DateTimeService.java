package dev.courses.springdemo.assignment.injection.service;

import java.time.Instant;

public class DateTimeService {
    public Instant getDateTimeNow() {
        return Instant.now();
    }
}
