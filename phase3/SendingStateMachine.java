import java.net.*;
import java.util.*;
import java.io.*;
import java.util.Vector;

//import SendingStateMachine.Event;
//import StateMachine.ReceiverState;
//import StateMachine.Event;
//import StateMachine.State;

public class SendingStateMachine extends StateMachine {
	Vector<DatagramPacket> packets = new Vector<DatagramPacket>();
	ServerReceived incomingPacket;
	private DatagramSocket socket;
	private Sender serverProcess = new Sender();

	protected enum SendState implements State {
        SEND_0, WAIT_FOR_0, SEND_1, WAIT_FOR_1
    }

	SendState sState;

	public SendingStateMachine(SendState stateOfServer) {
        this.sState = stateOfServer;
    }

    public void SendingEvent(ServerReceived packet){
        this.incomingPacket = packet;
    }

    public SendingStateMachine(DatagramSocket socket) {
        this.socket = socket;
    }

    public static class SendingEvent implements Event {
        private ServerReceived packet;

        public SendingEvent(ServerReceived packet){
            this.packet = packet;
        }

        public boolean isCorrupt() {
            return packet.isCorrupt();
        }

        public int getSeq() {
            return packet.getSeq();
        }

		public int getData() {
			return packet.getData();
		}

		public boolean isAck() {
			return packet.isAck();
		}
    }

    protected State delta(State currentState, Event event) {
        return delta((SendState) currentState, (SendingEvent) event);

    }

	protected SendState delta(SendState currentState, SendingEvent event) {

		makePackets(Sender.fileToSend);

		switch (currentState) {
		case SEND_0:
			sendPacket(0, Sender.sourceSocket);
			Sender.sourceSocket.close();
			return SendState.WAIT_FOR_0;
		case WAIT_FOR_0:
			if (event.isCorrupt() || (event.getSeq() != 0)) {
				sendPacket(0, Sender.sourceSocket);
				sourceSocket.close();

			} else {
				sourceSocket.close();
				return SendState.SEND_1;
			}
		case SEND_1:
			sourceSocket.close();
			return SendState.WAIT_FOR_1;
		case WAIT_FOR_1:
			if (event.isCorrupt() || (event.getSeq() != 1)) {
				sourceSocket.close();
				return SendState.WAIT_FOR_1;
			} else {
				sourceSocket.close();
				return SendState.SEND_1;
			}
		default:
			sourceSocket.close();
			return SendState.SEND_0;
		}
	}


    protected State initialState() {
        return SendState.SEND_0;
    }

    private void makePackets(byte[] contents){

    	for (int i = 0; i * (1500) < contents.length; i++) {
			int length = 1500;
			if ((i + 1) * length >= contents.length) {
				length = contents.length - i * length;
			}
			DatagramPacket packet = new DatagramPacket(contents, i*1024, 1024);
			packets.add(packet);
		}
    }

    private void sendPacket(byte seq, DatagramSocket dest) {
        byte[] header = {seq};

        try {
            dest.send(packets.elementAt(0));
            packets.remove(0);
        } catch (IOException e) {
            System.err.println("Error: exception caught while sending ACK");
            System.err.println("\tException: " + e);
        }
    }

	public isWaitingForAck() {
		return currentState == SendState.WAIT_FOR_0 || currentState == SendState.WAIT_FOR_1;
	}

}
