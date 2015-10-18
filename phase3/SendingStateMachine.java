import java.net.*;
import java.util.*;
import java.io.*;


//import SendingStateMachine.Event;
//import StateMachine.ReceiverState;
//import StateMachine.Event;
//import StateMachine.State;

public class SendingStateMachine extends StateMachine {
	
	protected enum SendState implements State {
        SEND_0, WAIT_FOR_0, SEND_1, WAIT_FOR_1
    }
	SendState sState;
    public SendingStateMachine(SendState stateOfServer) {
        this.sState = stateOfServer;
    }
    
    public class SendingEvent implements Event {
        private ServerRecieved packet;

        public SendingEvent(ServerRecieved packet){
            this.packet = packet;
        }

        public boolean isCorrupt() {
            return packet.isCorrupt();
        }

        public int getSeq() {
            return packet.getSeq();
        }
    }
    
    public Request getRequest(DatagramSocket socket) throws SocketException,IOException {
		// Get buffer size
		final int DGRAM_SIZE = 1024;

		// Make packet
		byte[] buffer = new byte[DGRAM_SIZE];
		DatagramPacket packet = new DatagramPacket(buffer, DGRAM_SIZE);

		// receive into packet
		// blocks until received
		socket.receive(packet);

		// build Request
		byte[] contents = new byte[packet.getLength()];
		System.arraycopy(buffer, packet.getOffset(), contents, 0,
				packet.getLength());
		Request request = new Request((InetSocketAddress) packet.getSocketAddress(), contents);

		return request;
	}
	
    public void whatToDo(){
		//open port and start listening
		final int portNumber = 12000;
		
		DatagramSocket serverSocket = new DatagramSocket(portNumber);
		Request request = getRequest(serverSocket);
		DatagramSocket sourceSocket = new  DatagramSocket(request.getSource());		
		DatagramPacket packet = new DatagramPacket
		while(true){
			switch (sState) {
			case SEND_0:
				
				break;
		
			case WAIT_FOR_0:
				if ((getSeq() != 00000000) || isCorrupt()) {
					
				} else {

				}
				break;
		
			case SEND_1:
				
				break;
			case WAIT_FOR_1:
				if ((getSeq() != 11111111) || isCorrupt()) {
					
				} else {

				}
				break;
		
			default:
				
				break;
			}
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

    }

    protected State initialState() {
        return SendState.WAIT_FOR_0;
    }


}



