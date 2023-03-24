package dev.courses.springdemo.utils;

import dev.courses.springdemo.util.ClockUtils;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;

public class TestClockUtils extends ClockUtils {

    private TestClockUtils() {
        // util class
    }

    public static void useFixedClockAt(Instant instant) {
        ClockUtils.setClock(Clock.fixed(instant, ZoneId.systemDefault()));
    }

    public static void useSystemClock() {
        ClockUtils.setClock(ClockUtils.systemClock);
    }
}
