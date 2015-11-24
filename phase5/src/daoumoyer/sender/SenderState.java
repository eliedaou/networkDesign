package daoumoyer.sender;

import daoumoyer.statemachine.State;

/**
 * @author Grant Moyer
 * @since 2015-11-24
 */
public enum SenderState implements State {
	SEND_0, WAIT_FOR_0, SEND_1, WAIT_FOR_1
}
