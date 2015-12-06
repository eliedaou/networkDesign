package daoumoyer;

import java.nio.ByteBuffer;

/**
 * @author Grant Moyer
 * @since 2015-12-05
 */
public class TCPSegmentToSend extends TCPSegment{
	private static final int MSS = 1500 - 40 - 8; //(Ethernet MTU) - (IPv6 header) - (UDP header)

	public TCPSegmentToSend(int size) {
		if (size > MSS) throw new MSSExcededException(size, MSS);
		buffer = ByteBuffer.allocateDirect(size);
	}

	//make maximum sized segment, can be made smaller later with buffer.limit()
	public TCPSegmentToSend() {
		this(MSS);
	}

	public void setSrcPort(short srcPort) {
		buffer.putShort(0, srcPort);
	}

	public void setDestPort(short destPort) {
		buffer.putShort(2, destPort);
	}

	public void setSeqNum(int seqNum) {
		buffer.putInt(4, seqNum);
	}

	public void setAckNum(int ackNum) {
		buffer.putInt(8, ackNum);
	}

	public void setHeadLen(byte headLen) {
		buffer.put(12, (byte) (headLen << 4));
	}

	//Flag constants
	private static final byte URG = 0b00100000;
	private static final byte ACK = 0b00010000;
	private static final byte PSH = 0b00001000;
	private static final byte RST = 0b00000100;
	private static final byte SYN = 0b00000010;
	private static final byte FIN = 0b00000001;

	public void setFlags(byte flags) {
		flags &= 0b00111111;

		buffer.put(13, flags);
	}

	public void setFlags(int flags) {
		setFlags((byte) (flags & 0xff));
	}

	private void setFlag(byte flag, boolean value) {
		byte flags = getFlags();
		if (value) {
			flags |= flag;
		} else {
			flags &= ~(flag & 0xff);
		}
		setFlags(flags);
	}

	public void setUrg(boolean urg) {
		setFlag(URG, urg);
	}

	public void setAck(boolean ack) {
		setFlag(ACK, ack);
	}

	public void setPsh(boolean psh) {
		setFlag(PSH, psh);
	}

	public void setRst(boolean rst) {
		setFlag(RST, rst);
	}

	public void setSyn(boolean syn) {
		setFlag(SYN, syn);
	}

	public void setFin(boolean fin) {
		setFlag(FIN, fin);
	}

	public void setRcvWin(short rcvWin) {
		buffer.putShort(14, rcvWin);
	}

	public void setChecksum(short checksum) {
		buffer.putShort(16, checksum);
	}

	public void setUrgDataPointer(short urgDataPointer) {
		buffer.putShort(18, urgDataPointer);
	}

	public void setData(ByteBuffer data) {
		buffer.position(getHeadLen());
		buffer.put(data);
	}

	public void setData(byte[] data) {
		buffer.position(getHeadLen());
		buffer.put(data);
	}
}
