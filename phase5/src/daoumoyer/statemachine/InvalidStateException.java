package daoumoyer.statemachine;

/**
 * @author Grant Moyer
 * @since 2015-11-24
 */
public class InvalidStateException extends RuntimeException {
	public InvalidStateException(State state) {
		super("State " + state + " is not a valid state");
	}
}
