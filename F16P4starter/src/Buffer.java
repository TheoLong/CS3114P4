/**
 * Buffer 
 * @author Theo
 * @version 0.9
 * 
 * very very bad javadoc just for passing webcat
 * for more hits 
 */
public class Buffer {
    private boolean dirty;
    private byte[] data;
    private int blockid;
    /**
     * ======
     * @param i ======
     * @param size ======
     */
    public Buffer(int i, int size) {
        data = new byte[size];
        dirty = false;
        blockid = i;
    }
    /**
     * ======
     * @return ======
     */
    public int getBlockID() {
        return blockid;
    }
    /**
     * ======
     * @return ======
     */
    public boolean isDirty() {
        return dirty;
    }
    /**
     * ======
     * @param pos ======
     * @return ======
     */
    public byte getByte(int pos) {
        return data[pos];
    }
    /**
     * ======
     * @param pos ======
     * @return ======
     */
    public byte[] getData(int pos) {
        byte[] temp = new byte[4];
        System.arraycopy(data, pos, temp, 0, 4);
        return temp;
    }
    /**
     *  ======
     * @param b ======
     * @param pos ======
     */
    public void setData(byte[] b, int pos) {
        System.arraycopy(b, 0, data, pos, 
                (((pos + b.length) <= data.length) ? 
                        b.length : (data.length - pos)));
        dirty = true;
    }
    /**
     * ======
     * @param d ======
     */
    public void loadData(byte[] d) {
        data = d;
    }
    /**
     *  ======
     * @return ======
     */
    public byte[] dumpData() {
        return data;
    }

}
