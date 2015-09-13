import java.io.*;
import java.net.*;

public class Client {
	public static void main(String args[]) throws Exception {
		try {
			// initialize *this socket, the server port and a response variable with default value null
			DatagramSocket clientSocket = new DatagramSocket();
			int port = 10002;
			Response response = null;

			// setting IP to localhost
			InetAddress IPAddress = InetAddress.getByName("127.0.0.1");

			//String to be sent to the server and getting the bytes from the string
			String requestSentence = new String("Hello Server");
			byte[] sendData = requestSentence.getBytes();

			// make the packet(s) and send to server
			DatagramPacket sendPacket = new DatagramPacket(sendData,
					sendData.length, IPAddress, port);
			clientSocket.send(sendPacket);

			// get, format and output the response
			response = getResponse(clientSocket);
			String responseMessage = null;
			responseMessage = new String(response.getContents());
			System.out.println("FROM SERVER: " + responseMessage);
			clientSocket.close();
		}
		// do nothing in the catch
		catch (Exception e) {
			// TODO: handle exception
		}
	}


final static Response getResponse(DatagramSocket socket) throws SocketException,
			IOException {
		// Get buffer size
		final int DGRAM_SIZE = socket.getReceiveBufferSize();

		// Make packet
		byte[] buffer = new byte[DGRAM_SIZE];
		DatagramPacket packet = new DatagramPacket(buffer, DGRAM_SIZE);

		// receive into packet
		// blocks until received
		socket.receive(packet);

		// build Response
		byte[] contents = new byte[packet.getLength()];
		System.arraycopy(buffer, packet.getOffset(), contents, 0,
				packet.getLength());
		Response response = new Response(
				(InetSocketAddress) packet.getSocketAddress(), contents);

		return response;
	}
}
