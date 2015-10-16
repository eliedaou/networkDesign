public abstract class StateMachine {
    protected enum Event {}
    protected enum State {}


    private State currentState;
    protected abstract State initialState();

    public StateMachine() {
        currentState = this.initialState();
    }

    protected abstract State delta(State currentState, Event event);

    public advance(Event event) {
        currentState = this.delta(currentState, event);
    }
}
