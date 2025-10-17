package sorting;

/**
 * 
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
      insertionSort(a, left, right);
      return;
    }

    int p = hoarePartition(a, left, right);
    sort(a, left, p, depthLimit - 1);
    sort(a, p + 1, right, depthLimit - 1);
  }

  private static <T extends Comparable<? super T>> int hoarePartition(T[] a, int left, int right) {
    int median = medianOfThree(a, left, (left + right) >>> 1, right);
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

  private static <T extends Comparable<? super T>> int medianOfThree(T[] a, int i, int j, int k) {
    if (a[i].compareTo(a[j]) > 0)
      SortUtils.swap(a, i, j);
    if (a[j].compareTo(a[k]) > 0)
      SortUtils.swap(a, j, k);
    if (a[i].compareTo(a[j]) > 0)
      SortUtils.swap(a, i, j);
    return j; // a[j] is the median
  }

  private static <T extends Comparable<? super T>> void insertionSort(T[] a, int left, int right) {
    for (int i = left + 1; i <= right; i++) {
      T key = a[i];
      int j = i - 1;
      while (j >= left && a[j].compareTo(key) > 0) {
        a[j + 1] = a[j];
        j--;
      }
      a[j + 1] = key;
    }
  }

}
