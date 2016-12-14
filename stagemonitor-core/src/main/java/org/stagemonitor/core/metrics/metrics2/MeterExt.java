package org.stagemonitor.core.metrics.metrics2;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;

import com.codahale.metrics.Clock;
import com.codahale.metrics.Meter;

/**
 * A meter metric which measures mean throughput and one-, five-, and fifteen-minute
 * exponentially-weighted moving average and total throughputs.
 *
 * @see Meter
 */
public class MeterExt extends Meter {

    List<Long> bucket = new CopyOnWriteArrayList<Long>();

    /**
     * Creates a new {@link MeterExt}.
     */
    public MeterExt() {
        super();
    }

    /**
     * Creates a new {@link Meter}.
     *
     * @param clock      the clock to use for the meter ticks
     */
    public MeterExt(Clock clock) {
        super(clock);
    }

    /**
     * Mark the occurrence of a given number of events.
     *
     * @param n the number of events
     */
    @Override
    public void mark(long n) {
        super.mark(n);

        tickIfNecessary();

        while (n-- > 0) {
            this.bucket.add(System.currentTimeMillis());
        }
    }

    private void tickIfNecessary() {
        long deadLine = System.currentTimeMillis() - TimeUnit.MINUTES.toMillis(15);

        while (true) {
            if ((this.bucket.size() > 0) && (this.bucket.get(0) < deadLine)) {
                this.bucket.remove(0);
            } else {
                break;
            }
        }
    }

    private long count(TimeUnit timeUnit, long duration) {
        long ret = 0;
        long deadLine = System.currentTimeMillis() - timeUnit.toMillis(duration);

        for (int idx = this.bucket.size() - 1; idx > -1; idx--) {
            if (this.bucket.get(idx) >= deadLine) {
                ret++;
            }
        }

        return ret;
    }

    public long getFifteenMinuteCount() {
        tickIfNecessary();
        return count(TimeUnit.MINUTES, 15);
    }

    public long getFiveMinuteCount() {
        tickIfNecessary();
        return count(TimeUnit.MINUTES, 5);
    }

    public long getOneMinuteCount() {
        tickIfNecessary();
        return count(TimeUnit.MINUTES, 1);
    }
}
