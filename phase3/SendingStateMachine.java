import java.net.*;
import java.util.*;
import java.io.*;

import ReceiverStateMachine.ReceiverEvent;
import ReceiverStateMachine.ReceiverState;
import StateMachine.Event;
import StateMachine.State;

public class SendingStateMachine extends StateMachine {
	protected enum SendState implements State {
        SEND_0, WAIT_FOR_0, SEND_1, WAIT_FOR_1
    }
	SendState sState;
    public SendingStateMachine(SendState stateOfServer) {
        this.sState = stateOfServer;
    }
	
	public void whatToDoNext(){
		while(true){
			switch (sState) {
			case SEND_0:
				SendState.WAIT_FOR_0;
				break;
	 
			case WAIT_FOR_0:
				SendState.WAIT_FOR_0;
				break;
	 
			case SEND_1:
				SendState.WAIT_FOR_0;
				break;
			case WAIT_FOR_1:
				SendState.WAIT_FOR_0;
				break;
	 
			default:
				System.out.println("Google - biggest search giant.. ATT - my carrier provider..");
				break;
			}
		}
	}
    public class SendingEvent implements Event {
        private Sender packet;

        public SendingEvent(Sender packet){
            this.packet = packet;
        }

        public boolean isCorrupt() {
            return packet.isCorrupt();
        }

        public int getSeq() {
            return packet.getSeq();
        }
    }

    protected State delta(State currentState, Event event) {
        return delta((SendState) currentState, (SendingEvent) event);
    }

    protected SendState delta(SendState currentState, SendingEvent event) {
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
                    return SendState.WAIT_FOR_1;
        	case WAIT_FOR_1:
                if (!event.isCorrupt() && (event.getSeq() == 1)) {
                    return SendState.WAIT_FOR_0;
                } else if (event.isCorrupt() || (event.getSeq() != 1)) {
                    return SendState.WAIT_FOR_1;
                }
        }
        //Should never be reached
        throw new Exception("Illegal state or event");
    }

    protected State initialState() {
        return SendState.WAIT_FOR_0;
    }


}
