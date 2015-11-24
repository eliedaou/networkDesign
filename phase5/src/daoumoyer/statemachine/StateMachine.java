package daoumoyer.statemachine;

public abstract class StateMachine {
    protected interface State {}
    protected interface Event {}


    protected State currentState;
    protected abstract State initialState();

    public State CurrentState(){
    	return currentState;
    }
    
    public StateMachine() {
        currentState = this.initialState();
    }

    protected abstract State delta(State currentState, Event event);

    public void advance(Event event) {
        currentState = this.delta(currentState, event);
    }
}
