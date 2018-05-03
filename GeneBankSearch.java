package BTree;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

public class GeneBankSearch {
	private boolean useCache = false;
	private static int cacheSize, debugLevel, cacheCapacity, metaDegree, metaSeqLength;
	private static String btreeFileName, queryFileName, metadataFileName;
	private static long searchedKey;
	static Cache cache;
	static BTree bt;

	//GeneBankSearch<0/1(no/with Cache> <btree file> <query file> <Cache Size> [<debug level>]
	public static void main(String args[]) throws URISyntaxException {

		//a possible solution for getting the degree and sequence could be parsing it from the .gbk filename
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
				BTreeNode searchKey = bt.search(bt.root, bt.sequenceToLong(currLine));
				System.out.println(searchKey);
				
				if(searchKey == null) return;
				for(int i = 0; i < searchKey.keys.length; i++){
					if(searchKey.keys[i].key == searchedKey){
						System.out.print(searchKey.keys[i].key);
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

	public static void parseArgs(String args[]){
		if(args.length > 5 || args.length < 3)
		{
			printUsage();
		}

//		if(!args[0].equals("0")){
//			System.out.println("this 1");
//			printUsage();
//		}

		if(args[0].equals("1") && args.length >= 4){
			cacheCapacity = Integer.parseInt(args[3]);
			
			cache = new Cache(cacheCapacity);
		}

		/*
		 * This will need to change to length 5 and args[4] once we implement the cache.
		 */
		if(args.length == 5){
			debugLevel = Integer.parseInt(args[4]);
		}

		btreeFileName = args[1];
		String temp = btreeFileName;
		queryFileName = args[2];
		metadataFileName = temp.replace("data", "metadata");

	}

	//	public static void parseMetadata(){
	//		File metadataFile = new File("gbk.btree.metadata.k.t");
	//		char token = 0;
	//		int filePos = 0;
	//		
	//		try{
	//			BufferedReader metadataScan = new BufferedReader(new FileReader(metadataFile));
	//			
	//			while(token != -1){
	//				token = (char) metadataScan.read();
	//				
	//				if(filePos == 0){
	//					metaDegree = token;
	//				}else if (filePos == 1){
	//					metaSeqLength = token;
	//				}
	//				
	//				filePos++;
	//						
	//			}
	//			
	//		}catch(Exception e){
	//			System.err.println("That file does not exist");
	//		}


	private static void printUsage(){
		System.err.println("Usage: Java GeneBankSearch "
				+ "<0/1(no/with Cache)> <Btree File>"
				+ " <Query File> <Cache Size> [<Debug level>]");
	}

}
