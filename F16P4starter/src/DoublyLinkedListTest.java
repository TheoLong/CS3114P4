import student.TestCase;

/**
 * JUnit test for Doublylinkedlist
 * 
 * @author Xianze
 * 
 * @version 9/5/2016
 *
 */
public class DoublyLinkedListTest extends TestCase {

    /**
     * declare the variable
     */
    DoublyLinkedList<String> mylist;

    @Override
    public void setUp() {
        mylist = new DoublyLinkedList<String>();
    }

    /**
     * test constructor
     */
    public void testConstructor() {
        assertEquals(0, mylist.getLength());
        assertEquals(null, mylist.getData());
        mylist.moveToHead();
        assertEquals(null, mylist.getData());
    }

    /**
     * test getData() method
     */
    public void testGetData() {
        mylist.insert("123456");
        assertTrue(mylist.getData().equals("123456"));
    }

    /**
     * test moveToTail() method
     */
    public void testMoveToTail() {
        mylist.insert("123456");
        mylist.moveToTail();
        assertFalse(mylist.hasNext());
    }

    /**
     * test moveToHead() method
     */
    public void testMoveToHead() {
        mylist.insert("A");
        mylist.moveToHead();
        assertFalse(mylist.previous());
    }

    /**
     * test search method
     */
    public void testSearch() {
        mylist.insert("A");
        mylist.insert("B");
        assertTrue(mylist.search("A"));
        assertTrue(mylist.search("B"));
        assertFalse(mylist.search("C"));
    }

    /**
     * test Previous() method
     */
    public void testPrevious() {
        assertFalse(mylist.previous());
        mylist.insert("A");
        mylist.insert("B");
        assertEquals("B", mylist.getData());
        mylist.moveToTail();
        assertTrue(mylist.previous());
        assertEquals("A", mylist.getData());
    }

    /**
     * test peekNext() method
     */
    public void testPeekNext() {
        assertEquals(null, mylist.peekNext());
        mylist.insert("A");
        mylist.moveToHead();
        assertEquals("A", mylist.peekNext());
    }

    /**
     * test next() method
     */
    public void testNext() {
        assertFalse(mylist.next());
        mylist.insert("A");
        mylist.moveToHead();
        assertTrue(mylist.next());
        assertEquals("A", mylist.getData());
    }

    /**
     * test hasNext() method
     */
    public void testHasNext() {
        assertFalse(mylist.hasNext());
        mylist.insert("A");
        assertFalse(mylist.hasNext());
    }

    /**
     * test getLength method
     */
    public void testGetLength() {
        assertEquals(0, mylist.getLength());
        mylist.insert("A");
        assertEquals(1, mylist.getLength());
        mylist.insert("B");
        assertEquals(2, mylist.getLength());
    }

    /**
     * test insert method
     */
    public void testInsert() {
        mylist.insert("A");
        assertEquals("A", mylist.getData());
        mylist.insert("B");
        assertEquals("B", mylist.getData());
    }

    /**
     * test remove method
     */
    public void testRemove() {
        assertFalse(mylist.remove());
        mylist.insert("A");
        mylist.insert("B");
        assertTrue(mylist.remove());
        assertEquals("A", mylist.getData());
        assertEquals(1, mylist.getLength());
        assertTrue(mylist.remove());
        assertEquals(0, mylist.getLength());
    }
}
