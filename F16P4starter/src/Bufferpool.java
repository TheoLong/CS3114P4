
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * Buffer pool for project 3 This class manages all the buffers
 * 
 * @author Xianze
 * @version 10/23/2016
 */
public class Bufferpool {
    /**
     * ======
     */
    private RandomAccessFile infile;
    /**
     * ======
     */
    private int maxbuff;
    /**
     * ======
     */
    private static int bufsize;
    /**
     * ======
     */
    private int size;
    /*
     * ======
     */
    private Buffer[] pool;
    /**
     * ======
     */
    public int cachehits = 0;
    /**
     * ======
     */
    public int diskhits = 0;

    /**
     * ======
     * 
     * @param filename
     *            ======
     * @param mb
     *            ======
     * @param bufSize
     *            ======
     * @throws IOException
     *             ======
     */
    public Bufferpool(String filename, int mb, int bufSize) throws IOException {
        infile = new RandomAccessFile(filename, "rw");
        maxbuff = mb;
        size = 0;
        pool = new Buffer[mb];
        bufsize = bufSize;
    }

    /**
     * ======
     */
    private void shiftPool() {
        for (int i = (size == maxbuff) ? (size - 1) : size; i > 0; i--) {
            pool[i] = pool[i - 1];
        }
    }

    /**
     * ======
     * 
     * @param i
     *            ======
     */
    private void shiftPool(int i) {
        Buffer temp = pool[i];
        for (int j = i; j > 0; j--) {
            pool[j] = pool[j - 1];
        }
        pool[0] = temp;
    }

    /**
     * ======
     * 
     * @param buf
     *            ======
     * @throws IOException
     *             ======
     */
    private void flushBuffer(Buffer buf) throws IOException {
        if (buf.isDirty()) {
            infile.seek(buf.getBlockID() * bufsize);
            infile.write(buf.dumpData());
            diskhits++;
        }

    }

    /**
     * ======
     * 
     * @param i
     *            ======
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
     * ======
     * 
     * @param i
     *            ======
     * @return ======
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
     * ======
     * 
     * @return ======
     * @throws IOException
     *             ======
     */
    public int getArraySize() throws IOException {
        return ((int) infile.length()) / 4;
    }

    /**
     * ======
     * 
     * @param index
     *            ======
     * @return ======
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
     * ======
     * 
     * @param index
     *            ======
     * @return ======
     * @throws IOException
     *             ======
     */
    public byte[] read(int index, short length) throws IOException {
        byte[] result = new byte[length];
        int bytesread = 0;
        int len = length;
        while (bytesread < len) {

            System.arraycopy(readHelp(index, length), 0, result, bytesread,
                    (bufsize - (index % bufsize) > length) ? length : bufsize - (index % bufsize));
//            index += bytesread;
//            length -= bytesread;
            bytesread += (bufsize - (index % bufsize));
            length -= (bufsize - (index % bufsize));
            index += (bufsize - (index % bufsize));
        }
        return result;
    }

    /**
     * ======
     * 
     * @param index
     *            ======
     * @param b
     *            ======
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
            System.arraycopy(b, bufsize - (index % bufsize), temp, 0, temp.length);
            index += (bufsize - (index % bufsize));
            write(index, temp);
        }
    }

    /**
     * ======
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
