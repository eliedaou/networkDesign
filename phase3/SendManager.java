import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.io.FileInputStream;
import java.io.IOException;

public class SendManager implements Runnable {
    private SendingStateMachine machine = null;
    private DatagramSocket socket = null;
    private InetSocketAddress sourceSocket = null;
    private Request request;

    public Request Request(){
    	return this.request;
    }

    public InetSocketAddress sourceSocket(){
    	return this.sourceSocket;
    }
    public SendManager(FileInputStream fIn, double ackError, double dataError) {
        //temporary variable needed for try-catch
        try {
            //open socket on specified port
            socket = new DatagramSocket();
            System.out.println("Opened socket on port " + socket.getLocalPort());

            //start state machine
            machine = new SendingStateMachine(socket);
        } catch (IOException e) {
            System.err.println("Fatal: exception caugth while opening socket");
            System.err.println("\tException: " + e);
            System.exit(-1);
        }
    }

    @Override
    public void run() {

        DatagramPacket dataPacket;
        ServerReceived packet;
        SendingStateMachine.SendingEvent event;
        byte[] receivedBuffer = new byte[1500];
        while (true) {
            //try to receive on socket until it is closed
            try {
                //get a packet from the client
                dataPacket = new DatagramPacket(receivedBuffer, receivedBuffer.length);
                socket.receive(dataPacket);

                request = getRequest(socket);
                sourceSocket = request.getSource();

                //build an event
                packet = new ServerReceived(dataPacket);
                event = new SendingStateMachine.SendingEvent(packet);

                //break if last packet
                if (!packet.isCorrupt() && (packet.getSeq() == -1)) break;

                //give the event to the state machine
                machine.advance(event);
            } catch (IOException e) {
                System.err.println("Error: caught exception during main loop");
                System.err.println("\tException: " + e);
            }
        }
    }

    private Request getRequest(DatagramSocket socket) throws SocketException,IOException {
 		// Get buffer size
 		final int DGRAM_SIZE = 1024;

 		// Make packet
 		byte[] buffer = new byte[DGRAM_SIZE];
 		DatagramPacket packet = new DatagramPacket(buffer, DGRAM_SIZE);

 		// receive into packet
 		// blocks until received
 		socket.receive(packet);

 		// build Request
 		byte[] contents = new byte[packet.getLength()];
 		System.arraycopy(buffer, packet.getOffset(), contents, 0,
 				packet.getLength());
 		Request request = new Request((InetSocketAddress) packet.getSocketAddress(), contents);

 		return request;
 	}

}
