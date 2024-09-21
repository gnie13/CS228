package edu.iastate.cs228.hw2;

import java.util.AbstractSequentialList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.NoSuchElementException;

/*
 * @author Gavin Nienke
 */

/**
 * Implementation of the list interface based on linked nodes that store
 * multiple items per node. Rules for adding and removing elements ensure that
 * each node (except possibly the last one) is at least half full.
 */
public class StoutList<E extends Comparable<? super E>> extends AbstractSequentialList<E> {
	/**
	 * Default number of elements that may be stored in each node.
	 */
	private static final int DEFAULT_NODESIZE = 4;

	/**
	 * Number of elements that can be stored in each node.
	 */
	private final int nodeSize;

	/**
	 * Dummy node for head. It should be private but set to public here only for
	 * grading purpose. In practice, you should always make the head of a linked
	 * list a private instance variable.
	 */
	public Node head;

	/**
	 * Dummy node for tail.
	 */
	private Node tail;

	/**
	 * Number of elements in the list.
	 */
	private int size;

	/**
	 * Constructs an empty list with the default node size.
	 */
	public StoutList() {
		this(DEFAULT_NODESIZE);
	}

	/**
	 * Constructs an empty list with the given node size.
	 * 
	 * @param nodeSize number of elements that may be stored in each node, must be
	 *                 an even number
	 */
	public StoutList(int nodeSize) {
		if (nodeSize <= 0 || nodeSize % 2 != 0)
			throw new IllegalArgumentException();

		// dummy nodes
		head = new Node();
		tail = new Node();
		head.next = tail;
		tail.previous = head;
		this.nodeSize = nodeSize;
	}

	/**
	 * Constructor for grading only. Fully implemented.
	 * 
	 * @param head
	 * @param tail
	 * @param nodeSize
	 * @param size
	 */
	public StoutList(Node head, Node tail, int nodeSize, int size) {
		this.head = head;
		this.tail = tail;
		this.nodeSize = nodeSize;
		this.size = size;
	}

	/**
	 * @return num of elements located in the list.
	 */
	@Override
	public int size() {

		return size;
	}

	/**
	 * appends a new element to the end of the list.
	 * 
	 * @param item the thing being added
	 * @return true if added, false if item already exists.
	 */
	@Override
	public boolean add(E item) {

		if (item == null) {
			throw new NullPointerException();
		}
		if (contains(item)) {
			return false;
		}
		// check if empty
		if (size == 0) {
			Node node = new Node();
			node.addItem(item);
			head.next = node;
			node.previous = head;
			node.next = tail;
			tail.previous = node;
		} else {
			// if the last node isn't full, add to said node.
			if (tail.previous.count < nodeSize) {
				tail.previous.addItem(item);
				// if it is full, append new node to the old node at the end.
			} else {
				Node node = new Node();
				node.addItem(item);
				Node temp = tail.previous;
				temp.next = node;
				node.previous = temp;
				node.next = tail;
				tail.previous = node;
			}
		}
		size++;
		return true;
	}

	/**
	 * Similar to regular add(), but instead adds to a specific location in the
	 * list.
	 * 
	 * @param pos  the location where the item is being added
	 * @param item the thing being added.
	 */
	@Override
	public void add(int pos, E item) {

		if (item == null) {
			throw new NullPointerException("Cannot add null elements");
		}
		if (pos < 0 || pos > size) {
			throw new IndexOutOfBoundsException("Index out of bounds: " + pos);
		}

		// If adding at the end, use the regular add method
		if (pos == size) {
			add(item);
			return;
		}

		// Find the node and offset where the item will be added
		NodeInfo info = find(pos);
		Node node = info.node;
		int offset = info.offset;

		// Check if the node has enough space to add the item
		if (node.count < nodeSize) {
			node.addItem(offset, item);
		} else {
			// Split the node if it's full
			Node newNode = new Node();
			int half = nodeSize / 2;
			for (int i = 0; i < half; i++) {
				newNode.addItem(node.data[half]);
				node.removeItem(half);
			}

			// Insert the new node into the list
			Node oldNode = node.next;
			node.next = newNode;
			newNode.previous = node;
			newNode.next = oldNode;
			oldNode.previous = newNode;

			// Determine where to add the item based on offset and node split
			if (offset > size / 2) {
				newNode.addItem(offset - size / 2, item);
			} else {
				newNode.addItem(offset, item);
			}
		}

		size++;
	}

	/**
	 * method that checks if an element being added has a duplicate in the list.
	 * 
	 * @param item the item being searched for
	 * @return true if the item is found in the list, false if not.
	 */
	public boolean contains(E item) {
		if (size < 1)
			return false;
		Node temp = head.next;
		while (temp != tail) {
			for (int i = 0; i < temp.count; i++) {
				if (temp.data[i].equals(item))
					return true;
				temp = temp.next;
			}
		}
		return false;
	}

	/**
	 * removes item at a particular posiiton
	 * 
	 * @param pos the position where the removing item is
	 * @return nodeVal value of the removed node
	 */
	@Override
	public E remove(int pos) {

		if (pos >= size || pos < 0) {
			throw new IndexOutOfBoundsException();
		}
		// find the node and offset corresponding to the position
		NodeInfo info = find(pos);
		Node tempNode = info.node;
		int offset = info.offset;
		E nodeVal = tempNode.data[offset];
		// check if there is only one element in the tempNode and if it is the last
		// element.
		if (tempNode.count == 1 && tempNode.next == tail) {
			Node behind = tempNode.previous;
			behind.next = tempNode.next;
			tempNode.next.previous = behind;
			tempNode = null;
			// if the count is > nodeSize / 2 or if it is the final node in the list
		} else if ((tempNode.count > nodeSize / 2) || tempNode.next == tail) {
			tempNode.removeItem(offset);
			// if the node has less than or equal to half of nodeSize elements
		} else {
			tempNode.removeItem(offset);
			Node nextNode = tempNode.next;
			// if the next node has more than half of nodeSize elements
			if (nextNode.count > nodeSize / 2) {// check node size
				// move the first item from the next node to the current node
				tempNode.addItem(nextNode.data[0]);
				// remove the item from the next node
				nextNode.removeItem(0);
				// if the next node has less than or equal to half of nodeSize elements
			} else if (nextNode.count <= nodeSize / 2) {
				// move all items from the next node to the current node
				for (int i = 0; i < nextNode.count; i++) {
					tempNode.addItem(nextNode.data[i]);
				}
				// remove the next node
				tempNode.next = nextNode.next;
				nextNode.next.previous = tempNode;
				nextNode = null;
			}
		}
		size--;
		return nodeVal;
	}

	/**
	 * Sort all elements in the stout list in the NON-DECREASING order. You may do
	 * the following. Traverse the list and copy its elements into an array,
	 * deleting every visited node along the way. Then, sort the array by calling
	 * the insertionSort() method. (Note that sorting efficiency is not a concern
	 * for this project.) Finally, copy all elements from the array back to the
	 * stout list, creating new nodes for storage. After sorting, all nodes but
	 * (possibly) the last one must be full of elements.
	 * 
	 * Comparator<E> must have been implemented for calling insertionSort().
	 */
	public void sort() {
		// create an array to hold all elements from the list
		E[] sortList = (E[]) new Comparable[size];

		Node tempNode = head.next;
		int index = 0;
		// go through all nodes and copy into the sortList array.
		while (tempNode != tail) {
			for (int i = 0; i < tempNode.count; i++) {
				sortList[index] = tempNode.data[i];
				index++;
			}
			tempNode = tempNode.next;
		}
		// reset list by connecting head and tail
		head.next = tail;
		tail.previous = head;
		// sort array using insertion sort and the ElementComparator.
		insertionSort(sortList, new ElementComparator());
		size = 0;
		// add sorted elements back into the list.
		for (int i = 0; i < sortList.length; i++) {
			add(sortList[i]);
		}
	}

	/**
	 * Sort all elements in the stout list in the NON-INCREASING order. Call the
	 * bubbleSort() method. After sorting, all but (possibly) the last nodes must be
	 * filled with elements.
	 * 
	 * Comparable<? super E> must be implemented for calling bubbleSort().
	 */
	public void sortReverse() {
		// build array to hold the elements.
		E[] sortReverseList = (E[]) new Comparable[size];

		int index = 0;
		Node tempNode = head.next;
		// go through all nodes and copy into the reversed list array.
		while (tempNode != tail) {
			for (int i = 0; i < tempNode.count; i++) {
				sortReverseList[index] = tempNode.data[i];
				index++;
			}
			tempNode = tempNode.next;
		}
		head.next = tail;
		tail.previous = head;
		// sort with bubbleSort
		bubbleSort(sortReverseList);
		size = 0;
		// put sorted elements back in the list
		for (int i = 0; i < sortReverseList.length; i++) {
			add(sortReverseList[i]);
		}
	}

	@Override
	public Iterator<E> iterator() {
		return new StoutListIterator();
	}

	@Override
	public ListIterator<E> listIterator() {
		return new StoutListIterator();
	}

	@Override
	public ListIterator<E> listIterator(int index) {
		return new StoutListIterator(index);
	}

	/**
	 * Returns a string representation of this list showing the internal structure
	 * of the nodes.
	 */
	public String toStringInternal() {
		return toStringInternal(null);
	}

	/**
	 * Returns a string representation of this list showing the internal structure
	 * of the nodes and the position of the iterator.
	 *
	 * @param iter an iterator for this list
	 */
	public String toStringInternal(ListIterator<E> iter) {
		int count = 0;
		int position = -1;
		if (iter != null) {
			position = iter.nextIndex();
		}

		StringBuilder sb = new StringBuilder();
		sb.append('[');
		Node current = head.next;
		while (current != tail) {
			sb.append('(');
			E data = current.data[0];
			if (data == null) {
				sb.append("-");
			} else {
				if (position == count) {
					sb.append("| ");
					position = -1;
				}
				sb.append(data.toString());
				++count;
			}

			for (int i = 1; i < nodeSize; ++i) {
				sb.append(", ");
				data = current.data[i];
				if (data == null) {
					sb.append("-");
				} else {
					if (position == count) {
						sb.append("| ");
						position = -1;
					}
					sb.append(data.toString());
					++count;

					// iterator at end
					if (position == size && count == size) {
						sb.append(" |");
						position = -1;
					}
				}
			}
			sb.append(')');
			current = current.next;
			if (current != tail)
				sb.append(", ");
		}
		sb.append("]");
		return sb.toString();
	}

	/**
	 * Node type for this list. Each node holds a maximum of nodeSize elements in an
	 * array. Empty slots are null.
	 */
	private class Node {
		/**
		 * Array of actual data elements.
		 */
		// Unchecked warning unavoidable.
		public E[] data = (E[]) new Comparable[nodeSize];

		/**
		 * Link to next node.
		 */
		public Node next;

		/**
		 * Link to previous node;
		 */
		public Node previous;

		/**
		 * Index of the next available offset in this node, also equal to the number of
		 * elements in this node.
		 */
		public int count;

		/**
		 * Adds an item to this node at the first available offset. Precondition: count
		 * < nodeSize
		 * 
		 * @param item element to be added
		 */
		void addItem(E item) {
			if (count >= nodeSize) {
				return;
			}
			data[count++] = item;
			// useful for debugging
			// System.out.println("Added " + item.toString() + " at index " + count + " to
			// node " + Arrays.toString(data));
		}

		/**
		 * Adds an item to this node at the indicated offset, shifting elements to the
		 * right as necessary.
		 * 
		 * Precondition: count < nodeSize
		 * 
		 * @param offset array index at which to put the new element
		 * @param item   element to be added
		 */
		void addItem(int offset, E item) {
			if (count >= nodeSize) {
				return;
			}
			for (int i = count - 1; i >= offset; --i) {
				data[i + 1] = data[i];
			}
			++count;
			data[offset] = item;
			// useful for debugging
//      System.out.println("Added " + item.toString() + " at index " + offset + " to node: "  + Arrays.toString(data));
		}

		/**
		 * Deletes an element from this node at the indicated offset, shifting elements
		 * left as necessary. Precondition: 0 <= offset < count
		 * 
		 * @param offset
		 */
		void removeItem(int offset) {
			E item = data[offset];
			for (int i = offset + 1; i < nodeSize; ++i) {
				data[i - 1] = data[i];
			}
			data[count - 1] = null;
			--count;
		}
	}

	private class StoutListIterator implements ListIterator<E> {

		/**
		 * array for the iterator.
		 */
		public E[] list;
		/**
		 * where the iterator/cursor is pointing
		 */
		int current;
		/**
		 * the last method called by the program, and is used to determine which items
		 * to remove/set.
		 */
		int last;

		/**
		 * Default constructor
		 */
		public StoutListIterator() {
			current = 0;
			last = -1;
			storeData();

		}

		/**
		 * Constructor finds node at a given position.
		 * 
		 * @param pos
		 */
		public StoutListIterator(int pos) {
			current = pos;
			last = -1;
			storeData();

		}

		/**
		 * helper method to store data in an array for StoutList.
		 */
		private void storeData() {
			list = (E[]) new Comparable[size];

			int index = 0;
			Node tempNode = head.next;
			while (tempNode != tail) {
				for (int i = 0; i < tempNode.count; i++) {
					list[index] = tempNode.data[i];
					index++;
				}
				tempNode = tempNode.next;
			}
		}

		/**
		 * overwrites the element located at the cursor.
		 * 
		 * @param arg0 the new replacing element
		 */
		@Override
		public void set(E arg0) {
			if (last == 0) {
				NodeInfo nodeInfo = find(current);
				nodeInfo.node.data[nodeInfo.offset] = arg0;
				list[current] = arg0;

			} else if (last == 1) {
				NodeInfo nodeInfo = find(current - 1);
				nodeInfo.node.data[nodeInfo.offset] = arg0;
				list[current - 1] = arg0;

			} else {
				throw new IllegalStateException();
			}

		}

		/**
		 * appends a new element to the end of the list.
		 * 
		 * @param arg0 element added to the end of the list.
		 */
		@Override
		public void add(E arg0) {
			if (arg0 == null)
				throw new NullPointerException();

			StoutList.this.add(current, arg0);
			current++;
			storeData();
			last = -1;

		}

		/**
		 * remove method that removes the previous or next element, depending on what is
		 * called.
		 */
		@Override
		public void remove() {

			if (last == 0) {
				StoutList.this.remove(current);
				storeData();
				last = -1;
			} else if (last == 1) {
				StoutList.this.remove(current - 1);
				storeData();
				last = -1;
				current--;
				if (current < 0)
					current = 0;

			} else {
				throw new IllegalStateException();
			}

		}

		/**
		 * @return true if there is a next value, false if not
		 */
		@Override
		public boolean hasNext() {
			if (current >= size) {
				return false;
			} else {
				return true;
			}
		}

		/**
		 * @returns the next value if there is one.
		 */
		@Override
		public E next() {
			if (!hasNext()) {
				throw new NoSuchElementException();
			}
			last = 1;

			return list[current++];
		}

		/**
		 * gets index of next element.
		 * 
		 * @return index of next element;
		 */
		@Override
		public int nextIndex() {

			return current;
		}

		/**
		 * checks if previous element exists.
		 * 
		 * @return true if element behind current exists (i.e. current > 0) , false if
		 *         not.
		 */
		@Override
		public boolean hasPrevious() {

			if (current <= 0)
				return false;
			else
				return true;

		}

		/**
		 * gets the index of previous element.
		 * 
		 * @return the index of previous element
		 */
		@Override
		public int previousIndex() {

			return current - 1;
		}

		/**
		 * accesses the previous element.
		 * 
		 * @return previous element if exists.
		 */
		@Override
		public E previous() {

			if (!hasPrevious()) {
				throw new NoSuchElementException();
			}
			last = -1;
			current--;
			return list[current];
		}

		// Other methods you may want to add or override that could possibly facilitate
		// other operations, for instance, addition, access to the previous element,
		// etc.
		//
		// ...
		//

		/*
		 * class that finds info on a specific point.
		 */
	}

	private class NodeInfo {
		public Node node;
		public int offset;

		public NodeInfo(Node node, int offset) {
			this.node = node;
			this.offset = offset;
		}
	}

	/*
	 * finds the node and the offset for the given logical index.
	 * 
	 * @param pos position of the node that we are finding info on
	 * 
	 * @return info the info of the node on the offset position
	 */
	private NodeInfo find(int pos) {
		Node tempNode = head.next;
		int current = 0;

		while (tempNode != tail) {
			if (pos >= current + tempNode.count) {
				current += tempNode.count;
				tempNode = tempNode.next;
				continue;
			}

			NodeInfo info = new NodeInfo(tempNode, pos - current);
			return info;
		}
		return null;
	}

	/**
	 * Sort an array arr[] using the insertion sort algorithm in the NON-DECREASING
	 * order.
	 * 
	 * @param arr  array storing elements from the list
	 * @param comp comparator used in sorting
	 */
	private void insertionSort(E[] arr, Comparator<? super E> comp) {
		for (int i = 1; i < arr.length; i++) {
			E key = arr[i];
			int j = i - 1;

			while (j >= 0 && comp.compare(arr[j], key) > 0) {
				arr[j + 1] = arr[j];
				j--;
			}
			arr[j + 1] = key;
		}
	}

	/**
	 * Sort arr[] using the bubble sort algorithm in the NON-INCREASING order. For a
	 * description of bubble sort please refer to Section 6.1 in the project
	 * description. You must use the compareTo() method from an implementation of
	 * the Comparable interface by the class E or ? super E.
	 * 
	 * @param arr array holding elements from the list
	 */
	private void bubbleSort(E[] arr) {
		int n = arr.length;
		for (int i = 0; i < n - 1; i++) {
			for (int j = 0; j < n - i - 1; j++) {
				if (arr[j].compareTo(arr[j + 1]) < 0) {
					E temp = arr[j];
					arr[j] = arr[j + 1];
					arr[j + 1] = temp;
				}
			}
		}
	}

	/**
	 * Comparator class to compare elements.
	 */
	private class ElementComparator implements Comparator<E> {
		@Override
		public int compare(E arg0, E arg1) {
			return arg0.compareTo(arg1);
		}
	}
}
