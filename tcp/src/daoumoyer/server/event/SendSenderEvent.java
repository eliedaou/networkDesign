package daoumoyer.server.event;

import daoumoyer.server.SendData;
import daoumoyer.SimpleTimer;

/**
 * @author Grant Moyer
 * @since 2015-11-24
 */
public class SendSenderEvent extends ServerEvent {
	private SendData data;
	public SendSenderEvent(SimpleTimer timer, SendData data) {
		super(timer);
		this.data = data;
	}

	public SendData getData() {
		return data;
	}
}
