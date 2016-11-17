
/**
 * Hash table for project 1
 *
 * @author Xianze Meng
 * 
 * @version August 4, 2016
 */

public class Hash {
    private Handle[] hashtable;
    private int size;
    private int currentsize;
    private String content;

    /**
     * Create a hashtable object
     * 
     * @param s
     *            - initial size of the hashtable
     * @param c
     *            - type of data in this hashtable
     */
    public Hash(int s, String c) {
        hashtable = new Handle[s];
        size = s;
        content = c;
        currentsize = 0;
    }

    /**
     * get byte[] from a string and push 2 bytes to the from of the array to
     * represent the length of the entire byte[]
     * 
     * @param s
     *            - string to get byte
     * @return - byte array
     */
    private byte[] getBytes(String s) {
        return s.getBytes();
    }

    /**
     * insert a record to the hashtable
     * 
     * @param record
     *            - item to be inserted
     * 
     * @param manager
     *            - associated memory manager to store the data
     * 
     * @return - true on success - false on failure
     */
    public Handle insert(String record, Memman manager) {
        int i = search(record, h(record, size), manager);
        if (i != -1) {
            System.out.printf(
                    "|%s| duplicates a record already in the %s database.\n",
                    record, content);
            return null;
        }
        else {
            if ((2 * currentsize + 2) > size) {
                expand(manager);
            }
            int index = quadraticProbing(hashtable, record,
                    h(record, size));
            hashtable[index] = manager.insert(getBytes(record),
                    getBytes(record).length);
            currentsize++;
            System.out.printf("|%s| is added to the %s database.\n",
                    record, content);
            return hashtable[index];
        }
    }

    /**
     * Convert the raw bytes from memory manager back to string
     * 
     * @param handle
     *            - Handle for the string
     * @param manager
     *            - associated memory manager
     * @return
     */
    private String getRecord(Handle handle, Memman manager) {
        return new String(manager.getRecord(handle));
    }

    /**
     * Double the size of this hash table
     * 
     * @param manager
     *            - associated memory manager
     */
    private void expand(Memman manager) {
        size *= 2;
        Handle[] temp = new Handle[size];
        for (int i = 0; i < size / 2; i++) {
            if ((hashtable[i] != null)
                    && (hashtable[i].pos() != -1)) {
                String s = getRecord(hashtable[i], manager);
                temp[quadraticProbing(temp, s,
                        h(s, size))] = hashtable[i];
            }
        }
        hashtable = temp;
        System.out.printf("%s hash table size doubled.\n",
                (content.equals("artist") ? "Artist" : "Song"));
    }

    /**
     * print the content of the hash table
     * 
     * @param manager
     *            - associated memory manager to retrieve data
     * 
     */
    public void print(Memman manager) {
        for (int i = 0; i < size; i++) {
            if (hashtable[i] != null && hashtable[i].pos() != -1) {
                System.out.printf("|%s| %d\n",
                        getRecord(hashtable[i], manager), i);
            }
        }

    }

    /**
     * Get the current size of hash table
     * 
     * @return - int size of hash table
     */
    public int getSize() {
        return this.currentsize;
    }

    /**
     * remove a record from this hash table
     * 
     * @param record
     *            - record to be removed
     * @param manager
     *            - associated memory manager
     * 
     * @return - true on success - false on failure
     */
    public boolean remove(String record, Memman manager) {
        int index = search(record, h(record, size), manager);
        if (index == -1) {
            return false;
        }
        else {
            manager.remove(hashtable[index]);
            hashtable[index] = new Handle(-1);
            currentsize--;
            return true;
        }
    }

    /**
     * Get the handle for a record in the hash table
     * 
     * @param record
     *            record to search
     * 
     * @param manager
     *            associated Memman
     * 
     * @return the handle of the record null if not found
     */
    public Handle getHandle(String record, Memman manager) {
        int index = search(record, h(record, size), manager);
        if (index != -1) {
            return hashtable[index];
        }
        return null;
    }

    /**
     * Do QuadraticProbing in case of hashing collsion to find a right slot for
     * a record
     * 
     * @param hasht
     *            - on which hashtable to look perform this operation
     * @param record
     *            - the record to insert
     * @param base
     *            - the basic hash result of the string
     * 
     * @return - true position for the record in the memory manager
     */
    private int quadraticProbing(Handle[] hasht, String record,
            int base) {
        int pos = base;
        for (int i = 1; (hasht[pos] != null)
                && (hasht[pos].pos() != -1); i++) {
            pos = (base + (i * i)) % size;
        }
        return pos;
    }

    /**
     * Search the record in the hash table to see whether it's already in it
     * 
     * @param record
     *            - the key to be searched
     * 
     * @param base
     *            - the base slot of the key
     * @param manager
     *            - the associated memory manager to retrieve the data
     * @return - -1 when it doesn't find a duplicate record - pos of the record
     *         when a duplicate is found
     */
    private int search(String record, int base, Memman manager) {
        int pos = base;
        for (int i = 1;; i++) {
            if (hashtable[pos] == null) {
                pos = -1;
                break;
            }
            else if (hashtable[pos].pos() != -1
                    && getRecord(hashtable[pos], manager)
                            .equals(record)) {
                break;
            }
            pos = (base + (i * i)) % size;
        }
        return pos;
    }

    /**
     * Compute the hash function. Uses the "sfold" method from the OpenDSA
     * module on hash functions
     *
     * @param s
     *            The string that we are hashing
     * @param m
     *            The size of the hash table
     * @return The home slot for that string
     */
    // Make this private in your project.
    // This is private for distributing hash function in a way that will
    // pass milestone 1 without change.
    public int h(String s, int m) {
        int intLength = s.length() / 4;
        long sum = 0;
        for (int j = 0; j < intLength; j++) {
            char[] c = s.substring(j * 4, (j * 4) + 4).toCharArray();
            long mult = 1;
            for (int k = 0; k < c.length; k++) {
                sum += c[k] * mult;
                mult *= 256;
            }
        }

        char[] c = s.substring(intLength * 4).toCharArray();
        long mult = 1;
        for (int k = 0; k < c.length; k++) {
            sum += c[k] * mult;
            mult *= 256;
        }

        return (int) (Math.abs(sum) % m);
    }
}
