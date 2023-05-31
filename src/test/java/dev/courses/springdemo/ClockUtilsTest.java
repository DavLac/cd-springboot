package dev.courses.springdemo;

import dev.courses.springdemo.util.ClockUtils;
import dev.courses.springdemo.utils.TestClockUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.LocalDate;

import static java.time.temporal.ChronoUnit.SECONDS;
import static org.assertj.core.api.Assertions.assertThat;

class ClockUtilsTest {

    private static final Instant TEST_TIME_INSTANT = Instant.parse("2023-02-23T00:00:00Z");
    private static final LocalDate TEST_TIME_LOCAL_DATE = LocalDate.of(2023, 2, 23);
    private static final long MAX_SECONDS_BETWEEN_TWO_INSTANTS = 2L;

    @AfterEach
    void clean() {
        TestClockUtils.useSystemClock();
    }

    @Test
    void localDateNow_shouldReturnCurrentDate() {
        TestClockUtils.useSystemClock();
        assertThat(ClockUtils.localDateNow()).isEqualTo(LocalDate.now());
    }

    @Test
    void instantNow_shouldReturnCurrentDateTime() {
        TestClockUtils.useSystemClock();
        assertThat(SECONDS.between(ClockUtils.instantNow(), Instant.now())).isLessThan(MAX_SECONDS_BETWEEN_TWO_INSTANTS);
    }

    @Test
    void instantNow_withFixedTime_shouldReturnFixedTime() {
        TestClockUtils.useFixedClockAt(TEST_TIME_INSTANT);
        assertThat(ClockUtils.instantNow()).isEqualTo(TEST_TIME_INSTANT);
    }

    @Test
    void localDateNow_withFixedTime_shouldReturnFixedDate() {
        TestClockUtils.useFixedClockAt(TEST_TIME_INSTANT);
        assertThat(ClockUtils.localDateNow()).isEqualTo(TEST_TIME_LOCAL_DATE);
    }

    @Test
    void instantNow_withFixedDAteTimeThenSystemDateTime_shouldReturnFixedDateTimeThenSystemDateTime() {
        // fixed
        TestClockUtils.useFixedClockAt(TEST_TIME_INSTANT);
        assertThat(ClockUtils.instantNow()).isEqualTo(TEST_TIME_INSTANT);
        assertThat(ClockUtils.localDateNow()).isEqualTo(TEST_TIME_LOCAL_DATE);

        // back to system date time
        TestClockUtils.useSystemClock();
        assertThat(ClockUtils.localDateNow()).isEqualTo(LocalDate.now());
        assertThat(SECONDS.between(ClockUtils.instantNow(), Instant.now())).isLessThan(MAX_SECONDS_BETWEEN_TWO_INSTANTS);
    }

}