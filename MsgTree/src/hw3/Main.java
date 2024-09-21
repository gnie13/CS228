package hw3;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.stream.Collectors;

/**
 * Main class to run the decoding  
 * 
 * @author Gavin Nienke
 */
public class Main {
	public static void main(String[] args) throws IOException {
		System.out.println("Please enter filename to decode:");
		try (Scanner scanner = new Scanner(System.in)) {
			String fileName = scanner.nextLine();

			String content = new String(Files.readAllBytes(Paths.get(fileName))).trim();
			int pos = content.lastIndexOf('\n');
			String pattern = content.substring(0, pos);
			String binCode = content.substring(pos).trim();

			String chardict = pattern.chars().filter(c -> c != '^').distinct().mapToObj(c -> String.valueOf((char) c))
					.collect(Collectors.joining());

			MsgTree root = new MsgTree(pattern);
			MsgTree.printCodes(root, chardict);
			root.decode(root, binCode);
		}
	}
}