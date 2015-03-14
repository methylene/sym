package com.github.methylene.sym;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Objects;

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
   * Enum of possible uniqueness settings. Default is to allow duplicates in the input of sort and from.
   * @see com.github.methylene.sym.PermutationFactory#sort
   * @see com.github.methylene.sym.PermutationFactory#from
   */
  public static enum UniquenessConstraint {
    ALLOW_DUPLICATES, FORBID_DUPLICATES
  }

  /**
   * Enum of possible NullPolicy settings. Default is to throw an exception if null is encountered
   * in the input of sort and from.
   * @see com.github.methylene.sym.PermutationFactory#sort
   * @see com.github.methylene.sym.PermutationFactory#from
   */
  public static enum NullPolicy {
    ALLOW_NULL, ALLERGIC
  }

  /**
   * Enum of paranoia settings. Default is to skip certain validations that are believed to be redundant.
   * In place of a proof of them being redundant, there are some unit tests which have this setting on.
   */
  static enum Paranoia {
    SKIP_VALIDATION, ALWAYS_VALIDATE
  }

  /* The uniqueness level */
  private final UniquenessConstraint uniquenessConstraint;

  /* The paranoia setting */
  private final Paranoia paranoia;

  /* The null policy */
  private final NullPolicy nullPolicy;

  private PermutationFactory(UniquenessConstraint uniquenessConstraint, NullPolicy nullPolicy, Paranoia paranoia) {
    this.uniquenessConstraint = uniquenessConstraint;
    this.paranoia = paranoia;
    this.nullPolicy = nullPolicy;
  }

  /**
   * Check the strictness setting of this factory.
   * @return the strictness setting of this factory
   */
  public UniquenessConstraint getUniquenessConstraint() {
    return uniquenessConstraint;
  }

  /**
   * Check the null policy of this factory.
   * @return the null policyof this factory
   */
  public NullPolicy getNullPolicy() {
    return nullPolicy;
  }

  /**
   * Get the paranoia setting of this factory.
   * @return the paranoia setting of this factory
   */
  Paranoia getParanoia() {
    return paranoia;
  }

  /**
   * A builder that can be used to create instances of PermutationFactory.
   * Builder instances can be obtained via {@link com.github.methylene.sym.PermutationFactory#builder}.
   * This may not be necessary as there are already PermutationFactory instances available
   * via {@link com.github.methylene.sym.Permutation#factory} and
   * {@link com.github.methylene.sym.Permutation#duplicateRejectingFactory}.
   */
  public static class Builder {
    private Builder() {}

    private UniquenessConstraint uniquenessConstraint = UniquenessConstraint.ALLOW_DUPLICATES;
    private Paranoia paranoia = Paranoia.SKIP_VALIDATION;
    private NullPolicy nullPolicy = NullPolicy.ALLERGIC;

    /**
     * Set the uniqueness constraint
     * @return the current instance
     */
    public Builder setUniquenessConstraint(UniquenessConstraint uniquenessConstraint) {
      this.uniquenessConstraint = uniquenessConstraint;
      return this;
    }

    /**
     * Set the paranoia level
     * @return the current instance
     */
    Builder setParanoia(Paranoia paranoia) {
      this.paranoia = paranoia;
      return this;
    }

    /**
     * Set the null policy
     * @return the current instance
     */
    public Builder setNullPolicy(NullPolicy nullPolicy) {
      this.nullPolicy = nullPolicy;
      return this;
    }

    public PermutationFactory build() {
      return new PermutationFactory(uniquenessConstraint, nullPolicy, paranoia);
    }
  }

  public static Builder builder() {
    return new Builder();
  }

  /* ================= sort ================= /*

  /**
   * Returns a permutation that sorts the input.
   * @param input an array
   * @see com.github.methylene.sym.PermutationFactory#sort(char[])
   */
  public Permutation sort(byte[] input) {
    byte[] sorted = Util.sortedCopy(input);
    int[] result = new int[input.length];
    boolean[] used = new boolean[input.length];
    for (int i = 0; i < input.length; i += 1) {
      int idx = Arrays.binarySearch(sorted, input[i]);
      int offset = 0;
      int direction = 1;
      while (used[idx + offset]) {
        if (uniquenessConstraint == UniquenessConstraint.FORBID_DUPLICATES)
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
   * @param input an array
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
        if (uniquenessConstraint == UniquenessConstraint.FORBID_DUPLICATES)
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
        if (uniquenessConstraint == UniquenessConstraint.FORBID_DUPLICATES)
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
        if (uniquenessConstraint == UniquenessConstraint.FORBID_DUPLICATES)
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
        if (uniquenessConstraint == UniquenessConstraint.FORBID_DUPLICATES)
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
        if (uniquenessConstraint == UniquenessConstraint.FORBID_DUPLICATES)
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
      if (nullPolicy == NullPolicy.ALLERGIC && input[i] == null)
        throw new NullPointerException("null values are not allowed when NullPolicy is ALLERGIC");
      int idx = Arrays.binarySearch(sorted, input[i]);
      int offset = 0;
      int direction = 1;
      while (used[idx + offset]) {
        if (uniquenessConstraint == UniquenessConstraint.FORBID_DUPLICATES)
          throw new IllegalArgumentException("duplicate: " + input[i]);
        offset += direction;
        if (idx + offset >= sorted.length || !Objects.equals(sorted[idx + offset], input[i])) {
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
      if (nullPolicy == NullPolicy.ALLERGIC && input[i] == null)
        throw new NullPointerException("null values are not allowed when NullPolicy is ALLERGIC");
      int idx = Arrays.binarySearch(sorted, input[i], comp);
      int offset = 0;
      int direction = 1;
      while (used[idx + offset]) {
        if (uniquenessConstraint == UniquenessConstraint.FORBID_DUPLICATES)
          throw new IllegalArgumentException("duplicate: " + input[i]);
        offset += direction;
        if (idx + offset >= sorted.length || !Objects.equals(sorted[idx + offset], input[i])) {
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
        if (uniquenessConstraint == UniquenessConstraint.FORBID_DUPLICATES)
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
