package daoumoyer;

import java.nio.ByteBuffer;

/**
 * Represents a TCP segment
 *
 * @author Grant Moyer
 * @since 2015-12-04
 */
public class TCPSegment {
	ByteBuffer fullBuffer;

	short getSrcPort() {

	}

	short getDestPort() {

	}

	int getSeqNum() {

	}

	int getAckNum() {

	}

	byte getHeadLen() {

	}

	byte getFlags() {

	}

	boolean isUrg() {

	}

	boolean isAck() {

	}

	boolean isPsh() {

	}

	boolean isRst() {

	}

	boolean isSyn() {

	}

	boolean isFin() {

	}

	short getRcvWin() {

	}

	short getChecksum() {

	}

	short getUrgDataPointer() {

	}
}
