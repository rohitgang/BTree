package BTree;

import java.util.ArrayList;

public class GeneBankSearch {
	private boolean useCache = false;
	private static int cacheSize, debugLevel;
	
	//GeneBankSearch<0/1(no/with Cache> <btree file> <query file> <Cache Size> [<debug level>]
	public static void main(String args[]) {
		
		//a possible solution for getting the degree and sequence could be parsing it from the .gbk filename
		//Checks arguments to make there is the proper amount 
		if(args.length > 5 || args.length < 3)
		{
			printUsage();
		}
		
	}
	
	private static void printUsage(){
		System.err.println("Usage: Java GeneBankSearch "
				+ "<0/1(no/with Cache)> <Btree File>"
				+ " <Query File> <Cache Size> [<Debug level>]");
	}

}
