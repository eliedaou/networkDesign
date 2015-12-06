package daoumoyer.tcp.event;

import daoumoyer.SimpleTimer;
import daoumoyer.tcp.TCPSegmentReceived;

import java.net.DatagramPacket;

/**
 * Represents a event meaning data was received over UDP for the TCP state machine. Contains the ack number.
 *
 * @author Grant Moyer
 * @since 2015-12-06
 */
public class DataReceivedTCPEvent extends TCPEvent {
	private TCPSegmentReceived segment;

	public DataReceivedTCPEvent(SimpleTimer timer, DatagramPacket packet) {
		super(timer);

		segment = new TCPSegmentReceived(packet);
	}

	public TCPSegmentReceived getSegment() {
		return segment;
	}
}
