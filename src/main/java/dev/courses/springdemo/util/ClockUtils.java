package dev.courses.springdemo.util;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;

public class ClockUtils {

    protected ClockUtils() {
        // util class
    }

    protected static final Clock systemClock = Clock.systemDefaultZone();
    private static Clock clock = systemClock;

    protected static void setClock(Clock clock) {
        ClockUtils.clock = clock;
    }

    public static LocalDate localDateNow() {
        return LocalDate.now(clock);
    }

    public static Instant instantNow() {
        return Instant.now(clock);
    }
}
