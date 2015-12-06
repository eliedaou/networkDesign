package daoumoyer.tcp;

import daoumoyer.statemachine.*;
import daoumoyer.tcp.event.TCPEvent;

import java.io.IOException;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * The TCP state machine. Used for both the server and the client, since TCP is full duplex.
 *
 * @author Grant Moyer
 * @since 2015-12-06
 */
public class TCPStateMachine extends StateMachine {
	private int nextSeqNum;
	private int sendBase;

	@Override
	protected State initialState() {
		return TCPState.WAIT;
	}

	@Override
	protected State delta(State currentState, Event event) throws CannotAdvanceException, IOException {
		return delta((TCPState) currentState, (TCPEvent) event);
	}

	protected State delta(TCPState currentState, TCPEvent event) {
		switch (currentState) {
			case WAIT:
				if (event instanceof )
			default:
				throw new InvalidStateException(currentState);
		}
	}

	public TCPStateMachine() {
		int rand = (new Random()).nextInt();

		nextSeqNum = rand;
		sendBase = rand;
	}
}
