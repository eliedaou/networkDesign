import java.io.File;
import java.io.FileInputStream;
import java.io.UnsupportedEncodingException;
import java.util.Scanner;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.net.InetSocketAddress;

public class Sender {
	public static byte [] fileToSend = null;
	public static InetSocketAddress sourceSocket = null;
    public static void main(String [] args) {
		//get a file name
		System.out.println("Enter a file to read from:")
		Scanner scan = new Scanner(System.in);
		String path = null
		try{
			path = scan.nextline();
		} catch (Exception e) {
            System.err.println("Fatal: exception caught while prompting for file name");
            System.err.println("\tException: " + e);
            System.exit(-1);
		}

		//open the file for reading
		File file = new File(path);
		final FileInputStream fIn;
		FileInputStream tempFIn = null;
		try {
			tempFIn = new FileInputStream(file);
		} catch (IOException e) {
            System.err.println("Fatal: exception caught while trying to open file");
            System.err.println("\tException: " + e);
            System.exit(-1);
        }
		fIn = tempFIn;

        //make sure file closes when program exits
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    fIn.close();
                } catch (IOException e) {
                    System.err.println("Error: exception caught while trying to close file");
                    System.err.println("\tException: " + e);
                }
            }
        }));

		//run SendManager
    	(new SendManager(fIn)).run();
    }
}
