import java.util.*;
import java.math.*;
/**
 * Class for graph. uses hash function to insert
 * @author Theo
 * @version 1.1
 */
public class Graph 
{
    /**
     * Adjacency list
     */
    private Node []ajList;
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
    private int [] stats;
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
        int handleIndex;
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
            handleIndex = i;
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
    public Graph(int size)
    {
        ajList = new Node[size];
        count = 0;
        this.size = size;    
        stats = new int[3];
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
        
        if (h != null && this.search(h.thePos) == -1)
        {
            int handle = h.thePos;
            //not duplicating
            count++;
            if (count > (size / 2))
            {
                //need to double the size
                expand();
            }
            //all clear, insert content
            return insert(handle, ajList);
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
        if (x != null && y != null)
        {
            int slotX = search(x.thePos);
            int slotY = search(y.thePos);
            if (slotX >= 0 && slotY >= 0 && !isEdge(slotX, y.thePos))
            {
                //append to adjacent list
                addListNode(slotX, new Node(y.thePos));
                addListNode(slotY, new Node(x.thePos));
                return true;
            }
        }
        return false;
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
        //List<Handle> toRemove = new ArrayList<Handle>();
        int subIndex;
        int targetIndex = search(h.thePos);
        Node checker = ajList[targetIndex];
        Node subChecker;
        checker = checker.next;
        //check each element in the list
        while (checker != null)
        {
            subIndex = search(checker.handleIndex);
            subChecker = ajList[subIndex];
            //need to remove the list
            if (subChecker.next != null)
            {
                while (subChecker.next.handleIndex != h.thePos)
                {
                    subChecker = subChecker.next;
                }
                subChecker.next = subChecker.next.next;
            }
            checker = checker.next;
        }
        //toRemove.add(new Handle (AJlist[index].index));
        ajList[targetIndex] = new Node(-1);      
        return null;
    }
    /**
     * =============       print list      =================
     * print adjacency list visually for debugging purpose
     */
    public void printList()
    {
        for (int i = 0; i < size; i++)
        {
            if (ajList[i] != null && ajList[i].handleIndex >= 0)
            {
                Node current = ajList[i];
                while (current != null)
                {
                    System.out.print(current.handleIndex);
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
     * @param hashSize size to hash
     * @return slot number
     * Just like a normal hash function using mod
     */
    private int hash(int content, int hashSize)
    {
        return content % hashSize;
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
        if (ajList[slot] == null)
        {
            ajList[slot] = node;
        }
        else
        {
            Node current = ajList[slot];
            //search for insertion position
            while (current.next != null && current != node)
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
        if (target[slot] == null || target[slot].handleIndex < 0 )
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
                else if (target[nextSlot].handleIndex == -1)
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
        if (ajList[slot] == null)
        {
            return -1;
        }
        //found, return key
        if (ajList[slot].handleIndex == target)
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
        while (ajList[nextSlot] != null)
        {
            //if something is there. compare. break out if found target
            if (ajList[nextSlot].handleIndex == target)
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
        if (ajList[nextSlot] == null)
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
        BigInteger b = new BigInteger(String.valueOf(size * 2));
        int newSize = (int)Long.parseLong(b.nextProbablePrime().toString());
        //temp list with new size
        Node [] tempList = new Node[newSize];
        //copy.
        for (int i = 0; i < size; i++)
        {
            //ignore null and tomb stone
            if (ajList[i] != null && ajList[i].handleIndex >= 0)
            {
                reinsert(ajList[i], tempList);
            }
        }
        //reinsert finished
        size = newSize;
        ajList = tempList;
    }
    /**
     * ===========  reinsert for doubling size  ============
     * @param node list to reinsert
     * @param target list you are trying to insert
     * @return slot number
     */
    private int reinsert(Node node, Node [] target)
    {
        //retrieve the content
        int content = node.handleIndex;
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
     */
    public void printGraph()
    {
        //reset status variable
        stats[0] = 0;
        stats[1] = 0;
        stats[2] = 0;
        //mark all vertexes unvisited;
        for (int i = 0; i < size; i++)
        {
            if (ajList[i] != null)
            {
                ajList[i].visited = false;
            }
        }
        //BFS
        int connectedCompNum = 0;
        int maxCompNum = 0;
        //Node maxCompNode;
        List<Node> maxGraph = null;
        List<Node> temp;
        //count howmany connected components
        for (int i = 0; i < size; i++)
        {
            //if visited == false, this will be a new connected graph
            if (ajList[i] != null && ajList[i].handleIndex >= 0 && 
                    !ajList[i].visited)
            {
                //this is a new connected graph. 
                connectedCompNum++;
                temp = bfVisit(ajList[i]);
                if (stats[1] > maxCompNum)
                {
                    maxCompNum = stats[1];
                    maxGraph = temp;                    
                }
            }
        } 
        stats[0] = connectedCompNum;
        stats[1] = maxCompNum;
        //now I know the largest connected graph
        //use Dijxxxx to find out the diameter of this graph
        if (count > 0)
        {
            stats[2] = dijkstra(maxGraph);
        }
        //printout message
        System.out.println("There are " + stats[0] + 
                " connected components");
        System.out.println("The largest connected "
                + "component has " + stats[1] + " elements"); 
        System.out.println("The diameter of the "
                + "largest component is " + stats[2]);
    }
    /**
     * ===============  BFS visit  ===================
     * @param root to start visit
     * @return all nodes in this connected graph
     * 
     * THis will do a visit of current group 
     * update the number of components in current group
     * and return all nodes from this connected graph
     */
    private List<Node> bfVisit(Node root)
    {
        //root itself is a component
        int numOfComp = 1;
        //mark it visited
        ajList[search(root.handleIndex)].visited = true;
        //queue for BFS visiting list, allNode for return
        List<Node> queue = new ArrayList<Node>();
        List<Node> allNode = new ArrayList<Node>();
        //push in queue
        queue.add(root);
        allNode.add(root);
        //when the queue is empty, visit finished
        while (!queue.isEmpty())
        {
            //marker is the current node, once visited, dequeue
            int marker = queue.get(0).handleIndex;
            queue.remove(0);
            
            //listCheck will go through all neighbor of marker
            Node listChecker = ajList[search(marker)].next;
            while (listChecker != null)
            {
                //check if this neighbor is visited
                int nodeChecker = search(listChecker.handleIndex);
                if (!ajList[nodeChecker].visited)
                {
                    //if it is not visited, add to numOfComp
                    numOfComp++;
                    //mark it visited, enqueue for later visit
                    ajList[nodeChecker].visited = true;
                    queue.add(ajList[nodeChecker]);
                    allNode.add(ajList[nodeChecker]);
                }
                //check next 
                listChecker = listChecker.next;
            } 
        }
        stats[1] = numOfComp;
        return allNode;
    }
    /**
     * ==============    find shortest path    ===================
     * @param allNode from biggest connected map
     * @return diameter
     * 
     * this function uses Dijxxxxxx algorithm
     * it will use all node as root, find the shortest path
     * then find the longest path of all these shortest path
     * this will be the diameter.
     */
    private int dijkstra(List<Node> allNode) 
    {
        
        int checker;
        int maxShort = 0;
      //make every element 0
        for (int i = 0; i < allNode.size(); i++)
        {
            //reset everything for the next run
            //distant = max, visited = false
            for (int j = 0; j < allNode.size(); j++)
            {
                checker = search(allNode.get(j).handleIndex);
                ajList[checker].distance = Integer.MAX_VALUE;
                ajList[checker].visited = false;
            }
            //make root distance 0 and visited
            checker = allNode.get(i).handleIndex; 
            int marker = search(checker);
            Node root = ajList[marker];
            ajList[marker].distance = 0;
            ajList[marker].visited = true;
            
            
            //start bfs
            //this is a queue for BFS
            List<Node> queue = new ArrayList<Node>();
            queue.add(root);
            //when the queue is empty, visiting finished
            while (!queue.isEmpty())
            {
                //marker is the current node in this loop.
                marker = queue.get(0).handleIndex;
                queue.remove(0);
                //list checker will go through all node from marker
                Node listChecker = ajList[search(marker)].next;
                
                //=========== list in a node ==================
                //checkout all adjacent component from marker
                while (listChecker != null)
                {
                    //check if visited
                    //because this is BFS and constant edge weight
                    //nodes who was visited first will be closest
                    //no need to update nodes that are visited,
                    //visited nodes got shortest path already
                    int nodeChecker = search(listChecker.handleIndex);
                    if (!ajList[nodeChecker].visited)
                    {
                        //mark it visited, update distance
                        ajList[nodeChecker].visited = true;
                        /*
                        int newDist = AJlist[search(marker)].distance + 1;
                        //update distance if new distance is shorter
                        if (newDist < AJlist[NodeChecker].distance)
                        {
                            AJlist[NodeChecker].distance = newDist;
                        }
                        */
                        ajList[nodeChecker].distance =  
                                ajList[search(marker)].distance + 1;
                        //this is a new node, push in queue and visit
                        //it's neighbor later
                        queue.add(ajList[nodeChecker]);
                    }
                    //check next neighbor of marker
                    listChecker = listChecker.next;
                }
            }
            //update distance complete, find the max distance
            for (int k = 0; k < allNode.size(); k++)
            {
                checker = search(allNode.get(k).handleIndex);
                maxShort = Math.max(ajList[checker].distance, maxShort);
            }
        }
        return maxShort;
    }
    /**
     * =========== is there a existing edge? ==========
     * @param slotX
     * @param indexY
     * @return
     */
    private boolean isEdge(int slotX, int indexY)
    {
        Node checkerX = ajList[slotX];
        while (checkerX != null)
        {
            if (checkerX.handleIndex == indexY)
            {
                return true;
            }
            checkerX = checkerX.next;
        }
        return false;
    }
}
