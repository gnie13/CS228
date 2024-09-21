package edu.iastate.cs228.hw1;

//Deleted unused imports. 

/**
 *  
 * @author Gavin Nienke
 *
 */

/**
 * 
 * This class implements the version of the quicksort algorithm presented in the
 * lecture.
 *
 */

public class QuickSorter extends AbstractSorter {

	// Other private instance variables if you need ...

	/**
	 * Constructor takes an array of points. It invokes the superclass constructor,
	 * and also set the instance variables algorithm in the superclass.
	 * 
	 * @param pts input array of integers
	 */
	public QuickSorter(Point[] pts) {
		super(pts);
		super.algorithm = "quicksort";
	}

	/**
	 * Carry out quicksort on the array points[] of the AbstractSorter class.
	 * 
	 */
	@Override
	public void sort() {
		quickSortRec(0, points.length - 1); // recursively calls to quick sort array.
	}

	/**
	 * Operates on the subarray of points[] with indices between first and last.
	 * 
	 * @param first starting index of the subarray
	 * @param last  ending index of the subarray
	 */
	private void quickSortRec(int first, int last) {
		if (first >= last) {
			return;
		}
		int newPartition = partition(first, last);
		quickSortRec(first, newPartition - 1);
		quickSortRec(newPartition + 1, last);

	}

	/**
	 * Operates on the subarray of points[] with indices between first and last.
	 * 
	 *  @param first starting index of the subarray
	 * @param last ending index of the subarray
	 * @returnthe index of the pivot element after partitioning
	 */
	private int partition(int first, int last) {
	    // Choose a pivot element from within the subarray [first, last]
	    int pivotIndex = first + (last - first) / 2;
	    Point pivot = points[pivotIndex];

	    // Move the pivot element to the end of the subarray
	    swap(pivotIndex, last);

	    int newIndex = first;

	    // Partition the subarray around the pivot
	    for (int i = first; i < last; i++) {
	        if (points[i].compareTo(pivot) <= 0) {
	            swap(i, newIndex);
	            newIndex++;
	        }
	    }

	    // Move the pivot element to its final position
	    swap(newIndex, last);
	    return newIndex;
	}
}
