package daoumoyer.server;

import daoumoyer.statemachine.State;

/**
 * @author Grant Moyer
 * @since 2015-11-24
 */
public enum ServerState implements State {
	WAIT("WAIT");

	private String stateName;

	ServerState(String stateName) {
		this.stateName = stateName;
	}

	public String toString() {
		return stateName;
	}
}
