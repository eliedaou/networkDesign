package daoumoyer.client;

import daoumoyer.statemachine.State;

/**
 * @author Grant Moyer
 * @since 2015-11-24
 */
public enum ClientState implements State {
	WAIT("WAIT");

	private String stateName;

	ClientState(String stateName) {
		this.stateName = stateName;
	}

	public String toString() {
		return stateName;
	}
}
