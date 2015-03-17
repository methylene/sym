package com.github.methylene.sym;

import static com.github.methylene.sym.Util.distinctInts;

import java.util.Arrays;
import java.util.Comparator;

public final class Rankings {

  private Rankings() {}

  private static final Comparator<int[]> COMPARE_FIRST = new Comparator<int[]>() {
    public int compare(int[] a, int[] b) {
      return a[0] - b[0];
    }
  };

  public static int[] invert(int[] ranking) {
    int[][] rankingWithIndex = Util.withIndex(ranking);
    Arrays.sort(rankingWithIndex, COMPARE_FIRST);
    int[] inverted = new int[ranking.length];
    for (int i = 0; i < ranking.length; i += 1)
      inverted[i] = rankingWithIndex[i][1];
    return inverted;
  }


  public static int[] random(int length) {
    return sort(distinctInts(length, 4));
  }

  public static int[] comp(int[] ranking, int[] other) {
    int length = Math.max(ranking.length, other.length);
    int[] lhs = Util.padding(ranking, length);
    int[] rhs = Util.padding(other, length);
    int[] result = new int[length];
    for (int i = 0; i < length; i += 1)
      result[i] = lhs[rhs[i]];
    return result;
  }


  /* ================= sort ================= */

  public static int[] sort(byte[] input) {
    byte[] sorted = Util.sortedCopy(input);
    int[] ranking = new int[input.length];
    boolean[] used = new boolean[input.length];
    for (int i = 0; i < input.length; i += 1) {
      int idx = Arrays.binarySearch(sorted, input[i]);
      int offset = 0;
      int direction = 1;
      while (used[idx + offset]) {
        offset += direction;
        if (idx + offset >= sorted.length || sorted[idx + offset] != input[i]) {
          assert direction != -1;
          offset = -1;
          direction = -1;
        }
      }
      ranking[i] = idx + offset;
      used[idx + offset] = true;
    }
    return ranking;
  }




  public static int[] sort(short[] input) {
    short[] sorted = Util.sortedCopy(input);
    int[] ranking = new int[input.length];
    boolean[] used = new boolean[input.length];
    for (int i = 0; i < input.length; i += 1) {
      int idx = Arrays.binarySearch(sorted, input[i]);
      int offset = 0;
      int direction = 1;
      while (used[idx + offset]) {
        offset += direction;
        if (idx + offset >= sorted.length || sorted[idx + offset] != input[i]) {
          assert direction != -1;
          offset = -1;
          direction = -1;
        }
      }
      ranking[i] = idx + offset;
      used[idx + offset] = true;
    }
    return ranking;
  }


  public static int[] sort(long[] input) {
    long[] sorted = Util.sortedCopy(input);
    int[] ranking = new int[input.length];
    boolean[] used = new boolean[input.length];
    for (int i = 0; i < input.length; i += 1) {
      int idx = Arrays.binarySearch(sorted, input[i]);
      int offset = 0;
      int direction = 1;
      while (used[idx + offset]) {
        offset += direction;
        if (idx + offset >= sorted.length || sorted[idx + offset] != input[i]) {
          assert direction != -1;
          offset = -1;
          direction = -1;
        }
      }
      ranking[i] = idx + offset;
      used[idx + offset] = true;
    }
    return ranking;
  }


  public static int[] sort(float[] input) {
    float[] sorted = Util.sortedCopy(input);
    int[] ranking = new int[input.length];
    boolean[] used = new boolean[input.length];
    for (int i = 0; i < input.length; i += 1) {
      int idx = Arrays.binarySearch(sorted, input[i]);
      int offset = 0;
      int direction = 1;
      while (used[idx + offset]) {
        offset += direction;
        if (idx + offset >= sorted.length || sorted[idx + offset] != input[i]) {
          assert direction != -1;
          offset = -1;
          direction = -1;
        }
      }
      ranking[i] = idx + offset;
      used[idx + offset] = true;
    }
    return ranking;
  }

  public static int[] sort(double[] input) {
    double[] sorted = Util.sortedCopy(input);
    int[] ranking = new int[input.length];
    boolean[] used = new boolean[input.length];
    for (int i = 0; i < input.length; i += 1) {
      int idx = Arrays.binarySearch(sorted, input[i]);
      int offset = 0;
      int direction = 1;
      while (used[idx + offset]) {
        if (idx + offset >= sorted.length || sorted[idx + offset] != input[i]) {
          assert direction != -1;
          offset = -1;
          direction = -1;
        }
      }
      ranking[i] = idx + offset;
      used[idx + offset] = true;
    }
    return ranking;
  }


  public static int[] sort(char[] input) {
    char[] sorted = Util.sortedCopy(input);
    int[] ranking = new int[input.length];
    boolean[] used = new boolean[input.length];
    for (int i = 0; i < input.length; i += 1) {
      int idx = Arrays.binarySearch(sorted, input[i]);
      int offset = 0;
      int direction = 1;
      while (used[idx + offset]) {
        offset += direction;
        if (idx + offset >= sorted.length || sorted[idx + offset] != input[i]) {
          assert direction != -1;
          offset = -1;
          direction = -1;
        }
      }
      ranking[i] = idx + offset;
      used[idx + offset] = true;
    }
    return ranking;
  }

  public static <E extends Comparable> int[] sort(E[] input) {
    Comparable[] sorted = Util.sortedCopy(input);
    int[] ranking = new int[input.length];
    boolean[] used = new boolean[input.length];
    for (int i = 0; i < input.length; i += 1) {
      if (input[i] == null) {throw new NullPointerException("null values are not allowed");}
      int idx = Arrays.binarySearch(sorted, input[i]);
      int offset = 0;
      int direction = 1;
      while (used[idx + offset]) {
        offset += direction;
        if (idx + offset >= sorted.length || !input[i].equals(sorted[idx + offset])) {
          assert direction != -1;
          offset = -1;
          direction = -1;
        }
      }
      ranking[i] = idx + offset;
      used[idx + offset] = true;
    }
    return ranking;
  }


  public static <E> int[] sort(E[] input, Comparator<E> comp) {
    Object[] sorted = Util.sortedCopy(input, comp);
    int[] ranking = new int[input.length];
    boolean[] used = new boolean[input.length];
    for (int i = 0; i < input.length; i += 1) {
      if (input[i] == null) {throw new NullPointerException("null values are not allowed");}
      @SuppressWarnings("unchecked")
      int idx = Arrays.binarySearch(sorted, input[i], (Comparator) comp);
      int offset = 0;
      int direction = 1;
      while (used[idx + offset]) {
        offset += direction;
        if (idx + offset >= sorted.length || !input[i].equals(sorted[idx + offset])) {
          assert direction != -1;
          offset = -1;
          direction = -1;
        }
      }
      ranking[i] = idx + offset;
      used[idx + offset] = true;
    }
    return ranking;
  }


  public static int[] sort(int[] input) {
    int[] sorted = Util.sortedCopy(input);
    int[] ranking = new int[input.length];
    boolean[] used = new boolean[input.length];
    for (int i = 0; i < input.length; i += 1) {
      int idx = Arrays.binarySearch(sorted, input[i]);
      int offset = 0;
      int direction = 1;
      while (used[idx + offset]) {
        offset += direction;
        if (idx + offset >= sorted.length || sorted[idx + offset] != input[i]) {
          assert direction != -1;
          offset = -1;
          direction = -1;
        }
      }
      ranking[i] = idx + offset;
      used[idx + offset] = true;
    }
    return ranking;
  }



  /* ================= from ================= */

  public static int[] from(int[] a, int[] b) {
    if (a.length != b.length) {throw new IllegalArgumentException("arguments must have equal length");}
    int[] sortB = sort(b);
    int[] sortedB = apply(sortB, b);
    int[] unsortB = invert(sortB);
    int[] ranking = new int[a.length];
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
      ranking[i] = unsortB[idx + offset];
      if (a[i] != b[ranking[i]])
        throw new IllegalArgumentException("multiplicity differs: " + a[i]);
      used[idx + offset] = true;
    }
    return ranking;
  }


  public static <E extends Comparable> int[] from(E[] a, E[] b) {
    if (a.length != b.length)
      throw new IllegalArgumentException("arguments must have equal length");
    int[] sortB = sort(b);
    Comparable[] sortedB = apply(sortB, b);
    int[] unsortB = invert(sortB);
    int[] ranking = new int[a.length];
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
      ranking[i] = unsortB[idx + offset];
      if (!a[i].equals(b[ranking[i]]))
        throw new IllegalArgumentException("multiplicity differs: " + a[i]);
      used[idx + offset] = true;
    }
    return ranking;
  }


  public static int[] from(byte[] a, byte[] b) {
    if (a.length != b.length)
      throw new IllegalArgumentException("arguments must have equal length");
    int[] sortB = sort(b);
    byte[] sortedB = apply(sortB, b);
    int[] unsortB = invert(sortB);
    int[] ranking = new int[a.length];
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
      ranking[i] = unsortB[idx + offset];
      if (a[i] != b[ranking[i]])
        throw new IllegalArgumentException("multiplicity differs: " + a[i]);
      used[idx + offset] = true;
    }
    return ranking;
  }


  public static int[] from(long[] a, long[] b) {
    if (a.length != b.length)
      throw new IllegalArgumentException("arguments must have equal length");
    int[] sortB = sort(b);
    long[] sortedB = apply(sortB, b);
    int[] unsortB = invert(sortB);
    int[] ranking = new int[a.length];
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
      ranking[i] = unsortB[idx + offset];
      if (a[i] != b[ranking[i]])
        throw new IllegalArgumentException("multiplicity differs: " + a[i]);
      used[idx + offset] = true;
    }
    return ranking;
  }



  public static int[] from(float[] a, float[] b) {
    if (a.length != b.length)
      throw new IllegalArgumentException("arguments must have equal length");
    int[] sortB = sort(b);
    float[] sortedB = apply(sortB, b);
    int[] unsortB = invert(sortB);
    int[] ranking = new int[a.length];
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
      ranking[i] = unsortB[idx + offset];
      if (a[i] != b[ranking[i]])
        throw new IllegalArgumentException("multiplicity differs: " + a[i]);
      used[idx + offset] = true;
    }
    return ranking;
  }


  public static int[] from(double[] a, double[] b) {
    if (a.length != b.length)
      throw new IllegalArgumentException("arguments must have equal length");
    int[] sortB = sort(b);
    double[] sortedB = apply(sortB, b);
    int[] unsortB = invert(sortB);
    int[] ranking = new int[a.length];
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
      ranking[i] = unsortB[idx + offset];
      if (a[i] != b[ranking[i]])
        throw new IllegalArgumentException("multiplicity differs: " + a[i]);
      used[idx + offset] = true;
    }
    return ranking;
  }


  public static <E> int[] from(E[] a, E[] b, Comparator<E> comp) {
    if (a.length != b.length)
      throw new IllegalArgumentException("arguments must have equal length");
    int[] sortB = sort(b, comp);
    Object[] sortedB = apply(sortB, b);
    int[] unsortB = invert(sortB);
    int[] ranking = new int[a.length];
    boolean[] used = new boolean[b.length];
    for (int i = 0; i < a.length; i += 1) {
      @SuppressWarnings("unchecked")
      int idx = Arrays.binarySearch(sortedB, a[i], (Comparator) comp);
      if (idx < 0) {throw new IllegalArgumentException("not in b: " + a[i]);}
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
      ranking[i] = unsortB[idx + offset];
      if (!a[i].equals(b[ranking[i]]))
        throw new IllegalArgumentException("multiplicity differs: " + a[i]);
      used[idx + offset] = true;
    }
    return ranking;
  }



  /* ================= apply ================= */


  public static Object[] apply(int[] ranking, Object[] input) {
    if (input.length < ranking.length)
      throw new IllegalArgumentException("input too short: " + input.length + ", minimum input length is " + ranking.length);
    Object[] result = new Object[input.length];
    for (int i = 0; i < ranking.length; i += 1)
      result[ranking[i]] = input[i];
    if (input.length > ranking.length)
      System.arraycopy(input, ranking.length, result, ranking.length, input.length - ranking.length);
    return result;
  }


  public static Comparable[] apply(int[] ranking, Comparable[] input) {
    if (input.length < ranking.length)
      throw new IllegalArgumentException("input too short: " + input.length + ", minimum input length is " + ranking.length);
    Comparable[] result = new Comparable[input.length];
    for (int i = 0; i < ranking.length; i += 1)
      result[ranking[i]] = input[i];
    if (input.length > ranking.length)
      System.arraycopy(input, ranking.length, result, ranking.length, input.length - ranking.length);
    return result;
  }


  public static String[] apply(int[] ranking, String[] input) {
    if (input.length < ranking.length)
      throw new IllegalArgumentException("input too short: " + input.length + ", minimum input length is " + ranking.length);
    String[] result = new String[input.length];
    for (int i = 0; i < ranking.length; i += 1)
      result[ranking[i]] = input[i];
    if (input.length > ranking.length)
      System.arraycopy(input, ranking.length, result, ranking.length, input.length - ranking.length);
    return result;
  }


  public static byte[] apply(int[] ranking, byte[] input) {
    if (input.length < ranking.length)
      throw new IllegalArgumentException("input too short: " + input.length + ", minimum input length is " + ranking.length);
    byte[] result = new byte[input.length];
    for (int i = 0; i < ranking.length; i += 1)
      result[ranking[i]] = input[i];
    if (input.length > ranking.length)
      System.arraycopy(input, ranking.length, result, ranking.length, input.length - ranking.length);
    return result;
  }


  public static short[] apply(int[] ranking, short[] input) {
    if (input.length < ranking.length)
      throw new IllegalArgumentException("input too short: " + input.length + ", minimum input length is " + ranking.length);
    short[] result = new short[input.length];
    for (int i = 0; i < ranking.length; i += 1)
      result[ranking[i]] = input[i];
    if (input.length > ranking.length)
      System.arraycopy(input, ranking.length, result, ranking.length, input.length - ranking.length);
    return result;
  }

  public static int[] apply(int[] ranking, int[] input) {
    if (input.length < ranking.length)
      throw new IllegalArgumentException("input too short: " + input.length + ", minimum input length is " + ranking.length);
    int[] result = new int[input.length];
    for (int i = 0; i < ranking.length; i += 1)
      result[ranking[i]] = input[i];
    if (input.length > ranking.length)
      System.arraycopy(input, ranking.length, result, ranking.length, input.length - ranking.length);
    return result;
  }


  public static long[] apply(int[] ranking, long[] input) {
    if (input.length < ranking.length)
      throw new IllegalArgumentException("input too short: " + input.length + ", minimum input length is " + ranking.length);
    long[] result = new long[input.length];
    for (int i = 0; i < ranking.length; i += 1)
      result[ranking[i]] = input[i];
    if (input.length > ranking.length)
      System.arraycopy(input, ranking.length, result, ranking.length, input.length - ranking.length);
    return result;
  }


  public static float[] apply(int[] ranking, float[] input) {
    if (input.length < ranking.length)
      throw new IllegalArgumentException("input too short: " + input.length + ", minimum input length is " + ranking.length);
    float[] result = new float[input.length];
    for (int i = 0; i < ranking.length; i += 1)
      result[ranking[i]] = input[i];
    if (input.length > ranking.length)
      System.arraycopy(input, ranking.length, result, ranking.length, input.length - ranking.length);
    return result;
  }


  public static double[] apply(int[] ranking, double[] input) {
    if (input.length < ranking.length)
      throw new IllegalArgumentException("input too short: " + input.length + ", minimum input length is " + ranking.length);
    double[] result = new double[input.length];
    for (int i = 0; i < ranking.length; i += 1)
      result[ranking[i]] = input[i];
    if (input.length > ranking.length)
      System.arraycopy(input, ranking.length, result, ranking.length, input.length - ranking.length);
    return result;
  }



  public static boolean[] apply(int[] ranking, boolean[] input) {
    if (input.length < ranking.length)
      throw new IllegalArgumentException("input too short: " + input.length + ", minimum input length is " + ranking.length);
    boolean[] result = new boolean[input.length];
    for (int i = 0; i < ranking.length; i += 1)
      result[ranking[i]] = input[i];
    if (input.length > ranking.length)
      System.arraycopy(input, ranking.length, result, ranking.length, input.length - ranking.length);
    return result;
  }


  public static char[] apply(int[] ranking, char[] input) {
    if (input.length < ranking.length)
      throw new IllegalArgumentException("input too short: " + input.length + ", minimum input length is " + ranking.length);
    char[] result = new char[input.length];
    for (int i = 0; i < ranking.length; i += 1)
      result[ranking[i]] = input[i];
    if (input.length > ranking.length)
      System.arraycopy(input, ranking.length, result, ranking.length, input.length - ranking.length);
    return result;
  }

}
