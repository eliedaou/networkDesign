package daoumoyer.statemachine;

/**
 * @author Grant Moyer
 * @since 2015-11-24
 */
public class InvalidEventException extends RuntimeException {
	public InvalidEventException(State state, Event event) {
		super("Event " + event + " is not valid for State " + state);
	}
}
