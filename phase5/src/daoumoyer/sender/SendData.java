package daoumoyer.sender;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.lang.Math;

/**
 * @author Grant Moyer
 * @since 2015-11-24
 */
public class SendData {
	private FileInputStream fIn;
	private SendWindow window;
	private DatagramSocket socket;
	private double dataLoss;

	public SendData(FileInputStream fIn, InetAddress remoteAddress, int remotePort, DatagramSocket socket, double dataLoss, double dataError) {
		this.fIn = fIn;
		this.socket = socket;
		this.dataLoss = dataLoss;
		window = new SendWindow(fIn, remoteAddress, remotePort, dataError);
	}

	public SendWindow getWindow() {
		return window;
	}

	public void udtSend(long seqNum) {
		try {
			if (Math.random() > dataLoss) socket.send(window.getPacket(seqNum));
		} catch (EndOfFileException ignore) {
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
