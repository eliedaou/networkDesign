package daoumoyer.tcp.event;

import daoumoyer.SimpleTimer;

/**
 * Represents a event meaning data was received over UDP for the TCP state machine. Contains the ack number.
 *
 * @author Grant Moyer
 * @since 2015-12-06
 */
public class DataReceivedTCPEvent extends TCPEvent {
	public DataReceivedTCPEvent(SimpleTimer timer) {
		super(timer);
	}
}
