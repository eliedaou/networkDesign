import java.io.*;
import java.net.*;

public class Client {
	public static void main(String args[]) throws Exception {
		try {
			DatagramSocket clientSocket = new DatagramSocket();
			int port = 55555;
			Response response = null;
			InetAddress IPAddress = InetAddress.getByName("127.0.0.1");
			byte[] sendData = new byte[1024];
			String requestSentence = new String("Hello Server");

			sendData = requestSentence.getBytes();

			DatagramPacket sendPacket = new DatagramPacket(sendData,
					sendData.length, IPAddress, port);
			clientSocket.send(sendPacket);

			response = getResponse(clientSocket);
			String responseMessage = null;
			responseMessage = new String(response.getContents());
			System.out.println("FROM SERVER: " + responseMessage);
			clientSocket.close();
		} catch (Exception e) {
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
