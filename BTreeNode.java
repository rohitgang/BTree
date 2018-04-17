package BTree;

public class BTreeNode {
	
	public TreeObject[] keys;
	public long[] children;
	public boolean isLeaf;
	public int n; //current number of keys
	public long filePos; //position of node in file
	
	public BTreeNode(int t, long filePos) {
		keys = new TreeObject[(2*t-1)];		
		for(int i=0; i<keys.length; i++) {
			keys[i] = new TreeObject(-1L, 1);
		}		
		children = new long[(2*t)];
		for(int i=0; i<children.length; i++) {
			children[i] = -1L;
		}
		isLeaf = true;
		n = 0;
		this.filePos = filePos;
	}
	
	//TODO remove children and other outputs for proper cli debug functionality 
	public void printNode() {
		System.out.println("filePos: " + filePos);
		System.out.println("isLeaf: " + false);
		System.out.println("n: " + n);
		System.out.println("keys: ");
		for(int i = 0; i < keys.length; i++) {
			System.out.print(keys[i].key + ", ");
		}
		System.out.println();
		System.out.println("children: " );
		for(int j = 0; j < children.length; j++) {
			System.out.print(children[j] + ", ");
		}
		System.out.println();
	}

}
