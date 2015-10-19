import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.SocketException;
import java.io.FileInputStream;
import java.io.IOException;

public class SendManager implements Runnable {
    private final SendingStateMachine machine;
    private final DatagramSocket socket;
    private final FileInputStream fIn;
    private final InetAddress remoteAddress;
    private final int remotePort;
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

        //set remoteAddress
        remoteAddress = InetAddress.getByName("127.0.0.1");
        remotePort = 10000;
    }

    @Override
    public void run() {

        DatagramPacket dataPacket;
        ServerReceived packet;
        SendingStateMachine.SendingEvent event;
        byte[] sendBuffer = new byte[1024];
        byte[] data;
        int bytesRead;
        while ((bytesRead = fIn.read(sendBuffer, 5, sendBuffer.length - 5)) != -1) {
            //make data array of correct size
            data = new byte[bytesRead + 5];
            System.arraycopy(sendBuffer, 0, data, 0, data.length)

            //make packet from file
            packet = new ServerReceived(data, remoteAddress, remotePort);

            //build an event
            event = new SendingStateMachine.SendingEvent(packet);

            //give the event to the state machine
            machine.advance(event);

            //wait for ACK
            while (machine.isWaitingForAck()) {
                //get ack from network
                dataPacket = new DatagramPacket(receivedBuffer, receivedBuffer.length);
                socket.receive(dataPacket);

                //build event
                packet = new ServerReceived(dataPacket);
                event = new SendingStateMachine.SendingEvent(packet);

                //give event to the state machine
                machine.advance(event);
            }
        }

        //send final packet 100 times for safety
        data = new byte[5];
        data[0] = -1;
        data[4] = -1;

        for (int i = 0; i < 100; ++i) {
            dataPacket = new DatagramPacket(data, data.length, remoteAddress, remotePort);
            socket.send(dataPacket);
        }
    }
}
