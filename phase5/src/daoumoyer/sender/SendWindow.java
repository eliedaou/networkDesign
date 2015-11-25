package daoumoyer.sender;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Grant Moyer
 * @since 2015-11-24
 */
public class SendWindow {
	private long base;
	private long nextSeqNum;
	private long endOfFile;
	private long windowSize = 100;
	private int packetSize = 1000;
	private InetAddress remoteAddress;
	private int remotePort;
	private FileInputStream fIn;
	private List<DatagramPacket> packets = new LinkedList<>();

	public SendWindow(FileInputStream fIn, InetAddress remoteAddress, int remotePort) {
		base = 0;
		endOfFile = -1;
		this.remoteAddress = remoteAddress;
		this.remotePort = remotePort;

		//populate window with packets
		this.fIn = fIn;
		for (nextSeqNum = 0; nextSeqNum < windowSize; ++nextSeqNum) {
			makePacket(nextSeqNum);
		}
	}

	private void makePacket(long seqNum) {
		byte[] buffer = new byte[packetSize];
		int headerSize = 12; //8 for sequence number and 4 for checksum
		try {
			//add file data
			int length = fIn.read(buffer, headerSize, buffer.length - headerSize);

			//add checksum
			byte[] checksum = makeChecksum(buffer, headerSize);
			System.arraycopy(checksum, 0, buffer, 0, 4);

			//add sequence number
			for (int i = 0; i < 8; ++i) {
				buffer[i + 4] = (byte) (seqNum >> (8 - 1 - i)*8 & 0xff);
			}

			if (length == -1) {
				packets.add(null);
				if (endOfFile == -1) endOfFile = seqNum;
			} else {
				packets.add(new DatagramPacket(buffer, 0, length, remoteAddress, remotePort));
			}
		} catch (IOException e) {
			System.err.println("Fatal: exception caught while reading file");
			e.printStackTrace();
			System.exit(-1);
		}
	}

	private byte[] makeChecksum(byte[] buffer, int offset) {
		byte[] checksum = new byte[4];
		for (int i = offset; i < buffer.length; ++i) {
			checksum[(i - offset) % 4] ^= buffer[i];
		}
		return checksum;
	}

	/* Getters */

	public long getBase() {
		return base;
	}

	public long getNextSeqNum() {
		return nextSeqNum;
	}

	public long getWindowSize() {
		return windowSize;
	}

	/* Mutators */

	public void incrementNext() {
		++nextSeqNum;
		makePacket(nextSeqNum);
	}

	public void setBase(long base) throws DoneException {
		if (base <= this.base) throw new NotInWindowException(this.base, nextSeqNum, base);
		for (long i = this.base; i < base; ++i) {
			packets.remove(0);
		}
		this.base = base;
		if (base >= endOfFile) throw new DoneException();
	}

	public DatagramPacket getPacket(long seqNum) throws EndOfFileException {
		if (seqNum >= base && seqNum < base + windowSize) {
			DatagramPacket packet = packets.get((int) (seqNum - base));
			if (packet != null) {
				return packet;
			} else {
				throw new EndOfFileException();
			}
		} else throw new NotInWindowException(base, nextSeqNum, seqNum);
	}
}
