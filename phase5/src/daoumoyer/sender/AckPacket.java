package daoumoyer.sender;

import java.net.DatagramPacket;

/**
 * @author Grant Moyer
 * @since 2015-11-24
 */
public class AckPacket {
	private boolean corrupt;
	private long seqNum;

	public AckPacket(DatagramPacket packet) {
		throw new Error("Need to implement daoumoyer.sender.AckPacket.AckPacket()");
		//TODO implement
	}

	public long getSeqNum() {
		return seqNum;
	}

	public boolean isCorrupt() {
		return corrupt;
	}
}
