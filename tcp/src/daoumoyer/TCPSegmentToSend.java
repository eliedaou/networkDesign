package daoumoyer;

import java.nio.ByteBuffer;

/**
 * @author Grant Moyer
 * @since 2015-12-05
 */
public class TCPSegmentToSend extends TCPSegment{
	private short srcPort;
	private short destPort;
	private int ackNum;
	private int seqNum;
	private byte headLen;
	private static final int MSS = 1500 - 40 - 8; //(Ethernet MTU) - (IPv6 header) - (UDP header)

	public TCPSegmentToSend(int size) {
		if (size > MSS) throw new MSSExcededException(size, MSS);
		buffer = ByteBuffer.allocateDirect(size);
	}

	//make maximum sized segment, can be made smaller later with buffer.limit()
	public TCPSegmentToSend() {
		this(MSS);
	}

	//TODO implement all set*() methods

	public void setSrcPort(short srcPort) {
		this.srcPort = srcPort;
	}

	public void setDestPort(short destPort) {
		this.destPort = destPort;
	}

	public void setSeqNum(int seqNum) {
		this.seqNum = seqNum;
	}

	public void setAckNum(int ackNum) {
		this.ackNum = ackNum;
	}

	public void setHeadLen(byte headLen) {
		this.headLen = headLen;
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
	}

	public void setFlags(int flags) {
		setFlags((byte) (flags & 0xff));
	}

	public boolean setUrg() {
	}

	public boolean setAck() {
	}

	public boolean setPsh() {
	}

	public boolean setRst() {
	}

	public boolean setSyn() {
	}

	public boolean setFin() {
	}

	public short setRcvWin() {
	}

	public short setChecksum() {
	}

	public short setUrgDataPointer() {
	}

	public ByteBuffer setData() {
	}
}
