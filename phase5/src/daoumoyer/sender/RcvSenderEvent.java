package daoumoyer.sender;

/**
 * @author Grant Moyer
 * @since 2015-11-24
 */
public class RcvSenderEvent {
	private AckPacket ack;

	public RcvSenderEvent(AckPacket ack) {
		this.ack = ack;
	}
}
