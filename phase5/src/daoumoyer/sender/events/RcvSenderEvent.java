package daoumoyer.sender.events;

import daoumoyer.sender.AckPacket;

/**
 * @author Grant Moyer
 * @since 2015-11-24
 */
public class RcvSenderEvent extends SenderEvent {
	private AckPacket ack;

	public RcvSenderEvent(AckPacket ack) {
		this.ack = ack;
	}
}
