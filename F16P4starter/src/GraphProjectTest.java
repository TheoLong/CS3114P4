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
            GraphProject.main(new String[] { "mem.txt", "1", "32", "32", "input.txt",
                    "stat.txt" });
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
