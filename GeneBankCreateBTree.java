package BTree;

import java.io.IOException;
import java.util.Random;

//GeneBankCreateBTree <degree> <gbk file> <sequence length> [<debug level>]
//With cache: GeneBankCreateBTree <cache 0/1> <degree> <gbk file> <sequence length> <cache size> [<debug level>]

public class GeneBankCreateBTree {

	public static void main(String args[]) {
		BTree bt;
		try {
			bt = new BTree(4, 4, "test-tree");
			Random r = new Random();
			
			for(int i = 0; i<100; i++) {
				long sequence = (long)r.nextLong();
				bt.insert(sequence);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		
		
		
		
		
	}
	
}
