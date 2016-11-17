
public class Buffer {
    private boolean dirty;
    private byte[] data;
    private int blockid;

    public Buffer(int i, int size) {
        data = new byte[size];
        dirty = false;
        blockid = i;
    }

    public int getBlockID() {
        return blockid;
    }

    public boolean isDirty() {
        return dirty;
    }
    
    public byte getByte(int pos) {
        return data[pos];
    }

    public byte[] getData(int pos) {
        byte[] temp = new byte[4];
        System.arraycopy(data, pos, temp, 0, 4);
        return temp;
    }

    public void setData(byte[] b, int pos) {
        System.arraycopy(b, 0, data, pos, 
                (((pos + b.length) <= data.length) ? 
                        b.length : (data.length - pos)));
        dirty = true;
    }

    public void loadData(byte[] d) {
        data = d;
    }

    public byte[] dumpData() {
        return data;
    }

}
