import java.util.ArrayList;

import student.TestCase;

public class GraphTest extends TestCase
{
    
    private Graph g;
    
    public void setUp()
    {
        g = new Graph(5);
    }
    public void testInsertion()
    {
        int numOfHandles = 9;
        ArrayList<Handle> handles = new ArrayList<Handle>();
        for (int i =0; i < numOfHandles; i++)
        {
            handles.add(new Handle(i));
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
    public void testGetDiameter()
    {
        g.printGraph();
        int numOfHandles = 9;
        ArrayList<Handle> handles = new ArrayList<Handle>();
        for (int i =0; i < numOfHandles; i++)
        {
            handles.add(new Handle(i));
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
