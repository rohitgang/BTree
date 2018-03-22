package Btree;

public class BTree {

	private int degree;
	
	public BTree(int degree) {
		this.degree = degree;
	}
	
	public void delete(long key) {
		
	}
	
	public BTreeNode max() {
		return new BTreeNode(0L);
	}
	
	public BTreeNode min() {
		return new BTreeNode(0L);
	}	
	
	public BTreeNode next(long key) {
		return new BTreeNode(0L);
	}
	
	public void insert(long key) {
		
	}
	
	public BTreeNode previous(long key) {
		return new BTreeNode(0L);
	}	
	
	public BTreeNode search(long key) {
		return new BTreeNode(0L);
	}
	
	
	public void rotateNode(BTreeNode node, String direction) {
		switch(direction) {
		case "L": break;
		case "R": break;
		case "LR": break;
		case "RL": break;
		case "RR": break;
		case "LL": break;
		}
	}
}
