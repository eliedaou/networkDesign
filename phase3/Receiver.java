import java.io.File;
import java.io.FileOutputStream;
import java.util.Scanner;
import java.io.IOException;
import java.io.FileNotFoundException;

public class Receiver {
    public static void main(String [] args) {
        //get a file name
        System.out.println("Enter a file to save to:");
        Scanner scan = new Scanner(System.in);
        String path = null;
        try {
            path = scan.nextLine();
        } catch (Exception e) {
            System.err.println("Fatal: exception caught while prompting for file name");
            System.err.println("\tException: " + e);
            System.exit(-1);
        }

        //open the file for writing
        File file = new File(path);
        final FileOutputStream fOut;
        FileOutputStream tempFOut = null;
        try {
            file.createNewFile();
            tempFOut = new FileOutputStream(file);
        } catch (IOException e) {
            System.err.println("Fatal: exception caught while trying to open file");
            System.err.println("\tException: " + e);
            System.exit(-1);
        }
        fOut = tempFOut;

        //make sure file closes when program exits
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            public void run() {
                try {
                    fOut.close();
                } catch (IOException e) {
                    System.err.println("Error: exception caught while trying to close file");
                    System.err.println("\tException: " + e);
                }
            }
        }));

        //run ReceiveManager
        (new ReceiveManager(fOut)).run();
    }
}
