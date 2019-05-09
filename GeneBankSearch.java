import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Scanner;

/*
 * driver class to search for a key in an object
 * 
 * USAGE:
 * 								1				2			3				4				5
 * java GeneBankSearch <0/1(no/with Cache)> <btree file> <query file> [<cache size>] [<debug level>]
 */
public class GeneBankSearch {

	static boolean isCacheUsed = false ;
	static int cacheSize;
	static BTree tree ;
	static BTreeNode root ;
	static int degree;
	static int nodeOffset;
	
	private static RandomAccessFile BTFile;
	private static FileChannel dataChannel;
	
	
	
	public static void main(String[] args) throws IOException {
		
		
		//checks to make sure args array is in the possible range 3-5
		if (args.length < 3 || args.length > 5) {
			printUsage();
		}
		
		//args 1
//		if(args[0].equals("1")) {
//			isCacheUsed = true;
//		} else if (args[0].equals("0") || args[0].equals("1")){
//			printUsage();
//		}
		int dotCount = 0;
		int seqLength = 0;
		
		for(int i= args[1].length()-1 ; i >= 0; i--) {
			char ch = args[1].charAt(i) ;
			if(ch == '.') {
				dotCount += 1 ;
				continue;
			}
			if(dotCount == 0)
				degree =  Character.getNumericValue(ch) ;
			if(dotCount == 1)
				seqLength = Character.getNumericValue(ch) ;
			if(dotCount == 2)
				break ;
		}
		
		nodeOffset = (32 * degree) - 3;
		// Get BTree metadata, open file, start stream---------------------------------------------------
		BTFile = new RandomAccessFile(new File(args[1]), "r"); //BTree File
		dataChannel = BTFile.getChannel();
		
		
		BTFile.seek(12);
		int rootAddress = BTFile.readInt();
		
		root = readNode(rootAddress);
		
		
		//-------------------------------------------------------------------------------------
		File queryFile = new File(args[2]); //Query File
		
		if (isCacheUsed=true && args.length >= 4) {
			cacheSize = Integer.parseInt(args[3]);
		}	
		
		//System.out.println("degree: "+degree);
		searchProcedure(queryFile);
	}
	
	
	public static BTreeNode readNode(int address) {
		BTreeNode storedNode = new BTreeNode(degree);
		try {
			ByteBuffer buffer = ByteBuffer.allocate(nodeOffset + 100); // CHANGE TO MAX

			dataChannel.read(buffer, address);
			buffer.flip();
			storedNode.recover(buffer);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return storedNode;
	}
	
	public static TreeObject search(BTreeNode node, long key) {
		int i = 0;
		while (i < node.getObjectCount() && key > node.objectArray[i].getKey()) {
			i = i + 1;
		}

		if (i < node.getObjectCount() && key == node.objectArray[i].getKey()) {
			return node.objectArray[i]; // key matches the object's key, so search successful and return
		} else {
			if (node.isLeaf()) {
				return null;
			} else {
				BTreeNode childNode = readNode(node.childPtrs[i]);
				return search(childNode, key);
			}
		}
	}
	

	
	public static void searchProcedure(File query) throws FileNotFoundException {
		Scanner scan = new Scanner(query) ;
		while(scan.hasNextLine()) {
			String geneSeq = scan.nextLine();
			long key = dnaToBinary(geneSeq);
			TreeObject object = search(root, key);
			if(object != null)
				System.out.println(object.toString());
		}
		scan.close();
	}
	
	

	public static long dnaToBinary(String dnaBases) {
		// Convert string to char Array.
		// Initialize string-builder with 1(to prevent loss of leading zeroes).
		// For each character in array, append corresponding binary value to string.
		// Convert string-builder to a long and return it.
		char dnaChar[] = dnaBases.toCharArray();
		StringBuilder temp = new StringBuilder("1"); // Prevents loss of leading zeroes for later conversion to letter
														// bases.
		for (char gene : dnaChar) {
			switch (gene) {
			case 'a':
			case 'A':
				temp.append("00");
				break;
			case 'c':
			case 'C':
				temp.append("01");
				break;
			case 'g':
			case 'G':
				temp.append("10");
				break;
			case 't':
			case 'T':
				temp.append("11");
				break;
			}
		}
		long binaryDNA = Long.parseLong(temp.toString(), 2); // Converts the string to a long (w/ radix 2 for binary)
		return binaryDNA;
	}
	
	/**
	 * Converts a long (in binary form) to a String containing its corresponding
	 * gene bases. (brandon)
	 * 
	 * @param long binaryDNA
	 * @return String geneString
	 */
	public static String binaryToDNA(long binaryDNA) {
		// Convert given long to its string representation. Initialize geneString.
		// Skip the leading 1 in the long, compare every two bits against the gene
		// values, append correct gene base.
		String num = Long.toBinaryString(binaryDNA);
		StringBuilder geneString = new StringBuilder();
		for (int i = 1; i < num.length(); i += 2) { // Skip the first protecting '1' in string, compare each two bits
			String build = "" + num.charAt(i) + num.charAt(i + 1); // Forms string with two bits, you need the ""
			switch (build) {
			case "00":
				geneString.append('A');
				break;
			case "01":
				geneString.append('C');
				break;
			case "10":
				geneString.append('G');
				break;
			case "11":
				geneString.append('T');
				break;
			}
		}
		return geneString.toString();
	}
	// prints the usage of the program
	public static void printUsage () {
		System.err.println("java GeneBankSearch <0/1(no/with Cache)> <btree file> <query file> [<cache size>] [<debug level>]");
		System.err.println("cache: "		+ "iscacheused"		+ "\n");
		System.err.println("btree file: "	+ "SOMETHING.gbk" 	+ "\n");
		System.err.println("query file: "	+ "query_result" 	+ "\n");
		System.err.println("cache size: " 	+ "cacheSIZE" 		+ "\n");
		System.err.println("debug level: " 	+ "debugLevel" 		+ "\n");
		System.exit(1);	//some issue occurred for usage to be printed, likely incorrect argument values
	}
}
