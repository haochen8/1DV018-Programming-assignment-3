package sorting;

import java.util.Arrays;
import java.util.Random;

/**
 * Utility class exposing common sorting-related helper methods.
 */
public class SortUtils {
  private SortUtils() {
  }

  public static <AnyType> void swap(AnyType[] a, int i, int j) {
    AnyType temp = a[i];
    a[i] = a[j];
    a[j] = temp;
  }

  public static <AnyType extends Comparable<AnyType>> boolean isSorted(AnyType[] a) {
    for (int i = 1; i < a.length; i++) {
      if (a[i - 1].compareTo(a[i]) > 0)
        return false;
    }
    return true;
  }

  public static Integer[] genRandomIntegers(int n, long seed, int bound) {
    Random rand = new Random(seed);
    Integer[] result = new Integer[n];
    for (int i = 0; i < n; i++) {
      result[i] = rand.nextInt(bound);
    }
    return result;
  }

  public static Integer[] genSortedIntegers(int n) {
    Integer[] result = new Integer[n];
    for (int i = 0; i < n; i++) {
      result[i] = i;
    }
    return result;
  }

  public static Integer[] genReversedIntegers(int n) {
    Integer[] result = new Integer[n];
    for (int i = 0; i < n; i++) {
      result[i] = n - i - 1;
    }
    return result;
  }

  public static <AnyType> AnyType[] copyOf(AnyType[] a) {
    return Arrays.copyOf(a, a.length);
  }

  public static <T extends Comparable<? super T>> void insertionSort(T[] a, int left, int right) {
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

  public static <T extends Comparable<? super T>> int medianOfThree(T[] a, int i, int j, int k) {
    if (a[i].compareTo(a[j]) > 0)
      SortUtils.swap(a, i, j);
    if (a[j].compareTo(a[k]) > 0)
      SortUtils.swap(a, j, k);
    if (a[i].compareTo(a[j]) > 0)
      SortUtils.swap(a, i, j);
    return j; // a[j] is the median
  }

}
