
/**
 * @author Xianze
 * 
 * @version 9/5/2016
 *
 */
public class Processor {
    private Memman manager;
    private Hash artisttable;
    private Hash songtable;
    private CommandParser parser;

    /**
     * Create a Processor object
     * 
     * @param m
     *            memory manager that stores the data
     * @param a
     *            artist hash table
     * @param s
     *            song hash table
     * @param p
     *            command parser
     * @param t
     *            2-3+ Tree
     */
    public Processor(Memman m, Hash a, Hash s, CommandParser p) {
        manager = m;
        artisttable = a;
        songtable = s;
        parser = p;
    }

    /**
     * Insert an artist and a song to the hashtable
     * 
     * @param artist
     *            - artist name
     * @param song
     *            - song name
     */
    private void insert(String artist, String song) {
        Handle temp1 = artisttable.insert(artist, manager);
        Handle temp2 = songtable.insert(song, manager);
        if (temp1 != null) {
            System.out.printf("|%s| is added to the artist database.", artist);
        }
        if (temp2 != null) {
            System.out.printf("|%s| is added to the song database.", song);
        }
    }

    /**
     * Remove a record from corresponding hashtable
     * 
     * @param location
     *            - name of the target hash table
     * @param name
     *            - name of the record
     */
    private void remove(String location, String name) {
        if (location.equals("artist")) {
            if (artisttable.remove(name, manager)) {
                System.out.printf("|%s| is removed from the artist database.\n", name);
            }
            else {
                System.out.printf("|%s| does not exist in the artist database.\n", name);
            }
        }
        else if (songtable.remove(name, manager)) {
            System.out.printf("|%s| is removed from the song database.\n", name);
        }
        else {
            System.out.printf("|%s| does not exist in the song database.\n", name);
        }
    }

    /**
     * Print stuff out
     * 
     * @param target
     *            - the target of the print command (artists/songs/blocks)
     */
    private void print(String target) {
        if (target.equals("artist")) {
            artisttable.print(manager);
            System.out.printf("total artists: %d\n", artisttable.getSize());
        }
        else if (target.equals("song")) {
            songtable.print(manager);
            System.out.printf("total songs: %d\n", songtable.getSize());
        }
        else {
            manager.print();
        }
    }

    /**
     * Process the command file
     * 
     * @return - true on success - false on failure
     */
    public boolean process() {
        String[] argument = parser.nextCommand();
        if (argument != null) {
            if (argument[0].equals("insert")) {
                insert(argument[1], argument[2]);
            }
            else if (argument[0].equals("remove")) {
                remove(argument[1], argument[2]);
            }
            else {
                print(argument[1]);
            }
            return true;
        }
        else {
            return false;
        }
    }
}
