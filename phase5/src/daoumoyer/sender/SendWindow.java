package daoumoyer.sender;

import java.io.File;

/**
 * @author Grant Moyer
 * @since 2015-11-24
 */
public class SendWindow {
	private long base;
	private long nextSeqNum;
	private long windowSize;

	public SendWindow(File file) {
		base = 0;
		nextSeqNum = 0;
		windowSize = 1000;
	}
}
