package io.github.srcimon.screwbox.core;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class DurationTest {

    @Test
    void seconds_returnsSeconds() {
        Duration seconds = Duration.ofSeconds(2);

        assertThat(seconds.seconds()).isEqualTo(2);
    }

    @Test
    void none_returnsDurationOfNotEvenANanoSecond() {
        assertThat(Duration.none().nanos()).isZero();
    }

    @Test
    void since_returnsDurationOfPositiveNanos() {
        Time now = Time.now();

        long nanosSince = Duration.since(now).nanos();

        assertThat(nanosSince).isNotNegative();
    }

    @Test
    void ofMillis_returnsNewInstance() {
        Duration duration = Duration.ofMillis(10);

        assertThat(duration.nanos()).isEqualTo(10 * Time.NANOS_PER_MILLISECOND);
    }

    @Test
    void ofMicros_returnsNewInstance() {
        Duration duration = Duration.ofMicros(10);

        assertThat(duration.nanos()).isEqualTo(10 * Time.NANOS_PER_MICROSECOND);
    }

    @Test
    void ofNanos_returnsNewInstance() {
        Duration duration = Duration.ofNanos(5 * Time.NANOS_PER_MILLISECOND);

        assertThat(duration.milliseconds()).isEqualTo(5);
    }

    @Test
    void between_thenIsBeforeNow_returnsPositiveDuration() {
        Time now = Time.atNanos(20_000);
        Time then = Time.atNanos(10_500);

        assertThat(Duration.between(now, then)).isEqualTo(Duration.ofNanos(9_500));
    }

    @Test
    void between_nowIsBeforeThen_returnsPositiveDuration() {
        Time now = Time.atNanos(10_500);
        Time then = Time.atNanos(20_000);

        assertThat(Duration.between(now, then)).isEqualTo(Duration.ofNanos(9_500));
    }

    @Test
    void isAtLeast_otherHasLessNanos_returnsTrue() {
        Duration other = Duration.ofMillis(10);
        Duration duration = Duration.ofMillis(20);

        assertThat(duration.isAtLeast(other)).isTrue();
    }

    @Test
    void isAtLeast_otherHasMoreNanos_returnsFalse() {
        Duration other = Duration.ofMillis(30);
        Duration duration = Duration.ofMillis(20);

        assertThat(duration.isAtLeast(other)).isFalse();
    }

    @Test
    void isLessThan_otherHasLessNanos_returnsFalse() {
        Duration other = Duration.ofMillis(10);
        Duration duration = Duration.ofMillis(20);

        assertThat(duration.isLessThan(other)).isFalse();
    }

    @Test
    void isLessThan_otherHasMoreNanos_returnsTrue() {
        Duration other = Duration.ofMillis(30);
        Duration duration = Duration.ofMillis(20);

        assertThat(duration.isLessThan(other)).isTrue();
    }

    @Test
    void ofSeconds_returnsNewInstance() {
        Duration duration = Duration.ofSeconds(10);

        assertThat(duration.nanos()).isEqualTo(10 * Time.NANOS_PER_SECOND);
    }

    @Test
    void hashcode_calculatesHashCode() {
        Duration aDuration = Duration.ofMillis(10);
        Duration anotherDuration = Duration.ofMillis(15);

        assertThat(aDuration.hashCode()).isEqualTo(10000031);
        assertThat(aDuration).doesNotHaveSameHashCodeAs(anotherDuration);
    }

    @Test
    void equals_same_isTrue() {
        assertThat(Duration.ofMillis(10)).isEqualTo(Duration.ofNanos(10_000_000));
    }

    @Test
    void equals_different_isFalse() {
        assertThat(Duration.ofMillis(10)).isNotEqualTo(Duration.ofMillis(3));
    }

    @Test
    void ofExecution_executionNull_throwsException() {
        assertThatThrownBy(() -> Duration.ofExecution(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("execution must not be null");
    }

    @Test
    void ofExecution_executionTakesTime_returnsNewInstance() {
        // burn some cpu
        Duration duration = Duration.ofExecution(Time::now);

        assertThat(duration.nanos()).isPositive();
    }

    @Test
    void add_valueSet_addsDuration() {
        Duration duration = Duration.ofSeconds(2);

        Duration result = duration.add(Duration.ofMillis(500));

        assertThat(result.milliseconds()).isEqualTo(2500);
    }

    @Test
    void isNone_hasLength_isFalse() {
        assertThat(Duration.ofSeconds(1).isNone()).isFalse();
    }

    @Test
    void isNone_noLength_isTrue() {
        assertThat(Duration.none().isNone()).isTrue();
    }

    @Test
    void add_valueNull_throwsException() {
        Duration duration = Duration.ofSeconds(2);
        assertThatThrownBy(() -> duration.add(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("duration must not be null");
    }

    @Test
    void addTo_validTime_returnsNewTime() {
        Time time = Time.atNanos(1239192);

        var result = Duration.ofSeconds(2).addTo(time);

        assertThat(result).isEqualTo(Time.atNanos(2001239192));
    }

    @ParameterizedTest
    @CsvSource(delimiter = ';', value = {
            "0; 0ns",
            "20; 20ns",
            "21_000; 21µs",
            "92_921; 92µs, 921ns",
            "1_000_000; 1ms",
            "34_000_000_000; 34s",
            "7_200_000_000_000; 2h",
            "7_200_000_000_999; 2h"
    })
    void humanReadable_isZero_isZeroSeconds(long nanos, String humanFormat) {
       assertThat(Duration.ofNanos(nanos).humanReadable()).isEqualTo(humanFormat);
    }
}
