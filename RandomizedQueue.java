// Models a Randomized queue, a data structure similar to a Stack or Queue, but in which values are removed uniformly at random. Represented as an array

import edu.princeton.cs.algs4.StdRandom;
import java.util.NoSuchElementException;
import java.util.Iterator;

public class RandomizedQueue<Item> implements Iterable<Item> {	
    private Item[] s;
    private int n;
    
    public RandomizedQueue() {
    // Initializes a RandomizedQueue with an initial array size of 2. n keeps track of # of items in the RandomizedQueue
        s = (Item[]) new Object[2];
        n = 0;
    }
    
    public boolean isEmpty()
    // Is the RandomizedQueue empty?
    {	return n == 0;	}
    
    public int size()
    // Returns the number of items in the Randomized Queue
    {	return n;	}
    
    public void enqueue(Item item) {
    // Adds a new item to the RandomizedQueue. If the array is at maximum capacity, double its capacity
        if (item == null) throw new NullPointerException();
        
        if (n == s.length) resize(2 * n);
        s[n++] = item;
    }
    
    private void resize(int capacity) {
    // Resizes array. Generic arrays cannot be directly created in Java so an Object[] has to be made and cast to Item[] :(
        Item[] copy = (Item[]) new Object[capacity];
        for (int i = 0; i < n; i++) copy[i] = s[i];
        s = copy;
    }
    
    public Item dequeue() {
    // Removes an item at random from the Randomized queue. If the array at one quarter capacity, halve its capacity (avoids thrashing)
        if (n == 0)	 throw new NoSuchElementException();
        int i = StdRandom.uniform(n);
        // Returns a random integer between [0, n)
    	
    	Item item = s[i];
    	s[i] = s[--n];
    	s[n] = null;
    	
    	if (n > 0 && n == s.length/4) resize(s.length/2);
    	
    	return item;
    }
    
    public Item sample() {
    // Returns (but does not remove) any random item from the RandomizedQueue
    	if (n == 0) throw new NoSuchElementException();
    	
    	int i = StdRandom.uniform(n);
    	return s[i];
    }
    
    public Iterator<Item> iterator()
    // Returns a new iterator for all items in RandomizedQueue
    {	return new RandomizedQueueIterator();    }
    
    private class RandomizedQueueIterator implements Iterator<Item> {
    // Representation of the RandomizedQueue iterator. Implemented as an array to support random iteration
    	private Item[] iterArray;
    	private int n;
    	
    	public RandomizedQueueIterator() {
    		
    		n = RandomizedQueue.this.n;
    		iterArray = (Item[]) new Object[s.length];
    		for (int i = 0; i < n; i++) iterArray[i] = s[i];
    	}
    	
    	public boolean hasNext()
    	{	return n > 0;	}
    	
    	public void remove() {
    		throw new UnsupportedOperationException();
    	}
    	
    	public Item next() {
    		if (!hasNext()) throw new NoSuchElementException();
    		int i = StdRandom.uniform(n);
    		
    		Item item = iterArray[i];
    		iterArray[i] = iterArray[--n];
    		iterArray[n] = null;
    		
    		return item;
    	}
    }
        
    public static void main(String[] args) {
        RandomizedQueue<Integer> Joe = new RandomizedQueue<Integer>();
        for (int i = 0; i < 14; i++)
            Joe.enqueue(i);
    	
    	//System.out.println(Joe.dequeue());
    	//System.out.println(Arrays.toString(Joe.s));
    	Iterator<Integer> JoeIter = Joe.iterator();
    	Iterator<Integer> JoeSecondIter = Joe.iterator();
    	
    	for (int i = 0; i < 14; i++) {
    	    System.out.println("Iter1 = " + JoeIter.next());    	
    	    System.out.println("Iter2 = " + JoeSecondIter.next());
    	}
    	
    	System.out.println(Joe.n);
    }
	
}
