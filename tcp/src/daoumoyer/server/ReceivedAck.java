package daoumoyer.server;

import java.net.DatagramPacket;
import java.lang.Math;

/**
 * @author Grant Moyer
 * @since 2015-11-24
 */
public class ReceivedAck {
	private boolean corrupt;
	private long seqNum;
	private static long corruptcount;
	
	public ReceivedAck(DatagramPacket packet, double ackError) {
		byte[] bytes = packet.getData();
		double checkIfBitError = Math.random();

		//get checksum in big endian format
		int o = packet.getOffset();
		long check = 0;
		for (int i = 0; i < 8; ++i) {
			// if check is less than error ration, simulate an error
			if (checkIfBitError < ackError) {
				++check;
			}
			check += ((long) bytes[o + i] & 0xff) << (8 - 1 - i)*8;
		}
		if (checkIfBitError < ackError) {
			System.out.println("Simulated ACK packet corruption " + ++corruptcount + " times");
		}
		//get sequence number in big endian format
		o += 8;
		seqNum = 0;
		for (int i = 0; i < 8; ++i) {
			seqNum += ((long) bytes[o + i] & 0xff) << (8 - 1 - i)*8;
		}

		corrupt = seqNum != check;
	}



	public long getSeqNum() {
		return seqNum;
	}

	public boolean isCorrupt() {
		return corrupt;
	}
}
