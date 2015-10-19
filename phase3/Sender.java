import java.io.File;
import java.io.FileOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Scanner;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.net.InetSocketAddress;

public class Sender {
	public static byte [] fileToSend = null;
	public static InetSocketAddress sourceSocket = null;
    public static void main(String [] args) {
    	String contents = null;
    	SendManager startSender = new SendManager(10000);
    	startSender.run();
    	
		try {
			contents = new String(startSender.Request().getContents(), "UTF-8");
			sourceSocket = startSender.Request().getSource();
		} catch (UnsupportedEncodingException e) {
			System.err.println("Fatal: unsupported string encoding");
			System.err.println("Exception: " + e);
			System.exit(-2);
		}
		
		if (contents.startsWith("SEND")) {
			String path = contents.substring(4).trim();
			fileToSend = path.getBytes();
		}
    }
}
