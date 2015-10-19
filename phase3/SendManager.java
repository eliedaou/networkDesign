import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.io.FileInputStream;
import java.io.IOException;

public class SendManager implements Runnable {
    private final SendingStateMachine machine;
    private final DatagramSocket socket;
    private final FileInputStream fIn;
    private final InetSocketAddress sourceSocket;
    private final double ackError;
    private final double dataError;

    public SendManager(FileInputStream fIn, double ackError, double dataError) {
        //temporary variable needed for try-catch
        DatagramSocket tempSocket = null
        try {
            //open socket on specified port
            tempSocket = new DatagramSocket();
            System.out.println("Opened socket on port " + serverSocket.getLocalPort());
        } catch (IOException e) {
            System.err.println("Fatal: exception caugth while opening socket");
            System.err.println("\tException: " + e);
            System.exit(-1);
        }
        socket = tempSocket;

        //close socket when program exits
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            @Override
            public void run() {
                if (socket.isBound() && !socket.isClosed()) {
                    socket.close();
                    System.out.println("Closed socket");
                }
            }
        }));

        //start state machine
        machine = new SendingStateMachine(socket);

        //keep file input stream
        this.fIn = fIn;

        //keep bit error variables
        this.ackError = ackError;
        this.dataError = dataError;
    }

    @Override
    public void run() {

        DatagramPacket dataPacket;
        ServerReceived packet;
        SendingStateMachine.SendingEvent event;
        byte[] sendBuffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = fIn.read(sendBuffer, 5, 1024 - 5)) != -1) {
            //make packet from file
            packet = new ServerReceived(sendBuffer);

            //build an event
            event = new SendingStateMachine.SendingEvent(packet);

            //give the event to the state machine
            machine.advance(event);

            //wait for ACK
            dataPacket = new DatagramPacket(receivedBuffer, receivedBuffer.length);
            socket.receive(dataPacket)
        }
        while (true) {
            //try to receive on socket until it is closed
            try {
                request = getRequest(serverSocket);
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
