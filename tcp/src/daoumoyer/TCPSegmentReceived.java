package daoumoyer;

import java.net.DatagramPacket;
import java.nio.*;

/**
 * Represents a TCP segment
 *
 * @author Grant Moyer
 * @since 2015-12-04
 */
public class TCPSegmentReceived extends TCPSegment {
	public TCPSegmentReceived(DatagramPacket datagram) {
		buffer = ByteBuffer.allocateDirect(datagram.getLength());

		buffer.put(datagram.getData(), datagram.getOffset(), datagram.getLength());
		buffer = buffer.asReadOnlyBuffer();
	}
}
