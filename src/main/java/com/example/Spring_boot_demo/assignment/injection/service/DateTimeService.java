package com.example.Spring_boot_demo.assignment.injection.service;


import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class DateTimeService {
    public Instant getDateTimeNow() {
        return Instant.now();
    }
}
