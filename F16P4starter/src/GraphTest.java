import java.util.ArrayList;

import student.TestCase;

public class GraphTest extends TestCase
{
    
    Graph g;
    public void testConstructor()
    {
        
    }
    public void testInsertion()
    {
        g = new Graph(5);
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
        //System.out.println( g.delete(h2));
        //g.printGraph();
        
        
        g.printGraph();
    }
}
