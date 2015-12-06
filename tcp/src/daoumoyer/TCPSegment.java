package daoumoyer;

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

	public boolean isUrg() {
		return (getFlags() & 0b00100000) != 0;
	}

	public boolean isAck() {
		return (getFlags() & 0b00010000) != 0;
	}

	public boolean isPsh() {
		return (getFlags() & 0b00001000) != 0;

	}

	public boolean isRst() {
		return (getFlags() & 0b00000100) != 0;
	}

	public boolean isSyn() {
		return (getFlags() & 0b00000010) != 0;
	}

	public boolean isFin() {
		return (getFlags() & 0b00000001) != 0;
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
