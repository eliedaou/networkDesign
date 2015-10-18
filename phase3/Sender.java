import java.net.*;
import java.io.*;

public class Sender {
    private final boolean corrupt;
    private byte seq;
    private byte[] checksum;
    private final byte[] data;
    private DatagramPacket packet;
    
    public Sender(DatagramPacket packet) {
        this.packet = packet;
        /*
         *Packet should be in the form of |checksum|seqNum|Data|
         *in that order, each at 8 bits, therefore, 
         *the first 8 bits are for checksum, 
         *the next 8 bits are for sequence number
         *the final 8 bits are for data (ack) 
         *
         */
        
        //data array without udp or rdt header, only 1 byte for ack
        data = new byte[1];
        
        //copy array data without header into data 
        System.arraycopy(packet.getData(), packet.getOffset() + 5, data, 0, 1);
        // copy the first byte after the datagram offset into the checksum 
        System.arraycopy(packet.getData(), packet.getOffset() , checksum, 0, 1);
        // copy the second byte after the datagram offset into the sequence
        System.arraycopy(packet.getData(), packet.getOffset() + 1, seq, 0, 1);
        
        //get header data
        corrupt = checkCorrupt(packet, checksum);
    }

    public byte getSeq() {
        return seq;
    }

    public boolean isCorrupt() {
        return corrupt;
    }

    public byte[] getData() {
        return data;
    }

    private boolean checkCorrupt(DatagramPacket packet, byte[] checksum) {
        byte[] buff = packet.getData();
        int off = packet.getOffset();

        return checksum[0] == buff[off/* -1 */ ];
    }
}
