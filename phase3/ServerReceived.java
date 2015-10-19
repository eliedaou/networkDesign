import java.net.*;
import java.io.*;

public class ServerReceived {
    private boolean corrupt;
    private byte seq;
    private byte[] checksum;
    private DatagramPacket packet;
    private final boolean isAck;
    private byte[] data;
    private int port;
    private InetAddress address;

    public ServerReceived(byte[] data, InetAddress address, int port) {
        isAck = false;
        this.data = data;

        checksum = makeChecksum();
        System.arraycopy(checksum, 0, data, 0, 4);

        this.address = address;
        this.port = port;
    }

    public byte[] getData() {
        return data;
    }

    private byte[] makeChecksum() {
        byte[] check = new byte[4];
        byte[] buff = data;
        int off = 4;
        int len = data.length - 4;

        //xor every group of 32 bits in the data including seq
        for (int i = 0; i < len; i += 4) {
            check[0] ^= buff[off + i];
            if (i + 1 < len) check[1] ^= buff[off + i + 1];
            if (i + 2 < len) check[2] ^= buff[off + i + 2];
            if (i + 3 < len) check[3] ^= buff[off + i + 3];
        }

        return check;
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

        isAck = true;
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

        return checksum[0] != buff[off];
    }
}
