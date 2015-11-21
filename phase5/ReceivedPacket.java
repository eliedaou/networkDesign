import java.net.DatagramPacket;
import java.net.SocketAddress;

public class ReceivedPacket {
    private final boolean corrupt;
    private final int seq;
    private byte[] checksum;
    private final byte[] data;
    private DatagramPacket packet;
    private SocketAddress source;

    public ReceivedPacket(DatagramPacket packet) {
        this.packet = packet;
        //do not need room for header
        data = new byte[packet.getLength() - 5];

        //copy array without header
        System.arraycopy(packet.getData(), packet.getOffset() + 5, data, 0, packet.getLength() - 5);

        //get header data
        seq = packet.getData()[packet.getOffset() + 4];
        checksum = makeChecksum();
        corrupt = checkCorrupt(packet, checksum);

        //get source address
        source = packet.getSocketAddress();
    }

    public int getSeq() {
        return seq;
    }

    public boolean isCorrupt() {
        return corrupt;
    }

    public byte[] getData() {
        return data;
    }

    private byte[] makeChecksum() {
        byte[] check = new byte[4];
        byte[] buff = packet.getData();
        int off = packet.getOffset() + 4;
        int len = packet.getLength() - 4;

        //xor every group of 32 bits in the data including seq
        for (int i = 0; i < len; i += 4) {
            check[0] ^= buff[off + i];
            if (i + 1 < len) check[1] ^= buff[off + i + 1];
            if (i + 2 < len) check[2] ^= buff[off + i + 2];
            if (i + 3 < len) check[3] ^= buff[off + i + 3];
        }

        return check;
    }

    private boolean checkCorrupt(DatagramPacket packet, byte[] checksum) {
        byte[] buff = packet.getData();
        int off = packet.getOffset();
        int len = packet.getLength();

        return checksum[0] != buff[off]
            || checksum[1] != buff[off + 1]
            || checksum[2] != buff[off + 2]
            || checksum[3] != buff[off + 3];
    }

    public SocketAddress getSource() {
        return source;
    }
}
