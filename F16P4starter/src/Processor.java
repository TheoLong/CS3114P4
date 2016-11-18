import java.io.IOException;
import java.util.List;

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
     * @param t
     *            2-3+ Tree
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
//        Handle temp1 = artisttable.insert(artist, manager);
//        Handle temp2 = songtable.insert(song, manager);
//        if (temp1 != null) {
//            System.out.printf("|%s| is added to the artist database.", artist);
//        }
//        if (temp2 != null) {
//            System.out.printf("|%s| is added to the song database.", song);
//        }
        Handle artistH = artisttable.insert(artist, manager);
        Handle songH = songtable.insert(song, manager);
        map.insertVertex(artistH);
        map.insertVertex(songH);
        map.addEdge(artistH, songH);
        //map.printList();
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
        if (location.equals("artist")) 
        {   
            Handle artistH = artisttable.getHandle(name, manager);
            if (artistH != null) 
            {
                //toRemove = song needs to be removed along this operation
                List<Handle> toRemove = map.delete(artistH);
                artisttable.remove(name, manager);
                System.out.printf("|%s| is removed from the artist database.\n", name);
                //to remove songs
//                for (int i = 0; i < toRemove.size(); i++)
//                {
//                    String song = songtable.getRecord(toRemove.get(i), manager);
//                    songtable.remove(song, manager);
//                    System.out.printf("|%s| is removed from the song database.\n", song);
//                }
                
            }
            
            else 
            {
                System.out.printf("|%s| does not exist in the artist database.\n", name);
            }
        }
        else if (songtable.getHandle(name, manager) != null) 
        {
            //toRemove = artist needs to be removed along this operation
            List<Handle> toRemove = map.delete(songtable.getHandle(name, manager));
            songtable.remove(name, manager);
            System.out.printf("|%s| is removed from the song database.\n", name);
            //to remove artist
//            for (int i = 0; i < toRemove.size(); i++)
//            {
//                //do what every to remove this artist.
//                String artist = artisttable.getRecord(toRemove.get(i), manager);
//                artisttable.remove(artist, manager);
//                System.out.printf("|%s| is removed from the artist database.\n", artist);
//            }
        }
        else 
        {
            System.out.printf("|%s| does not exist in the song database.\n", name);
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
        else if (target.equals("graph"))
        {
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
