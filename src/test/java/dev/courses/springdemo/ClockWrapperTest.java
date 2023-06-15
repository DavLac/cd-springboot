package dev.courses.springdemo;

import dev.courses.springdemo.util.ClockWrapper;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;

import static java.time.temporal.ChronoUnit.SECONDS;
import static org.assertj.core.api.Assertions.assertThat;

class ClockWrapperTest {

    private static final Instant TEST_TIME_INSTANT = Instant.parse("2023-02-23T00:00:00Z");
    private static final Instant TEST_TIME_INSTANT_START_OF_DAY = Instant.parse("2023-02-23T00:00:00Z");
    private static final LocalDate TEST_TIME_LOCAL_DATE = LocalDate.of(2023, 2, 23);
    private static final long MAX_SECONDS_BETWEEN_TWO_INSTANTS = 2L;

    @Test
    void localDateNow_shouldReturnCurrentDate() {
        ClockWrapper systemClock = new ClockWrapper();
        assertThat(systemClock.localDateNow()).isEqualTo(LocalDate.now(ZoneOffset.UTC));
    }

    @Test
    void instantNow_shouldReturnCurrentDateTime() {
        ClockWrapper systemClock = new ClockWrapper();
        assertThat(SECONDS.between(systemClock.instantNow(), Instant.now())).isLessThan(MAX_SECONDS_BETWEEN_TWO_INSTANTS);
    }

    @Test
    void instantNow_withFixedTime_shouldReturnFixedTime() {
        ClockWrapper fixedClock = new ClockWrapper(TEST_TIME_INSTANT);
        assertThat(fixedClock.instantNow()).isEqualTo(TEST_TIME_INSTANT);
    }

    @Test
    void localDateNow_withFixedTime_shouldReturnFixedDate() {
        ClockWrapper fixedClock = new ClockWrapper(TEST_TIME_INSTANT);
        assertThat(fixedClock.localDateNow()).isEqualTo(TEST_TIME_LOCAL_DATE);
    }

    @Test
    void instantNow_withFixedDAteTimeThenSystemDateTime_shouldReturnFixedDateTimeThenSystemDateTime() {
        // fixed
        ClockWrapper fixedClock = new ClockWrapper(TEST_TIME_INSTANT);
        assertThat(fixedClock.instantNow()).isEqualTo(TEST_TIME_INSTANT);
        assertThat(fixedClock.localDateNow()).isEqualTo(TEST_TIME_LOCAL_DATE);

        // back to system date time
        ClockWrapper systemClock = new ClockWrapper();
        assertThat(systemClock.localDateNow()).isEqualTo(LocalDate.now());
        assertThat(SECONDS.between(systemClock.instantNow(), Instant.now())).isLessThan(MAX_SECONDS_BETWEEN_TWO_INSTANTS);
    }

    @Test
    void instantNowAtStartOfDay_withFixedTimeEndOfDay_shouldReturnFixedDateTimeEndOfDay() {
        ClockWrapper fixedClock = new ClockWrapper("2023-02-23T23:59:59Z");
        assertThat(fixedClock.instantNowAtStartOfDay()).isEqualTo(TEST_TIME_INSTANT_START_OF_DAY);
    }

    @Test
    void instantNowAtStartOfDay_withFixedTimeMidDay_shouldReturnFixedDateTimeEndOfDay() {
        ClockWrapper fixedClock = new ClockWrapper("2023-02-23T12:34:56Z");
        assertThat(fixedClock.instantNowAtStartOfDay()).isEqualTo(TEST_TIME_INSTANT_START_OF_DAY);
    }

    @Test
    void instantNowAtStartOfDay_withFixedTimeStartOfDay_shouldReturnFixedDateTimeEndOfDay() {
        ClockWrapper fixedClock = new ClockWrapper(TEST_TIME_INSTANT_START_OF_DAY);
        assertThat(fixedClock.instantNowAtStartOfDay()).isEqualTo(TEST_TIME_INSTANT_START_OF_DAY);
    }

}