package daoumoyer.server.event;

import daoumoyer.SimpleTimer;
import daoumoyer.statemachine.Event;

/**
 * @author Grant Moyer
 * @since 2015-11-24
 */
public abstract class ServerEvent implements Event {
	private SimpleTimer timer;

	public ServerEvent(SimpleTimer timer) {
		this.timer = timer;
	}

	@Override
	public String toString() {
		return getClass().getName();
	}

	public SimpleTimer getTimer() {
		return timer;
	}
}
