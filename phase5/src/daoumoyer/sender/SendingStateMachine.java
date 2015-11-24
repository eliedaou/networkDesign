package daoumoyer.sender;

import daoumoyer.statemachine.Event;
import daoumoyer.statemachine.State;
import daoumoyer.statemachine.StateMachine;

import java.net.*;
import java.io.*;
import java.lang.Math;

public class SendingStateMachine extends StateMachine {
	private DatagramSocket socket;
	private InetAddress remoteAddress;
	private int remotePort;
	private SenderEvent prevEvent;
	private static double ackError;
	private static double dataError;
	private static double dataLoss;


	public SenderEvent PreviousEvent() {
		return prevEvent;
	}

	SenderState sState;

	public SendingStateMachine(DatagramSocket socket, InetAddress remoteAddress, int remotePort, double ackError,
	                           double dataError, double dataLoss)
	{
		this.socket = socket;
		this.ackError = ackError;
		this.dataError = dataError;
		this.dataLoss = dataLoss;
		this.remoteAddress = remoteAddress;
		this.remotePort = remotePort;

		prevEvent = null;
	}

	protected State delta(State currentState, Event event) {
		return delta((SenderState) currentState, (SenderEvent) event);

	}

	protected SenderState delta(SenderState currentState, SenderEvent event) {
		switch (currentState) {
			case SEND_0:
				sendPacket(event, (byte) 0);
				prevEvent = event;
				return SenderState.WAIT_FOR_0;
			case WAIT_FOR_0:
				if (!event.isAck()) { //timeout, resend
					sendPacket(prevEvent, (byte) 0);
					return SenderState.WAIT_FOR_0;
				} else if (event.isCorrupt(ackError) || (event.getSeq() != 0)) {
					//no op
					return SenderState.WAIT_FOR_0;
				} else {
					//time is implicitly stopped
					return SenderState.SEND_1;
				}
			case SEND_1:
				sendPacket(event, (byte) 1);
				prevEvent = event;
				return SenderState.WAIT_FOR_1;
			case WAIT_FOR_1:
				if (!event.isAck()) { //timeout, resend
					sendPacket(prevEvent, (byte) 1);
					return SenderState.WAIT_FOR_1;
				} else if (event.isCorrupt(ackError) || (event.getSeq() != 1)) {
					//no op
					return SenderState.WAIT_FOR_1;
				} else {
					//timer is implicitly stopped
					return SenderState.SEND_0;
				}
		}
		return currentState;
	}

	protected State initialState() {
		return SenderState.SEND_0;
	}

	void sendPacket(SenderEvent event, byte seq) {
		byte[] data = event.getData();
		data[4] = seq;

		byte[] checksum = makeChecksum(data);
		System.arraycopy(checksum, 0, data, 0, 4);

		DatagramPacket packet = new DatagramPacket(data, data.length, remoteAddress, remotePort);
		try {
			if (Math.random() > dataLoss) socket.send(packet);
		} catch (IOException e) {
			System.err.println("Fatal: exception caugth while sending data packet");
			System.err.println("\tException: " + e);
			System.exit(-1);
		}
	}

	public boolean isWaitingForAck() {
		return currentState == SenderState.WAIT_FOR_0 || currentState == SenderState.WAIT_FOR_1;
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
			data[4] = (byte) 4;
		}

		return check;
	}

}
