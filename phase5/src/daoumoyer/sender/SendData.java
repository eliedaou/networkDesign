package daoumoyer.sender;

import java.io.File;

/**
 * @author Grant Moyer
 * @since 2015-11-24
 */
public class SendData {
	private File file;

	public SendData(File file) {
		this.file = file;
	}

	public SendData(String path) {
		file = new File(path);
	}
}
