package daoumoyer.tcp.event;

import daoumoyer.SimpleTimer;

import java.nio.ByteBuffer;

/**
 * Represents an event meaning more data is available from above to the TCP state machine. Contains said data.
 *
 * @author Grant Moyer
 * @since 2015-12-06
 */
public class DataToSendTCPEvent extends TCPEvent {
	private ByteBuffer data;

	public DataToSendTCPEvent(byte[] data, SimpleTimer timer) {
		super(timer);
	}

	public ByteBuffer getData() {
		return data;
	}
}
