package BTree;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
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
	public static void main(String args[]) {

		//a possible solution for getting the degree and sequence could be parsing it from the .gbk filename
		parseArgs(args);
		File btreeFile = new File(btreeFileName);
		File metadataFile = new File(metadataFileName);

		try {
			if(args[0].equals("1")){
				BTree bt = new BTree(btreeFile, metadataFile, cache);
			}else if(args[0].equals("0")){
				BTree bt = new BTree(btreeFile, metadataFile);
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}

		System.out.println("Btree File:" + btreeFileName);
		System.out.println("Metadata File:" + metadataFileName);
		System.out.println("Query File:" + queryFileName);


		try{
			Scanner queryScanner = new Scanner(new File(queryFileName));

			while (queryScanner.hasNextLine()){
				String currLine = queryScanner.nextLine();

				searchedKey = bt.sequenceToLong(currLine);
				
				BTreeNode searchKey = bt.search(bt.root, searchedKey);
				
				for(int i = 0; i < searchKey.keys.length; i++){
					if(searchKey.keys[i].equals(searchedKey)){
//						System.out.println(searchKey.keys.)
					}
				}
				
				if(searchKey != null){
//					System.out.println("Searched key: " + searchKey. + "Searched Seqeunce ");
				}
				
			}
			
			queryScanner.close();

		}catch(Exception e){

		}


	}

	public static void parseArgs(String args[]){
		if(args.length > 5 || args.length < 3)
		{
			printUsage();
		}

		if(!(args[0].equals("0") || args[1].equals("1"))){
			printUsage();
		}

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
