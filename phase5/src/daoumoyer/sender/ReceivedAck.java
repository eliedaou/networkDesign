package daoumoyer.sender;

import java.net.DatagramPacket;
import java.lang.Math;

/**
 * @author Grant Moyer
 * @since 2015-11-24
 */
public class ReceivedAck {
	private boolean corrupt;
	private long seqNum;
	private double ackError;
	private double ackLoss;
	
	public ReceivedAck(DatagramPacket packet, double ackError, double ackLoss) {
		byte[] bytes = packet.getData();
		this.ackError = ackError;
		this.ackLoss = ackLoss;
		double checkIfBitError = Math.random();
		double checkIfAckLoss = Math.random();
		
		// if it's greater than the ratio, than the ack was received
		if (checkIfAckLoss > this.ackLoss) {
			
			//get checksum in big endian format
			int o = packet.getOffset();
			long check = 0;
			for (int i = 0; i < 8; ++i) {
				if (checkIfBitError < this.ackError) {
					//this is where we would change the bit because it is in the ratio
				}
				check += ((long) bytes[o + i] & 0xff) << (8 - 1 - i)*8;
			}
	
			//get sequence number in big endian format
			o += 8;
			seqNum = 0;
			for (int i = 0; i < 8; ++i) {
				seqNum += ((long) bytes[o + i] & 0xff) << (8 - 1 - i)*8;
			}
	
			corrupt = seqNum != check;
		}
		//if it's less than or equal to the ratio, then the ack was dropped, but i'm not sure how to represent that
		// i think we can just omit it
	}



	public long getSeqNum() {
		return seqNum;
	}

	public boolean isCorrupt() {
		return corrupt;
	}
}
