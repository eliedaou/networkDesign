package daoumoyer.server;

import daoumoyer.SimpleTimer;
import daoumoyer.tcp.TCPStateMachine;

import daoumoyer.server.event.RcvSenderEvent;
import daoumoyer.server.event.SendSenderEvent;
import daoumoyer.server.event.TimeoutSenderEvent;
import daoumoyer.statemachine.CannotAdvanceException;

import java.net.*;
import java.io.FileInputStream;
import java.io.IOException;

public class ServerSendManager implements Runnable {
	private final ServerStateMachine machine;
	private final DatagramSocket socket;
	private final FileInputStream fIn;
	private final InetAddress remoteAddress;
	private final int remotePort;
	private final double ackError;
	private final double dataError;
	private final double ackLoss;
	private final double dataLoss;
	private final SendData data;
	private long dropcount;

	public ServerSendManager(FileInputStream fIn, double ackError, double dataError, double ackLoss, double dataLoss) {
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
		machine = new ServerStateMachine(socket, remoteAddress, remotePort,
				ackError, dataError, dataLoss);

		// keep file input stream
		this.fIn = fIn;
		data = new SendData(fIn, remoteAddress, remotePort, socket, dataLoss, dataError);
	}

	public void run() {
		SimpleTimer timer = new SimpleTimer(300);
		long startTime = System.currentTimeMillis();
		
		//breaks when the windows base slides path the end of the file
		while (true) {
			try {
				if (timer.expired()) {
					try {
						machine.advance(new TimeoutSenderEvent(timer, data));
					} catch (DoneException e) {
						break;
					} catch (CannotAdvanceException ignore) {}
				}

				SendSenderEvent sendEvent = new SendSenderEvent(timer, data);
				try {
					machine.advance(sendEvent);
				} catch (DoneException e) {
					break;
				} catch (CannotAdvanceException ignore) {}

				socket.setSoTimeout(10);
				DatagramPacket rcvPacket = new DatagramPacket(new byte[100], 100);
				try {
					socket.receive(rcvPacket);

					ReceivedAck ack = new ReceivedAck(rcvPacket, ackError);
					RcvSenderEvent rcvEvent = new RcvSenderEvent(timer, data.getWindow(), ack);
					try {
						// simulate ack loss ackLoss proportion of the time by dropping it here
						if (Math.random() >= ackLoss) {
							machine.advance(rcvEvent);
						} else {
							System.out.println("Simulated dropped ACK packet " + ++dropcount + " times");
						}
					} catch (DoneException e) {
						break;
					} catch (CannotAdvanceException ignore) {}
				} catch (SocketTimeoutException ignore) {}
			} catch (IOException e) {
				System.err.println("Fatal: exception caught while waiting for ACK");
				System.err.println("\tException: " + e);
				System.exit(-1);
			}
		}
		
		System.out.println("Done sending file. Took " + (System.currentTimeMillis() - startTime) + "milliseconds");
	}
}
