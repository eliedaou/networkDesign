package daoumoyer.tcp.event;

import daoumoyer.SimpleTimer;
import daoumoyer.statemachine.Event;

/**
 * Represents a generic event given to the TCP state machine. Made to be extended.
 *
 * @author Grant Moyer
 * @since 2015-12-06
 */
public abstract class TCPEvent implements Event {
	private SimpleTimer timer;

	public TCPEvent(SimpleTimer timer) {
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
