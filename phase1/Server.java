import java.net.DatagramSocket
import java.net.DatagramPacket
import java.net.InetSocketAddress
import java.util.Vector

/**
 * Sends a file to the client
 */
public class Server {
    public static void main() {
        // Make sure server exits cleanly
        Runtime.getRuntime().addShutdownHook(new Thread(new ExitManager()))

        // Start the server
        ServerRunnable serve = new ServerRunnable();
        Thread serverThread = new Thread(serve);
        serverThread.start();

    }
}

/**
 * Manages exiting cleanly
 */
private class ExitManager implements Runnable {
    private staticfinal Vector threads;

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
        for (int thread = 0; thread < threads.size(); thread++) {
            thread.interrupt();
        }
        System.out.printl("All threads interrupted");
    }
}

/**
 * Responds to clients' requests
 */
private class ServerRunnable implements Runnable {
    // The port to listen on
    private int port;

    /**
     * Sets up the port as -1
     */
    public ServerRunnable() {
        this(-1);
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
        if (port > 0) {
            try {
                DatagramSocket serverSocket = new DatagramSocket(port);
            } catch (SocketException e) {
                System.err.printl("Fatal: Failed to open socket with port "
                    + port );
                System.exit(-1);
            }
        } else {
            try {
                DatagramSocket serverSocket = new DatagramSocket();
            } catch (SocketException e) {
                System.err.printl("Fatal: Failed to open socket with any port");
                System.exit(-1);
            }
            port = serverSocket.getPort();
        }

        // Only run until thread is interupted
        while (Thread.currentThread().isInterupted() == false) {
            Request request = Null;
            // wait for a request
            while (request == Null) {
                request = getRequest(serverSocket);
            }

            serveRequest(request);
        }

        // Makes sure exit manager doesn't try to interup inactive thread
        ExitManager.removeThread(Thread.currentThread);
    }

    /**
     * Looks for a request from a client on a socket
     *
     * Blocks until a request is received
     */
    private Request getRequest(DatagramSocket socket) {
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
        Request request = new Request(packet.getSocketAddress(), contents);

        return request;
    }

    /**
     * Respond to a request from a client
     */
    private void serveRequest(Request req) {
        
    }
}

/**
 * Holds a request from a client
 */
private class Request {
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
private class Message {
    // The data to send
    private final byte[] contents;

    // The data, packetized and ready to send
    private final Vector packets;

    // The client to send the data to
    private final InetSocketAddress destination;


}
