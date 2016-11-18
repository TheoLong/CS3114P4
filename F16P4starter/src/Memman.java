import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * This project would take commands from an inputfile and process the commands.
 * It inserts artists and songs to the hashtable and store the data 
 * as bytes in the memory manager. It can print out the song/artist lists and 
 * the free block list as well.
 */

/**
 * The class containing the main method.
 *
 * @author Xianze Meng
 * @version 9/10/2016
 */

// On my honor:
//
// - I have not used source code obtained from another student,
// or any other unauthorized source, either modified or
// unmodified.
//
// - All source code and documentation used in my program is
// either my original work, or was derived by me from the
// source code published in the textbook for this course.
//
// - I have not discussed coding details about this project with
// anyone other than my partner (in the case of a joint
// submission), instructor, ACM/UPE tutors or the TAs assigned
// to this course. I understand that I may discuss the concepts
// of this program with other students, and that another student
// may help me debug my program so long as neither of us writes
// anything during the discussion or modifies any computer file
// during the discussion. I have violated neither the spirit nor
// letter of this restriction.

public class Memman {

    private int initpoolsize;
    private int currpoolsize;
    // private byte[] memorypool;
    private Bufferpool bufpool;
    private DoublyLinkedList<Freeblock> freeblocklist;

    private class Freeblock {
        private int index;
        private int length;

        /**
         * Initialize a freeblock object
         * 
         * @param index
         *            - index of the freeblock
         * @param length
         *            - length of the freeblock
         */
        public Freeblock(int index, int length) {
            this.index = index;
            this.length = length;
        }

        @Override
        public boolean equals(Object other) {
            Freeblock node = (Freeblock) other;
            return (this.index == node.index) && (this.length == node.length);
        }
    }

    /**
     * Convert 2 bytes to an integer
     * 
     * @param b1
     *            - first byte
     * @param b2
     *            - second byte
     * @return - integer value of the 2 bytes
     */
    private int byte2Int(byte b1, byte b2) {
        return (b1 << 8) | (b2 & 0xFF);
    }

    /**
     * Expand the size of the memory manager by [initialpoolsize]
     */
    private void expand() {
        // byte[] temp = new byte[memorypool.length + initpoolsize];
        // System.arraycopy(memorypool, 0, temp, 0, memorypool.length);
        freeblocklist.moveToTail();
        freeblocklist.insert(new Freeblock(currpoolsize, initpoolsize));
        mergeFreeblock();
        currpoolsize += initpoolsize;
        // memorypool = temp;
        System.out.printf("Memory pool expanded "
                + "to be %d bytes.\n", currpoolsize);
    }

    /**
     * Using the bestfit logic to find where the data should reside in the
     * memory pool
     * 
     * @param space
     *            - byte[] to store
     * @param size
     *            - size of the byte[]
     * @return int - the starting index of the record in the memory pool
     */
    private int bestFit(byte[] space, int size) {
        int slot = -1;
        int slotsize = 0;
        if (freeblocklist.getLength() > 0) {
            freeblocklist.moveToHead();
            while (freeblocklist.hasNext()) {
                freeblocklist.next();
                if (freeblocklist.getData().length > size) {
                    if (slot == -1) {
                        slot = freeblocklist.getData().index;
                        slotsize = freeblocklist.getData().length;
                    }
                    else if (freeblocklist.getData().length < slotsize) {
                        slot = freeblocklist.getData().index;
                        slotsize = freeblocklist.getData().length;
                    }
                }
                else if (freeblocklist.getData().length == size) {
                    slot = freeblocklist.getData().index;
                    freeblocklist.remove();
                    return slot;
                }
            }

            if (freeblocklist.search(new Freeblock(slot, slotsize))) {
                freeblocklist.getData().index += size;
                freeblocklist.getData().length -= size;
            }
        }
        return slot;
    }

    /**
     * Merge the continuous freeblocks in the freeblock list
     * 
     */
    private void mergeFreeblock() {
        freeblocklist.moveToHead();
        freeblocklist.next();
        while (freeblocklist.hasNext()) {
            if (freeblocklist.getData().index 
                    + freeblocklist.getData().length
                    == freeblocklist.peekNext().index) {
                freeblocklist.peekNext().index = 
                        freeblocklist.getData().index;
                freeblocklist.peekNext().length += 
                        freeblocklist.getData().length;
                freeblocklist.remove();
                continue;
            }
            freeblocklist.next();
        }
    }

//    /**
//     * Create a memory manager object
//     * 
//     * @param poolsize
//     *            - initial size for the memory pool
//     * @throws IOException
//     */
    /**
     * ======
     * @param memFile ======
     * @param numBuff ======
     * @param buffSize ======
     * @throws IOException ======
     */
    public Memman(String memFile, int numBuff, int buffSize)
            throws IOException {
        initpoolsize = buffSize;
        bufpool = new Bufferpool(memFile, numBuff, buffSize);
        currpoolsize = buffSize;
        freeblocklist = new DoublyLinkedList<Freeblock>();
        freeblocklist.insert(new Freeblock(0, buffSize));
    }

    /**
     * insert data to the memory pool
     * 
     * @param space
     *            - data to store in the memory pool
     * @param size
     *            - size of the data
     * 
     * @return Handle - handle for the client to retrieve data
     * @throws IOException
     */
    public Handle insert(byte[] space, int size) throws IOException {
        size += 2;
        byte[] record = new byte[size];
        ByteBuffer buf = ByteBuffer.allocate(2);
        buf.putShort((short) (size - 2));
        System.arraycopy(buf.array(), 0, record, 0, 2);
        System.arraycopy(space, 0, record, 2, size - 2);
        int slot = bestFit(record, size);
        while (slot == -1) {
            expand();
            slot = bestFit(record, size);
        }
        bufpool.write(slot, record);
        // System.arraycopy(record, 0, memorypool, slot, record.length);
        return new Handle(slot);
    }

    /**
     * Get data from memory pool
     * 
     * @param handle
     *            - Handle of that memory block
     * 
     * @return byte[] of the data
     * @throws IOException
     */
    public byte[] getRecord(Handle handle) throws IOException {
        // int recordlength = byte2Int(memorypool[handle.pos()],
        // memorypool[handle.pos() + 1]);
        // byte[] record = new byte[recordlength - 2];
        // System.arraycopy(memorypool, handle.pos() + 2, record, 0,
        // recordlength - 2);
        // return record;
        int recordlength = byte2Int(bufpool.readByte(
                handle.pos()), bufpool.readByte(handle.pos() + 1));
        byte[] record = new byte[recordlength];
        for (int i = 0; i < recordlength; i++) {
            record[i] = bufpool.readByte(handle.pos() + i + 2);
        }
        return record;
    }

    /**
     * remove the data from the memory pool
     * 
     * @param handle
     *            - Handle of the data to remove
     * @throws IOException
     */
    public void remove(Handle handle) throws IOException {
        int recordlength = byte2Int(bufpool.readByte(handle.
                pos()), bufpool.readByte(handle.pos() + 1)) + 2;
        if (freeblocklist.getLength() == 0) {
            freeblocklist.moveToTail();
            freeblocklist.insert(new Freeblock(handle.pos(), 
                    recordlength));
        }
        else {
            freeblocklist.moveToHead();
            while (freeblocklist.hasNext()) {
                freeblocklist.next();
                if (freeblocklist.getData().index > handle.pos()) {
                    freeblocklist.insert(new Freeblock(
                            handle.pos(), recordlength));
                    break;
                }
                if (!freeblocklist.hasNext()) {
                    freeblocklist.moveToTail();
                    freeblocklist.insert(new Freeblock
                            (handle.pos(), recordlength));
                }
            }
            mergeFreeblock();
        }
    }

    /**
     * print out the Freeblock list
     * 
     */
    public void print() {
        if (freeblocklist.getLength() == 0) {
            System.out.printf("(%d,0)", currpoolsize);
        }
        else {
            freeblocklist.moveToHead();
            while (freeblocklist.hasNext()) {
                freeblocklist.next();
                System.out.printf("(%d,%d)", freeblocklist.
                        getData().index, freeblocklist.getData().length);
                if (freeblocklist.hasNext()) {
                    System.out.print(" -> ");
                }
            }
        }
        System.out.println();
    }
    /**
     * flush memory
     * @throws IOException ======
     */
    public void flush() throws IOException {
        bufpool.flush();
    }

}