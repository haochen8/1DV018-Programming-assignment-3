package sorting;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class HeapSortTest {

  @Test
  void sortsDescendingSequence() {
    Integer[] data = { 3, 2, 1 };

    HeapSort.sort(data, 0, data.length - 1);

    assertArrayEquals(new Integer[] { 1, 2, 3 }, data);
  }

  @Test
  void keepsSortedArrayIntact() {
    Integer[] data = { 1, 2, 3, 4 };

    HeapSort.sort(data, 0, data.length - 1);

    assertArrayEquals(new Integer[] { 1, 2, 3, 4 }, data);
  }

  @Test
  void sortsOnlySelectedRange() {
    Integer[] data = { 4, 1, 3, 2, 0 };

    HeapSort.sort(data, 1, 3);

    assertArrayEquals(new Integer[] { 4, 1, 2, 3, 0 }, data);
  }

  @Test
  void handlesDuplicateValues() {
    Integer[] data = { 5, 5, 5 };

    HeapSort.sort(data, 0, data.length - 1);

    assertArrayEquals(new Integer[] { 5, 5, 5 }, data);
  }

  @Test
  void sortsCustomComparableType() {
    Box[] data = { new Box(3), new Box(1), new Box(2) };

    HeapSort.sort(data, 0, data.length - 1);

    assertEquals(1, data[0].value);
    assertEquals(2, data[1].value);
    assertEquals(3, data[2].value);
  }

  private static final class Box implements Comparable<Box> {
    private final int value;

    private Box(int value) {
      this.value = value;
    }

    @Override
    public int compareTo(Box other) {
      return Integer.compare(this.value, other.value);
    }
  }
}
