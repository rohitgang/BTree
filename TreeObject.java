package BTree;

public class TreeObject {
	long key;
	int freq;
	
	public TreeObject(long key, int freq) {
		this.key = key;
		this.freq = freq;
	}
	
	public TreeObject(long key) {
		this.key = key;
		freq = 0;
	}
}
