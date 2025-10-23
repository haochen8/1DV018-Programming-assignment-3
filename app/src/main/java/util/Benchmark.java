package util;

import java.util.function.Supplier;

/**
 * Utility class for benchmarking code execution time.
 */
public class Benchmark {
  private Benchmark() {
  }

  public static long timeMillis(Runnable r, int warmup, int reps) {
    return timeWithNanos(r, warmup, reps).bestMillis();
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

  /**
   * Times a runnable and reports the best run in both milliseconds and nanoseconds.
   */
  public static Timing timeWithNanos(Runnable r, int warmup, int reps) {
    for (int i = 0; i < warmup; i++)
      r.run();
    long best = Long.MAX_VALUE;
    for (int i = 0; i < reps; i++) {
      long t0 = System.nanoTime();
      r.run();
      long ns = System.nanoTime() - t0;
      if (ns < best)
        best = ns;
    }
    return new Timing(best);
  }

  public static final class Timing {
    private final long bestNanos;

    private Timing(long bestNanos) {
      this.bestNanos = bestNanos;
    }

    public long bestNanos() {
      return bestNanos;
    }

    public long bestMillis() {
      return bestNanos / 1_000_000;
    }
  }
}
