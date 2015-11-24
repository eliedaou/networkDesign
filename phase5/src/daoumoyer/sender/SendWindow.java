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

	public SendWindow() {
		base = 0;
		nextSeqNum = 0;
		windowSize = 100;
	}

	/* Getters */

	public long getBase() {
		return base;
	}

	public long getNextSeqNum() {
		return nextSeqNum;
	}

	public long getWindowSize() {
		return windowSize;
	}

	/* Mutators */

	public void incrementNext() {
		++nextSeqNum;
	}

	public void setBase(long base) {
		this.base = base;
	}
}
