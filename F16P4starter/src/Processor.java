import java.io.IOException;

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
    private Graph map;

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
     */
    public Processor(Memman m, Hash a, Hash s, CommandParser p) {
        manager = m;
        artisttable = a;
        songtable = s;
        parser = p;
        map = new Graph(5);
    }

    /**
     * Insert an artist and a song to the hashtable
     * 
     * @param artist
     *            - artist name
     * @param song
     *            - song name
     * @throws IOException
     */
    private void insert(String artist, String song) throws IOException {

        Handle artistH = artisttable.insert(artist, manager);
        Handle songH = songtable.insert(song, manager);
        map.insertVertex(artistH);
        map.insertVertex(songH);
        map.addEdge(artistH, songH);
    }

    /**
     * Remove a record from corresponding hashtable
     * 
     * @param location
     *            - name of the target hash table
     * @param name
     *            - name of the record
     * @throws IOException
     */
    private void remove(String location, String name) throws IOException {
        if (location.equals("artist")) {
            Handle artistH = artisttable.getHandle(name, manager);
            if (artistH != null) {
                // toRemove = song needs to be removed along this operation
                // List<Handle> toRemove = map.delete(artistH);
                map.delete(artistH);
                artisttable.remove(name, manager);
                System.out.printf("|%s| is removed " + 
                    "from the artist database.\n", name);
            }

            else {
                System.out.printf("|%s| does not" + 
                        " exist in the artist database.\n", name);
            }
        }
        else if (songtable.getHandle(name, manager) != null) {
            // toRemove = artist needs to be removed along this operation
            map.delete(songtable.getHandle(name, manager));
            songtable.remove(name, manager);
            System.out.printf("|%s| is removed from " + 
                    "the song database.\n", name);

        }
        else {
            System.out.printf("|%s| does not " + 
                    "exist in the song database.\n", name);
        }
    }

    /**
     * Print stuff out
     * 
     * @param target
     *            - the target of the print command (artists/songs/blocks)
     * @throws IOException
     */
    private void print(String target) throws IOException {
        if (target.equals("artist")) {
            artisttable.print(manager);
            System.out.printf("total artists: %d\n", artisttable.getSize());
        }
        else if (target.equals("song")) {
            songtable.print(manager);
            System.out.printf("total songs: %d\n", songtable.getSize());
        }
        else if (target.equals("graph")) {
            map.printGraph();
        }
        else {
            manager.print();
        }
    }

    /**
     * Process the command file
     * 
     * @return - true on success - false on failure
     * @throws IOException
     */
    public boolean process() throws IOException {
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
