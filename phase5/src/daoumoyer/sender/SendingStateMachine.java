package daoumoyer.sender;

import daoumoyer.SimpleTimer;
import daoumoyer.sender.event.*;
import daoumoyer.statemachine.*;

import java.net.*;

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

	protected State delta(State currentState, Event event) throws DataRefusedException, DoneException {
		return delta((SenderState) currentState, (SenderEvent) event);
	}

	protected SenderState delta(SenderState currentState, SenderEvent event) throws DataRefusedException, DoneException {
		switch (currentState) {
			case WAIT:
				if (event instanceof SendSenderEvent) {
					//create variables
					SendSenderEvent sendEvent = (SendSenderEvent) event;
					SendData data = sendEvent.getData();
					SendWindow window = data.getWindow();

					//event logic
					if (window.getNextSeqNum() < window.getBase() + window.getWindowSize()) {
						data.udtSend(window.getNextSeqNum());
						if (window.getBase() == window.getNextSeqNum()) {
							sendEvent.getTimer().restart();
						}
						window.incrementNext();
					} else {
						throw new DataRefusedException();
					}

					return SenderState.WAIT;
				} else if (event instanceof TimeoutSenderEvent) {
					//create variables
					TimeoutSenderEvent timeoutEvent = (TimeoutSenderEvent) event;
					SendData data = timeoutEvent.getData();
					SendWindow window = data.getWindow();

					//event logic
					timeoutEvent.getTimer().restart();
					for (long i = window.getBase(); i < window.getNextSeqNum(); ++i) {
						data.udtSend(i);
					}

					return SenderState.WAIT;
				} else if (event instanceof RcvSenderEvent) {
					RcvSenderEvent rcvEvent = (RcvSenderEvent) event;
					if (!rcvEvent.isCorrupt()) {
						//create variables
						SendWindow window = rcvEvent.getWindow();
						ReceivedAck ack = rcvEvent.getAck();
						SimpleTimer timer = rcvEvent.getTimer();

						//event logic
						try {
							window.setBase(ack.getSeqNum() + 1);
						} catch (DoneException e) {

						}
						if (window.getBase() == window.getNextSeqNum()) {
							rcvEvent.getTimer().stop();
						} else {
							rcvEvent.getTimer().restart();
						}

						return SenderState.WAIT;
					} else {
						//event logic
						return SenderState.WAIT;
					}
				}
			default:
				throw new InvalidStateException(currentState);
		}
	}

	protected State initialState() {
		return SenderState.WAIT;
	}

}
