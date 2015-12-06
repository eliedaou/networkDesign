package daoumoyer.tcp;

import java.nio.ByteBuffer;

/**
 * @author Grant Moyer
 * @since 2015-12-05
 */
public abstract class TCPSegment {
	protected ByteBuffer buffer;

	public short getSrcPort() {
		return buffer.getShort(0);
	}

	public short getDestPort() {
		return buffer.getShort(2);
	}

	public int getSeqNum() {
		return buffer.getInt(4);
	}

	public int getAckNum() {
		return buffer.getInt(8);
	}

	public byte getHeadLen() {
		return (byte) (buffer.get(12) & 0xff >>> 4);
	}

	public byte getFlags() {
		return (byte) (buffer.get(13) & 0b00111111);
	}

	//Flag constants
	protected static final byte URG = 0b00100000;
	protected static final byte ACK = 0b00010000;
	protected static final byte PSH = 0b00001000;
	protected static final byte RST = 0b00000100;
	protected static final byte SYN = 0b00000010;
	protected static final byte FIN = 0b00000001;

	public boolean isUrg() {
		return (getFlags() & URG) != 0;
	}

	public boolean isAck() {
		return (getFlags() & ACK) != 0;
	}

	public boolean isPsh() {
		return (getFlags() & PSH) != 0;
	}

	public boolean isRst() {
		return (getFlags() & RST) != 0;
	}

	public boolean isSyn() {
		return (getFlags() & SYN) != 0;
	}

	public boolean isFin() {
		return (getFlags() & FIN) != 0;
	}

	public short getRcvWin() {
		return buffer.getShort(14);
	}

	public short getChecksum() {
		return buffer.getShort(16);
	}

	public short getUrgDataPointer() {
		return buffer.getShort(18);
	}

	public ByteBuffer getData() {
		buffer.position(getHeadLen());

		return buffer.slice();
	}
}
