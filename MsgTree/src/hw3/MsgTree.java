package hw3;

import java.util.Stack;

/**
 * MsgTree class for decoding archive files. This class constructs a binary tree
 * from an encoding string and prints statistics.
 * 
 * @author Gavin Nienke
 */
public class MsgTree {
	public char payloadChar;
	public MsgTree left;
	public MsgTree right;

	// private static int staticCharIdx = 0; was not needed
	
	//holds encoded message to be decoded. 
	private static String binCode;

	/**
	 * Constructor building the tree from a string
	 * 
	 * @param encodingString encoded string from the encoded file
	 */
	public MsgTree(String encodingString) {
		if (encodingString == null || encodingString.length() < 2) return;
		
		Stack<MsgTree> stack = new Stack<>();
		int index = 0;
		//initialize root node with the first character. 
		this.payloadChar = encodingString.charAt(index++);
		stack.push(this);
		MsgTree current = this;
		//track last operation, either insertion or traversal. 
		String lastOpt = "in";
		
		while (index < encodingString.length()) {
			MsgTree node = new MsgTree(encodingString.charAt(index++));
			if (lastOpt.equals("in")) {
				//insert node to the left
				current.left = node;
				if (node.payloadChar == '^') {
					// keep building tree if node is internal. 
					current = stack.push(node);
					lastOpt = "in";
				} else {
					if (!stack.empty())
						current = stack.pop();
					lastOpt = "out";
				}
			} else {
				// insert node to the right
				current.right = node;
				if (node.payloadChar == '^') {
					//continue building the tree if it's an internal node
					current = stack.push(node);
					lastOpt = "in";
				} else {
					if (!stack.empty())
						current = stack.pop();
					lastOpt = "out";
				}
			}
		}
	}

	/**
	 * Constructor for a single node with null children
	 * 
	 * @param payloadChar character at payload
	 */
	public MsgTree(char payloadChar) {
		this.payloadChar = payloadChar;
		this.left = null;
		this.right = null;
	}

	/**
	 * Method to print characters and their binary codes
	 * 
	 * @param root the root of the tree
	 * @param code the binary code
	 */
	public static void printCodes(MsgTree root, String code) {
		System.out.println("character  code");
		System.out.println("-------------------------");
		for (char c : code.toCharArray()) {
			getCodes(root, binCode = "", c);
			System.out.println("    " + (c == '\n' ? "\\n" : c + " ") + "    " + binCode);

		}
	}

	/**
	 * Getter method that recursively gets the code.
	 * 
	 * @param root      root of tree
	 * @param path      indicated path
	 * @param character current character
	 * @return true if tree with nodes exists, false otherwise.
	 */
	private static boolean getCodes(MsgTree root, String path, char character) {
		if (root != null) {
			if (root.payloadChar == character) {
				//set the binary code for found character
				binCode = path;
				return true;
			}
			return getCodes(root.left, path + "0", character) || getCodes(root.right, path + "1", character);
		}
		return false;
	}

	/**
	 * Decodes the message and prints to the console.
	 * 
	 * @param codes uncoded message
	 * @param msg   message to the console
	 */
	public void decode(MsgTree codes, String msg) {
		System.out.println("MESSAGE:");
		MsgTree current = codes;
		StringBuilder builder = new StringBuilder();
		
		for (int i = 0; i < msg.length(); i++) {
			char c = msg.charAt(i);
			// traverse the tree based on binary code
			current = (c == '0' ? current.left : current.right);
			//if its a leaf node
			if (current.payloadChar != '^') {
				//append the char to the decoded message
				getCodes(codes, binCode = "", current.payloadChar);
				//reset to root for the next char
				builder.append(current.payloadChar);
				current = codes;
			}
		}
		
		System.out.println(builder.toString());
		//print stats EC
		printStats(msg, builder.toString());
	}

	/**
	 * private method that prints the data (extra credit)
	 * 
	 * @param encodeStringData encoded data of the string
	 * @param decodeStringData decoded data of the string
	 */
	private void printStats(String encodeStringData, String decodeStringData) {
		System.out.println("STATISTICS");
		System.out.println(
				String.format("Avg bits/char:\t%.1f", encodeStringData.length() / (double) decodeStringData.length()));
		System.out.println("Total characters: \t" + decodeStringData.length());
		System.out.println(String.format("Space savings:\t%.1f%%",
				(1d - decodeStringData.length() / (double) encodeStringData.length()) * 100));
	}
}
