package Btree;

import java.util.ArrayList;

public class BTreeNode {
	
	public long key;
	public ArrayList<BTreeNode> children;
	
	public BTreeNode(long key) {
		this.key = key;
	}
	
	public BTreeNode(long key, ArrayList<BTreeNode> children) {
		this.key = key;
		this.children = children;
	}

}
