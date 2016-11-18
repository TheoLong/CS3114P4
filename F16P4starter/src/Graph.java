import java.util.*;
import java.math.*;
public class Graph 
{
    private Node []AJlist;
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
    //======================        constructor     =========================
    public Graph (int size)
    {
        AJlist = new Node[size];
        count = 0;
        this.size = size;    
    }
    //===========================   insert vertex      ======================
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
    //======================    add edge    ===========================
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
    //======================    delete  ===============================
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
    //======================    print graph         =======================
    public void printGraph()
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
    //===================================== private helper  ============================
    //
    private int hash(int content, int Hsize)
    {
        return content / Hsize;
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
    /**
     *===========  base insert function  ============
     * This function handles inserting content.
     * @param content
     * @param target
     * 
     * This is a private insert function. It takes the 
     * content trying to insert, and the target array trying to insert.
     * 1. first call h(content, length) to hash a key number
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
     * ======  search a content   ============
     * @param target things you want to search
     * @return
     * 
     * This will search the target content and return
     * the index number in the hash table.
     *         by using inverse algorithm.
     *     1. hash the key for this target.
     *     2. look up the hashTable with this key.
     *     3. if they do not match the first time, 
     *         collision happen. Do quadratic stepping.
     *     4. stepped to a new location. return index 
     *     if matches, keep stepped if doesn't
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
     *============== double the size ==================
     * This function will double the hash table
     * 
     * A private function.
     *1. create a temporary array that's double the current size
     *2. loop through the old hash table, re-insert 
     *     them to the new hash table using insert function.
     *3. replace the new array with old array
     */

    private void expand()
    {
        
        BigInteger b = new BigInteger(String.valueOf(size*2));
        int newSize = (int)Long.parseLong(b.nextProbablePrime().toString());
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
     * @param handle handle you are trying to insert
     * @param target target hTable you are trying to insert
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
}
