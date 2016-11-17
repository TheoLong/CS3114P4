//import java.util.Iterator;

/**
 * @author Xianze Meng
 * 
 * @version 9/8/2016
 *
 * @param <T>
 *            - the data to put into the container
 */
public class DoublyLinkedList<T> {
    private int length;
    private Node head;
    private Node tail;
    private Node curr;

    private class Node {
        Node previous;
        Node next;
        T data;

        /**
         * Create an innernode for this list
         * 
         * @param e
         *            - data to contain in the node
         */
        public Node(T e) {
            previous = null;
            next = null;
            data = e;
        }
    }


    /**
     * Constructor - initializes the doublylinkedlist
     */
    public DoublyLinkedList() {
        length = 0;
        head = new Node(null);
        tail = new Node(null);
        head.next = tail;
        tail.previous = head;
        curr = tail;
    }




    /**
     * Get the length of the list
     * 
     * @return - length of the list
     */
    public int getLength() {
        return length;
    }



    /**
     * insert data at position [i]
     * 
     * @param e
     *            - data to be inserted
     * @return - true on success - false on failure
     */
    public boolean insert(T e) {
        Node newNode = new Node(curr.data);
        newNode.previous = curr;
        newNode.next = curr.next;
        if (curr != tail) {
            newNode.next.previous = newNode;
        }
        else {
            tail = newNode;
        }
        curr.data = e;
        curr.next = newNode;
        length++;
        return true;
    }


    /**
     * remove the data at [i]
     * 
     * @return - true on success - false on failure
     */
    public boolean remove() {
        if (curr == tail) {
            return false;
        }
        else if (curr.next == tail) {
            tail.previous = curr.previous;
            curr.previous.next = tail;
            length--;
            return true;
        }
        else {
            curr.data = curr.next.data;
            curr.next = curr.next.next;
            curr.next.previous = curr;
            length--;
            return true;
        }
    }


    /**
     * test if list has next node
     * @return - true when it's not at the end
     *         - false when it is at the end
     */
    public boolean hasNext() {
        return (curr.next != tail) && (curr.next != null);
    }

    /**
     * Advance the curr pointer by 1
     * 
     * @return - true on success
     *         - false on failure
     */
    public boolean next() {
        if (hasNext()) {
            curr = curr.next;
            return true;
        }
        return false;
    }

    /**
     * peek the value stored in next node
     * 
     * @return - the value stored in next node
     *         - null on failure
     * 
     */
    public T peekNext() {
        if (hasNext()) {
            return curr.next.data;
        }
        return null;
    }

    /**
     * move the curr pointer 1 pos forward
     * 
     * @return - true on success
     *         - false when already at the beginning 
     */
    public boolean previous() {
        if (curr.previous != head && curr.previous != null) {
            curr = curr.previous;
            return true;
        }
        return false;
    }

    /**
     * move the curr pointer to the head
     * 
     * 
     */
    public void moveToHead() {
        curr = head;
    }

    /**
     *  move the curr pointer the the tail
     */
    public void moveToTail() {
        curr = tail;
    }

    /**
     * get the data stored in current node
     * @return - data
     */
    public T getData() {
        return curr.data;
    }

    /**
     * Check if data e has been in the list
     * @param e     
     *          - data to compare
     * @return  - true when found in the list
     *          - false when not found in the list
     */
    public boolean search(T e) {
        Node temp = head.next;
        while (temp != tail) {
            if (temp.data.equals(e)) {
                curr = temp;
                return true;
            }
            temp = temp.next;
        }
        return false;
    }


}
