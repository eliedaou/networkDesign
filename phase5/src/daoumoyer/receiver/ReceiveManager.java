package daoumoyer.receiver;

import daoumoyer.receiver.event.RcvReceiverEvent;

import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.io.FileOutputStream;
import java.io.IOException;

import daoumoyer.statemachine.CannotAdvanceException;

public class ReceiveManager implements Runnable {
	private final ReceiverStateMachine machine;
	private final DatagramSocket socket;
	private final FileOutputStream fOut;

	public ReceiveManager(FileOutputStream fOut) {
		//temporary varaible needed for try-catch
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
		machine = new ReceiverStateMachine(socket);

		//keep file variable
		this.fOut = fOut;
	}

	@Override public void run() {
		DatagramPacket dPacket;
		ReceivedPacket packet;
		RcvReceiverEvent event;
		byte[] rcvBuffer = new byte[1500];
		while (true) {
			//try to receive on socket until it is closed
			try {
				//get a packet from the network
				dPacket = new DatagramPacket(rcvBuffer, rcvBuffer.length);
				socket.receive(dPacket);

				//build an event
				packet = new ReceivedPacket(dPacket);
				event = new RcvReceiverEvent(packet, fOut);

				//break if last packet
				if (!packet.isCorrupt() && (packet.getSeq() == -1)) break;

				//give the event to the state machine
				machine.advance(event);
			} catch (IOException | CannotAdvanceException e ) {
				System.err.println("Error: caught exception during main loop");
				System.err.println("\tException: " + e);
			}
		}
	}
}
