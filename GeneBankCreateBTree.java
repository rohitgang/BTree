package BTree;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Random;

//GeneBankCreateBTree <degree> <gbk file> <sequence length> [<debug level>]
//With cache: GeneBankCreateBTree <cache 0/1> <degree> <gbk file> <sequence length> <cache size> [<debug level>]

public class GeneBankCreateBTree{

	private static boolean useCache = false;
	private static int treeDegree, degreeArg, sequenceSize;
	private final static int MAX_SEQUENCE_LENGTH = 31;
	private static String formattedFileName;


	public static void main(String args[]){
		//Checks to see if the proper amount of arguments has been entered.
		if(args.length < 3 || args.length > 6){
			printUsage();
		}

		//Check to see if the user wants to use a cache
		//		try{
		//			int numericCacheFlag = Integer.parseInt(args[0]);
		//			if(numericCacheFlag == 1){
		//				useCache = true;
		//			}else if(numericCacheFlag == 0){
		//				useCache = false;
		//			}else{
		//				printUsage();
		//			}
		//		}catch(NumberFormatException e){
		//			printUsage();
		//		}

		//Check degree argument
		try{
			degreeArg = Integer.parseInt(args[0]);
			if(degreeArg < 0){
				System.out.println("Failed in degree check");
				printUsage();
			}else if(degreeArg == 0){
				treeDegree = 102;
			}else {
				treeDegree = degreeArg;
			}
		}catch(NumberFormatException e){
			printUsage();
		}

		//Check sequence argument to make sure it is correct
		try{
			sequenceSize = Integer.parseInt(args[2]);
			if(sequenceSize < 1 || sequenceSize > MAX_SEQUENCE_LENGTH){
				printUsage();
			}
		}catch(NumberFormatException e){
			printUsage();
		}

		//I will come back during the week of 4/16/2018 to update and finish checking cache arguments

		//Reading a parsing the gbk file
		File gbkFile = new File(args[1]);
		BufferedReader gbkInput = null;

		try{
			gbkInput = new BufferedReader(new FileReader(gbkFile));
		}catch(FileNotFoundException e){
			e.printStackTrace();
		}

		String dnaSequence = null;
		StringBuilder fullSequence = null;
		
		try {
			dnaSequence = gbkInput.readLine().toUpperCase().trim();


			while(!dnaSequence.startsWith("ORIGIN")){
				dnaSequence = gbkInput.readLine();
			}
			
			char token = 0;
			fullSequence = new StringBuilder();

			while(token != '/'){
				token = (char) gbkInput.read();
				token = Character.toUpperCase(token);

				switch(token){
				case 'A':
					//Add in += to string for each of the following.
					fullSequence.append(Character.toString(token));
					break;
				case 'T':
					fullSequence.append(Character.toString(token));
					break;
				case 'C':
					fullSequence.append(Character.toString(token));;
					break;
				case 'G':
					fullSequence.append(Character.toString(token));
					break;
				case 'N':
					continue;
				default:
					continue;
				}

			}
			
		} catch (IOException e) {
			System.err.print("Invalid File");
			e.printStackTrace();
		}
		
		//create tree
		BTree bt;
		try {
			String  filename = args[1];
			bt = new BTree(treeDegree, sequenceSize, filename);

			//add subsequences to the tree
			int seqLength = sequenceSize - 1;
			int endOfSubseq = fullSequence.length() - seqLength;
			String subSequence;
			long convertedSequence;
			for (int i = 0; i < endOfSubseq; i++) {	
				subSequence = fullSequence.substring(i, i + seqLength);
				convertedSequence = bt.sequenceToLong(subSequence);
				bt.insert(convertedSequence);
			}
			System.out.println("The B-Tree was created succefully!");
			System.out.println("The following files were created.");
			System.out.println("Metadata file: " + filename + ".btree.metadata." + sequenceSize  + "." + treeDegree);
			System.out.println("B-Tree binary file: "  + filename + ".btree.data." + sequenceSize  + "." + treeDegree );
			//check debug
			int debugArg = 0;
			if (args.length > 3) {
				debugArg = Integer.parseInt(args[3]);
			}
			if (debugArg == 1) {
				//use stream to capture system output from btree node
				PrintStream fileOutput = new PrintStream(new File("debug"));
				//save current output so it can be restored
				PrintStream console = System.out;
				//change output to file
				System.setOut(fileOutput);
				bt.print(bt.root, true);
				//restore output
				System.setOut(console);
				System.out.println("B-Tree key file: debug");
			}	
			
		} catch (IOException e) {
			System.out.println("Error in Btree creation and output");
			e.printStackTrace();
		}

//		//Test the BTree!
//		try {
//			BTree bt = new BTree(2, 3, "test-tree");
//			System.out.println(bt.sequenceToLong("ACCTT"));
//			System.out.println(bt.longToSequence(980, 5));
//			
//			long[] longs = {1,2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15};
//			for(int i = 0; i<longs.length; i++) {
//				bt.insert(longs[i]);
//			}
//			System.out.println("ROOT!");
//			bt.root.printNode();
//			System.out.println("ROOT!");
//			bt.print(bt.root);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}		

	}

	private static void printUsage()
	{
		System.err.println("Usage: Java GeneBankCreateBTree <cache 0/1> <degree> <gbk file> <sequence length> <cache size> [<debug level>]");
		System.err.println("<cache>: 0 for no cache or 1 to use a cache");
		System.err.println("<degree>: Degree of Btree, 0 will default to a block size of 4096");
		System.err.println("<gbk file>: file with sequences saved to it");
		System.err.print("<sequence length>: length of subsequences allowed values are 1-31");
		System.err.println("<Cache Size>: If cache is enabled this will be the size desired by the user");
		System.err.println("[<Debug Level>]: 0 for helpful diagnostics, 1 to dump information to a file ");
		System.exit(1);
	}

}

