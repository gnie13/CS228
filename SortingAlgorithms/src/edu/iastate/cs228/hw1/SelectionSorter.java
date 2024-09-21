package edu.iastate.cs228.hw1;

//deleted unused imports. 

/**
 *  
 * @author Gavin Nienke
 *
 */

/**
 * 
 * This class implements selection sort.
 *
 */

public class SelectionSorter extends AbstractSorter {
	// Other private instance variables if you need ...

	/**
	 * Constructor takes an array of points. It invokes the superclass constructor,
	 * and also set the instance variables algorithm in the superclass.
	 * 
	 * @param pts
	 */
	public SelectionSorter(Point[] pts) {
		super(pts);
		super.algorithm = "selection sort";
	}

	/**
	 * Apply selection sort on the array points[] of the parent class
	 * AbstractSorter.
	 * 
	 */
	@Override
	public void sort() {
		for (int i = 0; i < points.length; i++) {
			int minIndex = i;
			 // Find the index of the minimum element in the unsorted part
			for (int j = i + 1; j < points.length; j++) {
				if (points[j].compareTo(points[minIndex]) < 0) {
					minIndex = j;
				}
			}
			// Swap the minimum element with the first unsorted element
			swap(i, minIndex);
		}
	}
}
