package edu.iastate.cs228.hw1;

//deleted unused imports. 
import java.util.Arrays;

/**
 *  
 * @author Gavin Nienke
 *
 */

/**
 * 
 * This class implements the mergesort algorithm.
 *
 */

public class MergeSorter extends AbstractSorter {
	// Other private instance variables if needed

	/**
	 * Constructor takes an array of points. It invokes the superclass constructor,
	 * and also set the instance variables algorithm in the superclass.
	 * 
	 * @param pts input array of integers
	 */
	public MergeSorter(Point[] pts) {
		super(pts);
		super.algorithm = "merge sort";
	}

	/**
	 * Perform mergesort on the array points[] of the parent class AbstractSorter.
	 * 
	 */
	@Override
	public void sort() {
		mergeSortRec(points);
	}

	/**
	 * This is a recursive method that carries out mergesort on an array pts[] of
	 * points. One way is to make copies of the two halves of pts[], recursively
	 * call mergeSort on them, and merge the two sorted subarrays into pts[].
	 * 
	 * @param pts point array
	 */
	private void mergeSortRec(Point[] pts) {
		if (pts.length <= 1) {
			return;
		}

		int mid = pts.length / 2;
		Point[] pointsLeft = Arrays.copyOfRange(pts, 0, mid);//create subarray of left half
		Point[] pointsRight = Arrays.copyOfRange(pts, mid, pts.length);//create subarray of right half

		mergeSortRec(pointsLeft);
		mergeSortRec(pointsRight);

		merge(pts, pointsLeft, pointsRight);
	}

	/**
	 * Merges two sorted subarrays into a single sorted array.
	 * 
	 * @param pts the original array
	 * @param left the sorted left subarray
	 * @param right the sorted right subarray
	 */
	private void merge(Point[] pts, Point[] left, Point[] right) {
		int i = 0, j = 0, k = 0;

		while (i < left.length && j < right.length) {
			if (pointComparator.compare(left[i], right[j]) <= 0) {
				pts[k++] = left[i++];
			} else {
				pts[k++] = right[j++];
			}
		}

		while (i < left.length) {
			pts[k++] = left[i++];
		}

		while (j < right.length) {
			pts[k++] = right[j++];
		}
	}

}
