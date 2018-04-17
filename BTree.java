package BTree;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.security.InvalidParameterException;

public class BTree{

	/**
	 *  a B-tree is a self-balancing tree data structure that keeps data sorted and allows searches, 
	 *  sequential access, insertions, and deletions in logarithmic time. In this case there is no
	 *  delete method because we are only interested in building and searching the tree.
	 *  @author Ben McAvoy, Ben Peterson
	 */
	private static final long serialVersionUID = 216708716941088359L;
	private int t;
	private int seqLength;
	BTreeNode root;
	File BtreeFile;
	RandomAccessFile btreeRAF;
	
	public BTree(int t, int k, String gbk) throws IOException{
		this.t = t;
		this.seqLength = k;
		File metadata = new File(gbk + ".btree.metadata." + k + "." + t);
		
		btreeRAF = new RandomAccessFile(metadata, "rw");
		btreeRAF.write(t); //tree degree in terms of t
		btreeRAF.write(k); //length of sequences stored in the tree
		btreeRAF.close();

		root = new BTreeNode(t,0);
		BtreeFile = new File(gbk + ".btree.data." + k + "." + t);			
		diskWrite(root);
	}
	
	public BTree(File BtreeFile, File metadata) throws IOException {
		btreeRAF = new RandomAccessFile(metadata, "r");
		this.t = btreeRAF.readInt(); //read in degree in terms of t
		this.seqLength = btreeRAF.readInt(); //sequence length (k) 
		btreeRAF.close();
		
		btreeRAF = new RandomAccessFile(BtreeFile, "r");
		root = diskRead(0);		
		btreeRAF.close();
	}
	
	public void insert(long key)  {
		//somewhere needs to be a check for if the key is already in the tree
		//if the key is already in the tree then just increment the frequency for it
		if(root.n == 2*t-1) {
			BTreeNode r = this.root;
			BTreeNode newNode = new BTreeNode(t, getFileLength());			
			newNode.isLeaf = false;
			newNode.n = 0;
			newNode.children[0] = r.filePos;
			this.root = newNode;
			diskWrite(newNode);			
			splitChild(newNode,0,r);
			insertNonFull(newNode,key);
		}else {
			insertNonFull(root,key);
		}
	}
	
	public void insertNonFull(BTreeNode x, long key) {
		int i = x.n - 1;
		boolean duplicateFound = false;
		if(x.isLeaf) {
			for(int j=0; j<x.keys.length; j++) {
				if(x.keys[j].key == key) {
					x.keys[j].freq++;
					duplicateFound = true;
				}
			}
			if(!duplicateFound) {
				while( i >= 0 && key < x.keys[i].key ) {
					x.keys[i+1] = x.keys[i];
					i--;
				}
				x.keys[i+1].key = key;
				x.keys[i+1].freq = 1;
				x.n++;
				diskWrite(x);
			}			
		}else {
			while( i >= 0 && key < x.keys[i].key) {
				i--;
			}
			i++;
			BTreeNode c;
			if(x.children[i] != -1) {
				c = diskRead(x.children[i]);
				if( c.n == 2*t-1 ) {
					splitChild(x, i, c);
					if( key > x.keys[i].key){
						i++;
					}
				}
				c = diskRead(x.children[i]);
				insertNonFull(c, key);
			}			
		}
	}
	
	public BTreeNode search(BTreeNode x, long key) {
		for(int i=0; i<x.keys.length; i++) {
			if(x.keys[i].key == key) return x;
		}
		if(x.isLeaf) return null;
		for(int i=0; i<x.children.length; i++) {
			return search(diskRead(x.children[i]), key);
		}
		return new BTreeNode(t, x.filePos);
	}
	
	public void splitChild(BTreeNode x, int i, BTreeNode y) {		
		BTreeNode z = new BTreeNode(t, getFileLength());
		diskWrite(z);
		z.isLeaf = y.isLeaf;
		z.n = t-1;
		for(int j=0; j<t-1; j++) {
			z.keys[j] = y.keys[j+t];
			y.keys[j+t] = new TreeObject(-1L, 0);
		}
		if(!y.isLeaf) {
			for(int j=0; j<t; j++) {
				z.children[j] = y.children[j+t];
				y.children[j+t] = -1;
			}
		}
		y.n = t-1;
		for(int j=x.n; j>=i+1; j--) {
			x.children[j+1] = x.children[j];
		}
		x.children[i+1] = z.filePos;
		for(int j=x.n-1; j>=i; j--) {
			x.keys[j+1] = x.keys[j];
		}
		x.keys[i] = y.keys[t-1];
		y.keys[t-1] = new TreeObject(-1L, 0);
		x.n = x.n + 1;
		diskWrite(z);		
		diskWrite(y);		
		diskWrite(x);
	}
	
	public long sequenceToLong(String s) {
		if( s.length() > 31 ) 
			throw new InvalidParameterException("stringToLong() string param must be 31 chars long !");
		Long retVal = 0L;
		for( int i=0; i<s.length(); i++ ) {
			int cur = 0;
			switch( s.substring(i, i+1) ){
				case "A": cur = 0; break;
				case "C": cur = 1; break;
				case "T": cur = 3; break;
				case "G": cur = 2; break;
			};
			if(i==0) retVal += cur;
			else retVal += cur * (long)Math.pow(4,i);
		}
		return retVal;
	}
	
	public String longToSequence(long key, int subsequenceLength) {
		String retString = "";
		for(int i=0; i < subsequenceLength; i++){
			String cur = "";
			switch((int)(key % 4)) {
				case 1: cur = "C"; break;
				case 2: cur = "G"; break;
				case 3: cur = "T"; break;
				case 0: cur = "A"; break;
			}
			retString += cur;
			key = key >> 2;
		}
		return retString;
	}
	
	public void diskWrite(BTreeNode node) {
		try {
			btreeRAF = new RandomAccessFile(BtreeFile, "rw");
			btreeRAF.seek(node.filePos);
			for (int i = 0; i < node.keys.length; i++) {
				btreeRAF.writeLong(node.keys[i].key);
				btreeRAF.writeInt(node.keys[i].freq);
			}
			for (int i = 0; i < node.children.length; i++) {
				btreeRAF.writeLong(node.children[i]);
			}
			btreeRAF.writeInt(node.n);
			btreeRAF.writeBoolean(node.isLeaf);
			btreeRAF.writeLong(node.filePos);
			btreeRAF.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public BTreeNode diskRead(long offset) {
		BTreeNode node = new BTreeNode(t,offset);
		try {
			btreeRAF = new RandomAccessFile(BtreeFile, "rw");
			btreeRAF.seek(offset);
			for (int i = 0; i < node.keys.length; i++) {
				node.keys[i].key = btreeRAF.readLong();
				node.keys[i].freq = btreeRAF.readInt();
			}
			for (int i = 0; i < node.children.length; i++) {
				node.children[i] = btreeRAF.readLong();
			}
			node.n = btreeRAF.readInt();
			node.isLeaf = btreeRAF.readBoolean();
			node.filePos = btreeRAF.readLong();
			btreeRAF.close();
		} catch (IOException e) {
			e.printStackTrace();
		}		
		return node;
	}
	
	public void print(BTreeNode root_node) {		
		for(int i=0; i<t; i++) {
			if(root_node.children[i] != -1L) {
				BTreeNode n = diskRead(root_node.children[i]);
				print(n);
			}
		}
		root_node.printNode();
		System.out.println();
		for(int i=t; i<root_node.children.length; i++) {
			if(root_node.children[i] != -1L) {
				BTreeNode n = diskRead(root_node.children[i]);
				print(n);
			}
		}		
	}
	
	long getFileLength() {
		long fileLength = -1L;
		try {
			btreeRAF = new RandomAccessFile(BtreeFile, "r");
			fileLength = btreeRAF.length();
			btreeRAF.close();
		} catch (IOException e) {
			System.out.println("Error accessing file");
			e.printStackTrace();
		}	
		return fileLength;
	}
	
}
