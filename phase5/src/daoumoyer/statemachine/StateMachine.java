package daoumoyer.statemachine;

public abstract class StateMachine {
    protected State currentState;
    protected abstract State initialState();

    public State CurrentState(){
    	return currentState;
    }
    
    public StateMachine() {
        currentState = this.initialState();
    }

    protected abstract State delta(State currentState, Event event) throws CannotAdvanceException;

    public void advance(Event event) throws CannotAdvanceException {
        currentState = this.delta(currentState, event);
    }
}
