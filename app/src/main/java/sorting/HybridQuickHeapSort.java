package sorting;

/**
 * Hybrid sorting algorithm combining QuickSort and HeapSort.
 */
public class HybridQuickHeapSort {
  private HybridQuickHeapSort() {
  }

  public static <T extends Comparable<T>> void sort(T[] a) {
    sort(a, 0, a.length - 1, defaultDepthLimit(a.length));
  }

  public static <T extends Comparable<T>> void sort(T[] a, int left, int right, int depthLimit) {

    if (depthLimit <= 0) {
      HeapSort.sort(a, left, right);
    }

    if (right - left + 1 <= 16) {
      SortUtils.insertionSort(a, left, right);
      return;
    }

    int p = QuickSort.hoarePartition(a, left, right);
    sort(a, left, p, depthLimit - 1);
    sort(a, p + 1, right, depthLimit - 1);
  }

  private static int defaultDepthLimit(int n) {
    if (n <= 0)
      return 0;
    int lg = 31 - Integer.numberOfLeadingZeros(n);
    return Math.max(1, 2 * lg); 
  }

}
