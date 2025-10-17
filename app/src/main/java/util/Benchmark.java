package util;

import java.util.function.Supplier;

/**
 * Utility class for benchmarking code execution time.
 */
public class Benchmark {
  private Benchmark() {
  }

  public static long timeMillis(Runnable r, int warmup, int reps) {
    for (int i = 0; i < warmup; i++)
      r.run();
    long best = Long.MAX_VALUE;
    for (int i = 0; i < reps; i++) {
      long t0 = System.nanoTime();
      r.run();
      long ms = (System.nanoTime() - t0) / 1_000_000;
      if (ms < best)
        best = ms;
    }
    return best;
  }

  public static <T> long timeSupplierMillis(Supplier<T> s, int warmup, int reps) {
    for (int i = 0; i < warmup; i++)
      s.get();
    long best = Long.MAX_VALUE;
    for (int i = 0; i < reps; i++) {
      long t0 = System.nanoTime();
      s.get();
      long ms = (System.nanoTime() - t0) / 1_000_000;
      if (ms < best)
        best = ms;
    }
    return best;
  }
}
