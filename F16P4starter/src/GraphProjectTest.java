import java.io.File;
import java.util.Scanner;

import student.TestCase;

/**
 * @author {Your Name Here}
 * @version {Put Something Here}
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
                    "1", "32", "10", "input.txt","stat.txt" });
            String output = systemOut().getHistory();
            String content = new Scanner(new File("P4sampleOutput.txt")).
                    useDelimiter("\\Z").next();
            assertEquals(content, output);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        
    }
}
