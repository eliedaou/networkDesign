package daoumoyer.receiver;

import daoumoyer.statemachine.State;

/**
 * @author Grant Moyer
 * @since 2015-11-24
 */
public enum ReceiverState implements State {
	WAIT("WAIT");

	private String stateName;

	ReceiverState(String stateName) {
		this.stateName = stateName;
	}

	public String toString() {
		return stateName;
	}
}
