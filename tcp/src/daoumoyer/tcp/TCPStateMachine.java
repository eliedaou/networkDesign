package daoumoyer.tcp;

import daoumoyer.SimpleTimer;
import daoumoyer.statemachine.*;
import daoumoyer.tcp.event.DataReceivedTCPEvent;
import daoumoyer.tcp.event.DataToSendTCPEvent;
import daoumoyer.tcp.event.TCPEvent;
import daoumoyer.tcp.event.TimeoutTCPEvent;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.nio.ByteBuffer;
import java.util.Random;

/**
 * The TCP state machine. Used for both the server and the client, since TCP is full duplex.
 *
 * @author Grant Moyer
 * @since 2015-12-06
 */
public class TCPStateMachine extends StateMachine {
	private int nextSeqNum;
	private int sendBase;
	private int ackNum;
	private boolean sendAck;
	private short rcvWin;

	private final InetAddress remoteAddress;
	private final int remotePort;
	private final short sourcePort;
	private final short destPort;

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
				if (event instanceof DataToSendTCPEvent) {
					//create TCP segment with sequence number NextSeqNum
					ByteBuffer data = ((DataToSendTCPEvent) event).getData();
					int flags = sendAck ? TCPSegment.ACK : 0;
					TCPSegmentToSend segment = new TCPSegmentToSend(sourcePort, destPort,
					                                                nextSeqNum, ackNum,
					                                                flags, rcvWin, (short) 0,
					                                                data);

					SimpleTimer timer = event.getTimer();
					if (timer.isStopped()) {
						timer.restart();
					}

					//pass segment to IP
					byte[] buffer = segment.toBytes();
					DatagramPacket datagram = new DatagramPacket(buffer, buffer.length, remoteAddress, remotePort);

					data.position(0);
					nextSeqNum = nextSeqNum + data.remaining();

					return TCPState.WAIT;
				} else if (event instanceof TimeoutTCPEvent) {

				} else if (event instanceof DataReceivedTCPEvent) {

				}
			default:
				throw new InvalidStateException(currentState);
		}
	}

	public TCPStateMachine(short sourcePort, short destPort, InetAddress remoteAddress, int remotePort) {
		int rand = (new Random()).nextInt();
		nextSeqNum = rand;
		sendBase = rand;

		this.remoteAddress = remoteAddress;
		this.remotePort = remotePort;
		this.sourcePort = sourcePort;
		this.destPort = destPort;
	}
}
