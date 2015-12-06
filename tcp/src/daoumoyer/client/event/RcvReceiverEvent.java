package daoumoyer.client.event;

import daoumoyer.client.ClientReceivedPacket;
import daoumoyer.statemachine.Event;

import java.io.FileOutputStream;
import java.net.SocketAddress;

/**
 * @author Grant Moyer
 * @since 2015-11-24
 */
public class RcvReceiverEvent implements Event {
	private ClientReceivedPacket packet;
	private FileOutputStream fOut;

	public RcvReceiverEvent(ClientReceivedPacket packet, FileOutputStream fOut){
		this.packet = packet;
		this.fOut = fOut;
	}

	public boolean isCorrupt() {
		return packet.isCorrupt();
	}

	public long getSeq() {
		return packet.getSeq();
	}

	public byte[] getData() {
		return packet.getData();
	}

	public FileOutputStream getFOut() {
		return fOut;
	}

	public SocketAddress getSource() {
		return packet.getSource();
	}

	@Override
	public String toString() {
		return getClass().getName();
	}
}
