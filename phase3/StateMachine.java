public abstract class StateMachine {
    protected interface State {}
    protected interface Event {}


    private State currentState;
    protected abstract State initialState();

    public StateMachine() {
        currentState = this.initialState();
    }

    protected abstract State delta(State currentState, Event event);

    public void advance(Event event) {
        currentState = this.delta(currentState, event);
    }
}
