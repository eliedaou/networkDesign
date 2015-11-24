package daoumoyer.sender;

import java.net.*;
import java.io.*;

public class ServerReceived {
    private final boolean corrupt;
    private byte seq;
    private byte[] checksum;
    private DatagramPacket packet;
    private final boolean ack;
    private byte[] data;
    private int port;
    private InetAddress address;

    public ServerReceived(byte[] data, InetAddress address, int port) {
        ack = false;
        this.data = data;

        this.address = address;
        this.port = port;
        corrupt = false;
    }

    public byte[] getData() {
        return data;
    }

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

        seq = packet.getData()[packet.getOffset() + 1];
        checksum = new byte[1];
        checksum[0] = seq;

        //check for corruption between checksum and data
        corrupt = checkCorrupt(packet, checksum);

        ack = true;
    }

    public byte getSeq() {
        return seq;
    }

    public boolean isCorrupt() {
        return corrupt;
    }

    public boolean isAck() {
        return ack;
    }

    private boolean checkCorrupt(DatagramPacket packet, byte[] checksum) {
        byte[] buff = packet.getData();
        int off = packet.getOffset();

        return checksum[0] != buff[off];
    }
}
