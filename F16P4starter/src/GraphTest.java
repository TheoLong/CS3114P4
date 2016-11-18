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
        
        g.addEdge(handles.get(1), handles.get(4));
        g.addEdge(handles.get(2), handles.get(4));
        g.addEdge(handles.get(3), handles.get(4));
        g.addEdge(handles.get(3), handles.get(2));
        g.addEdge(handles.get(7), handles.get(4));
        g.addEdge(handles.get(7), handles.get(8));
        
        g.addEdge(handles.get(5), handles.get(6));
    
        g.printList();
        g.printGraph();
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
}
