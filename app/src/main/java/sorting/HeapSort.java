package sorting;

/**
 * Utility class that exposes a heap sort implementation for array segments.
 */
public class HeapSort {
  private HeapSort() {
  }

  /**
   * Sorts the inclusive range {@code [left, right]} of the supplied array in ascending order
   * using the heap sort algorithm.
   *
   * @param <AnyType> the element type, which must be comparable to itself or a super type
   * @param a the array containing the elements to sort
   * @param left the inclusive lower bound of the range to sort
   * @param right the inclusive upper bound of the range to sort
   */
  public static <AnyType extends Comparable<? super AnyType>> void sort(AnyType[] a, int left, int right) {
    if (right - left + 1 <= 1)
      return;
    buildMaxHeap(a, left, right);
    for (int end = right; end > left; end--) {
      SortUtils.swap(a, left, end);
      siftDown(a, left, left, end - 1);
    }
  }

  /**
   * Builds a max-heap spanning the inclusive range {@code [left, right]} of the array so the
   * largest element resides at {@code left}.
   *
   * @param <AnyType> the element type, which must be comparable to itself or a super type
   * @param a the array containing the heap elements
   * @param left the inclusive lower bound of the heap
   * @param right the inclusive upper bound of the heap
   */
  private static <AnyType extends Comparable<? super AnyType>> void buildMaxHeap(AnyType[] a, int left, int right) {
    int lastParent = left + ((right - left + 1) / 2) - 1;
    for (int i = lastParent; i >= left; i--) {
      siftDown(a, i, left, right);
    }
  }

  /**
   * Restores the max-heap property for a node at {@code i} within the inclusive range
   * {@code [left, right]} by moving it downward until both children are less than or equal.
   *
   * @param <AnyType> the element type, which must be comparable to itself or a super type
   * @param a the array containing the heap elements
   * @param i the index of the node to sift
   * @param left the inclusive lower bound of the heap
   * @param right the inclusive upper bound of the heap
   */
  private static <AnyType extends Comparable<? super AnyType>> void siftDown(AnyType[] a, int i, int left, int right) {
    while (true) {
      int l = leftChild(i, left);
      int r = l + 1;
      int largest = i;
      if (l <= right && a[l].compareTo(a[largest]) > 0)
        largest = l;
      if (r <= right && a[r].compareTo(a[largest]) > 0)
        largest = r;
      if (largest == i)
        break;
      SortUtils.swap(a, i, largest);
      i = largest;
    }
  }

  private static int leftChild(int i, int left) {
    return (i - left) * 2 + 1 + left;
  }

}
