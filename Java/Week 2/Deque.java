/* Models a Deque, a data structure similar to a Stack or Queue, but that allows for item insertion and removal from two different ends. Represented as a linked 
 * list for efficient implementation of insert and remove from two ends. A front and a back end are arbitrarily distinguished for the Deque class */

import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {    
    private Node first;
    private Node last;
    private int n;
    
    public Deque() {
    // Initializes a Deque with a first node, last node, and size
        first = null;
        last = null;
        n = 0;
    }
    
    private class Node {
    /* Every item in the Deque is represented by a Node, each of which has a pointer to the Node immediately preceding and 
     * succeeding it. The Node contains a reference to the inserted item in its value "item" */        
        private Item item = null;
        private Node next = null;
        private Node prev = null;
    }
        
    public boolean isEmpty()
    // Is the Deque empty?
    {    return (first == null || last == null);    }
    
    public int size()
    // Returns number of items in the Deque
    {    return n;    }
    
    public void addFirst(Item item) {
    // Adds a new element to the front of the Deque and adjusts all references accordingly
        if (item == null) throw new NullPointerException();
        
        Node oldfirst = first;
        first = new Node();
        first.item = item;
        
        if (isEmpty()) last = first;
        else { 
            oldfirst.prev = first;
            first.next = oldfirst;
        }
        
        n++;
    }
    
    public void addLast(Item item) {
    // Adds a new element to the end of the Deque and adjusts all references accordingly
        if (item == null) throw new NullPointerException();
        
        Node oldlast = last;
        last = new Node();
        last.item = item;
        
        if (isEmpty()) first = last;
        else {
            oldlast.next = last;
            last.prev = oldlast;
        }
        
        n++;
    }
    
    public Item removeFirst() {
    // Pops the element from the front of the Deque and adjusts all references accordingly
        if (isEmpty()) throw new NoSuchElementException();
        
        Item item = first.item;
        first = first.next;
        if (isEmpty()) last = null;
        else first.prev = null;
        
        n--;
        return item;
        
    }
    
    public Item removeLast() {
    // Pops the element from the end of the Deque and adjusts all references accordingly
        if (isEmpty()) throw new NoSuchElementException();
        
        Item item = last.item;
        last = last.prev;
        if (isEmpty()) first = null;
        else last.next = null;
        
        n--;
        return item;
    }	
    
    public Iterator<Item> iterator()
    // Returns a new iterator for all items in Deque
    {    return new ListIterator();    }
    
    private class ListIterator implements Iterator<Item> {
    // Representation of the Deque iterator that implements the Iterator interface    
        private Node current = first;
        
        public boolean hasNext() 
        // Is there another Node to iterate over in the iterator?
        {	return current != null;	}
        
        public void remove() 
        // Remove is not supported
        {	throw new UnsupportedOperationException();	}
        
        public Item next() {
        // Returns the next value in the Iterator
            if (!hasNext()) throw new NoSuchElementException();
            
            Item item = current.item;
            current = current.next;
            return item;
        }
    }
    
    public static void main(String[] args) {
        
    }
}
