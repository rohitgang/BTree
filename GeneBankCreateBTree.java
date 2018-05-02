package BTree;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;

/**
 * 
 * TODO javadoc goes here!
 *
 */

public class GeneBankCreateBTree{

	static int cacheFlag, degreeArg, sequenceSize, cacheSize, debugArg;
	static String gbkFilename;

	
	public static void main(String args[]){
		
		parseArgs(args);
		
		try {
			BTree bt;
			File gbkFile = new File(gbkFilename);
			BufferedReader gbkInput = new BufferedReader(new FileReader(gbkFile));
			
			if (cacheFlag == 1) {
				Cache cache = new Cache(cacheSize);
				bt = new BTree(degreeArg, sequenceSize, gbkFilename, cache);
			}
			else{
				bt = new BTree(degreeArg, sequenceSize, gbkFilename, null);
			}
			
			StringBuilder fullSequence = new StringBuilder("Start");
			
			while (fullSequence != null) {
				fullSequence = parseGbkFile(gbkInput);
				if (fullSequence != null) {

					//add subsequences to the tree
					int seqLength = sequenceSize;
					int endOfSubseq = fullSequence.length() - seqLength;
					String subSequence;
					long convertedSequence;
					for (int i = 0; i < endOfSubseq; i++) {	
						subSequence = fullSequence.substring(i, i + seqLength);
						if (subSequence.contains("N")) {
							subSequence = null;
						}
						else {
							convertedSequence = bt.sequenceToLong(subSequence);
							bt.insert(convertedSequence);
						}
					}
				}
			}
			gbkInput.close();
			if (cacheFlag == 1) {
				bt.writeCache();
			}
			
			System.out.println("The B-Tree was created successfully!");
			System.out.println("The following files were created.");
			System.out.println("Metadata file: " + gbkFilename + ".btree.metadata." + sequenceSize  + "." + degreeArg );
			System.out.println("B-Tree binary file: "  + gbkFilename + ".btree.data." + sequenceSize  + "." + degreeArg);

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
	}
	
	//GeneBankCreateBTree <degree> <gbk file> <sequence length> [<debug level>]
	//TODO GeneBankCreateBTree <cache 0/1> <degree> <gbk file> <sequence length> <cache size> [<debug level>]
	private static void parseArgs(String args[]) {
		if(args.length < 5 || args.length > 6) printUsage();

		try{			
			cacheFlag = Integer.parseInt(args[0]);
			cacheSize = Integer.parseInt(args[4]);
			if(cacheFlag != 0 && cacheFlag != 1) printUsage();
			
			degreeArg = Integer.parseInt(args[1]);
			if(degreeArg < 2) printUsage();
			//if the degree arg is 0 configure degree so that each node fits within a memory block of size 4096
			//each node has (2*t-1)*(8+4) + (2*t)*8 + 4 + 4 + 8 bytes
			//(2*t-t)*(8+4) + (2*t)*8 + 4 + 4 + 8 = 4096 => t = 145 => degree = 2*145
			if(degreeArg == 0) degreeArg = 145;
			
			gbkFilename = args[2];
			
			sequenceSize = Integer.parseInt(args[3]);
			if(sequenceSize < 1 || sequenceSize > 31) printUsage();
			
			if (args.length > 3) debugArg = Integer.parseInt(args[5]);
			
		}catch(NumberFormatException e){
			printUsage();
		}

	}
	

	private static StringBuilder parseGbkFile(BufferedReader gbkInput) {		
		String dnaSequence = null;
		StringBuilder fullSequence = null;
				
		try {
			
			do{
				dnaSequence = gbkInput.readLine();
			}while(dnaSequence != null && !dnaSequence.startsWith("ORIGIN"));
			
			if (dnaSequence == null)
			{
				return fullSequence;
			}
			
			char token = 0;
			fullSequence = new StringBuilder();
			while(token != '/'){
				token = (char) gbkInput.read();
				token = Character.toUpperCase(token);

				switch(token){
				case 'A':
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
					fullSequence.append(Character.toString(token));
					break;
				}
			}	
			
		} catch (IOException e) {
			System.err.print("Invalid File");
			e.printStackTrace();
		}		
		return fullSequence;
	}

	private static void printUsage()
	{
		System.err.println("Usage: Java GeneBankCreateBTree <cache 0/1> <degree> <gbk file> <sequence length> <cache size> [<debug level>]");
		System.err.println("<cache>: 0 for no cache or 1 to use a cache");
		System.err.println("<degree>: Degree of Btree, 0 will default to a block size of 4096");
		System.err.println("<gbk file>: file with sequences saved to it");
		System.err.println("<sequence length>: length of subsequences allowed values are 1-31");
		System.err.println("<Cache Size>: If cache is enabled this will be the size desired by the user");
		System.err.println("[<Debug Level>]: 0 for helpful diagnostics, 1 to dump information to a file ");
		System.exit(1);
	}

}
