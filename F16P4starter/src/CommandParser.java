import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * @author Xianze Meng
 * 
 * @version 9/2/2016
 *
 */
public class CommandParser {

    private Scanner sc;

    /**
     * Construct the parser to parse the file "filename"
     * 
     * @param filename
     *            - the file to be read
     */
    public CommandParser(String filename) {
        try {
            sc = new Scanner(new File(filename));
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
            System.out.println("File not found.");
        }
    }

    /**
     * Extract the next Command from the file
     * 
     * @return - String array contains one line of commands - null if the input
     *         format isn't correct
     */
    public String[] nextCommand() {
        String[] result = null;
        if (sc.hasNextLine()) {
            String temp = sc.next();
            if (temp.equals("insert")) {
                result = new String[3];
                result[0] = "insert";
                temp = sc.nextLine().trim();
                result[1] = temp.split("<SEP>")[0];
                result[2] = temp.split("<SEP>")[1];
            }
            else if (temp.equals("remove")) {
                result = new String[3];
                result[0] = "remove";
                result[1] = sc.next();
                result[2] = sc.nextLine().trim();
            }
            else if (temp.equals("print")) {
                result = new String[2];
                result[0] = "print";
                result[1] = sc.nextLine().trim();
            }
            else {
                sc.nextLine();
            }
            return result;
        }
        return null;
    }

}
