package daoumoyer.sender;

import daoumoyer.statemachine.Event;

/**
 * @author Grant Moyer
 * @since 2015-11-24
 */
public class SenderEvent implements Event {
	private ServerReceived packet;

	public SenderEvent(ServerReceived packet) {
		this.packet = packet;
	}

	public boolean isCorrupt(double ackError) {
		double checkIfBitError = Math.random();
		if (checkIfBitError < ackError) {
			return true;
		}

		return packet.isCorrupt();
	}

	public int getSeq() {
		return packet.getSeq();
	}

	public byte[] getData() {
		return packet.getData();
	}

	public boolean isAck() {
		return packet.isAck();
	}
}
