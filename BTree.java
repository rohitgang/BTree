package BTree;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.security.InvalidParameterException;

public class BTree extends InvalidParameterException{

	private int t;
	BTreeNode root;
	File BtreeFile;
	RandomAccessFile btreeRAF;
	
	public BTree(int t, int k, String gbk) throws IOException{
		this.t = t;
		File metadata = new File(gbk + ".btree.metadata." + k + "." + t);
		
		//write BTree metadata
		btreeRAF = new RandomAccessFile(metadata, "rw");
		btreeRAF.write(t);
		btreeRAF.close();

		root = new BTreeNode(t,0);
		BtreeFile = new File(gbk + ".btree.data." + k + "." + t);
		btreeRAF = new RandomAccessFile(BtreeFile, "rw");
		
		diskWrite(root);
	}
	
	public BTree(File BtreeFile, File metadata)  throws IOException {
		btreeRAF = new RandomAccessFile(metadata, "rw");
		t = btreeRAF.readInt();
		btreeRAF.close();
		
		btreeRAF = new RandomAccessFile(BtreeFile, "rw");
		root = diskRead(0);
		
		
	}
	
	public void delete(long key) {
		
	}
	
	public void insert(long key) {
		//the filePos for this will probably need to be something different
		BTreeNode s = new BTreeNode(t, 0);
		if(root.n == 2*t-1) {
			BTreeNode r = root;
			root = s;
			s.isLeaf = false;
			s.n = 0;
			s.children[0] = r.filePos;
			splitChild(s,1,r);
			insertNonFull(s,key);
		}else {
			insertNonFull(s,key);
		}
	}
	
	public void insertNonFull(BTreeNode x, long key) {
		int i = x.n;
		if(x.isLeaf) {
			while( i >= 0 && key < x.keys[i].key ) {
				x.keys[i+1] = x.keys[i];
				i--;
			}
			x.keys[i+1].key = key;
			x.n++;
			diskWrite(x);
		}else {
			while( i >= 0 && key < x.keys[i].key ) {
				i--;
			}
			i++;
			diskRead(x.children[i]);
			if( diskRead(x.children[i]).n == 2*t-1 ) {
				splitChild(x, i, diskRead(x.children[i]));
				if( key > x.keys[i].key){
					i++;
				}
			}
			insertNonFull(diskRead(x.children[i]), key);
		}
	}

		
	//returns an empty node on unsuccessful search;
	public BTreeNode search(BTreeNode x, long key) {
		for(int i=0; i<x.keys.length; i++) {
			if(x.keys[i].key == key) return x;
		}
		//if x is a leaf return 'null' as a recursive base case
		if(x.isLeaf) return new BTreeNode(t, x.filePos);
		for(int i=0; i<x.children.length; i++) {
			return search(diskRead(x.children[i]), key);
		}
		return new BTreeNode(t, x.filePos);
	}
	
	public void splitChild(BTreeNode x, int i, BTreeNode y) {
		//again the file offset will need to be changed !!
		BTreeNode z = new BTreeNode(t, x.filePos);
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
		x.children[i+1] = z.filePos;
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
	
	public BTreeNode diskRead(long offset) {
		return new BTreeNode(0,0);
	}
	
}
