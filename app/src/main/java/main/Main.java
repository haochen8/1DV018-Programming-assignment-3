package main;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import sorting.HybridQuickHeapSort;
import sorting.SortUtils;
import util.Benchmark;

public class Main {
  public static void main(String[] args) {
    demoSmall();
    demoMiniBench();
  }

  private static void demoSmall() {
    Integer[] demo = { 9, 3, 5, 1, 8, 2, 7, 4, 6 };
    System.out.println("Before " + Arrays.toString(demo));
    HybridQuickHeapSort.sort(demo);
    System.out.println("After " + Arrays.toString(demo));
    System.out.println("Is Sorted? " + SortUtils.isSorted(demo));
    System.out.println();
  }

  private static void demoMiniBench() {
    int warmups = 3;
    int reps = 15;

    List<String> csv = new ArrayList<>();
    csv.add("n,k,depth,bestMillis,bestNanos,ok");

    int[] sizes = { 5_000, 20_000, 100_000 };

    for (int n : sizes) {
      Integer[] base = SortUtils.genRandomIntegers(n, 42L, 1_000_000);
      System.out.println("=== Benchmark for n = " + n + " ===");

      for (int kScaled = 50; kScaled <= 400; kScaled += 25) {
        double k = kScaled / 100.0;
        int lg = 31 - Integer.numberOfLeadingZeros(n);
        int depth = Math.max(1, (int) Math.floor(k * lg));
        Integer[] arr = base.clone();
        Benchmark.Timing timing = Benchmark
            .timeWithNanos(() -> HybridQuickHeapSort.sort(arr, 0, arr.length - 1, depth), warmups, reps);
        boolean ok = SortUtils.isSorted(arr);
        long bestMillis = timing.bestMillis();
        long bestNanos = timing.bestNanos();
        System.out.printf(Locale.US, "n=%d, k=%.2f (depth=%d) => %d ms (%d ns), ok=%s%n", n, k, depth, bestMillis,
            bestNanos, ok);
        csv.add(String.format(Locale.US, "%d,%.2f,%d,%d,%d,%s", n, k, depth, bestMillis, bestNanos, ok));
      }

      System.out.println();
    }

    writeCsv(Paths.get("src/main/csv", "mini-bench.csv"), csv);
  }

  private static void writeCsv(Path path, List<String> lines) {
    try {
      if (path.getParent() != null)
        Files.createDirectories(path.getParent());
      Files.write(path, lines, StandardCharsets.UTF_8);
      System.out.println("CSV results written to " + path.toAbsolutePath());
    } catch (IOException e) {
      System.err.println("Failed to write CSV results: " + e.getMessage());
    }
  }
}
