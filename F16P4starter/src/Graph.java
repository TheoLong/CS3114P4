
public class Graph 
{
    private Node []AJlist;
    private Handle [] LUT;
    private int size;
    private int count;
    
    private class Node 
    {
        Node previous;
        Node next;
        int index;
        public Node(int i) 
        {
            previous = null;
            next = null;
            index = i;
        }
    }
    
    public Graph (int size)
    {
        AJlist = new Node[size];
        LUT = new Handle[size];
        count = 0;
        this.size = size;
        
    }
    
    public boolean insertNode(Handle h)
    {
        int slot = availableSlot();
        if (slot < 0 )
        {
            return false;
        }
        else
        {
            LUT[slot] = h;
            return true;
        }
    }
    
    public boolean addEdge(Handle x, Handle y)
    {
        int slotX = findSlot(x);
        int slotY = findSlot(y);
        if(slotX >= 0 && slotY >=0)
        {
            addListNode(slotX, new Node(slotY));
            addListNode(slotY, new Node(slotX));
            return true;
        }
        else
        {
            return false;
        }
    }
    
    //=====================================
    private int availableSlot()
    {
        if (count == size)
        {
            expand();
        }
        for (int i = 0; i < size; i++)
        {
            if (LUT[i] == null)
            {
                return i;
            }
        }
        //error
        return -1;
    }
    private void expand()
    {
        //expand arrays by 2;
    }
    private void addListNode(int slot, Node node)
    {
        //empty list
       if ( AJlist[slot] == null)
       {
           AJlist[slot] = node;
       }
       else
       {
           Node current = AJlist[slot];
           //search for insertion position
           while(current.next != null && current != node)
           {
               current = current.next;
           }
           //Node does not exist
           if (current != node)
           {
               current.next = node;
               node.previous = current;
           }
       }
    }
    
    private int findSlot(Handle h)
    {
        for (int i = 0; i < size; i++)
        {
            if (LUT[i] == h)
            {
                return i;
            }
        }
        return -1;
    }
}
