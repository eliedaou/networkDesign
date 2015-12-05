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
	private SendWindow window;
	private DatagramSocket socket;
	private double dataLoss;
	private long dropcount;

	public SendData(FileInputStream fIn, InetAddress remoteAddress, int remotePort, DatagramSocket socket, double dataLoss, double dataError) {
		this.socket = socket;
		this.dataLoss = dataLoss;
		window = new SendWindow(fIn, remoteAddress, remotePort, dataError);
	}

	public SendWindow getWindow() {
		return window;
	}

	public void udtSend(long seqNum) {
		try {
			// simulate dropped packet dataLoss proportion of the time
			if (Math.random() >= dataLoss) socket.send(window.getPacket(seqNum));
			else System.out.println("Simulated dropped data packet " + ++dropcount + " times");
		} catch (EndOfFileException ignore) {
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
