import java.net.*;
import java.util.*;
import java.io.*;
import java.util.Vector;
import java.lang.Math;

public class SendingStateMachine extends StateMachine {
	private DatagramSocket socket;
	private InetAddress remoteAddress;
	private int remotePort;
	private SendingEvent prevEvent;
    private static double ackError;
    private static double dataError;

	protected enum SendState implements State {
        SEND_0, WAIT_FOR_0, SEND_1, WAIT_FOR_1
    }

	SendState sState;

    public SendingStateMachine(DatagramSocket socket, InetAddress remoteAddress, int remotePort, double ackError, double dataError) {
        this.socket = socket;
        this.ackError = ackError;
        this.dataError = dataError;
		this.remoteAddress = remoteAddress;
		this.remotePort = remotePort;

		prevEvent = null;
    }

    public static class SendingEvent implements Event {
        private ServerReceived packet;

        public SendingEvent(ServerReceived packet){
            this.packet = packet;
        }

        public boolean isCorrupt() {
 			double checkIfBitError = Math.random();
 			if (checkIfBitError < ackError) {
 				return true;
 			}
 			
        	return packet.isCorrupt();
        }

        public int getSeq() {
            return packet.getSeq();
        }

		public byte[] getData() {
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
		switch (currentState) {
			case SEND_0:
				sendPacket(event, (byte) 0);
				prevEvent = event;
				return SendState.WAIT_FOR_0;
			case WAIT_FOR_0:
				if (event.isCorrupt() || (event.getSeq() != 0)) {
					sendPacket(prevEvent, (byte) 0);
					return SendState.WAIT_FOR_0;
				} else {
					return SendState.SEND_1;
				}
			case SEND_1:
				sendPacket(event, (byte) 1);
				prevEvent = event;
				return SendState.WAIT_FOR_1;
			case WAIT_FOR_1:
				if (event.isCorrupt() || (event.getSeq() != 1)) {
					sendPacket(prevEvent, (byte) 1);
					return SendState.WAIT_FOR_1;
				} else {
					return SendState.SEND_0;
				}
		}
		return currentState;
	}

    protected State initialState() {
        return SendState.SEND_0;
    }

    private void sendPacket(SendingEvent event, byte seq) {
		byte[] data = event.getData();
		data[4] = seq;

		byte[] checksum = makeChecksum(data);
		System.arraycopy(checksum, 0, data, 0, 4);

		DatagramPacket packet = new DatagramPacket(data, data.length, remoteAddress, remotePort);
		try {
			socket.send(packet);
		} catch (IOException e) {
			System.err.println("Fatal: exception caugth while sending data packet");
			System.err.println("\tException: " + e);
			System.exit(-1);
		}
    }

	public boolean isWaitingForAck() {
		return currentState == SendState.WAIT_FOR_0 || currentState == SendState.WAIT_FOR_1;
	}
	
    private byte[] makeChecksum(byte[] data) {
        byte[] check = new byte[4];
        byte[] buff = data;
        int off = 4;
        int len = data.length - 4;
  
        //xor every group of 32 bits in the data including seq
        for (int i = 0; i < len; i += 4) {
            check[0] ^= buff[off + i];
            if (i + 1 < len) check[1] ^= buff[off + i + 1];
            if (i + 2 < len) check[2] ^= buff[off + i + 2];
            if (i + 3 < len) check[3] ^= buff[off + i + 3];
        }
        
		double checkIfBitError = Math.random();
		if (checkIfBitError < dataError) {
			data[4] = (byte)4;
		}
        
        return check;
    }

}
