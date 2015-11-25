package daoumoyer.sender;

import daoumoyer.SimpleTimer;
import daoumoyer.sender.event.RcvSenderEvent;
import daoumoyer.sender.event.SendSenderEvent;
import daoumoyer.sender.event.TimeoutSenderEvent;
import daoumoyer.statemachine.CannotAdvanceException;

import java.net.*;
import java.io.FileInputStream;
import java.io.IOException;

public class SendManager implements Runnable {
	private final SendingStateMachine machine;
	private final DatagramSocket socket;
	private final FileInputStream fIn;
	private final InetAddress remoteAddress;
	private final int remotePort;
	private final double ackError;
	private final double dataError;
	private final double ackLoss;
	private final double dataLoss;
	private final SendData data;

	public SendManager(FileInputStream fIn, double ackError, double dataError, double ackLoss, double dataLoss) {
		// temporary variable needed for try-catch
		DatagramSocket tempSocket = null;
		try {
			// open socket on specified port
			tempSocket = new DatagramSocket();
			System.out.println("Opened socket on port "
					+ tempSocket.getLocalPort());
		} catch (IOException e) {
			System.err.println("Fatal: exception caugth while opening socket");
			System.err.println("\tException: " + e);
			System.exit(-1);
		}
		socket = tempSocket;

		// close socket when program exits
		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
			public void run() {
				if (socket.isBound() && !socket.isClosed()) {
					socket.close();
					System.out.println("Closed socket");
				}
			}
		}));

		// set remoteAddress
		InetAddress tempAddress = null;
		try {
			tempAddress = InetAddress.getByName("127.0.0.1");
		} catch (UnknownHostException e) {
			System.err
					.println("Fatal: exception caugth while resolving remote address");
			System.err.println("\tException: " + e);
			System.exit(-1);
		}
		remoteAddress = tempAddress;
		remotePort = 10000;

		// keep bit error variables
		this.ackError = ackError;
		this.dataError = dataError;
		this.ackLoss = ackLoss;
		this.dataLoss = dataLoss;

		// start state machine
		machine = new SendingStateMachine(socket, remoteAddress, remotePort,
				ackError, dataError, dataLoss);

		// keep file input stream
		this.fIn = fIn;
		data = new SendData(fIn, remoteAddress, remotePort, socket);
	}

	public void run() {
		SimpleTimer timer = new SimpleTimer(500);

		//breaks when the windows base slides path the end of the file
		while (true) {
			try {
				if (timer.expired()) machine.advance(new TimeoutSenderEvent(timer, data));

				SendSenderEvent sendEvent = new SendSenderEvent(timer, data);
				machine.advance(sendEvent);

				socket.setSoTimeout(10);
				DatagramPacket rcvPacket = new DatagramPacket(new byte[100], 100);
				try {
					socket.receive(rcvPacket);
					ReceivedAck ack = new ReceivedAck(rcvPacket);
					RcvSenderEvent rcvEvent = new RcvSenderEvent(timer, data.getWindow(), ack);
					machine.advance(rcvEvent);
				} catch (SocketTimeoutException ignore) {}
			} catch (DoneException e) {
				break;
			} catch (CannotAdvanceException ignore) {
			} catch (IOException e) {
				System.err.println("Fatal: exception caught while waiting for ACK");
				System.err.println("\tException: " + e);
				System.exit(-1);
			}
		}
		System.out.println("Done sending file");
	}
}
