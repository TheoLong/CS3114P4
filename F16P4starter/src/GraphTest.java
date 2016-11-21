import java.util.ArrayList;

import student.TestCase;
/**
 * This is a Graph tester
 * @author Theo
 * @version 0.9
 */
public class GraphTest extends TestCase
{
    /**
     * a graph for test
     */
    private Graph g;
    
    /**
     * setup for the test
     */
    public void setUp()
    {
        g = new Graph(5);
    }
    /**
     * test insertion
     */
    public void testInsertion()
    {
        int numOfHandles = 9;
        ArrayList<Handle> handles = new ArrayList<Handle>();
        for (int i = 0; i < numOfHandles; i++)
        {
            assertEquals(true , handles.add(new Handle(i)));
            g.insertVertex(new Handle(i));
        }
        
        g.delete(new Handle(0));
        assertEquals(g.insertVertex(null), -1);
        g.addEdge(handles.get(1), handles.get(4));
        g.addEdge(handles.get(2), handles.get(4));
        g.addEdge(handles.get(3), handles.get(4));
        g.addEdge(handles.get(3), handles.get(2));
        g.addEdge(handles.get(7), handles.get(4));
        g.addEdge(handles.get(7), handles.get(8));
        g.addEdge(handles.get(5), handles.get(6));
    }
    /**
     * test add edge between two vertex
     */
    public void testAddEdge()
    {
        int numOfHandles = 9;
        ArrayList<Handle> handles = new ArrayList<Handle>();
        for (int i = 0; i < numOfHandles; i++)
        {
            assertEquals(true , handles.add(new Handle(i)));
            g.insertVertex(new Handle(i));
        }
        
        g.delete(new Handle(0));
        assertEquals(g.insertVertex(null), -1);
        assertEquals(g.addEdge(handles.get(1), null), false);
        assertEquals(g.addEdge(null, handles.get(1)), false);
        assertEquals(g.addEdge(null, null), false);
        g.addEdge(handles.get(2), handles.get(4));
        g.addEdge(handles.get(3), handles.get(4));
        g.addEdge(handles.get(3), handles.get(2));
        g.addEdge(handles.get(7), handles.get(4));
        g.addEdge(handles.get(7), handles.get(8));
        
        g.addEdge(handles.get(5), handles.get(6));
    }
    /**
     * test get diameter function
     */
    public void testGetDiameter()
    {
        g.printGraph();
        int numOfHandles = 9;
        ArrayList<Handle> handles = new ArrayList<Handle>();
        for (int i = 0; i < numOfHandles; i++)
        {
            assertEquals(handles.add(new Handle(i)), true);
            g.insertVertex(new Handle(i));
            g.printGraph();
        }
        
        g.delete(new Handle(0));
        
        g.addEdge(handles.get(1), handles.get(2));
        g.addEdge(handles.get(1), handles.get(3));
        g.addEdge(handles.get(1), handles.get(4));
        g.addEdge(handles.get(1), handles.get(5));
        g.printList();
        g.printGraph();
    }
    /**
     * test deletion
     */
    public void testDelete()
    {
        int numOfHandles = 9;
        ArrayList<Handle> handles = new ArrayList<Handle>();
        for (int i = 0; i < numOfHandles; i++)
        {
            assertEquals(handles.add(new Handle(i)), true);
            g.insertVertex(new Handle(i));
            g.printGraph();
        }
        
        g.delete(new Handle(0));
        
        assertEquals(g.addEdge(handles.get(1), handles.get(2)),true);
        assertEquals(g.addEdge(handles.get(1), handles.get(3)),true);
        assertEquals(g.addEdge(handles.get(1), handles.get(4)),true);
        assertEquals(g.addEdge(handles.get(1), handles.get(5)),true);
        g.printList();
        g.printGraph();
        g.delete(new Handle(1));
        g.delete(new Handle(3));
        
    }
}
