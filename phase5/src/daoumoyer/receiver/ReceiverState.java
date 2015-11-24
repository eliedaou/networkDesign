package daoumoyer.receiver;

import daoumoyer.statemachine.State;

/**
 * @author Grant Moyer
 * @since 2015-11-24
 */
public enum ReceiverState implements State {
	WAIT_FOR_0,
	WAIT_FOR_1
}
