package com.github.methylene.sym;

import java.util.Arrays;
import java.util.Comparator;

/**
 * <p>This class contains the {@code sort} and {@code from} factory methods.</p>
 *
 * <p>{@code sort} delegates to {@code java.util.Arrays.sort},
 * however it doesn't actually sort the input but
 * only returns a permutation that will sort the input when applied to it.</p>
 *
 * <p>The {@code from} factory method allows permutation definition
 * &quot;by example&quot;.</p>
 */
public final class PermutationFactory {

  /**
   * Enum of possible Strictness settings. Default is nonstrict behaviour, i.e.
   * {@link com.github.methylene.sym.PermutationFactory.Strictness#ALLOW_DUPLICATES}
   * @see com.github.methylene.sym.PermutationFactory.Builder#setStrictness
   */
  public static enum Strictness {
    ALLOW_DUPLICATES, FORBID_DUPLICATES
  }

  /**
   * Enum of possible paranoia settings. Default is not paranoid, i.e.
   * {@link com.github.methylene.sym.PermutationFactory.Paranoia#SKIP_VALIDATION}.
   * @see com.github.methylene.sym.PermutationFactory.Builder#setParanoia
   */
  static enum Paranoia {
    SKIP_VALIDATION, ALWAYS_VALIDATE
  }

  /* The strictness level */
  private final Strictness strictness;

  /* The paranoia setting */
  private final Paranoia paranoia;

  private PermutationFactory(Strictness strictness, Paranoia paranoia) {
    this.strictness = strictness;
    this.paranoia = paranoia;
  }

  /**
   * Check the strictness setting of this factory.
   * @return the strictness setting of this factory
   * @see com.github.methylene.sym.PermutationFactory.Builder#setStrictness
   */
  public Strictness getStrictness() {
    return strictness;
  }

  /**
   * Get the paranoia setting of this factory.
   * @return the paranoia setting of this factory
   * @see com.github.methylene.sym.PermutationFactory.Builder#setParanoia
   */
  Paranoia getParanoia() {
    return paranoia;
  }

  /**
   * A builder that can be used to create instances of PermutationFactory.
   * Builder instances can be obtained via {@link com.github.methylene.sym.PermutationFactory#builder}.
   * This may not be necessary as there are already PermutationFactory instances available
   * via {@link com.github.methylene.sym.Permutation#factory} and
   * {@link com.github.methylene.sym.Permutation#strictFactory}.
   */
  public static class Builder {
    private Builder() {}

    private Strictness strictness = Strictness.ALLOW_DUPLICATES;
    private Paranoia paranoia = Paranoia.SKIP_VALIDATION;

    /**
     * When strictness is set, the resulting Factory does not allow duplicate elements in input arrays,
     * in the {@link com.github.methylene.sym.PermutationFactory#from(int[], int[])}
     * or {@link com.github.methylene.sym.PermutationFactory#sort(char[])} methods and their various overloads,
     * and will throw an IllegalArgumentException instead.
     * @return the current instance
     */
    public Builder setStrictness(Strictness strictness) {
      this.strictness = strictness;
      return this;
    }

    /**
     * When paranoia is set, run an extra assertion on each Permitation returned by the
     * from {@link com.github.methylene.sym.PermutationFactory#from(int[], int[])}
     * and {@link com.github.methylene.sym.PermutationFactory#sort(char[])} methods and their various overloads.
     * This is package-private as it should not be necessary outside of unit tests for this class.
     * @return the current instance
     */
    Builder setParanoia(Paranoia validate) {
      this.paranoia = validate;
      return this;
    }

    public PermutationFactory build() {
      return new PermutationFactory(strictness, paranoia);
    }
  }

  public static Builder builder() {
    return new Builder();
  }

  /* ================= sort ================= /*

  /**
   * Returns a permutation that sorts the input.
   * @see com.github.methylene.sym.PermutationFactory#sort(char[]) */
  public Permutation sort(byte[] input) {
    byte[] sorted = Util.sortedCopy(input);
    int[] result = new int[input.length];
    boolean[] used = new boolean[input.length];
    for (int i = 0; i < input.length; i += 1) {
      int idx = Arrays.binarySearch(sorted, input[i]);
      int offset = 0;
      int direction = 1;
      while (used[idx + offset]) {
        if (strictness == Strictness.FORBID_DUPLICATES)
          throw new IllegalArgumentException("duplicate: " + input[i]);
        offset += direction;
        if (idx + offset >= sorted.length || sorted[idx + offset] != input[i]) {
          assert direction != -1;
          offset = -1;
          direction = -1;
        }
      }
      result[i] = idx + offset;
      used[idx + offset] = true;
    }
    return new Permutation(result, paranoia == Paranoia.ALWAYS_VALIDATE);
  }

  /**
   * Returns a permutation that sorts the input.
   * @see com.github.methylene.sym.PermutationFactory#sort(char[]) */
  public Permutation sort(short[] input) {
    short[] sorted = Util.sortedCopy(input);
    int[] result = new int[input.length];
    boolean[] used = new boolean[input.length];
    for (int i = 0; i < input.length; i += 1) {
      int idx = Arrays.binarySearch(sorted, input[i]);
      int offset = 0;
      int direction = 1;
      while (used[idx + offset]) {
        if (strictness == Strictness.FORBID_DUPLICATES)
          throw new IllegalArgumentException("duplicate: " + input[i]);
        offset += direction;
        if (idx + offset >= sorted.length || sorted[idx + offset] != input[i]) {
          assert direction != -1;
          offset = -1;
          direction = -1;
        }
      }
      result[i] = idx + offset;
      used[idx + offset] = true;
    }
    return new Permutation(result, paranoia == Paranoia.ALWAYS_VALIDATE);
  }

  /**
   * Returns a permutation that sorts the input.
   * @see com.github.methylene.sym.PermutationFactory#sort(char[]) */
  public Permutation sort(long[] input) {
    long[] sorted = Util.sortedCopy(input);
    int[] result = new int[input.length];
    boolean[] used = new boolean[input.length];
    for (int i = 0; i < input.length; i += 1) {
      int idx = Arrays.binarySearch(sorted, input[i]);
      int offset = 0;
      int direction = 1;
      while (used[idx + offset]) {
        if (strictness == Strictness.FORBID_DUPLICATES)
          throw new IllegalArgumentException("duplicate: " + input[i]);
        offset += direction;
        if (idx + offset >= sorted.length || sorted[idx + offset] != input[i]) {
          assert direction != -1;
          offset = -1;
          direction = -1;
        }
      }
      result[i] = idx + offset;
      used[idx + offset] = true;
    }
    return new Permutation(result, paranoia == Paranoia.ALWAYS_VALIDATE);
  }

  /**
   * Returns a permutation that sorts the input.
   * @see com.github.methylene.sym.PermutationFactory#sort(char[]) */
  public Permutation sort(float[] input) {
    float[] sorted = Util.sortedCopy(input);
    int[] result = new int[input.length];
    boolean[] used = new boolean[input.length];
    for (int i = 0; i < input.length; i += 1) {
      int idx = Arrays.binarySearch(sorted, input[i]);
      int offset = 0;
      int direction = 1;
      while (used[idx + offset]) {
        if (strictness == Strictness.FORBID_DUPLICATES)
          throw new IllegalArgumentException("duplicate: " + input[i]);
        offset += direction;
        if (idx + offset >= sorted.length || sorted[idx + offset] != input[i]) {
          assert direction != -1;
          offset = -1;
          direction = -1;
        }
      }
      result[i] = idx + offset;
      used[idx + offset] = true;
    }
    return new Permutation(result, paranoia == Paranoia.ALWAYS_VALIDATE);
  }

  /**
   * Returns a permutation that sorts the input.
   * @see com.github.methylene.sym.PermutationFactory#sort(char[]) */
  public Permutation sort(double[] input) {
    double[] sorted = Util.sortedCopy(input);
    int[] result = new int[input.length];
    boolean[] used = new boolean[input.length];
    for (int i = 0; i < input.length; i += 1) {
      int idx = Arrays.binarySearch(sorted, input[i]);
      int offset = 0;
      int direction = 1;
      while (used[idx + offset]) {
        if (strictness == Strictness.FORBID_DUPLICATES)
          throw new IllegalArgumentException("duplicate: " + input[i]);
        offset += direction;
        if (idx + offset >= sorted.length || sorted[idx + offset] != input[i]) {
          assert direction != -1;
          offset = -1;
          direction = -1;
        }
      }
      result[i] = idx + offset;
      used[idx + offset] = true;
    }
    return new Permutation(result, paranoia == Paranoia.ALWAYS_VALIDATE);
  }

  /**
   * Returns a permutation that sorts the input array.
   * There is no guarantee which permutation will be chosen if there is a choice,
   * i.e. if the input contains duplicates.
   * @param input an array, not necessarily distinct
   * @return a permutation that sorts the input
   * @throws java.lang.IllegalArgumentException if {@code strictness} is true and {@code input} contains duplicates
   */
  public Permutation sort(char[] input) {
    char[] sorted = Util.sortedCopy(input);
    int[] result = new int[input.length];
    boolean[] used = new boolean[input.length];
    for (int i = 0; i < input.length; i += 1) {
      int idx = Arrays.binarySearch(sorted, input[i]);
      int offset = 0;
      int direction = 1;
      while (used[idx + offset]) {
        if (strictness == Strictness.FORBID_DUPLICATES)
          throw new IllegalArgumentException("duplicate: " + input[i]);
        offset += direction;
        if (idx + offset >= sorted.length || sorted[idx + offset] != input[i]) {
          assert direction != -1;
          offset = -1;
          direction = -1;
        }
      }
      result[i] = idx + offset;
      used[idx + offset] = true;
    }
    return new Permutation(result, paranoia == Paranoia.ALWAYS_VALIDATE);
  }

  /**
   * Returns a permutation that sorts the input.
   * @see com.github.methylene.sym.PermutationFactory#sort(char[]) */
  public Permutation sort(Comparable[] input) {
    Comparable[] sorted = Util.sortedCopy(input);
    int[] result = new int[input.length];
    boolean[] used = new boolean[input.length];
    for (int i = 0; i < input.length; i += 1) {
      int idx = Arrays.binarySearch(sorted, input[i]);
      int offset = 0;
      int direction = 1;
      while (used[idx + offset]) {
        if (strictness == Strictness.FORBID_DUPLICATES)
          throw new IllegalArgumentException("duplicate: " + input[i]);
        offset += direction;
        if (idx + offset >= sorted.length || !sorted[idx + offset].equals(input[i])) {
          assert direction != -1;
          offset = -1;
          direction = -1;
        }
      }
      result[i] = idx + offset;
      used[idx + offset] = true;
    }
    return new Permutation(result, paranoia == Paranoia.ALWAYS_VALIDATE);
  }

  /** @see com.github.methylene.sym.PermutationFactory#sort(char[]) */
  public Permutation sort(Object[] input, Comparator comp) {
    Object[] sorted = Util.sortedCopy(input, comp);
    int[] result = new int[input.length];
    boolean[] used = new boolean[input.length];
    for (int i = 0; i < input.length; i += 1) {
      int idx = Arrays.binarySearch(sorted, input[i], comp);
      int offset = 0;
      int direction = 1;
      while (used[idx + offset]) {
        if (strictness == Strictness.FORBID_DUPLICATES)
          throw new IllegalArgumentException("duplicate: " + input[i]);
        offset += direction;
        if (idx + offset >= sorted.length || !sorted[idx + offset].equals(input[i])) {
          assert direction != -1;
          offset = -1;
          direction = -1;
        }
      }
      result[i] = idx + offset;
      used[idx + offset] = true;
    }
    return new Permutation(result, paranoia == Paranoia.ALWAYS_VALIDATE);
  }

  /**
   * Returns a permutation that sorts the input array.
   * There is no guarantee which one will be chosen if there is a choice,
   * i.e. if the input contains duplicates.
   * @param input an array of integers
   * @return a permutation that sorts the input
   * @throws java.lang.IllegalArgumentException if {@code strictness} is true and {@code input} contains duplicates
   */
  public Permutation sort(int[] input) {
    int[] sorted = Util.sortedCopy(input);
    int[] result = new int[input.length];
    boolean[] used = new boolean[input.length];
    for (int i = 0; i < input.length; i += 1) {
      int idx = Arrays.binarySearch(sorted, input[i]);
      int offset = 0;
      int direction = 1;
      while (used[idx + offset]) {
        if (strictness == Strictness.FORBID_DUPLICATES)
          throw new IllegalArgumentException("duplicate: " + input[i]);
        offset += direction;
        if (idx + offset >= sorted.length || sorted[idx + offset] != input[i]) {
          assert direction != -1;
          offset = -1;
          direction = -1;
        }
      }
      result[i] = idx + offset;
      used[idx + offset] = true;
    }
    return new Permutation(result, paranoia == Paranoia.ALWAYS_VALIDATE);
  }

  /**
   * Returns a permutation that sorts the input string.
   * @param s a string
   * @return a permutation that sorts {@code s}
   */
  public Permutation sort(String s) {
    char[] chars = new char[s.length()];
    s.getChars(0, chars.length, chars, 0);
    return sort(chars);
  }

  /* ================= from ================= */

  /**
   * Returns a permutation that rearranges {@code a} into {@code b}.
   * @param a an array
   * @param b an array that can be obtained by changing the order of the elements of {@code a}
   * @return a permutation so that {@code Arrays.equals(Permutation.from(a, b).apply(a), b)} is true
   * @throws java.lang.IllegalArgumentException if {@code strictness} is true and {@code a} or {@code b} contain duplicates,
   * or if {@code b} can not be obtained by rearranging {@code a}.
   */
  public Permutation from(int[] a, int[] b) {
    if (a.length != b.length) {throw new IllegalArgumentException("arguments must have equal length");}
    Permutation sortB = sort(b);
    int[] sortedB = sortB.apply(b);
    Permutation unsortB = sortB.invert();
    int[] result = new int[a.length];
    boolean[] used = new boolean[b.length];
    for (int i = 0; i < a.length; i += 1) {
      int idx = Arrays.binarySearch(sortedB, a[i]);
      if (idx < 0) {throw new IllegalArgumentException("not in b: " + a[i]);}
      int offset = 0;
      int direction = 1;
      while (used[idx + offset]) {
        offset += direction;
        if (direction == 1) {
          if (idx + offset >= sortedB.length
              || sortedB[idx + offset] != a[i]) {
            if (idx > 0) {
              offset = -1;
              direction = -1;
            } else {
              throw new IllegalArgumentException("multiplicity differs: " + a[i]);
            }
          }
        } else if (idx + offset < 0 || sortedB[idx + offset] != a[i]) {
          throw new IllegalArgumentException("multiplicity differs: " + a[i]);
        }
      }
      result[i] = unsortB.apply(idx + offset);
      if (a[i] != b[result[i]])
        throw new IllegalArgumentException("multiplicity differs: " + a[i]);
      used[idx + offset] = true;
    }
    return new Permutation(result, paranoia == Paranoia.ALWAYS_VALIDATE);
  }

  /**
   * Returns a permutation that rearranges {@code a} into {@code b}.
   * See the documentation of
   * {@link com.github.methylene.sym.PermutationFactory#from(int[], int[])} for details.
   * @throws java.lang.NullPointerException if {@code a} or {@code b} contain null
   */
  public Permutation from(Comparable[] a, Comparable[] b) {
    if (a.length != b.length)
      throw new IllegalArgumentException("arguments must have equal length");
    Permutation sortB = sort(b);
    Comparable[] sortedB = sortB.apply(b);
    Permutation unsortB = sortB.invert();
    int[] result = new int[a.length];
    boolean[] used = new boolean[b.length];
    for (int i = 0; i < a.length; i += 1) {
      int idx = Arrays.binarySearch(sortedB, a[i]);
      if (idx < 0)
        throw new IllegalArgumentException("not in b: " + a[i]);
      int offset = 0;
      int direction = 1;
      while (used[idx + offset]) {
        offset += direction;
        if (direction == 1) {
          if (idx + offset >= sortedB.length
              || !sortedB[idx + offset].equals(a[i])) {
            if (idx > 0) {
              offset = -1;
              direction = -1;
            } else {
              throw new IllegalArgumentException("multiplicity differs: " + a[i]);
            }
          }
        } else if (idx + offset < 0 || !sortedB[idx + offset].equals(a[i])) {
          throw new IllegalArgumentException("multiplicity differs: " + a[i]);
        }
      }
      result[i] = unsortB.apply(idx + offset);
      if (!a[i].equals(b[result[i]]))
        throw new IllegalArgumentException("multiplicity differs: " + a[i]);
      used[idx + offset] = true;
    }
    return new Permutation(result, paranoia == Paranoia.ALWAYS_VALIDATE);
  }

  /**
   * Returns a permutation that rearranges {@code a} into {@code b}.
   * See the documentation of
   * {@link com.github.methylene.sym.PermutationFactory#from(int[], int[])} for details.
   */
  public Permutation from(byte[] a, byte[] b) {
    if (a.length != b.length)
      throw new IllegalArgumentException("arguments must have equal length");
    Permutation sortB = sort(b);
    byte[] sortedB = sortB.apply(b);
    Permutation unsortB = sortB.invert();
    int[] result = new int[a.length];
    boolean[] used = new boolean[b.length];
    for (int i = 0; i < a.length; i += 1) {
      int idx = Arrays.binarySearch(sortedB, a[i]);
      if (idx < 0)
        throw new IllegalArgumentException("not in b: " + a[i]);
      int offset = 0;
      int direction = 1;
      while (used[idx + offset]) {
        offset += direction;
        if (direction == 1) {
          if (idx + offset >= sortedB.length
              || sortedB[idx + offset] != a[i]) {
            if (idx > 0) {
              offset = -1;
              direction = -1;
            } else {
              throw new IllegalArgumentException("multiplicity differs: " + a[i]);
            }
          }
        } else if (idx + offset < 0 || sortedB[idx + offset] != a[i]) {
          throw new IllegalArgumentException("multiplicity differs: " + a[i]);
        }
      }
      result[i] = unsortB.apply(idx + offset);
      if (a[i] != b[result[i]])
        throw new IllegalArgumentException("multiplicity differs: " + a[i]);
      used[idx + offset] = true;
    }
    return new Permutation(result, paranoia == Paranoia.ALWAYS_VALIDATE);
  }

  /**
   * Returns a permutation that rearranges {@code a} into {@code b}.
   * See the documentation of
   * {@link com.github.methylene.sym.PermutationFactory#from(int[], int[])} for details.
   */
  public Permutation from(long[] a, long[] b) {
    if (a.length != b.length)
      throw new IllegalArgumentException("arguments must have equal length");
    Permutation sortB = sort(b);
    long[] sortedB = sortB.apply(b);
    Permutation unsortB = sortB.invert();
    int[] result = new int[a.length];
    boolean[] used = new boolean[b.length];
    for (int i = 0; i < a.length; i += 1) {
      int idx = Arrays.binarySearch(sortedB, a[i]);
      if (idx < 0)
        throw new IllegalArgumentException("not in b: " + a[i]);
      int offset = 0;
      int direction = 1;
      while (used[idx + offset]) {
        offset += direction;
        if (direction == 1) {
          if (idx + offset >= sortedB.length
              || sortedB[idx + offset] != a[i]) {
            if (idx > 0) {
              offset = -1;
              direction = -1;
            } else {
              throw new IllegalArgumentException("multiplicity differs: " + a[i]);
            }
          }
        } else if (idx + offset < 0 || sortedB[idx + offset] != a[i]) {
          throw new IllegalArgumentException("multiplicity differs: " + a[i]);
        }
      }
      result[i] = unsortB.apply(idx + offset);
      if (a[i] != b[result[i]])
        throw new IllegalArgumentException("multiplicity differs: " + a[i]);
      used[idx + offset] = true;
    }
    return new Permutation(result, paranoia == Paranoia.ALWAYS_VALIDATE);
  }

  /**
   * Returns a permutation that rearranges {@code a} into {@code b}.
   * See the documentation of
   * {@link com.github.methylene.sym.PermutationFactory#from(int[], int[])} for details.
   */
  public Permutation from(float[] a, float[] b) {
    if (a.length != b.length)
      throw new IllegalArgumentException("arguments must have equal length");
    Permutation sortB = sort(b);
    float[] sortedB = sortB.apply(b);
    Permutation unsortB = sortB.invert();
    int[] result = new int[a.length];
    boolean[] used = new boolean[b.length];
    for (int i = 0; i < a.length; i += 1) {
      int idx = Arrays.binarySearch(sortedB, a[i]);
      if (idx < 0)
        throw new IllegalArgumentException("not in b: " + a[i]);
      int offset = 0;
      int direction = 1;
      while (used[idx + offset]) {
        offset += direction;
        if (direction == 1) {
          if (idx + offset >= sortedB.length
              || sortedB[idx + offset] != a[i]) {
            if (idx > 0) {
              offset = -1;
              direction = -1;
            } else {
              throw new IllegalArgumentException("multiplicity differs: " + a[i]);
            }
          }
        } else if (idx + offset < 0 || sortedB[idx + offset] != a[i]) {
          throw new IllegalArgumentException("multiplicity differs: " + a[i]);
        }
      }
      result[i] = unsortB.apply(idx + offset);
      if (a[i] != b[result[i]])
        throw new IllegalArgumentException("multiplicity differs: " + a[i]);
      used[idx + offset] = true;
    }
    return new Permutation(result, paranoia == Paranoia.ALWAYS_VALIDATE);
  }

  /**
   * Returns a permutation that rearranges {@code a} into {@code b}.
   * See the documentation of
   * {@link com.github.methylene.sym.PermutationFactory#from(int[], int[])} for details.
   */
  public Permutation from(double[] a, double[] b) {
    if (a.length != b.length)
      throw new IllegalArgumentException("arguments must have equal length");
    Permutation sortB = sort(b);
    double[] sortedB = sortB.apply(b);
    Permutation unsortB = sortB.invert();
    int[] result = new int[a.length];
    boolean[] used = new boolean[b.length];
    for (int i = 0; i < a.length; i += 1) {
      int idx = Arrays.binarySearch(sortedB, a[i]);
      if (idx < 0)
        throw new IllegalArgumentException("not in b: " + a[i]);
      int offset = 0;
      int direction = 1;
      while (used[idx + offset]) {
        offset += direction;
        if (direction == 1) {
          if (idx + offset >= sortedB.length
              || sortedB[idx + offset] != a[i]) {
            if (idx > 0) {
              offset = -1;
              direction = -1;
            } else {
              throw new IllegalArgumentException("multiplicity differs: " + a[i]);
            }
          }
        } else if (idx + offset < 0 || sortedB[idx + offset] != a[i]) {
          throw new IllegalArgumentException("multiplicity differs: " + a[i]);
        }
      }
      result[i] = unsortB.apply(idx + offset);
      if (a[i] != b[result[i]])
        throw new IllegalArgumentException("multiplicity differs: " + a[i]);
      used[idx + offset] = true;
    }
    return new Permutation(result, paranoia == Paranoia.ALWAYS_VALIDATE);
  }

  /**
   * Returns a permutation that rearranges {@code a} into {@code b}.
   * The comparator {@code comp} must be able to compare all elements in {@code a} and {@code b}
   * It is needed for efficient lookups in {@code b}.
   * See the documentation of
   * {@link com.github.methylene.sym.PermutationFactory#from(int[], int[])} for details.
   * @throws java.lang.NullPointerException if {@code a} or {@code b} contain null
   */
  public Permutation from(Object[] a, Object[] b, Comparator comp) {
    if (a.length != b.length)
      throw new IllegalArgumentException("arguments must have equal length");
    Permutation sortB = sort(b, comp);
    Object[] sortedB = sortB.apply(b);
    Permutation unsortB = sortB.invert();
    int[] result = new int[a.length];
    boolean[] used = new boolean[b.length];
    for (int i = 0; i < a.length; i += 1) {
      int idx = Arrays.binarySearch(sortedB, a[i], comp);
      if (idx < 0)
        throw new IllegalArgumentException("not in b: " + a[i]);
      int offset = 0;
      int direction = 1;
      while (used[idx + offset]) {
        offset += direction;
        if (direction == 1) {
          if (idx + offset >= sortedB.length
              || !sortedB[idx + offset].equals(a[i])) {
            if (idx > 0) {
              offset = -1;
              direction = -1;
            } else {
              throw new IllegalArgumentException("multiplicity differs: " + a[i]);
            }
          }
        } else if (idx + offset < 0 || !sortedB[idx + offset].equals(a[i])) {
          throw new IllegalArgumentException("multiplicity differs: " + a[i]);
        }
      }
      result[i] = unsortB.apply(idx + offset);
      if (!a[i].equals(b[result[i]]))
        throw new IllegalArgumentException("multiplicity differs: " + a[i]);
      used[idx + offset] = true;
    }
    return new Permutation(result, paranoia == Paranoia.ALWAYS_VALIDATE);
  }

}
