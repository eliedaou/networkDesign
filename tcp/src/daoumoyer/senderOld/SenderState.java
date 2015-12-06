package daoumoyer.sender;

import daoumoyer.statemachine.State;

/**
 * @author Grant Moyer
 * @since 2015-11-24
 */
public enum SenderState implements State {
	WAIT("WAIT");

	private String stateName;

	SenderState(String stateName) {
		this.stateName = stateName;
	}

	public String toString() {
		return stateName;
	}
}
