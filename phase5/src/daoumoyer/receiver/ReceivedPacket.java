package daoumoyer.receiver;

import java.net.DatagramPacket;
import java.net.SocketAddress;

public class ReceivedPacket {
	private final boolean corrupt;
	private final int seq;
	private int checksum;
	private final byte[] data;
	private DatagramPacket packet;
	private SocketAddress source;

	public ReceivedPacket(DatagramPacket packet) {
		this.packet = packet;
		//do not need room for header
		data = new byte[packet.getLength() - 24];

		//copy array without header
		System.arraycopy(packet.getData(), packet.getOffset() + 5, data, 0, packet.getLength() - 5);

		//get header data
		seq = packet.getData()[packet.getOffset() + 4];
		checksum = makeChecksum();
		corrupt = checkCorrupt(packet, checksum);

		//get source address
		source = packet.getSocketAddress();
	}

	public int getSeq() {
		return seq;
	}

	public boolean isCorrupt() {
		return corrupt;
	}

	public byte[] getData() {
		return data;
	}

	private int calcChecksum(byte[] buffer, int offset) {
		byte[] checksum = new byte[4];
		for (int i = offset; i < buffer.length; ++i) {
			checksum[(i - offset) % 4] ^= buffer[i];
		}
		return checksum;
	}

	private boolean checkCorrupt(DatagramPacket packet, byte[] checksum) {
		byte[] buff = packet.getData();
		int off = packet.getOffset();
		int len = packet.getLength();

		return checksum[0] != buff[off] || checksum[1] != buff[off + 1] || checksum[2] != buff[off + 2]
			|| checksum[3] != buff[off + 3];
	}

	public SocketAddress getSource() {
		return source;
	}
}
