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
	static double numberNeeded;

    public static void main(String [] args) {
		//get a file name
		System.out.println("Enter a file to read from:");
		Scanner scan = new Scanner(System.in);
		String path = null;
		try{
			path = scan.nextLine();
		} catch (Exception e) {
            System.err.println("Fatal: exception caught while prompting for file name");
            System.err.println("\tException: " + e);
            System.exit(-1);
		}

		//get bit error simulation options
		System.out.println("Pick a bit error simulation option:");
		System.out.println("\t1. No errors");
		System.out.println("\t2. ACK bit errors");
		System.out.println("\t3. Data loss");
		int bitLoss = 1;
		float errorPercent = 0;
		try{
			bitLoss = scan.nextInt();
			if (bitLoss == 2 || bitLoss == 3) {
				System.out.println("Enter desired percent error (input '25' for 25% error):");
				errorPercent = scan.nextFloat();
			}
		} catch (Exception e) {
            System.err.println("Fatal: exception caught while prompting for bit error options");
            System.err.println("\tException: " + e);
            System.exit(-1);
		}
		if (bitLoss < 1 || bitLoss > 3) bitLoss = 1;
		if (errorPercent < 0 || errorPercent > 100) errorPercent = 0;
		else numberNeeded = 100/errorPercent;

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
		switch (bitLoss) {
			case 1:
				(new SendManager(fIn, 0 , 0)).run();
				break;
			case 2:
				(new SendManager(fIn, numberNeeded, 0)).run();
				break;
			case 3:
				(new SendManager(fIn, 0, numberNeeded)).run();
				break;
		}
    }
}
