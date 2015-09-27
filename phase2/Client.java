import java.io.*;
import java.net.*;
import java.nio.file.Path;
import java.util.Scanner;

public class Client {
	public static void main(String args[]) throws Exception {
		try {
			// initialize *this socket, the server port and a response variable
			// with default value null
			DatagramSocket clientSocket = new DatagramSocket();
			int port = 10002;
			Response response = null;

			// setting IP to localhost
			InetAddress IPAddress = InetAddress.getByName("127.0.0.1");

			System.out.print("Select one of the following:");
			System.out.print("1.Hello Server!");
			System.out.print("2.SEND a file");
			System.out.print("3.EXIT");
			Scanner scanner = new Scanner(System.in);
			int selection = scanner.nextInt();
			byte[] sendData = null;
			String path = null;

			// String to be sent to the server and getting the bytes from the
			// string
			switch (selection) {
			case 1:
				String requestSentence = new String("Hello Server!");
				sendData = requestSentence.getBytes();
				break;
			case 2:
				System.out.print("Input full file path:");
				path = scanner.next().toString();
				sendData = ("SEND" + path).getBytes();
				break;
			case 3:
				System.exit(0);
			default:
				break;
			}

			// make the packet(s) and send to server
			DatagramPacket sendPacket = new DatagramPacket(sendData,
					sendData.length, IPAddress, port);
			clientSocket.send(sendPacket);

			switch (selection) {
			case 1:
				response = getResponse(clientSocket);
				String responseMessage = null;
				responseMessage = new String(response.getContents());
				System.out.println("FROM SERVER: " + responseMessage);
				break;
			case 2:
				response = new Response(clientSocket);
				response.receiveMessage();
				writeTofile(path, response);
				break;
			case 3:
				System.exit(0);
			default:
				break;
			}

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

	final static void writeTofile(String Path, Response response)
			throws IOException {
		File file = new File(Path);

		// if file doesnt exists, then create it
		if (!file.exists()) {
			file.createNewFile();
		}
		FileOutputStream fos = new FileOutputStream(file);
		for (DatagramPacket packet : response.packets) {
			fos.write(packet.getData());
		}
		fos.close();

		System.out.println("Done");
	}

	final static Response getResponse(DatagramSocket socket)
			throws SocketException, IOException {
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
