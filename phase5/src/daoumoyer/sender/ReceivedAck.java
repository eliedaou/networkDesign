package daoumoyer.sender;

import java.net.DatagramPacket;

/**
 * @author Grant Moyer
 * @since 2015-11-24
 */
public class ReceivedAck {
	private boolean corrupt;
	private long seqNum;

	public ReceivedAck(DatagramPacket packet) {
		byte[] bytes = packet.getData();

		//get checksum in big endian format
		int o = packet.getOffset();
		long check = 0;
		for (int i = 0; i < 8; ++i) {
			check += ((long) bytes[o + i] & 0xff) << (8 - 1 - i)*8;
		}

		//get sequence number in big endian format
		o += 8;
		seqNum = 0;
		for (int i = 0; i < 8; ++i) {
			seqNum += ((long) bytes[o + i] & 0xff) << (8 - 1 - i)*8;
		}

		corrupt = seqNum == check;
	}



	public long getSeqNum() {
		return seqNum;
	}

	public boolean isCorrupt() {
		return corrupt;
	}
}
