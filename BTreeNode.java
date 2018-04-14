package BTree;

public class BTreeNode {
	
	public TreeObject[] keys;
	public long[] children;
	public boolean isLeaf;
	public int n; //current number of keys
	long filePos; //position of node in file
	
	public BTreeNode(int t, long filePos) {
		keys = new TreeObject[(2*t-1)];
		children = new long[(2*t)];
		isLeaf = true;
		n = 0;
		this.filePos = filePos;
	}

}
