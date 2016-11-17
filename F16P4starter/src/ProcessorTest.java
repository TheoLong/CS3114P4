import student.TestCase;

/**
 * @author Xianze Meng
 * 
 * @version 9/5/2016
 *
 */
public class ProcessorTest extends TestCase {

    private Processor cprocessor;

    @Override
    public void setUp() throws Exception {
        Hash artists = new Hash(10, "Artist");
        Hash songs = new Hash(10, "Song");
        Memman manager = new Memman(32);
        CommandParser cparser = new CommandParser(
                "ProcessorTest.txt");
        cprocessor = new Processor(manager, artists, songs, cparser);

    }

    /**
     * test the process method from the processor class
     */
    public void test() {
        while (true) {
            if (!cprocessor.process()) {
                break;
            }
        }
        assertTrue(systemOut().getHistory().contains(
                "|Blind Lemon Jefferson| is added to the artist database.\n"));
        assertTrue(systemOut().getHistory().contains(
                "|Ma Rainey's Black Bottom| duplicates a record already in "
                        + "the song database.\n"));
        assertTrue(systemOut().getHistory().contains(
                "|Long Lonesome Blues| is added to the song database.\n"));
        assertTrue(systemOut().getHistory().contains(
                "|Ma Rainey's Black Bottom| duplicates a record already "
                        + "in the song database.\n"));
        assertTrue(systemOut().getHistory().contains(
                "|Ma Rainey| is removed from the artist database.\n"));
        assertTrue(systemOut().getHistory().contains(
                "|Mississippi Boweavil Blues| is removed from "
                        + "the song database.\n"));
    }

}
