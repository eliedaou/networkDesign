import java.net.*;
import java.io.*;

public class ServerReceived {
    private final boolean corrupt;
    private byte seq;
    private byte[] checksum; 
    private DatagramPacket packet;
    
    public ServerReceived(DatagramPacket packet) {
        this.packet = packet;
        /*
         * 
         *Packet should be in the form of |checksum|seqNum|
         *in that order, each at 8 bits, therefore, 
         *the first 8 bits are for checksum, 
         *the next 8 bits are for sequence number 
         *
         */
        
        System.arraycopy(packet.getData(), packet.getOffset() , checksum, 0, 1);
        
        System.arraycopy(packet.getData(), packet.getOffset() + 1, seq, 0, 1);
        
        //check for corruption between checksum and data
        corrupt = checkCorrupt(packet, checksum);
    }

    public byte getSeq() {
        return seq;
    }

    public boolean isCorrupt() {
        return corrupt;
    }

    private boolean checkCorrupt(DatagramPacket packet, byte[] checksum) {
        byte[] buff = packet.getData();
        int off = packet.getOffset();

        return checksum[0] == buff[off/* -1 */ ];
    }
}

class Request {
	private final InetSocketAddress source;
	private final byte[] contents;

	public Request(InetSocketAddress source, byte[] contents) {
		this.source = source;
		this.contents = contents;
	}

	public InetSocketAddress getSource() {
		return source;
	}

	public byte[] getContents() {
		return contents;
	}

}
