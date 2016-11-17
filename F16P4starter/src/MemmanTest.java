
import java.io.IOException;

import student.TestCase;

/**
 * @author Xianze Meng
 * 
 * @version 9/5/2016
 */
public class MemmanTest extends TestCase {
    /**
     * 
     */
    Memman manager;

//    /**
//     * Sets up the tests that follow. In general, used for initialization
//     */
//    @Override
//    public void setUp() {
//        manager = new Memman(5);
//    }

    /**
     * Get code coverage of the class declaration.
     */
    public void testMInit() {
        assertNotNull(manager);
    }

    /**
     * test the insert method
     * @throws IOException 
     */
    public void testInsertandGetRecord() throws IOException {
        Handle h1 = manager.insert("bestf".getBytes(), 5);
        Handle h2 = manager.insert("b".getBytes(), 1);
        Handle h3 = manager.insert("abc".getBytes(), 3);
        manager.insert("f".getBytes(), 1);
        Handle h6 = manager.insert("asdffda".getBytes(), 7);
        manager.insert("cde".getBytes(), 3);
        assertEquals("bestf", new String(manager.getRecord(h1)));
        assertEquals("b", new String(manager.getRecord(h2)));
        assertEquals("abc", new String(manager.getRecord(h3)));
        manager.remove(h1);
        manager.remove(h3);
        manager.remove(h6);
        Handle h4 = manager.insert("cs".getBytes(), 2);
        Handle h8 = manager.insert("longstring".getBytes(), 10);
        manager.remove(h8);
        assertEquals("cs", new String(manager.getRecord(h4)));
        Handle h9 = manager.insert("longst".getBytes(), 6);
        assertEquals("longst", new String(manager.getRecord(h9)));
        manager.print();
    }

    /**
     * Get code coverage for getRecord method
     * @throws IOException 
     */
    public void testgetRecord() throws IOException {
        Handle han = manager.insert("abc".getBytes(), 3);
        assertEquals("abc", new String(manager.getRecord(han)));
    }

    /**
     * Test expand method
     * @throws IOException 
     */
    public void testexpand() throws IOException {
        Handle han1 = manager.insert("abc".getBytes(), 3);
        manager.insert(new byte[3], 3);
        assertTrue(systemOut().getHistory().contains("expanded"));
        manager.remove(han1);
        manager.print();
        assertTrue(systemOut().getHistory().contains("(0,5)"));
        manager.insert(new byte[2], 2);
        manager.print();
        manager.insert(new byte[7], 7);
        assertTrue(systemOut().getHistory().contains("expanded"));
    }

    /**
     * Test print method
     * @throws IOException 
     */
    public void testprint() throws IOException {
        manager.insert(new byte[3], 3);
        manager.print();
        assertTrue(systemOut().getHistory().endsWith("(5,0)\n"));
    }

    /**
     * test remove method
     * @throws IOException 
     */
    public void testRemove() throws IOException {
        manager.insert("abc".getBytes(), 3);
        Handle h2 = manager.insert("cde".getBytes(), 3);
        manager.insert("efghi".getBytes(), 5);
        Handle h4 = manager.insert("Boring".getBytes(), 6);
        manager.print();
        assertTrue(systemOut().getHistory().endsWith("(25,0)\n"));
        manager.remove(h2);
        manager.remove(h4);
        manager.print();
        assertTrue(systemOut().getHistory()
                .endsWith("(5,5) -> (17,8)\n"));
    }

}
