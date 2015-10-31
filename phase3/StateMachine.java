public abstract class StateMachine {
    protected interface State {}
    protected interface Event {}


<<<<<<< HEAD
    private State currentState;
    public State CurrentState(){
    	return currentState;
    }
=======
    protected State currentState;
>>>>>>> SendingStateMachine
    protected abstract State initialState();

    public StateMachine() {
        currentState = this.initialState();
    }

    protected abstract State delta(State currentState, Event event);

    public void advance(Event event) {
        currentState = this.delta(currentState, event);
    }
}
