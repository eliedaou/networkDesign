package daoumoyer;

/**
 * @author Grant Moyer
 * @since 2015-12-05
 */
public class MSSExcededException extends RuntimeException {
	public MSSExcededException(int requested, int mss) {
		super("The requested segment size, " + requested + ", is larger than the MSS, " + mss + ".");
	}
}
