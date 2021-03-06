package daoumoyer.sender;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.util.LinkedList;
import java.util.List;
import java.lang.Math;

/**
 * @author Grant Moyer
 * @since 2015-11-24
 */
public class ServerWindow {
	private long base;
	private long nextSeqNum;
	private long endOfFile;
	private long windowSize = 20;
	private int packetSize = 1000;
	private InetAddress remoteAddress;
	private int remotePort;
	private FileInputStream fIn;
	private List<DatagramPacket> packets = new LinkedList<>();
	private double dataError;
	private static long corruptcount;

	public ServerWindow(FileInputStream fIn, InetAddress remoteAddress, int remotePort, double dataError) {
		base = 0;
		endOfFile = -1;
		this.dataError = dataError;
		this.remoteAddress = remoteAddress;
		this.remotePort = remotePort;

		//populate window with packets
		this.fIn = fIn;
		for (int i = 0; i < windowSize; ++i) {
			makePacket(i);
		}
	}

	private void makePacket(long seqNum) {
		byte[] buffer = new byte[packetSize];
		int headerSize = 12; //8 for sequence number and 4 for checksum
		try {
			//add file data
			int length = fIn.read(buffer, headerSize, buffer.length - headerSize);

			//add sequence number
			for (int i = 0; i < 8; ++i) {
				buffer[i + 4] = (byte) (seqNum >> (8 - 1 - i)*8 & 0xff);
			}

			//add checksum
			byte[] checksum = makeChecksum(buffer, headerSize - 8);
			System.arraycopy(checksum, 0, buffer, 0, 4);

			if (length == -1) {
				packets.add(null);
				if (endOfFile == -1) endOfFile = seqNum;
			} else {
				packets.add(new DatagramPacket(buffer, 0, buffer.length, remoteAddress, remotePort));
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
	}

	public void setBase(long base) {
		if (base <= this.base) throw new NotInWindowException(this.base, nextSeqNum, base);
		for (long i = this.base; i < base; ++i) {
			packets.remove(0);
			makePacket(i + windowSize);
		}
		this.base = base;
	}

	public DatagramPacket getPacket(long seqNum) throws EndOfFileException {
		if (base == endOfFile) { //last packet
			byte[] buffer = new byte[12];

			//add sequence number
			for (int i = 0; i < 8; ++i) {
				buffer[i + 4] = (byte) (-1 >> (8 - 1 - i)*8 & 0xff); //sequence number is -1 for last packet
			}

			//add checksum
			byte[] checksum = makeChecksum(buffer, 4);
			System.arraycopy(checksum, 0, buffer, 0, 4);

			DatagramPacket packet = new DatagramPacket(buffer, buffer.length, remoteAddress, remotePort);
			return packet;
		} else if (seqNum >= base && seqNum < base + windowSize) {
			DatagramPacket packet = packets.get((int) (seqNum - base));
			if (packet != null) {
				//simulate data packet error dataError proportion of the time
				if (Math.random() < dataError) {
					byte[] buffer = new byte[packet.getLength()];
					System.arraycopy(packet.getData(), packet.getOffset(), buffer, 0, packet.getLength());
					buffer[0] = (byte) (-buffer[0] & 0xff);
					System.out.println("Simulated data packet corruption " + ++corruptcount + " times");
					return new DatagramPacket(buffer, buffer.length,remoteAddress, remotePort);
				} else {
					return packet;
				}
			} else {
				throw new EndOfFileException();
			}
		} else throw new NotInWindowException(base, nextSeqNum, seqNum);
	}
}
