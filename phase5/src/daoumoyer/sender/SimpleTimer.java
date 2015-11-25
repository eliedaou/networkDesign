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
		return !stopped && System.currentTimeMillis() >= endTime;
	}

	public void restart() {
		endTime = System.currentTimeMillis() + duration;
		stopped = false;
	}

	public void stop() {
		stopped = true;
	}
}
