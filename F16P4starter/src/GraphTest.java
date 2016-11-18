import student.TestCase;

public class GraphTest extends TestCase
{
    
    Graph g;
    public void testConstructor()
    {
        
    }
    public void testInsertion()
    {
        g = new Graph(4);
        Handle h4 = new Handle(4);
        Handle h5 = new Handle(5);
        Handle h2 = new Handle(2);
        Handle h1 = new Handle(1);
        
        g.insertVertex(h4);
        g.insertVertex(h5);
        g.insertVertex(h2);
        g.insertVertex(h1);
        //g.insertVertex(new Handle(6));
        
        g.addEdge(h2, h1);
        g.addEdge(h4, h5);
        g.addEdge(h2, h5);
        
        g.printGraph();
        System.out.println( g.delete(h2));
        g.printGraph();
        
    }
}
