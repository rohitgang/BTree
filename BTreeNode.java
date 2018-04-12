package BTree;

public class BTreeNode {
	
	public long[] keys;
	public BTreeNode[] children;
	public boolean isLeaf;
	public int n; //current number of keys
	
	public BTreeNode(int t) {
		keys = new long[(2*t-1)];
		children = new BTreeNode[(2*t)];
		isLeaf = true;
		n = 0;
	}

}
