package daoumoyer.sender;

/**
 * @author Grant Moyer
 * @since 2015-11-24
 */
public class NotInWindowException extends RuntimeException {
	public NotInWindowException(long base, long nextSeqNum, long seqNum) {
		super("seqNum " + seqNum + " is not in window [base, nextSeqNum) [" + base + ", " + nextSeqNum + ")");
	}
}
