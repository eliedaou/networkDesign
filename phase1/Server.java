import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Vector;

/**
 * Sends a file to the client
 */
public class Server {
    public static void main(String[] args) {
        // Make sure server exits cleanly
        Runtime.getRuntime().addShutdownHook(new Thread(new ExitManager()));

        // Start the server
        ServerRunnable serve = new ServerRunnable();
        Thread serverThread = new Thread(serve);
        serverThread.start();
        System.out.println("Server Started");
    }
}

/**
 * Manages exiting cleanly
 */
class ExitManager implements Runnable {
    private static final Vector threads = new Vector();

    public static void addThread(Thread thread) {
        threads.add(thread);
    }

    public static boolean removeThread(Thread thread) {
        if (threads.contains(thread)) {
            threads.remove(thread);
            return true;
        } else {
            return false;
        }
    }

    public void run() {
        System.out.println("Interrupting all threads");
        for (int i = 0; i < ExitManager.threads.size(); i++) {
            Thread thread = (Thread) threads.get(i);
            thread.interrupt();
        }
        System.out.println("All threads interrupted");
    }
}

/**
 * Responds to clients' requests
 */
class ServerRunnable implements Runnable {
    // The port to listen on
    private int port;

    /**
     * Sets up the port as -1
     */
    public ServerRunnable() {
        this(10000);
    }

    /**
     * Sets the port as the port argument
     */
    public ServerRunnable(int port) {
        this.port = port;
    }

    public void run() {
        // Makes sure thread exits correctly
        ExitManager.addThread(Thread.currentThread());

        // Open server socket
        DatagramSocket serverSocket = null;
        if (port > 0) {
            try {
                serverSocket = new DatagramSocket(port);
            } catch (Exception e) {
                System.err.println("Fatal: Failed to open socket with port " + port );
                System.err.println("Exception: " + e);
                System.exit(-1);
            }
        } else {
            try {
                serverSocket = new DatagramSocket();
                port = serverSocket.getLocalPort();
            } catch (Exception e) {
                System.err.println("Fatal: Failed to open socket with any port");
                System.err.println("Exception: " + e);
                System.exit(-1);
            }
        }
        System.out.println("Socket opened on port " + port);

        // Only run until thread is interrupted
        while (Thread.currentThread().isInterrupted() == false) {
            Request request = null;
            // wait for a request
            while (request == null) {
                try {
                    request = getRequest(serverSocket);
                } catch (Exception e) {
                    System.err.println("Error: Caught Exception while trying to get request");
                    System.err.println("Exception: " + e);
                }
            }

            try {
                serveRequest(request, serverSocket);
            } catch (Exception e) {
                System.err.println("Error: Caught Exception while trying to serve request");
                System.err.println("Exception: " + e);
            }
        }

        // Closes server socket
        serverSocket.close();
        System.out.println("Socket closed");

        // Makes sure exit manager doesn't try to interup inactive thread
        ExitManager.removeThread(Thread.currentThread());
    }

    /**
     * Looks for a request from a client on a socket
     *
     * Blocks until a request is received
     */
    private Request getRequest(DatagramSocket socket) throws SocketException, IOException {
        // Get buffer size
        final int DGRAM_SIZE = socket.getReceiveBufferSize();

        // Make packet
        byte[] buffer = new byte[DGRAM_SIZE];
        DatagramPacket packet = new DatagramPacket(buffer, DGRAM_SIZE);

        // receive into packet
        // blocks until received
        socket.receive(packet);

        // build Request
        byte[] contents = new byte[packet.getLength()];
        System.arraycopy(buffer, packet.getOffset(), contents, 0, packet.getLength());
        Request request = new Request((InetSocketAddress) packet.getSocketAddress(), contents);

        return request;
    }

    /**
     * Respond to a request from a client from socket.
     */
    private void serveRequest(Request req, DatagramSocket socket) throws SocketException {
        String contents = null;
        try {
            contents = new String(req.getContents(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            System.err.println("Fatal: unsupported string encoding");
            System.err.println("Exception: " + e);
            System.exit(-2);
        }
        if (contents == "Hello Server") {
            System.out.println("Got a greeting from a client at " + req.getSource());
            System.out.println("Responding \"Hello Client at " + req.getSource() + "\"");

            Message response = null;
            try {
                response = new Message("Hello Client at" + req.getSource(), socket, req.getSource());
            } catch (UnsupportedEncodingException e) {
                System.err.println("Fatal: unsupported string encoding");
                System.err.println("Exception: " + e);
                System.exit(-2);
            }
            response.sendMessage();
        } else {
            System.err.println("Got a bad request from a client at " + req.getSource());
        }
    }
}

/**
 * Holds a request from a client
 */
class Request {
    private final InetSocketAddress source;
    private final byte[] contents;

    public Request(InetSocketAddress source, byte[] contents) {
        this.source = source;
        this.contents = contents;
    }

    public InetSocketAddress getSource() {
        return source;
    }

    public byte[] getContents() {
        return contents;
    }
}

/**
 * Holds a message to send over UDP
 */
class Message {
    // The data to send
    private final byte[] contents;

    // The data, packetized and ready to send
    private final Vector packets;

    // The client to send the data to
    private final InetSocketAddress destination;

    // The socket to send from
    private final DatagramSocket socket;

    public Message(byte[] contents, DatagramSocket socket, InetSocketAddress destination) throws SocketException {
        this.contents = contents;
        this.socket = socket;
        this.destination = destination;
        packets = new Vector();


        // Get buffer size
        final int DGRAM_SIZE = socket.getReceiveBufferSize();
        final int HEADER_SIZE = 0;

        // break contents into packets
        for (int i = 0; i * (DGRAM_SIZE - HEADER_SIZE) < contents.length; i++) {
            int length = DGRAM_SIZE - HEADER_SIZE;
            if ((i + 1) * length >= contents.length) {
                length = contents.length - i * length;
            }
            DatagramPacket packet = new DatagramPacket(contents,
                                                       i * (DGRAM_SIZE - HEADER_SIZE),
                                                       length,
                                                       destination);
            packets.add(packet);
        }
    }

    public Message(String str, DatagramSocket socket, InetSocketAddress destination) throws UnsupportedEncodingException, SocketException {
        this(str.getBytes("UTF-8"), socket, destination);
    }

    public void sendMessage() {
        try {
            for (int packet = 0; packet < packets.size(); packet++) {
                socket.send((DatagramPacket) packets.get(packet));
            }
        } catch (Exception e) {
            System.err.println("Error: Caught Exception while trying to send message");
            System.err.println("Exception: " + e);
        }
    }

}
