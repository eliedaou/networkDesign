package daoumoyer.sender;

import java.io.File;

/**
 * @author Grant Moyer
 * @since 2015-11-24
 */
public class SendData {
	private File file;
	private SendWindow window;

	public SendData(File file) {
		this.file = file;
		window = new SendWindow();
	}

	public SendData(String path) {
		this(new File(path));
	}

	public SendWindow getWindow() {
		return window;
	}

	public void udtSend(long seqNum) {

	}
}
