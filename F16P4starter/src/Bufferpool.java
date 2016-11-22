
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * Buffer pool for project 3 This class manages all the buffers
 * 
 * @author Xianze
 * @version 10/23/2016
 */
public class Bufferpool {

    private RandomAccessFile infile;
    private int maxbuff;
    private static int bufsize;
    private int size;
    private Buffer[] pool;

    /**
     * Number of cache hits
     */
    public int cachehits = 0;

    /**
     * Number of disk hits
     */
    public int diskhits = 0;

    /**
     * The constructor, it opens the data file, 
     * defines the maximum number of buffers and the size of the buffer
     * 
     * @param filename
     *       - file on the disk managed by the memory manager/buffer pool
     * @param mb
     *       - maximum block number defined by the user
     * @param bufSize
     *       - buffer size defined by the user
     * @throws IOException
     * 
     */
    public Bufferpool(String filename, int mb, int bufSize) throws IOException {
        infile = new RandomAccessFile(filename, "rw");
        maxbuff = mb;
        size = 0;
        pool = new Buffer[mb];
        bufsize = bufSize;
    }

    /**
     * This function shifts the buffers in the bufferpool to the right by 1
     */
    private void shiftPool() {
        for (int i = (size == maxbuff) ? (size - 1) : size; i > 0; i--) {
            pool[i] = pool[i - 1];
        }
    }

    /**
     * This function put the ith buffer to the beginning
     * 
     * @param i
     *            - buffer to be moved to the top
     */
    private void shiftPool(int i) {
        Buffer temp = pool[i];
        for (int j = i; j > 0; j--) {
            pool[j] = pool[j - 1];
        }
        pool[0] = temp;
    }

    /**
     * This function flushes the buffer and write the content to the disk file
     * if the buffer is dirty
     * 
     * @param buf
     *            - the buffer to be flushed
     * @throws IOException
     * 
     */
    private void flushBuffer(Buffer buf) throws IOException {
        if (buf.isDirty()) {
            infile.seek(buf.getBlockID() * bufsize);
            infile.write(buf.dumpData());
            diskhits++;
        }

    }

    /**
     * This function loads a buffer into the buffer pool
     * 
     * @param i
     *            Load the ith segment of the file into the buffer pool
     * @throws IOException
     *             ======
     */
    private void loadBuffer(int i) throws IOException {
        byte[] temp = new byte[bufsize];
        infile.seek(i * bufsize);
        infile.read(temp);
        if (size < maxbuff) {
            shiftPool();
            size++;
        }
        else {
            flushBuffer(pool[size - 1]);
            shiftPool();
        }
        pool[0] = new Buffer(i, bufsize);
        pool[0].loadData(temp);
    }

    /**
     * This function would search for a buffer with a certain segment of the
     * file
     * 
     * @param i
     *            - the ith segment of the file
     * @return - the index of the buffer when found -1 when not found
     */
    private int findBlock(int i) {
        for (int j = 0; j < size; j++) {
            if (pool[j].getBlockID() == i) {
                return j;
            }
        }
        return -1;
    }


    /**
     * This function reads a byte from the buffer
     * 
     * @param index
     *            - index of the byte to read
     * @return - byte on that index
     * @throws IOException
     *             ======
     */
    public byte readByte(int index) throws IOException {
        int i = findBlock(index / bufsize);
        if (i > -1) {
            shiftPool(i);
            cachehits++;
        }
        else {
            loadBuffer(index / bufsize);
            diskhits++;
        }
        return pool[0].getByte(index % bufsize);
    }

    /**
     * This function reads in a byte array from the buffer
     * 
     * @param index
     *            - the starting index
     * @param length
     *            - the buffer length
     * @return - the byte array read from the buffer
     * @throws IOException
     */
    private byte[] readHelp(int index, int length) throws IOException {
        int i = findBlock(index / bufsize);
        if (i > -1) {
            shiftPool(i);
            cachehits++;
        }
        else {
            loadBuffer(index / bufsize);
            diskhits++;
        }
        return pool[0].getData(index % bufsize, length);
    }

    /**
     * This function reads in a record that may be be stored across several
     * buffers
     * 
     * @param index
     *            - starting index
     * @param length
     *            - length of the record
     * @return - record in raw bytes
     * @throws IOException
     *             ======
     */
    public byte[] read(int index, short length) throws IOException {
        byte[] result = new byte[length];
        int bytesread = 0;
        int len = length;
        while (bytesread < len) {

            System.arraycopy(readHelp(index, length), 0, result, bytesread,
                    (bufsize - (index % bufsize) > length) ? length :
                        bufsize - (index % bufsize));
            bytesread += (bufsize - (index % bufsize));
            length -= (bufsize - (index % bufsize));
            index += (bufsize - (index % bufsize));
        }
        return result;
    }

    /**
     * This function writes a byte array into the buffer
     * 
     * @param index
     *            - index of the destination location
     * @param b
     *            - byte array to be written
     * @throws IOException
     *             ======
     */
    public void write(int index, byte[] b) throws IOException {
        int i = findBlock(index / bufsize);
        if (i > -1) {
            shiftPool(i);
            cachehits++;
        }
        else {
            loadBuffer(index / bufsize);
            diskhits++;
        }
        pool[0].setData(b, index % bufsize);
        if (bufsize - (index % bufsize) < b.length) {
            byte[] temp = new byte[b.length - (bufsize - (index % bufsize))];
            System.arraycopy(b, bufsize - (index % bufsize),
                    temp, 0, temp.length);
            index += (bufsize - (index % bufsize));
            write(index, temp);
        }
    }

    /**
     * This function flushes all buffers to the disk
     * 
     * @throws IOException
     *             ======
     */
    public void flush() throws IOException {
        for (int i = 0; i < size; i++) {
            flushBuffer(pool[i]);
        }
    }
}
