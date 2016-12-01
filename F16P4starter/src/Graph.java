import java.math.*;
/**
 * Class for graph. uses hash function to insert
 * I later realize there is no need for hash table in this class
 * However, I spent a lot of time to write it and Dr.Shaffer
 * agree that he will not deduct points because of this.
 * Therefore, I decided to keep it as it works fine 
 * @author Theo
 * @version 1.1
 * 
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
    public boolean insertVertex(Handle h)
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
            insert(handle, ajList);
            return true;
        }
        return false;
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
            if (!isEdge(slotX, slotY, x.thePos, y.thePos))
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
     * 
     * this function will return list of handles that
     * must be delete along this operation.
     */
    public void delete(Handle h)
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
            while (subChecker.next.handleIndex != h.thePos)
            {
                subChecker = subChecker.next;
            }
            subChecker.next = subChecker.next.next;
            checker = checker.next;
        }
        //toRemove.add(new Handle (AJlist[index].index));
        ajList[targetIndex] = new Node(-1);
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
        Node current = ajList[slot];
        //search for insertion position
        while (current.next != null)
        {
            current = current.next;
        }
        current.next = node;
        node.previous = current;
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
    private void insert(int content, Node [] target)
    {
      //initialize a slot
        int slot = hash(content, target.length);
        
        
        //if this is unused slot, place directly in
        if (target[slot] == null || target[slot].handleIndex < 0 )
        {
            target[slot] = new Node(content);
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
            while (target[nextSlot] != null && 
                    target[nextSlot].handleIndex != -1)
            {
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
        Node maxGraph = null;
        Node temp;
        //count how many connected components
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
    private Node bfVisit(Node root)
    {
        //root itself is a component
        int numOfComp = 1;
        //mark it visited
        ajList[search(root.handleIndex)].visited = true;
        //queue for BFS visiting list, allNode for return
        Node queueHead;
        Node queueTail;
        Node allNode = new Node(root.handleIndex);
        //List<Node> allNode = new ArrayList<Node>();
        //push in queue
        queueHead = new Node(root.handleIndex);
        queueTail = queueHead;
        //when the queue is empty, visit finished
        while (queueHead != null)
        {
            //marker is the current node, once visited, dequeue
            int marker = queueHead.handleIndex;
            //only one in the queue
            if (queueHead.next == null)
            {
                queueHead = null;
                queueTail = null;
            }
            else
            {
                queueHead = queueHead.next;
                queueHead.previous = null;
            }
            
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
                    //enqueue
                    if (queueHead == null)
                    {
                        queueHead = new Node(ajList[nodeChecker].handleIndex);
                        queueTail = queueHead;
                    }
                    else
                    {
                        queueTail.next = new Node(
                                ajList[nodeChecker].handleIndex);
                        queueTail = queueTail.next;
                    }
                    allNode.next = new Node(ajList[nodeChecker].handleIndex);
                    (allNode.next).previous = allNode;
                    allNode = allNode.next;
                }
                //check next 
                listChecker = listChecker.next;
            } 
        }
        stats[1] = numOfComp;
        while (allNode.previous != null)
        {
            allNode = allNode.previous;
        }
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
    private int dijkstra(Node allNode) 
    {
        Node holder = allNode;
        int checker;
        int maxShort = 0;
        //make every element 0
        while (holder != null)
        {
            Node resetNode = allNode;
            //reset everything for the next run
            //distant = max, visited = false
            while (resetNode != null)
            {
                checker = search(resetNode.handleIndex);
                ajList[checker].distance = Integer.MAX_VALUE;
                ajList[checker].visited = false;
                resetNode = resetNode.next;
            }
            //make root distance 0 and visited
            checker = holder.handleIndex; 
            int marker = search(checker);
            Node root = ajList[marker];
            ajList[marker].distance = 0;
            ajList[marker].visited = true;
            
            
            //start bfs
            //this is a queue for BFS
            Node queueHead = new Node(root.handleIndex);
            Node queueTail = queueHead;
            //when the queue is empty, visiting finished
            while (queueHead != null)
            {
                //marker is the current node in this loop.
                marker = queueHead.handleIndex;
                if (queueHead.next == null)
                {
                    queueHead = null;
                    queueTail = null;
                }
                else
                {
                    queueHead = queueHead.next;
                    queueHead.previous = null;
                }
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
                        if (queueHead == null)
                        {
                            queueHead = new Node(
                                    ajList[nodeChecker].handleIndex);
                            queueTail = queueHead;
                        }
                        else
                        {
                            queueTail.next = new Node(
                                    ajList[nodeChecker].handleIndex);
                            queueTail = queueTail.next;
                        }
                    }
                    //check next neighbor of marker
                    listChecker = listChecker.next;
                }
            }
            //update distance complete, find the max distance
            Node counter = allNode;
            while (counter != null)
            {
                checker = search(counter.handleIndex);
                maxShort = Math.max(ajList[checker].distance, maxShort);
                counter = counter.next;
            }
            holder = holder.next;
        }
        return maxShort;
    }
    /**
     * =========== is there a existing edge? ==========
     * @param slotX of the ajList
     * @param slotY of the ajList
     * @param indexX handle
     * @param indexY handle
     * @return is it an edge?
     */
    private boolean isEdge(int slotX, int slotY, int indexX, int indexY)
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
