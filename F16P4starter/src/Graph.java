import java.util.*;
import java.math.*;
/**
 * Graph class
 * @author Theo Long
 * this graph class uses hash to place vertex in list 
 */
public class Graph 
{
    /**
     * Adjacency list
     */
    private Node []AJlist;
    /**
     * size of the list
     */
    private int size;
    /**
     * current vertex count
     */
    private int count;
    /**
     * A sub class for Adjacency node lists
     * @author Theo Long
     */
    private class Node 
    {
        /**
         * previous pointer
         */
        Node previous;
        /**
         * next pointer
         */
        Node next;
        /**
         * index, used to hold handle position
         */
        int index;
        /**
         * if this is visited
         */
        boolean visited;
        /**
         * distance. This is only for
         * shortest path calculation
         */
        int distance;
        /**
         * default constructor
         * @param i initial index
         */
        public Node(int i) 
        {
            previous = null;
            next = null;
            index = i;
            visited = false;
            distance = Integer.MAX_VALUE;
        }
    }
    /**
     * =============    constructor     =============
     * @param size initial adjacency list size
     * 
     * I strongly recommend using prime number as
     * initial size, as this will reduce chance of colission
     */
    public Graph (int size)
    {
        AJlist = new Node[size];
        count = 0;
        this.size = size;    
    }
    /**
     * =============   insertVertex    ===============
     * @param h handle to insert as vertex
     * @return slot number
     * 
     * This will insert handle into the list.
     * it will return the slot that it inserted into
     * -1 will be returned if insertion failed;
     * failure typically due to duplication
     */
    public int insertVertex(Handle h)
    {
        int handle = h.thePos;
        if (this.search(handle) == -1)
        {
            //not duplicating
            count++;
            if (count > (size / 2))
            {
                //need to double the size
                expand();
            }
            //all clear, insert content
            return insert(handle, AJlist);
        }
        //return -1 if duplicated
        else
        {
            return -1;
        }
    }
    /**
     * =============    addEdge     ==============
     * @param x first vertex
     * @param y second vertex
     * @return true false
     * 
     * this function will add connection between two 
     * vertex. it will return false if one of the vertex
     * does not exist
     */
    //assume vertex does exist
    public boolean addEdge(Handle x, Handle y)
    {
        int slotX = search(x.thePos);
        int slotY = search(y.thePos);
        if(slotX >= 0 && slotY >=0)
        {
            addListNode(slotX, new Node(y.thePos));
            addListNode(slotY, new Node(x.thePos));
            return true;
        }
        else
        {
            return false;
        }
    }
    /**
     * =============    delete      =================
     * @param h handle to delete
     * @return other necessary handle to delete
     * 
     * this function will return list of handles that
     * must be delete along this operation.
     */
    public List<Handle> delete(Handle h)
    {
        List<Handle> toRemove = new ArrayList<Handle>();
        int subIndex;
        int index =search(h.thePos);
        Node checker = AJlist[index];
        Node subChecker;
        checker = checker.next;
        //check each element in the list
        while(checker != null)
        {
            subIndex = search(checker.index);
            subChecker = AJlist[subIndex].next;
            //need to remove the list
            if (subChecker.next == null)
            {
                //this list only have 1 adjacent. remove it.
                toRemove.add(new Handle(subChecker.previous.index));
                AJlist[subIndex].index = -1;
                AJlist[subIndex].next = null;
            }
            //list stays, but element needs to be gone
            else
            {
                while(subChecker != null && subChecker.index != AJlist[index].index)
                {
                    subChecker = subChecker.next;
                }
                //remove it from the list
                subChecker.previous.next = subChecker.next;
            }
            checker = checker.next;
        }
        toRemove.add(new Handle (AJlist[index].index));
        AJlist[index].index = -1;
        AJlist[index].next = null;       
        return toRemove;
    }
    /**
     * =============       print list      =================
     * print adjacency list visually for debugging purpose
     */
    public void printList()
    {
        for (int i =0; i < size; i++)
        {
            if (AJlist[i] != null && AJlist[i].index >=0)
            {
                Node current = AJlist[i];
                while(current != null)
                {
                    System.out.print(current.index);
                    System.out.print("->");
                    current = current.next;
                }
                System.out.println("|");
            }
        }
    }
    //===================================================
    //=============     private helper      =============
    //===================================================
    /**
     * =============    Hash function   ================
     * @param content things to hash
     * @param Hsize size to hash
     * @return slot number
     * Just like a normal hash function using mod
     */
    private int hash(int content, int Hsize)
    {
        return content / Hsize;
    }
    /**
     * =============    addListNode     ================
     * @param slot to add
     * @param node to add
     * add a node to the adjacency list
     */
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
    /**
     *===========  base insert function  ============
     * This function handles inserting content.
     * @param content to insert
     * @param target Node list
     * 
     * This is a private insert function. It takes the 
     * content trying to insert, and the target array trying to insert.
     * 1. first call hash(content, length) to hash a key number
     *         if the hashTable is empty, add in directly
     * 2. else do quadratic stepping
     */
    private int insert(int content, Node [] target)
    {
      //initialize a slot
        int slot = hash(content, target.length);
        
        
        //if this is unused slot, place directly in
        if (target[slot] == null || target[slot].index < 0 )
        {
            target[slot] = new Node(content);
            return slot;
        }
        //else, collision happened, find next slot
        else 
        {
            int step = 1;
            int nextSlot = slot + step * step;
            if (nextSlot >= target.length)
            {
                nextSlot = nextSlot % target.length;
            }
            // if I don't see a null, or a tomb stone, keep stepping
            while (true)
            {
                if (target[nextSlot] == null)
                {
                    break;
                }
                else if (target[nextSlot].index == -1)
                {
                    break;
                }
                step++;
                nextSlot = slot + step * step;
                if (nextSlot >= target.length)
                {
                    nextSlot = nextSlot % target.length;
                }
            }
            
            // loop was broken, I either saw a tomb stone or null
            //doesn't matter if it is tomb stone or a null, just place it there
            target[nextSlot] = new Node(content);
            return nextSlot;
        }
    }
    /**
     * ===========  search a content   ============
     * @param target things you want to search
     * @return slot number
     * 
     * find a content in the list. return the slot number
     * return -1 if it doesn't exist
     */
    private int search(int target)
    {
        //rehash the key
        int slot = hash(target , size);
        //check the first result   
           //if the place is empty, return -1
        if (AJlist[slot] == null)
        {
            return -1;
        }
        //found, return key
        if (AJlist[slot].index == target)
        {
            //check information
            return slot;
        }
        int step = 1;
        int nextSlot = slot + step * step;
        if (nextSlot >= size)
        {
            nextSlot = nextSlot % size;
        }
        // ignore tomb stone. Only stop when it sees a null
        while (AJlist[nextSlot] != null)
        {
            //if something is there. compare. break out if found target
            if (AJlist[nextSlot].index == target)
            {
                break;
            }
            //quadratic stepping
            step++;
            nextSlot = slot + step * step;
            if (nextSlot >= size)
            {
                nextSlot = nextSlot % size;
            }
        }
        // loop is broken. either target is found or null is found
        if (AJlist[nextSlot] == null)
        {
            return -1;
        }
        else
        {
            return nextSlot;
        }
        
    }
    /**
     *==============     expand  ==================
     * This function expand the list
     * expand to list to next available prime number of size*2 
     */
    private void expand()
    {
        //get the prime number
        BigInteger b = new BigInteger(String.valueOf(size*2));
        int newSize = (int)Long.parseLong(b.nextProbablePrime().toString());
        //temp list with new size
        Node [] tempList = new Node[newSize];
        //copy.
        for (int i = 0; i < size; i++)
        {
            //ignore null and tomb stone
            if (AJlist[i] != null && AJlist[i].index >= 0)
            {
                reinsert(AJlist[i], tempList);
            }
        }
        //reinsert finished
        size = newSize;
        AJlist = tempList;
    }
    /**
     * ===========  reinsert for doubling size  ============
     * @param Node list to reinsert
     * @param target list you are trying to insert
     * @return slot number
     */
    private int reinsert(Node node, Node [] target)
    {
        //retrieve the content
        int content = node.index;
        //retrieve
        int slot = hash(content, target.length);
 
        //if this is unused slot, place directly in
        if (target[slot] == null)
        {
            target[slot] = node;
            return slot;
        }
        
        //else, collision happened, find next slot
        else 
        {
            int step = 1;
            int nextSlot = slot + step * step;
            if (nextSlot >= target.length)
            {
                nextSlot = nextSlot % target.length;
            }
            // if I don't see a null, or a tomb stone, keep stepping
            while (target[nextSlot] != null)
            {   
                step++;
                nextSlot = slot + step * step;
                if (nextSlot >= target.length)
                {
                    nextSlot = nextSlot % target.length;
                }
            }
            // loop was broken, I either saw a null
            target[nextSlot] = node;
            return nextSlot;
        }      
    }
    /**
     * =============    print graph stats   ================
     * @return
     */
    public void printGraph()
    {
        //mark all vertexes unvisited;
        for (int i = 0; i < size; i++)
        {
            if (AJlist[i] !=null)
            {
                AJlist[i].visited = false;
            }
        }
        //BFS
        int connectedCompNum = 0;
        int maxCompNum = 0;
        Node maxCompNode;
        for (int i = 0; i < size; i++)
        {
            if (AJlist[i] !=null && AJlist[i].index >= 0 && AJlist[i].visited == false)
            {
                //this is a new component. 
                connectedCompNum++;
                int newMaxComp = BFvisit(AJlist[i]);
                if (newMaxComp > maxCompNum)
                {
                    maxCompNum = newMaxComp;
                    maxCompNode = AJlist[i];                        
                }
            }
        }
        System.out.println("There are " + connectedCompNum + " connected components");
        System.out.println("The largest connected component has " + maxCompNum + " elements"); 
        System.out.println("The diameter of the largest component is " + 0);
    }
    /**
     * ================   Visit nodes     =================
     * @param root to start visit
     * @return components in this group
     * 
     * THis will do a visit of current group 
     * and report the number of components in current group
     */
    private int BFvisit(Node root)
    {
        int numOfComp = 1;
        AJlist[search(root.index)].visited = true;
        List <Node> queue = new ArrayList<Node>();
        List <Node> allNode = new ArrayList<Node>();
        queue.add(root);
        allNode.add(root);
        //when the queue is empty, visit finished
        while (!queue.isEmpty())
        {
            int marker = queue.get(0).index;
            queue.remove(0);
            Node listChecker = AJlist[search(marker)].next;
            //checkout all adjacent component from this root
            while(listChecker != null)
            {
                int NodeChecker = search(listChecker.index);
                if (AJlist[NodeChecker].visited == false)
                {
                    //if it is not visited, add to numOfComp
                    numOfComp++;
                    AJlist[NodeChecker].visited =true;
                    queue.add(AJlist[NodeChecker]);
                    allNode.add(AJlist[NodeChecker]);
                }
                //check next 
                listChecker = listChecker.next;
            }
            
        }
        return numOfComp;
    }
    
    private List <Node>  Dijkstra(List <Node> allNode, int numOfComp) 
    {
        //make every element 0
        int checker;
        for (int i=0; i < numOfComp; i++)
        {
            checker = search(allNode.get(i).index);
            AJlist[checker].distance = Integer.MAX_VALUE;
            AJlist[checker].visited = false;
            checker = allNode.get(0).index;
            AJlist[search(checker)].distance = 0;
            /*
            for (int i=0; i<numOfComp; i++) 
            {  
                checker = search(allNode.get(i).index);
                for (Node subChecker = AJlist[checker].next; subChecker != null; subChecker = subChecker.next)
                {
                    subChecker.index
                }
            }
            */
        }
        
        
        return null;
      }
}
