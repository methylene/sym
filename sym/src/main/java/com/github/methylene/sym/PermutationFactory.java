package com.github.methylene.sym;

import java.util.Arrays;
import java.util.Comparator;

public class PermutationFactory {

  /* ================= sort ================= /*

  /**
   * Returns a permutation that sorts the input.
   * @see com.github.methylene.sym.PermutationFactory#sort(char[], boolean) */
  public static Permutation sort(byte[] input, boolean strict) {
    byte[] sorted = Util.sortedCopy(input);
    int[] result = new int[input.length];
    boolean[] used = new boolean[input.length];
    for (int i = 0; i < input.length; i += 1) {
      int idx = Arrays.binarySearch(sorted, input[i]);
      int offset = 0;
      int direction = 1;
      while (used[idx + offset]) {
        if (strict)
          throw new IllegalArgumentException("duplicate: " + input[i]);
        offset += direction;
        if (idx + offset >= sorted.length || sorted[idx + offset] != input[i]) {
          offset = -1;
          direction = -1;
        }
      }
      result[i] = idx + offset;
      used[idx + offset] = true;
    }
    return new Permutation(result, false);
  }

  /**
   * Returns a permutation that sorts the input.
   * @see com.github.methylene.sym.PermutationFactory#sort(char[]) */
  public static Permutation sort(byte[] input) {
    return sort(input, false);
  }

  /**
   * Returns a permutation that sorts the input.
   * @see com.github.methylene.sym.PermutationFactory#sort(char[], boolean) */
  public static Permutation sort(long[] input, boolean strict) {
    long[] sorted = Util.sortedCopy(input);
    int[] result = new int[input.length];
    boolean[] used = new boolean[input.length];
    for (int i = 0; i < input.length; i += 1) {
      int idx = Arrays.binarySearch(sorted, input[i]);
      int offset = 0;
      int direction = 1;
      while (used[idx + offset]) {
        if (strict)
          throw new IllegalArgumentException("duplicate: " + input[i]);
        offset += direction;
        if (idx + offset >= sorted.length || sorted[idx + offset] != input[i]) {
          offset = -1;
          direction = -1;
        }
      }
      result[i] = idx + offset;
      used[idx + offset] = true;
    }
    return new Permutation(result, false);
  }

  /**
   * Returns a permutation that sorts the input.
   * @see com.github.methylene.sym.PermutationFactory#sort(char[]) */
  public static Permutation sort(long[] input) {
    return sort(input, false);
  }

  /**
   * Returns a permutation that sorts the input.
   * @see com.github.methylene.sym.PermutationFactory#sort(char[], boolean) */
  public static Permutation sort(float[] input, boolean strict) {
    float[] sorted = Util.sortedCopy(input);
    int[] result = new int[input.length];
    boolean[] used = new boolean[input.length];
    for (int i = 0; i < input.length; i += 1) {
      int idx = Arrays.binarySearch(sorted, input[i]);
      int offset = 0;
      int direction = 1;
      while (used[idx + offset]) {
        if (strict)
          throw new IllegalArgumentException("duplicate: " + input[i]);
        offset += direction;
        if (idx + offset >= sorted.length || sorted[idx + offset] != input[i]) {
          offset = -1;
          direction = -1;
        }
      }
      result[i] = idx + offset;
      used[idx + offset] = true;
    }
    return new Permutation(result, false);
  }

  /**
   * Returns a permutation that sorts the input.
   * @see com.github.methylene.sym.PermutationFactory#sort(char[]) */
  public static Permutation sort(float[] input) {
    return sort(input, false);
  }

  /**
   * Returns a permutation that sorts the input.
   * @see com.github.methylene.sym.PermutationFactory#sort(char[], boolean) */
  public static Permutation sort(double[] input, boolean strict) {
    double[] sorted = Util.sortedCopy(input);
    int[] result = new int[input.length];
    boolean[] used = new boolean[input.length];
    for (int i = 0; i < input.length; i += 1) {
      int idx = Arrays.binarySearch(sorted, input[i]);
      int offset = 0;
      int direction = 1;
      while (used[idx + offset]) {
        if (strict)
          throw new IllegalArgumentException("duplicate: " + input[i]);
        offset += direction;
        if (idx + offset >= sorted.length || sorted[idx + offset] != input[i]) {
          offset = -1;
          direction = -1;
        }
      }
      result[i] = idx + offset;
      used[idx + offset] = true;
    }
    return new Permutation(result, false);
  }

  /**
   * Returns a permutation that sorts the input.
   * @see com.github.methylene.sym.PermutationFactory#sort(char[]) */
  public static Permutation sort(double[] input) {
    return sort(input, false);
  }

  /**
   * Returns a permutation that sorts the input array.
   * There is no guarantee which permutation will be chosen if there is a choice,
   * i.e. if the input contains duplicates.
   * @param input an array, not necessarily distinct
   * @param strict do not allow duplicates if this is true, throw Exception instead
   * @return a permutation that sorts the input
   * @throws java.lang.IllegalArgumentException if {@code strict} is true and {@code input} contains duplicates
   */
  public static Permutation sort(char[] input, boolean strict) {
    char[] sorted = Util.sortedCopy(input);
    int[] result = new int[input.length];
    boolean[] used = new boolean[input.length];
    for (int i = 0; i < input.length; i += 1) {
      int idx = Arrays.binarySearch(sorted, input[i]);
      int offset = 0;
      int direction = 1;
      while (used[idx + offset]) {
        if (strict)
          throw new IllegalArgumentException("duplicate: " + input[i]);
        offset += direction;
        if (idx + offset >= sorted.length || sorted[idx + offset] != input[i]) {
          offset = -1;
          direction = -1;
        }
      }
      result[i] = idx + offset;
      used[idx + offset] = true;
    }
    return new Permutation(result, false);
  }

  /**
   * Return a permutation that sorts the input array.
   * @param input an array
   * @return a permutation that sorts the input
   */
  public static Permutation sort(char[] input) {
    return sort(input, false);
  }

  /**
   * Returns a permutation that sorts the input.
   * @see com.github.methylene.sym.PermutationFactory#sort(char[], boolean) */
  public static Permutation sort(Comparable[] input, boolean strict) {
    Comparable[] sorted = Util.sortedCopy(input);
    int[] result = new int[input.length];
    boolean[] used = new boolean[input.length];
    for (int i = 0; i < input.length; i += 1) {
      int idx = Arrays.binarySearch(sorted, input[i]);
      int offset = 0;
      int direction = 1;
      while (used[idx + offset]) {
        if (strict)
          throw new IllegalArgumentException("duplicate: " + input[i]);
        offset += direction;
        if (idx + offset >= sorted.length || sorted[idx + offset] != input[i]) {
          offset = -1;
          direction = -1;
        }
      }
      result[i] = idx + offset;
      used[idx + offset] = true;
    }
    return new Permutation(result, false);
  }

  /**
   * Returns a permutation that sorts the input.
   * @see com.github.methylene.sym.PermutationFactory#sort(char[]) */
  public static Permutation sort(Comparable[] input) {
    return sort(input, false);
  }

  /** @see com.github.methylene.sym.PermutationFactory#sort(char[], boolean) */
  public static Permutation sort(Object[] input, Comparator comp, boolean strict) {
    Object[] sorted = Util.sortedCopy(input, comp);
    int[] result = new int[input.length];
    boolean[] used = new boolean[input.length];
    for (int i = 0; i < input.length; i += 1) {
      int idx = Arrays.binarySearch(sorted, input[i], comp);
      int offset = 0;
      int direction = 1;
      while (used[idx + offset]) {
        if (strict)
          throw new IllegalArgumentException("duplicate: " + input[i]);
        offset += direction;
        if (idx + offset >= sorted.length || sorted[idx + offset] != input[i]) {
          offset = -1;
          direction = -1;
        }
      }
      result[i] = idx + offset;
      used[idx + offset] = true;
    }
    return new Permutation(result, false);
  }

  /** @see com.github.methylene.sym.PermutationFactory#sort(char[]) */
  public static Permutation sort(Object[] input, Comparator comp) {
    return sort(input, comp, false);
  }

  /**
   * Returns a permutation that sorts the input array.
   * There is no guarantee which one will be chosen if there is a choice,
   * i.e. if the input contains duplicates.
   * @param input an array of integers
   * @return a permutation that sorts the input
   * @throws java.lang.IllegalArgumentException if {@code strict} is true and {@code input} contains duplicates
   */
  public static Permutation sort(int[] input, boolean strict) {
    int[] sorted = Util.sortedCopy(input);
    int[] result = new int[input.length];
    boolean[] used = new boolean[input.length];
    for (int i = 0; i < input.length; i += 1) {
      int idx = Arrays.binarySearch(sorted, input[i]);
      int offset = 0;
      int direction = 1;
      while (used[idx + offset]) {
        if (strict)
          throw new IllegalArgumentException("duplicate: " + input[i]);
        offset += direction;
        if (idx + offset >= sorted.length || sorted[idx + offset] != input[i]) {
          offset = -1;
          direction = -1;
        }
      }
      result[i] = idx + offset;
      used[idx + offset] = true;
    }
    return new Permutation(result, false);
  }

  /**
   * Return a permutation that sorts the input array.
   * @param input an array
   * @return a permutation that sorts the input
   */
  public static Permutation sort(int[] input) {
    return sort(input, false);
  }

  /**
   * Returns a permutation that sorts the input string.
   * @param s a string
   * @return a permutation that sorts {@code s}
   */
  public static Permutation sort(String s) {
    char[] chars = new char[s.length()];
    s.getChars(0, chars.length, chars, 0);
    return sort(chars);
  }

  /* ================= from ================= */

  /**
   * Returns a permutation that leads from a to b.
   * @param a an array
   * @param b an array that can be obtained by reordering the element of {@code a}
   * @param strict if true, throw an Exception if either array contains duplicates
   * @return a permutation so that {@code Arrays.equals(Permutation.from(a, b).apply(a), b)} is true
   * @throws java.lang.IllegalArgumentException if {@code strict} is true and {@code a} or {@code b} contain duplicates
   */
  public static Permutation from(int[] a, int[] b, boolean strict) {
    if (a.length != b.length)
      throw new IllegalArgumentException("arguments must have equal length");
    Permutation sortB = sort(b, strict);
    int[] sortedB = sortB.apply(b);
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
      used[idx + offset] = true;
    }
    return new Permutation(result, false);
  }

  /**
   * Returns a permutation that leads from a to b. Duplicates in the arrays are allowed.
   * @see com.github.methylene.sym.PermutationFactory#from(int[], int[], boolean)
   */
  public static Permutation from(int[] a, int[] b) {
    return from(a, b, false);
  }

  /**
   * Returns a permutation that leads from a to b.
   * @see com.github.methylene.sym.PermutationFactory#from(int[], int[], boolean)
   */
  public static Permutation from(Comparable[] a, Comparable[] b, boolean strict) {
    if (a.length != b.length)
      throw new IllegalArgumentException("arguments must have equal length");
    Permutation sortB = sort(b, strict);
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
      used[idx + offset] = true;
    }
    return new Permutation(result, false);
  }

  /**
   * Returns a permutation that leads from a to b. Duplicates in the arrays are allowed.
   * @see com.github.methylene.sym.PermutationFactory#from(int[], int[], boolean)
   */
  public static Permutation from(Comparable[] a, Comparable[] b) {
    return from(a, b, false);
  }

  /**
   * Returns a permutation that leads from a to b.
   * @see com.github.methylene.sym.PermutationFactory#from(int[], int[], boolean)
   */
  public static Permutation from(byte[] a, byte[] b, boolean strict) {
    if (a.length != b.length)
      throw new IllegalArgumentException("arguments must have equal length");
    Permutation sortB = sort(b, strict);
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
      used[idx + offset] = true;
    }
    return new Permutation(result, false);
  }

  /**
   * Returns a permutation that leads from a to b. Duplicates in the arrays are allowed.
   * @see com.github.methylene.sym.PermutationFactory#from(int[], int[], boolean)
   */
  public static Permutation from(byte[] a, byte[] b) {
    return from(a, b, false);
  }

  /**
   * Returns a permutation that leads from a to b.
   * @see com.github.methylene.sym.PermutationFactory#from(int[], int[], boolean)
   */
  public static Permutation from(long[] a, long[] b, boolean strict) {
    if (a.length != b.length)
      throw new IllegalArgumentException("arguments must have equal length");
    Permutation sortB = sort(b, strict);
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
      used[idx + offset] = true;
    }
    return new Permutation(result, false);
  }

  /**
   * Returns a permutation that leads from a to b. Duplicates in the arrays are allowed.
   * @see com.github.methylene.sym.PermutationFactory#from(int[], int[], boolean)
   */
  public static Permutation from(long[] a, long[] b) {
    return from(a, b, false);
  }

  /**
   * Returns a permutation that leads from a to b.
   * @see com.github.methylene.sym.PermutationFactory#from(int[], int[], boolean)
   */
  public static Permutation from(float[] a, float[] b, boolean strict) {
    if (a.length != b.length)
      throw new IllegalArgumentException("arguments must have equal length");
    Permutation sortB = sort(b, strict);
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
      used[idx + offset] = true;
    }
    return new Permutation(result, false);
  }

  /**
   * Returns a permutation that leads from a to b. Duplicates in the arrays are allowed.
   * @see com.github.methylene.sym.PermutationFactory#from(int[], int[], boolean)
   */
  public static Permutation from(float[] a, float[] b) {
    return from(a, b, false);
  }

  /**
   * Returns a permutation that leads from a to b.
   * @see com.github.methylene.sym.PermutationFactory#from(int[], int[], boolean)
   */
  public static Permutation from(double[] a, double[] b, boolean strict) {
    if (a.length != b.length)
      throw new IllegalArgumentException("arguments must have equal length");
    Permutation sortB = sort(b, strict);
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
      used[idx + offset] = true;
    }
    return new Permutation(result, false);
  }

  /**
   * Returns a permutation that leads from a to b. Duplicates in the arrays are allowed.
   * @see com.github.methylene.sym.PermutationFactory#from(int[], int[], boolean)
   */
  public static Permutation from(double[] a, double[] b) {
    return from(a, b, false);
  }


  /**
   * Returns a permutation that leads from a to b.
   * @see com.github.methylene.sym.PermutationFactory#from(int[], int[], boolean)
   */
  public static Permutation from(Object[] a, Object[] b, Comparator comp, boolean strict) {
    if (a.length != b.length)
      throw new IllegalArgumentException("arguments must have equal length");
    Permutation sortB = sort(b, comp, strict);
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
      used[idx + offset] = true;
    }
    return new Permutation(result, false);
  }

  /**
   * Returns a permutation that leads from a to b. Duplicates in the arrays are allowed.
   * @see com.github.methylene.sym.PermutationFactory#from(int[], int[], boolean)
   */
  public static Permutation from(Object[] a, Object[] b, Comparator comp) {
    return from(a, b, comp, false);
  }

}
