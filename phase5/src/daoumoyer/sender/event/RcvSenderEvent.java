package daoumoyer.sender.event;

import daoumoyer.sender.RcvedAck;
import daoumoyer.sender.SendWindow;
import daoumoyer.SimpleTimer;

/**
 * @author Grant Moyer
 * @since 2015-11-24
 */
public class RcvSenderEvent extends SenderEvent {
	private SendWindow window;
	private RcvedAck ack;

	public RcvSenderEvent(SimpleTimer timer, SendWindow window, RcvedAck ack) {
		super(timer);
		this.window = window;
		this.ack = ack;
	}

	public boolean isCorrupt() {
		return ack.isCorrupt();
	}

	/* Getters */

	public RcvedAck getAck() {
		return ack;
	}

	public SendWindow getWindow() {
		return window;
	}
}
