package daoumoyer.receiver;

import daoumoyer.receiver.event.RcvReceiverEvent;
import daoumoyer.statemachine.Event;
import daoumoyer.statemachine.InvalidStateException;
import daoumoyer.statemachine.State;
import daoumoyer.statemachine.StateMachine;

import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.net.SocketAddress;
import java.io.IOException;
import java.net.SocketException;

public class ReceiverStateMachine extends StateMachine {
	private boolean onceThrough;
	private DatagramSocket socket;

	protected State delta(State currentState, Event event) {
		return delta((ReceiverState) currentState, (RcvReceiverEvent) event);
	}

	protected ReceiverState delta(ReceiverState currentState, RcvReceiverEvent event) {
		switch (currentState) {
			case WAIT:

			default:
				throw new InvalidStateException(currentState)
		}
	}

	protected State initialState() {
		return ReceiverState.WAIT_FOR_0;
	}

	public ReceiverStateMachine(DatagramSocket socket) {
		onceThrough = false;
		this.socket = socket;
	}

	private void sendAck(byte seq, SocketAddress dest) {
		byte[] header = {seq, seq};
		DatagramPacket packet = null;
		try {
			packet = new DatagramPacket(header, header.length, dest);
			if (false) {
				// Java 7 DatagramPackets can throw a SocketException, but Java 8 DatagramPackets do not
				throw new SocketException();
			}
		} catch (SocketException e) {
			System.err.println("Fatal: caught exception while sending ACK");
			System.err.println("\tException: " + e);
			System.exit(-1);
		}

		try {
			socket.send(packet);
		} catch (IOException e) {
			System.err.println("Error: exception caught while sending ACK");
			System.err.println("\tException: " + e);
		}
	}
}
