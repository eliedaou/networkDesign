package daoumoyer.sender;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * @author Grant Moyer
 * @since 2015-11-24
 */
public class SendData {
	private FileInputStream fIn;
	private SendWindow window;
	private DatagramSocket socket;

	public SendData(FileInputStream fIn, InetAddress remoteAddress, int remotePort, DatagramSocket socket) {
		this.fIn = fIn;
		this.socket = socket;
		window = new SendWindow(fIn, remoteAddress, remotePort);
	}

	public SendWindow getWindow() {
		return window;
	}

	public void udtSend(long seqNum) {
		try {
			socket.send(window.getPacket(seqNum));
		} catch (EndOfFileException ignore) {
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
