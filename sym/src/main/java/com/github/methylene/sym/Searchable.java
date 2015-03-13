package com.github.methylene.sym;

import java.util.Arrays;
import java.util.Comparator;

/**
 * The various {@code *Array} inner classes add an efficient {@code indexOf} method to standard arrays.
 * If there is a choice, that is if the elements of the original array are not pairwise distinct,
 * there is no guarantee which index will be chosen.
 */
public class Searchable {

  private final PermutationFactory factory;

  public Searchable(PermutationFactory permutationFactory) {
    this.factory = permutationFactory;
  }

  private static final Searchable STRICT = new Searchable(Permutation.strictFactory());
  private static final Searchable NON_STRICT = new Searchable(Permutation.factory());

  public static Searchable strictSearchable() {
    return STRICT;
  }

  public static Searchable searchable() {
    return NON_STRICT;
  }

  private static abstract class AArray {

    protected final Permutation unsort;

    protected AArray(Permutation unsort) {
      this.unsort = unsort;
    }

    public final Permutation getUnsort() {
      return unsort;
    }

  }

  public IntArray array(int[] a) {
    Permutation sort = factory.sort(a);
    return new IntArray(sort.apply(a), sort.invert());
  }

  public LongArray array(long[] a) {
    Permutation sort = factory.sort(a);
    return new LongArray(sort.apply(a), sort.invert());
  }

  public ByteArray array(byte[] a) {
    Permutation sort = factory.sort(a);
    return new ByteArray(sort.apply(a), sort.invert());
  }

  public CharArray array(char[] a) {
    Permutation sort = factory.sort(a);
    return new CharArray(sort.apply(a), sort.invert());
  }

  public FloatArray array(float[] a) {
    Permutation sort = factory.sort(a);
    return new FloatArray(sort.apply(a), sort.invert());
  }

  public DoubleArray array(double[] a) {
    Permutation sort = factory.sort(a);
    return new DoubleArray(sort.apply(a), sort.invert());
  }

  public ShortArray array(short[] a) {
    Permutation sort = factory.sort(a);
    return new ShortArray(sort.apply(a), sort.invert());
  }

  public ComparableArray array(Comparable[] a) {
    Permutation sort = factory.sort(a);
    return new ComparableArray(sort.apply(a), sort.invert());
  }

  public ObjectArray array(Object[] a, Comparator comparator) {
    Permutation sort = factory.sort(a, comparator);
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

  public static final class FloatArray extends AArray {
    private final float[] sorted;

    private FloatArray(float[] sorted, Permutation unsort) {
      super(unsort);
      this.sorted = sorted;
    }

    public float[] getArray() {
      return unsort.apply(sorted);
    }

    public float[] getSortedArray() {
      return Arrays.copyOf(sorted, sorted.length);
    }

    public int indexOf(float el) {
      int i = Arrays.binarySearch(sorted, el);
      return i < 0 ? -1 : unsort.apply(i);
    }
  }

  public static final class DoubleArray extends AArray {
    private final double[] sorted;

    private DoubleArray(double[] sorted, Permutation unsort) {
      super(unsort);
      this.sorted = sorted;
    }

    public double[] getArray() {
      return unsort.apply(sorted);
    }

    public double[] getSortedArray() {
      return Arrays.copyOf(sorted, sorted.length);
    }

    public int indexOf(double el) {
      int i = Arrays.binarySearch(sorted, el);
      return i < 0 ? -1 : unsort.apply(i);
    }
  }

  public static final class ShortArray extends AArray {
    private final short[] sorted;

    private ShortArray(short[] sorted, Permutation unsort) {
      super(unsort);
      this.sorted = sorted;
    }

    public short[] getArray() {
      return unsort.apply(sorted);
    }

    public short[] getSortedArray() {
      return Arrays.copyOf(sorted, sorted.length);
    }

    public int indexOf(short el) {
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
