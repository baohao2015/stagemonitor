package org.stagemonitor.core.metrics.metrics2;

import java.util.concurrent.TimeUnit;

import com.codahale.metrics.Clock;
import com.codahale.metrics.ExponentiallyDecayingReservoir;
import com.codahale.metrics.Reservoir;
import com.codahale.metrics.Timer;

/**
 * A timer metric which aggregates timing durations and provides duration statistics, plus
 * throughput statistics via {@link MeterExt}.
 */
public class TimerExt extends Timer {

    private final MeterExt meterExt;

    /**
     * Creates a new {@link TimerExt} using an {@link ExponentiallyDecayingReservoir} and the default
     * {@link Clock}.
     */
    public TimerExt() {
        this(new ExponentiallyDecayingReservoir());
    }

    /**
     * Creates a new {@link TimerExt} that uses the given {@link Reservoir}.
     *
     * @param reservoir the {@link Reservoir} implementation the timer should use
     */
    public TimerExt(Reservoir reservoir) {
        this(reservoir, Clock.defaultClock());
    }

    /**
     * Creates a new {@link TimerExt} that uses the given {@link Reservoir} and {@link Clock}.
     *
     * @param reservoir the {@link Reservoir} implementation the timer should use
     * @param clock  the {@link Clock} implementation the timer should use
     */
    public TimerExt(Reservoir reservoir, Clock clock) {
        super(reservoir, clock);
        this.meterExt = new MeterExt(clock);
    }

    /**
     * Adds a recorded duration.
     *
     * @param duration the length of the duration
     * @param unit     the scale unit of {@code duration}
     */
    @Override
    public void update(long duration, TimeUnit unit) {
        super.update(duration, unit);
        update(unit.toNanos(duration));
    }

    public long getFifteenMinuteCount() {
        return this.meterExt.getFifteenMinuteCount();
    }

    public long getFiveMinuteCount() {
        return this.meterExt.getFiveMinuteCount();
    }

    public long getOneMinuteCount() {
        return this.meterExt.getOneMinuteCount();
    }

    public MeterExt getMeterExt() {
        return this.meterExt;
    }

    private void update(long duration) {
        if (duration >= 0) {
            this.meterExt.mark();
        }
    }
}
