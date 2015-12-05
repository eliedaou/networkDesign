package daoumoyer.sender.event;

import daoumoyer.sender.ReceivedAck;
import daoumoyer.sender.SendWindow;
import daoumoyer.SimpleTimer;

/**
 * @author Grant Moyer
 * @since 2015-11-24
 */
public class RcvSenderEvent extends SenderEvent {
	private SendWindow window;
	private ReceivedAck ack;

	public RcvSenderEvent(SimpleTimer timer, SendWindow window, ReceivedAck ack) {
		super(timer);
		this.window = window;
		this.ack = ack;
	}

	public boolean isCorrupt() {
		return ack.isCorrupt();
	}

	/* Getters */

	public ReceivedAck getAck() {
		return ack;
	}

	public SendWindow getWindow() {
		return window;
	}
}
