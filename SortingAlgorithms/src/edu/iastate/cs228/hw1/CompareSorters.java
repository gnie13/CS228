package edu.iastate.cs228.hw1;

/**
 *  
 * @author Gavin Nienke
 *
 */

/**
 * 
 * This class executes four sorting algorithms: selection sort, insertion sort, mergesort, and
 * quicksort, over randomly generated integers as well integers from a file input. It compares the 
 * execution times of these algorithms on the same input. 
 *
 */

import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.Random;

public class CompareSorters {
	/**
	 * Repeatedly take integer sequences either randomly generated or read from
	 * files. Use them as coordinates to construct points. Scan these points with
	 * respect to their median coordinate point four times, each time using a
	 * different sorting algorithm.
	 * 
	 * @param args
	 **/
	public static void main(String[] args) throws FileNotFoundException {
		//
		// Conducts multiple rounds of comparison of four sorting algorithms. Within
		// each round,
		// set up scanning as follows:
		//
		// a) If asked to scan random points, calls generateRandomPoints() to initialize
		// an array
		// of random points.
		//
		// b) Reassigns to the array scanners[] (declared below) the references to four
		// new
		// PointScanner objects, which are created using four different values
		// of the Algorithm type: SelectionSort, InsertionSort, MergeSort and QuickSort.
		//
		//
		PointScanner[] scanners = new PointScanner[4];
		// Array to hold four PointScanner objects
		Scanner scanner = new Scanner(System.in);
		// Trial counter
		int trial = 1;

		System.out.println("Performances of Four Sorting Algorithms in Point Scanning");
		System.out.println();
		System.out.println("keys:  1 (random integers)  2 (file input)  3 (exit)");

		while (true) {

			System.out.print("Trial " + trial + ": ");
			// Read user's selection
			int select = scanner.nextInt();

			if (select == 3) {
				// Exit the loop if user selects 3
				break;
			} else if (select == 1 || select == 2) {
				Point[] pts;
				if (select == 1) {
					System.out.print("Enter number of random points: ");
					int randPoints = scanner.nextInt();
					// Generate random points
					pts = generateRandomPoints(randPoints, new Random());
				} else {
					System.out.print("File name: ");
					// Read file name from user
					String fileName = scanner.next();
					// Create PointScanner objects for each sorting algorithm using the file input
					scanners[0] = new PointScanner(fileName, Algorithm.SelectionSort);
					scanners[1] = new PointScanner(fileName, Algorithm.InsertionSort);
					scanners[2] = new PointScanner(fileName, Algorithm.MergeSort);
					scanners[3] = new PointScanner(fileName, Algorithm.QuickSort);
					trial++;
					continue;
				}
				// Create PointScanner objects for each sorting algorithm using the generated
				// points
				scanners[0] = new PointScanner(pts, Algorithm.SelectionSort);
				scanners[1] = new PointScanner(pts, Algorithm.InsertionSort);
				scanners[2] = new PointScanner(pts, Algorithm.MergeSort);
				scanners[3] = new PointScanner(pts, Algorithm.QuickSort);

				System.out.println();
				System.out.println("algorithm   size  time (ns)");
				System.out.println("----------------------------------");
				// Scan points with each PointScanner and print statistics for each algorithm
				for (PointScanner scan : scanners) {
					scan.scan();
					System.out.println(scan.stats());
				}
				System.out.println("----------------------------------");
				System.out.println();

				trial++;
			}
		}

		scanner.close();
	}

	// For each input of points, do the following.
	//
	// a) Initialize the array scanners[].
	//
	// b) Iterate through the array scanners[], and have every scanner call the
	// scan()
	// method in the PointScanner class.
	//
	// c) After all four scans are done for the input, print out the statistics
	// table from
	// section 2.
	//
	// A sample scenario is given in Section 2 of the project description.

	/**
	 * This method generates a given number of random points. The coordinates of
	 * these points are pseudo-random numbers within the range [-50,50] � [-50,50].
	 * Please refer to Section 3 on how such points can be generated.
	 * 
	 * Ought to be private. Made public for testing.
	 * 
	 * @param numPts number of points
	 * @param rand   Random object to allow seeding of the random number generator
	 * @throws IllegalArgumentException if numPts < 1
	 */
	public static Point[] generateRandomPoints(int numPts, Random rand) throws IllegalArgumentException {
		if (numPts < 1) {
			throw new IllegalArgumentException("Invalid number of points; must be greater than 0.");
		}

		Point[] points = new Point[numPts];// builds array for the randomly generated points.
		for (int i = 0; i < numPts; i++) {
			int xCoord = rand.nextInt(101) - 50;
			int yCoord = rand.nextInt(101) - 50;
			points[i] = new Point(xCoord, yCoord);// builds new object for Point with the random coords.
		}
		return points;

	}

}
