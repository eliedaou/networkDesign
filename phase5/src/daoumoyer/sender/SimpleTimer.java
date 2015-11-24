package daoumoyer.sender;

import java.util.Calendar;

/**
 * @author Grant Moyer
 * @since 2015-11-24
 */
public class SimpleTimer {
	private long endTime;
	private final long duration;
	private boolean stopped;

	public SimpleTimer(long duration) {
		this.duration = duration;
		stopped = true;
	}

	public boolean expired() {
		if (!stopped) {
			return System.currentTimeMillis() >= endTime;
		} else {
			throw new RuntimeException("Timer is stopped");
		}
	}

	public void restart() {
		endTime = System.currentTimeMillis() + duration;
		stopped = false;
	}

	public void stop() {
		stopped = true;
	}
}
