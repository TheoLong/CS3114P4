import student.TestCase;

/**
 * Testing Command Parser
 * 
 * @author Xianze
 * 
 * @version 9/5/2016
 * 
 *
 */
public class CommandParserTest extends TestCase 
{

    /**
     * declare cp
     */
    CommandParser cp;


    @Override
    public void setUp() throws Exception 
    {
        cp = new CommandParser("P1sampleInputTest.txt");
    }

    /**
     * testing excption
     * 
     */
    public void testException() 
    {
        new CommandParser("nonexistingfile");
        assertTrue(systemOut().getHistory().contains("File not found."));
    }
//  /**
//  * test nextCommand() method
//  */
// public void testnextCommand() 
// {
//     String[] arguments = cp.nextCommand();
//     assertEquals("remove", arguments[0]);
//     assertEquals("song", arguments[1]);
//     assertEquals("When Summer's Through", arguments[2]);
//     assertEquals(3, arguments.length);
//     arguments = cp.nextCommand();
//     assertEquals("print", arguments[0]);
//     assertEquals("blocks", arguments[1]);
//     assertEquals(2, arguments.length);
//     arguments = cp.nextCommand();
//     assertEquals("song", arguments[1]);
//     arguments = cp.nextCommand();
//     assertEquals(null, arguments);
//     cp.nextCommand();
//     arguments = cp.nextCommand();
//     assertEquals(3, arguments.length);
//     assertEquals("insert", arguments[0]);
//     assertEquals("Blind Lemon Jefferson", arguments[1]);
//     assertEquals("Long Lonesome Blues", arguments[2]);
//     while (arguments != null) {
//         for (int i = 0; i < arguments.length; i++) {
//             System.out.print(arguments[i] + " ");
//         }
//         System.out.println();
//         arguments = cp.nextCommand();
//     }
// }
}


