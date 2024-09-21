package edu.iastate.cs228.hw2;

import java.util.ListIterator;

public class StoutListTester {
	public static void main(String[] args) {
		StoutList<Integer> list = new StoutList<>();

		// Test 1: Add elements
		System.out.println("Test 1: Add elements");
		list.add(1); // [1]
		list.add(2); // [1, 2]
		list.add(3); // [1, 2, 3]
		list.add(4); // [1, 2, 3, 4]
		list.add(5); // [1, 2, 3, 4, 5]
		System.out.println("Expected: [1, 2, 3, 4, 5]");
		System.out.print("Actual:   ");
		printList(list);

		// Test 2: Add elements at specific positions
		System.out.println("\nTest 2: Add elements at specific positions");
		list.add(0, 0); // [0, 1, 2, 3, 4, 5]
		list.add(3, 10); // [0, 1, 2, 10, 3, 4, 5]
		list.add(7, 20); // [0, 1, 2, 10, 3, 4, 5, 20]
		System.out.println("Expected: [0, 1, 2, 10, 3, 4, 5, 20]");
		System.out.print("Actual:   ");
		printList(list);

		// Test 3: Remove elements
		System.out.println("\nTest 3: Remove elements");
		list.remove(0); // [1, 2, 10, 3, 4, 5, 20]
		list.remove(2); // [1, 2, 3, 4, 5, 20]
		list.remove(5); // [1, 2, 3, 4, 5]
		System.out.println("Expected: [1, 2, 3, 4, 5]");
		System.out.print("Actual:   ");
		printList(list);

		// Test 4: Sort elements
		System.out.println("\nTest 4: Sort elements");
		list.add(-15); // [1, 2, 3, 4, 5, -15]
		list.add(15); // [1, 2, 3, 4, 5, -15, 15]
		list.sort(); // [-15, 1, 2, 3, 4, 5, 15]
		System.out.println("Expected: [-15, 1, 2, 3, 4, 5, 15]");
		System.out.print("Actual:   ");
		printList(list);

		// Test 5: Sort elements in reverse order
		System.out.println("\nTest 5: Sort elements in reverse order");
		list.sortReverse(); // [15, 5, 4, 3, 2, 1, -15]
		System.out.println("Expected: [15, 5, 4, 3, 2, 1, -15]");
		System.out.print("Actual:   ");
		printList(list);

		// Test 6: StoutListIterator forward iteration
		System.out.println("\nTest 6: StoutListIterator forward iteration");
		ListIterator<Integer> iterator = list.listIterator();
		System.out.print("Expected: [15, 5, 4, 3, 2, 1, -15]");
		System.out.print("\nActual:   [");
		while (iterator.hasNext()) {
			System.out.print(iterator.next() + (iterator.hasNext() ? ", " : ""));
		}
		System.out.println("]");

		// Test 7: StoutListIterator backward iteration
		System.out.println("\nTest 7: StoutListIterator backward iteration");
		System.out.print("Expected: [-15, 1, 2, 3, 4, 5, 15]");
		System.out.print("\nActual:   [");
		while (iterator.hasPrevious()) {
			System.out.print(iterator.previous() + (iterator.hasPrevious() ? ", " : ""));
		}
		System.out.println("]");

		// Test 8: StoutListIterator add and remove
		System.out.println("\nTest 8: StoutListIterator add and remove");
		ListIterator<Integer> iter8 = list.listIterator();
		iter8.next(); // 15
		iter8.next(); // 5
		iter8.next(); // 4
		iter8.add(100); // Add 100 after 4
		System.out.println("Expected after add: [15, 5, 4, 100, 3, 2, 1, -15]");
		System.out.println("            Actual: " + list);

		iter8.previous(); // Move back to 100
		iter8.previous(); // Move back to 4
		iter8.remove(); // Remove 4
		System.out.println("Expected after remove after previous: [15, 5, 100, 3, 2, 1, -15]");
		System.out.println("                              Actual: " + list);

		// Additional test to ensure remove works after next
		iter8.next(); // Move to 100
		iter8.next(); // Move to 3
		iter8.remove(); // Remove 3
		System.out.println("Expected after remove after next: [15, 5, 100, 2, 1, -15]");
		System.out.println("                          Actual: " + list);

		// Test 9: Add and remove in empty list
		StoutList<Integer> emptyList = new StoutList<>();
		System.out.println("\nTest 9: Add and remove in empty list");
		try {
			emptyList.add(0, 1); // Add to empty list
			System.out.println("Expected: [1]");
			System.out.print("Actual:   ");
			printList(emptyList);

			emptyList.remove(0); // Remove from empty list
			System.out.println("Expected: []");
			System.out.print("Actual:   ");
			printList(emptyList);
		} catch (Exception e) {
			System.out.println("Exception occurred: " + e.getMessage());
		}

		// Test 10: Null element handling
		System.out.println("\nTest 10: Null element handling");
		try {
			System.out.println("Expected: NullPointerException");
			list.add(null);
		} catch (NullPointerException e) {
			System.out.println("Actual:   NullPointerException");
		}

		try {
			System.out.println("Expected: NullPointerException");
			list.add(0, null);
		} catch (NullPointerException e) {
			System.out.println("Actual:   NullPointerException");
		}

		// Test 11: Index out of bounds
		System.out.println("\nTest 11: Index out of bounds");
		try {
			System.out.println("Expected: IndexOutOfBoundsException");
			list.add(100, 1);
		} catch (IndexOutOfBoundsException e) {
			System.out.println("Actual:   IndexOutOfBoundsException");
		}

		try {
			System.out.println("Expected: IndexOutOfBoundsException");
			list.remove(100);
		} catch (IndexOutOfBoundsException e) {
			System.out.println("Actual:   IndexOutOfBoundsException");
		}
	}

	private static void printList(StoutList<?> list) {
		ListIterator<?> iterator = list.listIterator();
		System.out.print("[");
		while (iterator.hasNext()) {
			System.out.print(iterator.next() + (iterator.hasNext() ? ", " : ""));
		}
		System.out.println("]");
	}
}
