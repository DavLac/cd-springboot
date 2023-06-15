package dev.courses.springdemo.util;

import org.springframework.stereotype.Component;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;

@Component
public class ClockWrapper {

    private final Clock clock;

    public ClockWrapper() {
        this.clock = Clock.system(ZoneOffset.UTC);
    }

    public ClockWrapper(Instant instant) {
        this.clock = Clock.fixed(instant, ZoneOffset.UTC);
    }

    public ClockWrapper(String instantString) {
        this.clock = Clock.fixed(Instant.parse(instantString), ZoneOffset.UTC);
    }

    public LocalDate localDateNow() {
        return LocalDate.now(clock);
    }

    public Instant instantNow() {
        return Instant.now(clock);
    }

    public Instant instantNowAtStartOfDay() {
        return localDateNow()
                .atStartOfDay(ZoneOffset.UTC)
                .toInstant();
    }

}
