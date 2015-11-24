package daoumoyer.sender.events;

import daoumoyer.sender.SendData;
import daoumoyer.sender.SimpleTimer;

/**
 * @author Grant Moyer
 * @since 2015-11-24
 */
public class TimeoutSenderEvent extends SenderEvent {
	private SendData data;

	public TimeoutSenderEvent(SimpleTimer timer, SendData data) {
		super(timer);
		this.data = data;
	}

	public SendData getData() {
		return data;
	}
}
