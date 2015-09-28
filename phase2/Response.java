import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.util.Vector;

class Response {
	private InetSocketAddress source;
	private byte[] contents;

	// The data, ready to receive
	final Vector<DatagramPacket> packets;

	

	// The socket to send from
	private final DatagramSocket socket;

	public Response(DatagramSocket socket) {
		this.socket = socket;
		packets = new Vector<DatagramPacket>();
	}

	public Response(InetSocketAddress source, byte[] contents) {
		this.source = source;
		this.contents = contents;
		this.socket = null;
		packets = new Vector<DatagramPacket>();
	}

	public InetSocketAddress getSource() {
		return source;
	}

	public byte[] getContents() {
		return contents;
	}

	public void receiveMessage() {
		try {
			final int DGRAM_SIZE = 1024;

			// Make packet
			byte[] buffer = new byte[DGRAM_SIZE];
		    DatagramPacket tempPacket= new DatagramPacket(buffer, DGRAM_SIZE);
			socket.receive(tempPacket);
			while (tempPacket.getLength() > 0) {
				packets.addElement(tempPacket);
				socket.receive(tempPacket);
			}
		} catch (Exception e) {
			System.err
					.println("Error: Caught Exception while trying to receive message");
			System.err.println("Exception: " + e);
		}
	}

}