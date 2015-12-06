package daoumoyer.tcp;

import daoumoyer.statemachine.State;

/**
 * Represents the set of states for the TCP state machine. There is only one state, WAIT.
 *
 * @author Grant Moyer
 * @since 2015-12-06
 */
public enum TCPState implements State {
	WAIT("WAIT");

	private String name;

	TCPState(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return name;
	}
}
