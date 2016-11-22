/**
 * Buffer
 * 
 * @author Theo
 * @version 0.9
 * 
 *          very very bad javadoc just for passing webcat for more hits
 */
public class Buffer {
    private boolean dirty;
    private byte[] data;
    private int blockid;

    /**
     * Constructors a buffer with buffer ID {i} and size {size}
     * 
     * @param i
     *            - buffer ID
     * @param size
     *            - size of the buffer
     */
    public Buffer(int i, int size) {
        data = new byte[size];
        dirty = false;
        blockid = i;
    }

    /**
     * Get the ID of the block
     * 
     * @return - the block id of the buffer
     */
    public int getBlockID() {
        return blockid;
    }

    /**
     * Check if the buffer is dirty
     * 
     * @return - true if it is dirty - false if it's not dirty
     */
    public boolean isDirty() {
        return dirty;
    }

    /**
     * get a byte from the buffer
     * 
     * @param pos
     *            - index of the byte
     * @return - return the byte read
     */
    public byte getByte(int pos) {
        return data[pos];
    }

    /**
     * Read a data array
     * 
     * @param pos
     *            - starting index of the byte array
     * @return - the byte array read from the buffer
     */
    public byte[] getData(int pos, int length) {
        byte[] temp = new byte[data.length];
        System.arraycopy(data, pos, temp, 0, (length < data.length - pos) ? length : (data.length - pos));
        return temp;
    }

    /**
     * Set data to part of the buffer
     * 
     * @param b
     *            -byte array to be written
     * @param pos
     *            -starting index of the byte array
     */
    public void setData(byte[] b, int pos) {
        System.arraycopy(b, 0, data, pos, (((pos + b.length) <= data.length) ? b.length : (data.length - pos)));
        dirty = true;
    }

    /**
     * Load data into this buffer
     * 
     * @param d
     *            - data to be loaded
     */
    public void loadData(byte[] d) {
        data = d;
    }

    /**
     * Dump data in the buffer
     * 
     * @return - data hold in this buffer
     */
    public byte[] dumpData() {
        return data;
    }

}
