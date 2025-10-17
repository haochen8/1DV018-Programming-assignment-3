package sorting;

import java.util.Arrays;
import sorting.HybridQuickHeapSort;
import util.Benchmark;

public class Main {
  public static void main(String[] args) {
    demoSmall();
    demoMiniBench();
  }

  private static void demoSmall() {
    Integer[] demo = { 9, 3, 5, 1, 8, 2, 7, 4, 6 };
    System.out.println("Före: " + Arrays.toString(demo));
    HybridQuickHeapSort.sort(demo);
    System.out.println("Efter: " + Arrays.toString(demo));
    System.out.println("Är sorterad? " + SortUtils.isSorted(demo));
    System.out.println();
  }

  private static void demoMiniBench() {
    int n = 20_000; // håll det modest för gradle run
    Integer[] base = SortUtils.genRandomIntegers(n, 42L, 1_000_000);

    // Testa ett par depth-faktorer: k * log2(n)
    double[] ks = { 1.5, 2.0, 2.5 };
    for (double k : ks) {
      int lg = 31 - Integer.numberOfLeadingZeros(n);
      int depth = Math.max(1, (int) Math.floor(k * lg));
      Integer[] arr = base.clone();
      long ms = Benchmark.timeMillis(() -> HybridQuickHeapSort.sort(arr, 0, arr.length - 1, depth), 2, 3);
      System.out.printf("n=%d, k=%.2f (depth=%d) => %d ms, ok=%s\n", n, k, depth, ms, SortUtils.isSorted(arr));
    }
  }
}
