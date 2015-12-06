package daoumoyer.receiver;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.FileNotFoundException;

import java.util.Scanner;
import java.util.NoSuchElementException;
import java.lang.IllegalStateException;

public class Client {

	private static FileOutputStream stream;

	public static void main(String[] args) {

		//get a file name
		System.out.println("Enter a file to save to:");

		try {
			Scanner scan = new Scanner(System.in);
			String filePath = scan.nextLine();
			File file = new File(filePath);
			file.createNewFile();
			stream = new FileOutputStream(file);
		} catch (NoSuchElementException | IllegalStateException e) {
			System.err.println("\tException: " + e);
			System.exit(-1);
		} catch (IOException e) {
			System.err.println("Fatal: exception caught while trying to open file");
			System.exit(-1);
		} catch (Exception e) {
			System.err.println("Fatal: exception caught while prompting for file name");
			System.err.println("\tException: " + e);
			System.exit(-1);
		}

		//make sure file closes when program exits
		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
			@Override public void run() {
				try {
					stream.close();
				} catch (IOException e) {
					System.err.println("Error: exception caught while trying to close file");
					System.err.println("\tException: " + e);
				}
			}
		}));

		//run ReceiveManager
		(new ReceiveManager(stream)).run();
	}
}