package sorting;

/**
 * Quick sort implementation using Hoare's partitioning scheme.
 */
public class QuickSort {
  private QuickSort() {
  }

  private static final int INSERTION_THRESHOLD = 16;

  public static <AnyType extends Comparable<? super AnyType>> void sort(AnyType[] a, int left, int right,
      int depthLimit) {
    if (left >= right)
      return;
    if (right - left + 1 <= INSERTION_THRESHOLD) {
      SortUtils.insertionSort(a, left, right);
      return;
    }

    int p = hoarePartition(a, left, right);
    sort(a, left, p, depthLimit - 1);
    sort(a, p + 1, right, depthLimit - 1);
  }

  public static <T extends Comparable<? super T>> int hoarePartition(T[] a, int left, int right) {
    int median = SortUtils.medianOfThree(a, left, (left + right) >>> 1, right);
    T pivot = a[median];

    int i = left - 1;
    int j = right + 1;
    while (true) {
      do {
        i++;
      } while (a[i].compareTo(pivot) < 0);
      do {
        j--;
      } while (a[j].compareTo(pivot) > 0);
      if (i >= j)
        return j;
      SortUtils.swap(a, i, j);
    }
  }

}
