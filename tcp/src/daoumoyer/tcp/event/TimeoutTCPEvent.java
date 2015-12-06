package daoumoyer.tcp.event;

import daoumoyer.SimpleTimer;

/**
 * Represents a timeout event for the TCP state machine.
 *
 * @author Grant Moyer
 * @since 2015-12-06
 */
public class TimeoutTCPEvent extends TCPEvent {
	public TimeoutTCPEvent(SimpleTimer timer) {
		super(timer);
	}
}
