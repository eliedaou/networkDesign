package daoumoyer.receiver;

import daoumoyer.receiver.event.RcvReceiverEvent;
import daoumoyer.sender.DoneException;
import daoumoyer.statemachine.*;

import java.net.*;
import java.io.IOException;

public class ClientStateMachine extends StateMachine {
	private long expectedSeqNum;
	private AckToSend currentAck;
	private DatagramSocket socket;

	protected State delta(State currentState, Event event) throws DoneException, IOException {
		return delta((ClientState) currentState, (RcvReceiverEvent) event);
	}

	protected ClientState delta(ClientState currentState, RcvReceiverEvent event) throws DoneException, IOException {
		switch (currentState) {
			case WAIT:
				//make variables
				long seqNum = event.getSeq();

				//event logic
				if (seqNum == -1 && !event.isCorrupt()) {
					AckToSend finalAck = new AckToSend(-1, (InetSocketAddress) event.getSource());
					//send finalAck many times to be safe
					for (int i = 0; i < 100; ++i) {
						sendAck(finalAck);
					}
					throw new DoneException();
				} else if (!event.isCorrupt() && seqNum == expectedSeqNum) {
					byte[] data = event.getData();
					event.getFOut().write(data);
					currentAck = new AckToSend(seqNum, (InetSocketAddress) event.getSource());
					sendAck(currentAck);
					++expectedSeqNum;

					return ClientState.WAIT;
				} else {
					if (currentAck != null) {
						sendAck(currentAck);
					}

					return ClientState.WAIT;
				}
			default:
				throw new InvalidStateException(currentState);
		}
	}

	protected State initialState() {
		return ClientState.WAIT;
	}

	public ClientStateMachine(DatagramSocket socket) {
		expectedSeqNum = 0;
		this.socket = socket;
		currentAck = null;
	}

	private void sendAck(AckToSend ack) {
		try {
			socket.send(ack.getPacket());
		} catch (IOException e) {
			System.err.println("Fatal: exception caught while sending ACK");
			e.printStackTrace();
			System.exit(-1);
		}
	}
}
