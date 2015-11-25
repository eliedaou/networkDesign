package daoumoyer.receiver;

import java.net.DatagramPacket;
import java.net.InetAddress;

/**
 * @author Grant Moyer
 * @since 2015-11-25
 */
public class AckToSend {
	private DatagramPacket packet;
	private long seqNum;

	public AckToSend(long seqNum, InetAddress senderAddress, int senderPort) {

		byte[] buffer = new byte[16]; //8 bytes for ack and 8 bytes for check

		//add sequence number and check
		for (int i = 0; i < 8; ++i) {
			buffer[i] = (byte) (seqNum >> (8 - 1 - i)*8 & 0xff);
			buffer[i + 8] = buffer[i];
		}

		packet = new DatagramPacket(buffer, buffer.length, senderAddress, senderPort);
	}

	public DatagramPacket getPacket() {
		return packet;
	}
}
