package edu.iastate.cs228.hw1;

/**
 * 
 * @author Gavin Nienke
 *
 */

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.io.File;


/**
 * 
 * This class sorts all the points in an array of 2D points to determine a
 * reference point whose x and y coordinates are respectively the medians of the
 * x and y coordinates of the original points.
 * 
 * It records the employed sorting algorithm as well as the sorting time for
 * comparison.
 *
 */
public class PointScanner {

	String fileName;
	private Point[] points;
	private Point medianCoordinatePoint; // point whose x and y coordinates are respectively the medians of
											// the x coordinates and y coordinates of those points in the array
											// points[].
	private Algorithm sortingAlgorithm;
	protected long scanTime; // execution time in nanoseconds.

	/**
	 * This constructor accepts an array of points and one of the four sorting
	 * algorithms as input. Copy the points into the array points[].
	 * 
	 * @param pts input array of points
	 * @throws IllegalArgumentException if pts == null or pts.length == 0.
	 */
	public PointScanner(Point[] pts, Algorithm algo) throws IllegalArgumentException {
		if (pts.length == 0 || pts == null) {
			throw new IllegalArgumentException();
		} else {
			points = pts;
			sortingAlgorithm = algo;
		}
	}

	/**
	 * This constructor reads points from a file.
	 * 
	 * @param inputFileName
	 * @throws FileNotFoundException
	 * @throws InputMismatchException if the input file contains an odd number of
	 *                                integers
	 */
	protected PointScanner(String inputFileName, Algorithm algo) throws FileNotFoundException, InputMismatchException {

		fileName = inputFileName;
		sortingAlgorithm = algo;

		try (Scanner fileScan = new Scanner(new File(inputFileName))) {
			int numIntegers = 0;
			
			// Count the number of integers in the file
			while (fileScan.hasNext()) {
				numIntegers++;
				fileScan.nextInt();

			}
			 // Check if the number of integers is odd
			if (numIntegers % 2 != 0) {
				throw new InputMismatchException("Input file contains an odd number of integers.");
			}
			  // Initialize the points array with half the number of integers
			points = new Point[numIntegers / 2];
			 //statement to ensure the scanner is closed after use
			try (Scanner fileScan2 = new Scanner(new File(inputFileName))) {
				// Read the integers from the file and create Point objects
				for (int i = 0; i < points.length; i++) {
					int x = fileScan2.nextInt();
					int y = fileScan2.nextInt();
					points[i] = new Point(x, y);
				}
			}
		}
	}

	/**
	 * Carry out two rounds of sorting using the algorithm designated by
	 * sortingAlgorithm as follows:
	 * 
	 * a) Sort points[] by the x-coordinate to get the median x-coordinate. b) Sort
	 * points[] again by the y-coordinate to get the median y-coordinate. c)
	 * Construct medianCoordinatePoint using the obtained median x- and
	 * y-coordinates.
	 * 
	 * Based on the value of sortingAlgorithm, create an object of SelectionSorter,
	 * InsertionSorter, MergeSorter, or QuickSorter to carry out sorting.
	 * 
	 * @param algo
	 * @return
	 */
	public void scan() {

		// create an object to be referenced by aSorter according to sortingAlgorithm.
		// for each of the two
		// rounds of sorting, have aSorter do the following:
		//
		// a) call setComparator() with an argument 0 or 1.
		//
		// b) call sort().
		//
		// c) use a new Point object to store the coordinates of the
		// medianCoordinatePoint
		//
		// d) set the medianCoordinatePoint reference to the object with the correct
		// coordinates.
		//
		// e) sum up the times spent on the two sorting rounds and set the instance
		// variable scanTime.

		AbstractSorter aSorter = null;

		switch (sortingAlgorithm) {
		case SelectionSort:
			aSorter = new SelectionSorter(points);
			break;
		case InsertionSort:
			aSorter = new InsertionSorter(points);
			break;
		case MergeSort:
			aSorter = new MergeSorter(points);
			break;
		case QuickSort:
			aSorter = new QuickSorter(points);
			break;
		}

		aSorter.setComparator(0);
		long start = System.nanoTime();
		aSorter.sort();
		long end = System.nanoTime();
		long xTime = end - start;
		int medianXCoord = aSorter.getMedian().getX();

		aSorter.setComparator(1);
		start = System.nanoTime();
		aSorter.sort();
		end = System.nanoTime();
		long yTime = end - start;
		int medianYCoord = aSorter.getMedian().getY();
		medianCoordinatePoint = new Point(medianXCoord, medianYCoord);
		scanTime = xTime + yTime;
	}

	/**
	 * Outputs performance statistics in the format:
	 * 
	 * <sorting algorithm> <size> <time>
	 * 
	 * For instance,
	 * 
	 * selection sort 1000 9200867
	 * 
	 * Use the spacing in the sample run in Section 2 of the project description.
	 */
	public String stats() {

		String stats = null;

		if (sortingAlgorithm == Algorithm.SelectionSort) {
			stats = "SelectionSort    " + points.length + "  " + scanTime;// SelectionSort
		}
		if (sortingAlgorithm == Algorithm.InsertionSort) {
			stats = "InsertionSort    " + points.length + "  " + scanTime;// InsertionSort
		}
		if (sortingAlgorithm == Algorithm.MergeSort) {
			stats = "MergeSort        " + points.length + "  " + scanTime;// MergeSort
		}
		if (sortingAlgorithm == Algorithm.QuickSort) {
			stats = "QuickSort        " + points.length + "  " + scanTime;// QuickSort
		}
		return stats;
	}

	/**
	 * Write MCP after a call to scan(), in the format "MCP: (x, y)" The x and y
	 * coordinates of the point are displayed on the same line with exactly one
	 * blank space in between.
	 */
	@Override
	public String toString() {
		return medianCoordinatePoint.toString();

	}

	/**
	 * 
	 * This method, called after scanning, writes point data into a file by
	 * outputFileName. The format of data in the file is the same as printed out
	 * from toString(). The file can help you verify the full correctness of a
	 * sorting result and debug the underlying algorithm.
	 * 
	 * @throws FileNotFoundException
	 */
	public void writeMCPToFile() throws FileNotFoundException {
        try (FileWriter file = new FileWriter(fileName)) {
            file.write(toString());
        } catch (Exception e) {
            throw new FileNotFoundException("Error writing to file: " + e.getMessage());
        }
    }

}
