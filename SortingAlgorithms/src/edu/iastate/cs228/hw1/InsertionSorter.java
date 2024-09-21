package edu.iastate.cs228.hw1;

//imports were unused, so I deleted them. 

/**
 *  
 * @author Gavin Nienke
 *
 */

/**
 * 
 * This class implements insertion sort.
 *
 */

public class InsertionSorter extends AbstractSorter {
	// Other private instance variables if you need ...

	/**
	 * Constructor takes an array of points. It invokes the superclass constructor,
	 * and also set the instance variables algorithm in the superclass.
	 * 
	 * @param pts
	 */
	public InsertionSorter(Point[] pts) {
		super(pts);
		super.algorithm = "insertion sort";
	}

	/**
	 * Perform insertion sort on the array points[] of the parent class
	 * AbstractSorter.
	 */
	@Override
	public void sort() {
		for (int i = 1; i < points.length; i++) {
			int j = i;
			//move elements with the swap method. 
			while (j > 0 && pointComparator.compare(points[j - 1], points[j]) > 0) {
				swap(j, j - 1);
				j--;
			}
		}
	}
}
