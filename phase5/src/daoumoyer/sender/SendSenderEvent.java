package daoumoyer.sender;

/**
 * @author Grant Moyer
 * @since 2015-11-24
 */
public class SendSenderEvent extends SenderEvent {
	private SendData data;
	public SendSenderEvent(SendData data) {
		this.data = data;
	}
}
