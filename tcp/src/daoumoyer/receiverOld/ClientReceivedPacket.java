package daoumoyer.receiver;

import java.net.DatagramPacket;
import java.net.SocketAddress;

public class ClientReceivedPacket {
	private final boolean corrupt;
	private long seq;
	private final byte[] data;
	private SocketAddress source;

	public ClientReceivedPacket(DatagramPacket packet) {
		byte[] buffer = packet.getData();
		int off = packet.getOffset();
		int length = packet.getLength();

		//do not need room for header, 4 bytes for checksum and 8 bytes for sequence
		data = new byte[packet.getLength() - 12];

		//copy array without header
		System.arraycopy(buffer, off + 12, data, 0, length - 12);

		//get checksum
		int checksum = 0;
		for (int i = 0; i < 4; ++i) {
			checksum += ((int) buffer[i + off] & 0xff) << (4 - 1 - i)*8;
		}

		corrupt = checksum != calcChecksum(buffer, off + 4, length - 4);

		//get header data
		seq = 0;
		for (int i = 0; i < 8; ++i) {
			seq += ((long) buffer[i + off + 4] & 0xff) << (8 - 1 - i)*8;
		}

		//get source address
		source = packet.getSocketAddress();
	}

	public long getSeq() {
		return seq;
	}

	public boolean isCorrupt() {
		return corrupt;
	}

	public byte[] getData() {
		return data;
	}

	private int calcChecksum(byte[] buffer, int offset, int length) {
		byte[] checksumArray = new byte[4];
		for (int i = offset; i < offset + length; ++i) {
			checksumArray[(i - offset) % 4] ^= buffer[i];
		}
		int checksum = 0;
		for (int i = 0; i < 4; ++i) {
			checksum += ((int) checksumArray[i] & 0xff) << (4 - 1 - i)*8;
		}
		return checksum;
	}

	public SocketAddress getSource() {
		return source;
	}
}
