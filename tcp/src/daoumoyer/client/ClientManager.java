package daoumoyer.client;

import daoumoyer.client.event.RcvReceiverEvent;
import daoumoyer.tcp.TCPStateMachine;

import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.io.FileOutputStream;
import java.io.IOException;

import daoumoyer.server.DoneException;
import daoumoyer.statemachine.CannotAdvanceException;

public class ClientManager implements Runnable {
	private final ClientStateMachine machine;
	private final DatagramSocket socket;
	private final FileOutputStream fOut;

	public ClientManager(FileOutputStream fOut) {
		//temporary variable needed for try-catch
		DatagramSocket tempSocket = null;
		try {
			//open socket on port 10000
			tempSocket = new DatagramSocket(10000);
			System.out.println("Opened socket on port " + tempSocket.getLocalPort());
		} catch (IOException e) {
			System.err.println("Fatal: exception caugth while opening socket");
			System.err.println("\tException: " + e);
			System.exit(-1);
		}
		socket = tempSocket;

		//close socket when program exits
		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
			@Override public void run() {
				if (socket.isBound() && !socket.isClosed()) {
					socket.close();
					System.out.println("Closed socket");
				}
			}
		}));

		//start state machine
		machine = new ClientStateMachine(socket);

		//keep file variable
		this.fOut = fOut;
	}

	@Override public void run() {
		while (true) {
			try {
				//get packet from network
				byte[] buffer = new byte[1000];
				DatagramPacket udpPacket = new DatagramPacket(buffer, buffer.length);
				socket.receive(udpPacket);
				ClientReceivedPacket rcvPacket = new ClientReceivedPacket(udpPacket);

				//make event
				RcvReceiverEvent rcvEvent = new RcvReceiverEvent(rcvPacket, fOut);
				machine.advance(rcvEvent);
			} catch (DoneException e) {
				break;
			} catch (CannotAdvanceException ignore) {
			} catch (IOException e) {
				System.err.println("Fatal: exception caught while waiting for data");
				e.printStackTrace();
				System.exit(-1);
			}
		}
	}
}
