package com.github.methylene.sym;

import java.util.Arrays;
import java.util.Comparator;

public class Searchable {

  private static abstract class AArray {

    protected final Permutation unsort;

    protected AArray(Permutation unsort) {
      this.unsort = unsort;
    }

    public final Permutation getUnsort() {
      return unsort;
    }

  }

  public static IntArray create(int[] a) {
    Permutation sort = Permutation.factory().sort(a);
    return new IntArray(sort.apply(a), sort.invert());
  }

  public static LongArray create(long[] a) {
    Permutation sort = Permutation.factory().sort(a);
    return new LongArray(sort.apply(a), sort.invert());
  }

  public static ByteArray create(byte[] a) {
    Permutation sort = Permutation.factory().sort(a);
    return new ByteArray(sort.apply(a), sort.invert());
  }

  public static CharArray create(char[] a) {
    Permutation sort = Permutation.factory().sort(a);
    return new CharArray(sort.apply(a), sort.invert());
  }

  public static ComparableArray create(Comparable[] a) {
    Permutation sort = Permutation.factory().sort(a);
    return new ComparableArray(sort.apply(a), sort.invert());
  }

  public static ObjectArray create(Object[] a, Comparator comparator) {
    Permutation sort = Permutation.factory().sort(a, comparator);
    return new ObjectArray(sort.apply(a), comparator, sort.invert());
  }

  public static final class ByteArray extends AArray {
    private final byte[] sorted;
    private ByteArray(byte[] sorted, Permutation unsort) {
      super(unsort);
      this.sorted = sorted;
    }
    public byte[] getArray() {
      return unsort.apply(sorted);
    }
    public byte[] getSortedArray() {
      return Arrays.copyOf(sorted, sorted.length);
    }
    public int indexOf(byte el) {
      int i = Arrays.binarySearch(sorted, el);
      return i < 0 ? -1 : unsort.apply(i);
    }
  }

  public static final class LongArray extends AArray {
    private final long[] sorted;
    private LongArray(long[] sorted, Permutation unsort) {
      super(unsort);
      this.sorted = sorted;
    }
    public long[] getArray() {
      return unsort.apply(sorted);
    }
    public long[] getSortedArray() {
      return Arrays.copyOf(sorted, sorted.length);
    }
    public int indexOf(long el) {
      int i = Arrays.binarySearch(sorted, el);
      return i < 0 ? -1 : unsort.apply(i);
    }
  }

  public static final class CharArray extends AArray {
    private final char[] sorted;
    private CharArray(char[] sorted, Permutation unsort) {
      super(unsort);
      this.sorted = sorted;
    }
    public char[] getArray() {
      return unsort.apply(sorted);
    }
    public char[] getSortedArray() {
      return Arrays.copyOf(sorted, sorted.length);
    }
    public int indexOf(char el) {
      int i = Arrays.binarySearch(sorted, el);
      return i < 0 ? -1 : unsort.apply(i);
    }
  }



  public static final class IntArray extends AArray {
    private final int[] sorted;
    private IntArray(int[] sorted, Permutation unsort) {
      super(unsort);
      this.sorted = sorted;
    }
    public int[] getArray() {
      return unsort.apply(sorted);
    }
    public int[] getSortedArray() {
      return Arrays.copyOf(sorted, sorted.length);
    }
    public int indexOf(int el) {
      int i = Arrays.binarySearch(sorted, el);
      return i < 0 ? -1 : unsort.apply(i);
    }
  }

  public static final class ComparableArray extends AArray {
    private final Comparable[] sorted;
    private ComparableArray(Comparable[] sorted, Permutation unsort) {
      super(unsort);
      this.sorted = sorted;
    }
    public Comparable[] getArray() {
      return unsort.apply(sorted);
    }
    public Comparable[] getSortedArray() {
      return Arrays.copyOf(sorted, sorted.length);
    }
    public int indexOf(Comparable el) {
      int i = Arrays.binarySearch(sorted, el);
      return i < 0 ? -1 : unsort.apply(i);
    }
  }


  public static final class ObjectArray extends AArray {
    private final Object[] sorted;
    private final Comparator comparator;
    private ObjectArray(Object[] sorted, Comparator comparator, Permutation unsort) {
      super(unsort);
      this.sorted = sorted;
      this.comparator = comparator;
    }
    public Object[] getArray() {
      return unsort.apply(sorted);
    }
    public Object[] getSortedArray() {
      return Arrays.copyOf(sorted, sorted.length);
    }
    public Comparator getComparator() {
      return comparator;
    }
    public int indexOf(Object el) {
      int i = Arrays.binarySearch(sorted, el, comparator);
      return i < 0 ? -1 : unsort.apply(i);
    }
  }

}
