package BTree;

import java.util.Random;

//GeneBankCreateBTree <degree> <gbk file> <sequence length> [<debug level>]
//With cache: GeneBankCreateBTree <cache 0/1> <degree> <gbk file> <sequence length> <cache size> [<debug level>]

public class GeneBankCreateBTree {

	public static void main(String args[]) {
		BTree bt = new BTree(4);		
		
		Random r = new Random();
		
		for(int i = 0; i<100; i++) {
			long sequence = (long)r.nextLong();
			bt.insert(sequence);
		}
		
		
		
	}
	
}
