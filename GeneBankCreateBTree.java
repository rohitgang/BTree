//package BTree;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

//GeneBankCreateBTree <degree> <gbk file> <sequence length> [<debug level>]
//With cache: GeneBankCreateBTree <cache 0/1> <degree> <gbk file> <sequence length> <cache size> [<debug level>]

public class GeneBankCreateBTree{

	private static boolean useCache = false;
	private static int treeDegree, degreeArg, sequenceSize;
	private final static int MAX_SEQUENCE_LENGTH = 31;
	private static String formattedFileName;
	static ArrayList<String> sequences = new ArrayList();


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

		try {
			dnaSequence = gbkInput.readLine().toUpperCase().trim();


			while(!dnaSequence.startsWith("ORIGIN")){
				dnaSequence = gbkInput.readLine();
			}
			
			char token = 0;
//			String fullSequence = null;
			StringBuilder fullSequence = new StringBuilder();
			int index = 0;

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
			//I cannot get the sequence to print to the console. however if you go into debug you can see that the whole sequence is there.
			System.out.println(fullSequence);
			
		} catch (IOException e) {
			System.err.print("Invalid File");
			e.printStackTrace();
		}
		
		
	}



	//		BTree bt;
	//		try {
	//			bt = new BTree(4, 4, "test-tree");
	//			Random r = new Random();
	//
	//			for(int i = 0; i<100; i++) {
	//				long sequence = (long)r.nextLong();
	//				bt.insert(sequence);
	//			}
	//		} catch (IOException e) {
	//			// TODO Auto-generated catch block
	//			e.printStackTrace();
	//		}









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

