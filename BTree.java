package BTree;

import java.security.InvalidParameterException;

public class BTree extends InvalidParameterException{

	private int t;
	BTreeNode root;
	
	public BTree(int t) {
		this.t = t;
	}
	
	public void delete(long key) {
		
	}
	
	public void insert(long key) {
		BTreeNode s = new BTreeNode(t);
		if(root==null) root = s;
		if(root.n == 2*t-1) {
			root = s;
			s.isLeaf = false;
			s.n = 0;
			s.children[0] = root;
			splitChild(s,1,root);
			insertNonFull(s,key);
		}else {
			insertNonFull(s,key);
		}
	}
	
	public void insertNonFull(BTreeNode x, long key) {
		int i = x.n;
		if(x.isLeaf) {
			while( i >= 0 && key < x.keys[i] ) {
				x.keys[i+1] = x.keys[i];
				i--;
			}
			x.keys[i+1] = key;
			x.n++;
			diskWrite(x);
		}else {
			while( i >= 0 && key < x.keys[i] ) {
				i--;
			}
			i++;
			diskRead(x.children[i]);
			if( x.children[i].n == 2*t-1 ) {
				splitChild(x, i, x.children[i]);
				if( key > x.keys[i]){
					i++;
				}
			}
			insertNonFull(x.children[i], key);
		}
	}

		
	//returns an empty node on unsuccessful search;
	public BTreeNode search(BTreeNode x, long key) {
		for(int i=0; i<x.keys.length; i++) {
			if(x.keys[i] == key) return x;
		}
		if(x.isLeaf) return new BTreeNode(t);
		for(int i=0; i<x.children.length; i++) {
			return search(x.children[i], key);
		}
		return new BTreeNode(t);
	}
	
	public void splitChild(BTreeNode x, int i, BTreeNode y) {
		BTreeNode z = new BTreeNode(t);
		z.isLeaf = y.isLeaf;
		z.n = t-1;
		for(int j=0; j<t; j++) {
			z.keys[j] = y.keys[j+t];
		}
		if(!y.isLeaf) {
			for(int j=0; j<t; j++) {
				z.children[j] = y.children[j+t];
			}
		}
		y.n = t-1;
		for(int j=x.n; j>i; j--) {
			x.children[j+1] = x.children[j];
		}
		x.children[i+1] = z;
		for(int j=x.n; j>i; j--) {
			x.keys[j+1] = x.keys[j];
		}
		x.keys[i] = y.keys[t];
		x.n = x.n + 1;
		diskWrite(y);
		diskWrite(z);
		diskWrite(x);
	}
	
	public long stringToLong(String s) {
		if( s.length() > 31 ) 
			throw new InvalidParameterException("stringToLong() string param must be 31 chars long !");
		Long retVal = 0L;
		int j = 0;
		for( int i=s.length()-1; i>=0; i-- ) {
			int cur = 0;
			switch( s.substring(i, i+1) ){
				case "A": cur = 0; break;
				case "C": cur = 3; break;
				case "T": cur = 1; break;
				case "G": cur = 2; break;
			};
			if(j==0) retVal += cur;
			else retVal += cur * (long)Math.pow(j,4);
			j++;
		}
		return retVal;
	}
	
	public String longToString(long l) {
		String retString = "";
		while(l != 0) {
			String cur = "";
			switch((int)(l % 4)) {
			case 3: cur = "C"; break;
			case 2: cur = "G"; break;
			case 1: cur = "T"; break;
			case 0: cur = "A"; break;
			}
			retString = cur + retString;
			l = l >> 2;
		}
		return retString;
	}
	
	public void diskWrite(BTreeNode node) {
		
	}
	
	public void diskRead(BTreeNode node) {
		
	}
	
}
