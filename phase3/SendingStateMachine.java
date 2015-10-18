import java.net.*;
import java.util.*;
import java.io.*;

import ReceiverStateMachine.ReceiverEvent;
import ReceiverStateMachine.ReceiverState;
import StateMachine.Event;
import StateMachine.State;

public class SendingStateMachine extends StateMachine {
    private boolean onceThrough;
    public class SendingEvent implements Event {
        private SendingPacket packet;

        public SendingEvent(ReceivedPacket packet){
            this.packet = packet;
        }

        public boolean isCorrupt() {
            return packet.isCorrupt();
        }

        public int getSeq() {
            return packet.getSeq();
        }
    }

    protected enum SendState implements State {
        SEND_0,
    	WAIT_FOR_0,
    	SEND_1,
        WAIT_FOR_1
    }

    protected State delta(State currentState, Event event) {
        return delta((ReceiverState) currentState, (ReceiverEvent) event);
    }

    protected ReceiverState delta(ReceiverState currentState, ReceiverEvent event) {
        switch (currentState) {
        	case SEND_0:
	               return SendState.WAIT_FOR_0;
        	case WAIT_FOR_0:
                if (!event.isCorrupt() && (event.getSeq() == 0)) {
                    return SendState.WAIT_FOR_1;
                } else if (event.isCorrupt() || (event.getSeq() != 0)) {
                    return SendState.WAIT_FOR_0;
                }
        	case SEND_1:
                    return ReceiverState.WAIT_FOR_1;
        	case WAIT_FOR_1:
                if (!event.isCorrupt() && (event.getSeq() == 1)) {
                    return ReceiverState.WAIT_FOR_0;
                } else if (event.isCorrupt() || (event.getSeq() != 1)) {
                    return ReceiverState.WAIT_FOR_1;
                }
        }
        //Should never be reached
        throw new Exception("Illegal state or event");
    }

    protected State initialState() {
        return ReceiverState.WAIT_FOR_0;
    }

    public ReceiverStateMachine() {
        onceThrough = false;
    }
}
