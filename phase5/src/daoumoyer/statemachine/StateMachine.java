package daoumoyer.statemachine;

import java.io.IOException;

public abstract class StateMachine {
    protected State currentState;
    protected abstract State initialState();

    public State CurrentState(){
    	return currentState;
    }
    
    public StateMachine() {
        currentState = this.initialState();
    }

    protected abstract State delta(State currentState, Event event) throws CannotAdvanceException, IOException;

    public void advance(Event event) throws CannotAdvanceException, IOException {
        currentState = this.delta(currentState, event);
    }
}
