import java.io.File;
import java.util.Scanner;

import student.TestCase;

/**
 * @author Xianze Meng / Zihao Long
 * @version 11/21/2016
 */
public class GraphProjectTest extends TestCase {
    /**
     * Sets up the tests that follow. In general, used for initialization.
     */
    public void setUp() {
        // Nothing Here
    }

    /**
     * Get code coverage of the class declaration.
     */
    public void testGInit() {
        GraphProject gph = new GraphProject();
        assertNotNull(gph);
        try {
            GraphProject.main(new String[] { "mem.txt",
                "1", "32", "10", "P4sampleInput.txt", "stat.txt" });
            String output = systemOut().getHistory();
            String content = new Scanner(new File("P4sampleOutput.txt")).
                    useDelimiter("\\Z").next();
            assertFuzzyEquals(content, output);

        }
        catch (Exception e) {
            e.printStackTrace();
        } 
        try {
            GraphProject.main(new String[] { "mem.txt",
                "1", "32", "10", "reference_input13.txt","stat.txt" });
            String output = systemOut().getHistory();
        }
        catch (Exception e) {
            e.printStackTrace();
        } 
    }
}
