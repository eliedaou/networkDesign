package daoumoyer.tcp.event;

import daoumoyer.SimpleTimer;

/**
 * Represents an event meaning more data is available from above to the TCP state machine. Contains said data.
 *
 * @author Grant Moyer
 * @since 2015-12-06
 */
public class DataToSendTCPEvent extends TCPEvent {
	public DataToSendTCPEvent(SimpleTimer timer) {
		super(timer);
	}
}
