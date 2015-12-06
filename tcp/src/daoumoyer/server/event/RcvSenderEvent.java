package daoumoyer.server.event;

import daoumoyer.server.ReceivedAck;
import daoumoyer.server.ServerWindow;
import daoumoyer.SimpleTimer;

/**
 * @author Grant Moyer
 * @since 2015-11-24
 */
public class RcvSenderEvent extends ServerEvent {
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
