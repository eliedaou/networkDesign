package daoumoyer.receiver;

import daoumoyer.statemachine.Event;

import java.io.FileOutputStream;
import java.net.SocketAddress;

/**
 * @author Grant Moyer
 * @since 2015-11-24
 */
public class ReceiverEvent implements Event {
	private ReceivedPacket packet;
	private FileOutputStream fOut;

	public ReceiverEvent(ReceivedPacket packet, FileOutputStream fOut){
		this.packet = packet;
		this.fOut = fOut;
	}

	public boolean isCorrupt() {
		return packet.isCorrupt();
	}

	public int getSeq() {
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
}
