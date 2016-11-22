import java.io.IOException;

import student.TestCase;

// -------------------------------------------------------------------------
/**
 * Test the hash function (you should throw this away for your project)
 *
 * @author CS3114 staff
 * @version Aug 27, 2016
 */
public class HashTest extends TestCase {

    /**
     * Declaration of important variables for the test
     */
    Memman manager;
    /**
     * Declaration
     */
    Hash testHash;

    /**
     * Sets up the tests that follow.
     * @throws IOException 
     */
    @Override
    public void setUp() throws IOException {
        manager = new Memman("memman.txt", 3, 3);
        testHash = new Hash(5, "Song");
    }

    /**
     * test the constructor of the hash class
     */
    public void testConstructor() 
    {
        assertNotNull(testHash);
        assertEquals(0, testHash.getSize());
    }

    /**
     * test the insert function
     * @throws IOException 
     */
    public void testInsert() throws IOException {
        assertNotNull(testHash.insert("aaaa", manager));
        assertNotNull(testHash.insert("bbbb", manager));
        assertNotNull(testHash.insert("aaaa", manager));
        assertNotNull(testHash.insert("aaab", manager));
        assertNotNull(testHash.insert("aaba", manager));
        testHash.remove("abaa", manager);
        testHash.insert("baaa", manager);
        assertNotNull(testHash.insert("abaa", manager));
    }

    /**
     * test the Remove method
     * @throws IOException 
     */
    public void testRemove() throws IOException {
        assertFalse(testHash.remove("aaaa", manager));
        assertNotNull(testHash.insert("aaaa", manager));
        assertNotNull(testHash.insert("bbbb", manager));
        assertNotNull(testHash.insert("aaaa", manager));
        assertNotNull(testHash.insert("aaab", manager));
        assertNotNull(testHash.insert("aaba", manager));
        assertTrue(testHash.remove("bbbb", manager));
        assertTrue(testHash.remove("aaaa", manager));
        assertTrue(testHash.remove("aaab", manager));
        assertTrue(testHash.remove("aaba", manager));
    }

    /**
     * test the print method
     * @throws IOException 
     */
    public void testPrint() throws IOException {
        assertNotNull(testHash.insert("aaaa", manager));
        testHash.print(manager);
        assertTrue(systemOut().getHistory().endsWith("|aaaa| 3\n"));
        assertNotNull(testHash.insert("bbbb", manager));
        assertNotNull(testHash.insert("aaab", manager));
        testHash.print(manager);
        assertTrue(systemOut().getHistory().contains("|bbbb| 2\n"));
        assertNotNull(testHash.insert("aaba", manager));
        testHash.print(manager);
        testHash.remove("aaba", manager);
        testHash.print(manager);
    }

    /**
     * Test the hash function
     * @throws IOException 
     */
    public void testh() throws IOException {
        testHash.print(manager);
        assertNotNull(testHash.insert("Life is Strange", manager));
        testHash.remove("Imposible", manager);
        testHash.insert("aaaabbbb", manager);
        testHash.insert("aaaabbbb", manager);
        testHash.insert("bbbbaaaa", manager);
        testHash.remove("aaaabbbb", manager);
    }
}
