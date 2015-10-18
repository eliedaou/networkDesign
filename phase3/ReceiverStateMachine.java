
public class ReceiverStateMachine extends StateMachine {
    private boolean onceThrough;
    public class ReceiverEvent implements Event {
        private ReceivedPacket packet;

        public ReceiverEvent(ReceivedPacket packet){
            this.packet = packet;
        }

        public boolean isCorrupt() {
            return packet.isCorrupt();
        }

        public int getSeq() {
            return packet.getSeq();
        }
    }

    protected enum ReceiverState implements State {
        WAIT_FOR_0,
        WAIT_FOR_1
    }

    protected State delta(State currentState, Event event) {
        return delta((ReceiverState) currentState, (ReceiverEvent) event);
    }

    protected ReceiverState delta(ReceiverState currentState, ReceiverEvent event) {
        switch (currentState) {
            case WAIT_FOR_0:
                if (!event.isCorrupt() && (event.getSeq() == 0)) {
                    return ReceiverState.WAIT_FOR_1;
                } else if (event.isCorrupt() || (event.getSeq() != 0)) {
                    return ReceiverState.WAIT_FOR_0;
                }
            case WAIT_FOR_1:
                if (!event.isCorrupt() && (event.getSeq() == 1)) {
                    return ReceiverState.WAIT_FOR_0;
                } else if (event.isCorrupt() || (event.getSeq() != 1)) {
                    return ReceiverState.WAIT_FOR_1;
                }
        }
        //Should never be reached
        throw new Exception("Illegal state or event");
    }

    protected State initialState() {
        return ReceiverState.WAIT_FOR_0;
    }

    public ReceiverStateMachine() {
        onceThrough = false;
    }
}