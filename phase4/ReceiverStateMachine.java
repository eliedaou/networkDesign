import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.net.SocketAddress;
import java.io.IOException;
import java.io.FileOutputStream;
import java.net.SocketException;

public class ReceiverStateMachine extends StateMachine {
    private boolean onceThrough;
    private DatagramSocket socket;
    static public class ReceiverEvent implements Event {
        private ReceivedPacket packet;
        private FileOutputStream fOut;

        public ReceiverEvent(ReceivedPacket packet, FileOutputStream fOut){
            this.packet = packet;
            this.fOut = fOut;
        }

        public boolean isCorrupt() {
            return packet.isCorrupt();
        }

        public int getSeq() {
            return packet.getSeq();
        }

        public byte[] getData() {
            return packet.getData();
        }

        public FileOutputStream getFOut() {
            return fOut;
        }

        public SocketAddress getSource() {
            return packet.getSource();
        }
    }

    static protected enum ReceiverState implements State {
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
                    //extract and deliver data
                    try {
                        event.getFOut().write(event.getData());
                    } catch (IOException e) {
                        System.err.println("Fatal: caught exception while writing to file");
                        System.err.println("\tException: " + e);
                        System.exit(-1);
                    }

                    //send ACK 0
                    sendAck((byte) 0, event.getSource());

                    //set onceThrough
                    onceThrough = true;

                    //move to WAIT_FOR_1
                    return ReceiverState.WAIT_FOR_1;
                } else if (event.isCorrupt() || (event.getSeq() != 0)) {
                    //send ACK 1
                    if (onceThrough) sendAck((byte) 1, event.getSource());

                    //stay in WAIT_FOR_0
                    return ReceiverState.WAIT_FOR_0;
                }
            case WAIT_FOR_1:
                if (!event.isCorrupt() && (event.getSeq() == 1)) {
                    //extract and deliver data
                    try {
                        event.getFOut().write(event.getData());
                    } catch (IOException e) {
                        System.err.println("Fatal: caught exception while writing to file");
                        System.err.println("\tException: " + e);
                        System.exit(-1);
                    }

                    //send ACK 1
                    sendAck((byte) 1, event.getSource());

                    //move to WAIT_FOR_0
                    return ReceiverState.WAIT_FOR_0;
                } else if (event.isCorrupt() || (event.getSeq() != 1)) {
                    //send ACK 0
                    sendAck((byte) 0, event.getSource());

                    //stay in WAIT_FOR_1
                    return ReceiverState.WAIT_FOR_1;
                }
        }
        return currentState;
    }

    protected State initialState() {
        return ReceiverState.WAIT_FOR_0;
    }

    public ReceiverStateMachine(DatagramSocket socket) {
        onceThrough = false;
        this.socket = socket;
    }

    private void sendAck(byte seq, SocketAddress dest) {
        byte[] header = {seq, seq};
        DatagramPacket packet = null;
        try {
            packet = new DatagramPacket(header, header.length, dest);
            if (false) {
                // Java 7 DatagramPackets can throw a SocketException, but Java 8 DatagramPackets do not
                throw new SocketException();
            }
        } catch (SocketException e) {
            System.err.println("Fatal: caught exception while sending ACK");
            System.err.println("\tException: " + e);
            System.exit(-1);
        }

        try {
            socket.send(packet);
        } catch (IOException e) {
            System.err.println("Error: exception caught while sending ACK");
            System.err.println("\tException: " + e);
        }
    }
}
