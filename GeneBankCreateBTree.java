package BTree;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;

//GeneBankCreateBTree <degree> <gbk file> <sequence length> [<debug level>]
//TODO With cache: GeneBankCreateBTree <cache 0/1> <degree> <gbk file> <sequence length> <cache size> [<debug level>]

public class GeneBankCreateBTree{

	static boolean useCache;
	static int treeDegree, degreeArg, sequenceSize;

	public static void main(String args[]){
		
		parseArgs(args);
		StringBuilder fullSequence = parseGbkFile(args[1]);
		
		try {
			String  filename = args[1];
			BTree bt = new BTree(treeDegree, sequenceSize, filename);

			//add subsequences to the tree
			int seqLength = sequenceSize;
			int endOfSubseq = fullSequence.length() - seqLength;
			String subSequence;
			long convertedSequence;
			for (int i = 0; i < endOfSubseq; i++) {	
				subSequence = fullSequence.substring(i, i + seqLength);
				convertedSequence = bt.sequenceToLong(subSequence);
				bt.insert(convertedSequence);
			}
			System.out.println("The B-Tree was created successfully!");
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
	}
	
	private static void parseArgs(String args[]) {
		if(args.length < 3 || args.length > 6) printUsage();

		try{
//			int numericCacheFlag = Integer.parseInt(args[0]);
//			if(numericCacheFlag == 1){
//				useCache = true;
//			}else if(numericCacheFlag == 0){
//				useCache = false;
//			}else{
//				printUsage();
//			}
			
			degreeArg = Integer.parseInt(args[0]);
			if(degreeArg < 0){
				printUsage();
			}else if(degreeArg == 0){
				treeDegree = 102;
			}else {
				treeDegree = degreeArg;
			}
			
			sequenceSize = Integer.parseInt(args[2]);
			if(sequenceSize < 1 || sequenceSize > 31){
				printUsage();
			}
		}catch(NumberFormatException e){
			printUsage();
		}

	}
	
	private static StringBuilder parseGbkFile(String filename) {
		File gbkFile = new File(filename);
		BufferedReader gbkInput = null;		
		String dnaSequence = null;
		StringBuilder fullSequence = null;
				
		try {
			gbkInput = new BufferedReader(new FileReader(gbkFile));
			
			do{
				dnaSequence = gbkInput.readLine();
			}while(!dnaSequence.startsWith("ORIGIN"));
			
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
		System.err.print("<sequence length>: length of subsequences allowed values are 1-31");
		System.err.println("<Cache Size>: If cache is enabled this will be the size desired by the user");
		System.err.println("[<Debug Level>]: 0 for helpful diagnostics, 1 to dump information to a file ");
		System.exit(1);
	}

}

