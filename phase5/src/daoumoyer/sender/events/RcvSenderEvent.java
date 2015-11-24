package daoumoyer.sender.events;

import daoumoyer.sender.AckPacket;
import daoumoyer.sender.SendWindow;
import daoumoyer.sender.SimpleTimer;

/**
 * @author Grant Moyer
 * @since 2015-11-24
 */
public class RcvSenderEvent extends SenderEvent {
	private SendWindow window;
	private AckPacket ack;

	public RcvSenderEvent(SimpleTimer timer, SendWindow window, AckPacket ack) {
		super(timer);
		this.window = window;
		this.ack = ack;
	}

	public boolean isCorrupt() {
		return ack.isCorrupt();
	}

	/* Getters */

	public AckPacket getAck() {
		return ack;
	}

	public SendWindow getWindow() {
		return window;
	}
}
