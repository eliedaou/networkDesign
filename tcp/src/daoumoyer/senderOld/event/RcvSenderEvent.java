package daoumoyer.sender.event;

import daoumoyer.sender.ReceivedAck;
import daoumoyer.sender.ServerWindow;
import daoumoyer.SimpleTimer;

/**
 * @author Grant Moyer
 * @since 2015-11-24
 */
public class RcvSenderEvent extends SenderEvent {
	private ServerWindow window;
	private ReceivedAck ack;

	public RcvSenderEvent(SimpleTimer timer, ServerWindow window, ReceivedAck ack) {
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

	public ServerWindow getWindow() {
		return window;
	}
}
