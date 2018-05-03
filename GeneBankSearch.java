package BTree;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Scanner;

public class GeneBankSearch {
	static int cacheCapacity, metaSeqLength, debugLevel, cacheSize;
	static String btreeFileName, queryFileName, metadataFileName;
	static long searchedKey;
	static Cache cache;
	static BTree bt;

	
	public static void main(String args[]) throws URISyntaxException {

		parseArgs(args);
		
		try {		
			File bf = new File(btreeFileName);
			File mf = new File(metadataFileName);
			BTree bt = null;
			if(args[0].equals("1")){
				bt = new BTree(bf, mf, cache);
			}else if(args[0].equals("0")){
				bt = new BTree(bf, mf, null);
			}

			System.out.println("Btree File:" + btreeFileName);
			System.out.println("Metadata File:" + metadataFileName);
			System.out.println("Query File:" + queryFileName);

			Scanner queryScanner = new Scanner(new File(queryFileName));

			String currLine = "";
			do{				
				currLine = queryScanner.nextLine();
				long k = bt.sequenceToLong(currLine);
				BTreeNode searchKey = bt.search(bt.root, k);
				if(searchKey == null) return;
				for(int i = 0; i < searchKey.keys.length; i++){
					if(searchKey.keys[i].key == k){
						System.out.print(bt.longToSequence(k, metaSeqLength));
						System.out.print(' ');
						System.out.print(searchKey.keys[i].freq);
						System.out.println();
					}
				}				
			}while(queryScanner.hasNextLine());
			
			queryScanner.close();

		}catch(FileNotFoundException e){
			e.printStackTrace();
		}catch (IOException e) {
			e.printStackTrace();
		}
	}

	//GeneBankSearch <0/1 with/without Cache> <btree file> <query file> <Cache Size> [<debug level>]
	public static void parseArgs(String args[]){
		if(args.length > 5 || args.length < 3 || !args[4].equals("0"))	
			printUsage();
		
		if(args.length == 5)
			debugLevel = Integer.parseInt(args[4]);

		if(args[0].equals("1")){
			cacheSize = Integer.parseInt(args[3]);
			cache = new Cache(cacheCapacity);
		}		

		btreeFileName = args[1];
		queryFileName = args[2];
		metadataFileName = btreeFileName.replace("data", "metadata");
		metaSeqLength = Integer.parseInt(metadataFileName.split("\\.")[4]);
	}

	private static void printUsage(){
		System.err.println("Usage: Java GeneBankSearch "
				+ "<0/1(no/with Cache)> <Btree File>"
				+ " <Query File> <Cache Size> [<Debug level>]");
		System.exit(1);
	}

}
