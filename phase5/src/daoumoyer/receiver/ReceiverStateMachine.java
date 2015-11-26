package daoumoyer.receiver;

import daoumoyer.receiver.event.RcvReceiverEvent;
import daoumoyer.sender.DoneException;
import daoumoyer.statemachine.*;

import java.net.*;
import java.io.IOException;

public class ReceiverStateMachine extends StateMachine {
	private long expectedSeqNum;
	private AckToSend currentAck;
	private DatagramSocket socket;

	protected State delta(State currentState, Event event) throws DoneException, IOException {
		return delta((ReceiverState) currentState, (RcvReceiverEvent) event);
	}

	protected ReceiverState delta(ReceiverState currentState, RcvReceiverEvent event) throws DoneException, IOException {
		switch (currentState) {
			case WAIT:
				//make variables
				long seqNum = event.getSeq();

				//event logic
				if (seqNum == -1) {
					throw new DoneException();
				} else if (!event.isCorrupt() && seqNum == expectedSeqNum) {
					byte[] data = event.getData();
					event.getFOut().write(data);
					currentAck = new AckToSend(seqNum, (InetSocketAddress) event.getSource());
					sendAck(currentAck);
					++expectedSeqNum;
				} else {
					if (currentAck != null) {
						sendAck(currentAck);
					}
				}
			default:
				throw new InvalidStateException(currentState);
		}
	}

	protected State initialState() {
		return ReceiverState.WAIT;
	}

	public ReceiverStateMachine(DatagramSocket socket) {
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
