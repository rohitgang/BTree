package BTree;

import java.util.LinkedList;

/**
 * Implements a 1 or 2 level cache based on parameters passed into constructor. 
 * The cache stores generic objects in a linked list. Each node represents one
 * open space in the cache. 
 *  
 * @author Ben Peterson
 */
public class Cache { 
	//cache
	private LinkedList<BTreeNode> cache1;
	//cache size
	private int cache1Size; 
	
	/**
	 * Constructor for one level cache.
	 * 
	 * @param cache1Size Size for 1st-level cache
	 */
	public Cache(int cache1Size) {
		//initialize class variables
		cache1 = new LinkedList<BTreeNode>();
		this.cache1Size = cache1Size;

	}//end of Cache
		
	/**
	 * Adds an object to the cache if it does not
	 * already exist. Moves object to front of the
	 * cache if it does exist. Need to check if
	 * cache is full before running. 
	 * 
	 * @param toAdd object to be added to the cache
	 */
	public void addObject(BTreeNode toAdd) {
		
		//add in object if it is not already in cache
		BTreeNode moveToFront = getObject(toAdd);
		if (moveToFront == null){
			cache1.addFirst(toAdd);
		}
		else { //already in cache move to front
			cache1.addFirst(toAdd);
		}
		
	}//end of addObject
	
	/**
	 * Looks for BTreeNode in cache and returns it if found. 
	 * 
	 * @param toGet object to check cache for
	 * @return  BTreeNode if found, null if not found
	 */
	public BTreeNode getObject(BTreeNode toGet) {
		//look for object in cache and return it if found
		for (int i = 0; i < cache1.size(); i++)
		 if (cache1.get(i).equals(toGet)) {
			 BTreeNode found = cache1.get(i);
			 cache1.remove(i);
			 return found;
		 }
		//not found
		return null;
	}//end of getObject
	
	
	/**
	 * Looks for BTreeNode by file offset in cache and returns it if found. 
	 * 
	 * @param toGet object to check cache for
	 * @return  BTreeNode if found, null if not found
	 */
	public BTreeNode getObject(long fileOffset) {
		//look for object in cache and return it if found
		for (int i = 0; i < cache1.size(); i++)
		 if (cache1.get(i).filePos == fileOffset) {
			 BTreeNode found = cache1.get(i);
			 cache1.remove(i);
			 return found;
		 }
		//not found
		return null;
	}//end of getObject
	
	/**
	 * Returns last node in the cache.
	 * 
	 * @return last node in cache
	 */
	public BTreeNode getLast () {
		return cache1.removeLast();
	}


	/**
	 * Checks if cache is full
	 * 
	 * @return true if full false otherwise
	 */
	public boolean isFull() {
		return cache1.size() == cache1Size;
	}
	
	/**
	 * Gets cache size
	 * 
	 * @return cacheSize
	 */
	public int cacheSize() {
		return cache1.size();
	}
}//end of class Cache