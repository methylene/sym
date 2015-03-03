package sorted.sym;

import java.util.Arrays;
import java.util.Comparator;

public final class Permutation implements Comparable<Permutation> {

  private final int[] posmap;

  /**
   * @param posmap A list of indexes.
   *               For example, {@code posmap = new int[]{1, 2, 0}} creates the permutation
   *               that sends {@code new char[]{'a', 'b', 'c'}} to {@code new char[]{'c', 'a', 'b'}}.
   */
  public Permutation(int[] posmap) {
    Util.validate(posmap);
    this.posmap = posmap;
  }

  static public Permutation perm1(int... posmap1based) {
    return new Permutation(Util.add(posmap1based, -1));
  }

  static public Permutation cycle1(int... cycle1based) {
    int maxPos = 0;
    int[] cycle = new int[cycle1based.length];
    for (int i = 0; i < cycle1based.length; i += 1) {
      if (cycle1based[i] < 1) throw new IllegalArgumentException();
      cycle[i] = cycle1based[i] - 1;
      maxPos = Math.max(maxPos, cycle[i]);
    }
    int resultLength = maxPos + 1;
    int[] cycleIndexes = new int[resultLength];
    for (int i: cycle) {
      if (cycleIndexes[i] != 0) throw new IllegalArgumentException();
      cycleIndexes[i] = 1;
    }
    int[] result = new int[resultLength];
    for (int i = 0; i < resultLength; i += 1)
      result[i] = cycleIndexes[i] == 0
              ? i
              : cycle[(Util.indexOf(cycle, i) + 1) % cycle.length];
    return new Permutation(result);
  }

  static public Permutation identity(int length) {
    int[] posmap = new int[length];
    for (int i = 0; i < length; i += 1)
      posmap[i] = i;
    return new Permutation(posmap);
  }

  public Permutation comp(Permutation other) {
    int targetLength = Math.max(posmap.length, other.posmap.length);
    int[] lhs = Util.pad(posmap, targetLength);
    int[] rhs = Util.pad(other.posmap, targetLength);
    int[] result = new int[targetLength];
    for (int i = 0; i < targetLength; i += 1)
      result[i] = lhs[rhs[i]];
    return new Permutation(result);
  }

  public static Permutation prod(Permutation... permutations) {
    if (permutations.length == 0) return identity(0);
    Permutation result = permutations[0];
    for (int i = 1; i < permutations.length; i += 1)
      result = result.comp(permutations[i]);
    return result;
  }

  public Permutation pow(int power) {
    if (power == 0) return identity(length());
    Permutation seed = power < 0 ? invert() : this;
    Permutation result = seed;
    for (int i = 1; i < Math.abs(power); i += 1)
      result = result.comp(seed);
    return result;
  }

  public Permutation invert() {
    int[][] posmapWithIndex = Util.withIndex(posmap);
    Arrays.sort(posmapWithIndex, Util.COMPARE_2ND);
    int[] result = new int[posmap.length];
    for (int i = 0; i < posmap.length; i += 1)
      result[i] = posmapWithIndex[i][0];
    return new Permutation(result);
  }

  public boolean isIdentity() {
    for (int i = 0; i < posmap.length; i += 1)
      if (posmap[i] != i)
        return false;
    return true;
  }

  public int length() {
    return posmap.length;
  }

  @Override
  public String toString() {
    return Arrays.toString(Util.add(posmap, 1));
  }

  @Override
  public boolean equals(Object other) {
    if (this == other) return true;
    if (other == null || getClass() != other.getClass()) return false;
    return Arrays.equals(posmap, ((Permutation) other).posmap);
  }

  @Override
  public int hashCode() {
    return Arrays.hashCode(posmap);
  }

  @Override
  public int compareTo(Permutation other) {
    if (this == other) return 0;
    for (int i = 0; i < Math.min(this.posmap.length, other.posmap.length); i += 1)
      if (this.posmap[i] != other.posmap[i])
        return this.posmap[i] - other.posmap[i];
    return other.posmap.length - this.posmap.length;
  }

  public int apply(int pos) {
    return posmap[pos];
  }

  /* overloaded versions of apply */

  public Object[] apply(Object[] input) {
    if (input.length < posmap.length)
      throw new IllegalArgumentException("too short: " + input.length);
    Object[] result = new Object[input.length];
    for (int i = 0; i < posmap.length; i += 1)
      result[posmap[i]] = input[i];
    if (input.length > posmap.length)
      System.arraycopy(input, posmap.length, result, posmap.length, input.length - posmap.length);
    return result;
  }

  public String[] apply(String[] input) {
    if (input.length < posmap.length)
      throw new IllegalArgumentException("too short: " + input.length);
    String[] result = new String[input.length];
    for (int i = 0; i < posmap.length; i += 1)
      result[posmap[i]] = input[i];
    if (input.length > posmap.length)
      System.arraycopy(input, posmap.length, result, posmap.length, input.length - posmap.length);
    return result;
  }

  public byte[] apply(byte[] input) {
    if (input.length < posmap.length)
      throw new IllegalArgumentException("too short: " + input.length);
    byte[] result = new byte[input.length];
    for (int i = 0; i < posmap.length; i += 1)
      result[posmap[i]] = input[i];
    if (input.length > posmap.length)
      System.arraycopy(input, posmap.length, result, posmap.length, input.length - posmap.length);
    return result;
  }

  public short[] apply(short[] input) {
    if (input.length < posmap.length)
      throw new IllegalArgumentException("too short: " + input.length);
    short[] result = new short[input.length];
    for (int i = 0; i < posmap.length; i += 1)
      result[posmap[i]] = input[i];
    if (input.length > posmap.length)
      System.arraycopy(input, posmap.length, result, posmap.length, input.length - posmap.length);
    return result;
  }

  public int[] apply(int[] input) {
    if (input.length < posmap.length)
      throw new IllegalArgumentException("too short: " + input.length);
    int[] result = new int[input.length];
    for (int i = 0; i < posmap.length; i += 1)
      result[posmap[i]] = input[i];
    if (input.length > posmap.length)
      System.arraycopy(input, posmap.length, result, posmap.length, input.length - posmap.length);
    return result;
  }

  public long[] apply(long[] input) {
    if (input.length < posmap.length)
      throw new IllegalArgumentException("too short: " + input.length);
    long[] result = new long[input.length];
    for (int i = 0; i < posmap.length; i += 1)
      result[posmap[i]] = input[i];
    if (input.length > posmap.length)
      System.arraycopy(input, posmap.length, result, posmap.length, input.length - posmap.length);
    return result;
  }

  public float[] apply(float[] input) {
    if (input.length < posmap.length)
      throw new IllegalArgumentException("too short: " + input.length);
    float[] result = new float[input.length];
    for (int i = 0; i < posmap.length; i += 1)
      result[posmap[i]] = input[i];
    if (input.length > posmap.length)
      System.arraycopy(input, posmap.length, result, posmap.length, input.length - posmap.length);
    return result;
  }

  public double[] apply(double[] input) {
    if (input.length < posmap.length)
      throw new IllegalArgumentException("too short: " + input.length);
    double[] result = new double[input.length];
    for (int i = 0; i < posmap.length; i += 1)
      result[posmap[i]] = input[i];
    if (input.length > posmap.length)
      System.arraycopy(input, posmap.length, result, posmap.length, input.length - posmap.length);
    return result;
  }


  public boolean[] apply(boolean[] input) {
    if (input.length < posmap.length)
      throw new IllegalArgumentException("too short: " + input.length);
    boolean[] result = new boolean[input.length];
    for (int i = 0; i < posmap.length; i += 1)
      result[posmap[i]] = input[i];
    if (input.length > posmap.length)
      System.arraycopy(input, posmap.length, result, posmap.length, input.length - posmap.length);
    return result;
  }

  public char[] apply(char[] input) {
    if (input.length < posmap.length)
      throw new IllegalArgumentException("too short: " + input.length);
    char[] result = new char[input.length];
    for (int i = 0; i < posmap.length; i += 1)
      result[posmap[i]] = input[i];
    if (input.length > posmap.length)
      System.arraycopy(input, posmap.length, result, posmap.length, input.length - posmap.length);
    return result;
  }

  public String[] apply() {
    return apply(Util.symbols(posmap.length));
  }

  /* overloaded versions of sort */

  static public Permutation sort(byte[] distinct) {
    byte[] sorted = Arrays.copyOf(distinct, distinct.length);
    Arrays.sort(sorted);
    int[] result = new int[distinct.length];
    for (int i = 0; i < distinct.length; i += 1) {
      result[i] = Arrays.binarySearch(sorted, distinct[i]);
    }
    return new Permutation(result);
  }

  static public Permutation sort(short[] distinct) {
    short[] sorted = Arrays.copyOf(distinct, distinct.length);
    Arrays.sort(sorted);
    int[] result = new int[distinct.length];
    for (int i = 0; i < distinct.length; i += 1) {
      result[i] = Arrays.binarySearch(sorted, distinct[i]);
    }
    return new Permutation(result);
  }

  static public Permutation sort(int[] distinct) {
    int[] sorted = Arrays.copyOf(distinct, distinct.length);
    Arrays.sort(sorted);
    int[] result = new int[distinct.length];
    for (int i = 0; i < distinct.length; i += 1) {
      result[i] = Arrays.binarySearch(sorted, distinct[i]);
    }
    return new Permutation(result);
  }

  static public Permutation sort(long[] distinct) {
    long[] sorted = Arrays.copyOf(distinct, distinct.length);
    Arrays.sort(sorted);
    int[] result = new int[distinct.length];
    for (int i = 0; i < distinct.length; i += 1) {
      result[i] = Arrays.binarySearch(sorted, distinct[i]);
    }
    return new Permutation(result);
  }

  static public Permutation sort(float[] distinct) {
    float[] sorted = Arrays.copyOf(distinct, distinct.length);
    Arrays.sort(sorted);
    int[] result = new int[distinct.length];
    for (int i = 0; i < distinct.length; i += 1) {
      result[i] = Arrays.binarySearch(sorted, distinct[i]);
    }
    return new Permutation(result);
  }

  static public Permutation sort(double[] distinct) {
    double[] sorted = Arrays.copyOf(distinct, distinct.length);
    Arrays.sort(sorted);
    int[] result = new int[distinct.length];
    for (int i = 0; i < distinct.length; i += 1) {
      result[i] = Arrays.binarySearch(sorted, distinct[i]);
    }
    return new Permutation(result);
  }

  static public Permutation sort(char[] distinct) {
    char[] sorted = Arrays.copyOf(distinct, distinct.length);
    Arrays.sort(sorted);
    int[] result = new int[distinct.length];
    for (int i = 0; i < distinct.length; i += 1) {
      result[i] = Arrays.binarySearch(sorted, distinct[i]);
    }
    return new Permutation(result);
  }

  static public Permutation sort(Comparable[] distinct) {
    Comparable[] sorted = Arrays.copyOf(distinct, distinct.length);
    Arrays.sort(sorted);
    int[] result = new int[distinct.length];
    for (int i = 0; i < distinct.length; i += 1) {
      result[i] = Arrays.binarySearch(sorted, distinct[i]);
    }
    return new Permutation(result);
  }

  static public Permutation sort(Object[] distinct, Comparator comparator) {
    Object[] sorted = Arrays.copyOf(distinct, distinct.length);
    Arrays.sort(sorted, comparator);
    int[] result = new int[distinct.length];
    for (int i = 0; i < distinct.length; i += 1) {
      result[i] = Arrays.binarySearch(sorted, distinct[i], comparator);
    }
    return new Permutation(result);
  }

}
