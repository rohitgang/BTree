TODO: Describe the layout of the B-Tree file on disk
TODO: Report on how using cache sizes 100 and 500 affects the performance of the program

****************
* BTree
* CS321
* 5/5/2018
* Ben Mcavoy, Nick Figura, Ben Peterson 
**************** 

OVERVIEW:

Creates a BTree based on gene sequences from a gbk (gene bank file) file. The BTree can then be searched for occurrences of 
specific sequences from the BTree. 
 
INCLUDED FILES:
Source Files:
* BTree.java - The main class for the BTRee. It can construct and search a BTree.
* BtreeNode.java - Node that is stored in the BTree. Each Node holds multiple sequences and children.
* Cache.java - A BTree specific cache that is used to reduce the number of disk reads and writes while
creating and searching the BTree.
* GeneBankCreateBTree.java - Creates a Btree based on a gbk file. See the compiling and running section of this document 
for more details on its operation.
* GeneBankSearch.java - Searches a BTree for certain sequences from a query file. See the compiling and running section 
of this document for more details on its operation.
* TreeObject.java - Stores the gene sequence and the frequence of the gene sequence. These are stored in the BTreeNode 
class. 
 Other Files:
 * README - this file


COMPILING AND RUNNING:

Place all of the above listed source files in the same directory. Run the following
command for the command line to compile:
 $ javac *.java

Execute the following command to run the GeneBankCreateBTree.java:
 $ java  GeneBankCreateBTree <cache 0/1> <degree> <gbk file> <sequence length> <cache size> [<debug level>]

<cache>: 0 for no cache or 1 to use a cache
<degree>: Degree of Btree, 0 will default to a block size of 4096
<gbk file>: file with sequences saved to it
<sequence length>: length of subsequences allowed values are 1-31
<Cache Size>: If cache is enabled this will be the size desired by the user
[<Debug Level>] (optional): 0 for helpful diagnostics, 1 to dump information to a file. Defaults to 0. 
 
The result of this program will be 2 files, a meta data file and a file containing the BTree. The files will be named:

BTree file: gbkFilename.btree.data.sequenceLength.degree (ex: test2.gbk.btree.data.5.5)
Meta data file:  gbkFilename.btree.metadata.sequenceLength.degree (ex: test2.gbk.btree.metadata.5.5)

 
Execute the following command to run the GeneBankSearch.java: 
 $ java Java GeneBankSearch <0/1(no/with Cache)> <Btree File> <Query File> <Cache Size> [<Debug level>]
 
<cache>: 0 for no cache or 1 to use a cache
<degree>: Degree of Btree, 0 will default to a block size of 4096
<BTree File>: The file created by the GeneBankCreateBTree program. Tree metadata file created by GeneBankCreateBTree
must also be present.
<Cache Size>: If cache is enabled this will be the size desired by the user
[<Debug level>] (optional): 0 will enable debugging output. The default is zero.

This will 

 
PROGRAM DESIGN:

