package com.github.methylene.sym;

import java.util.Arrays;

public class PermutationFactory {

  /**
   * @param chars an array of characters, not necessarily distinct.
   * @return a permutation that sorts {@code chars}. There is no guarantee which one will be chosen if there is more
   * than one permutation that sorts {@code chars}, i.e. if {@code chars} contains duplicates.
   * @see Permutation#sort(java.lang.Object[], java.util.Comparator)
   */
  static public Permutation sort(char[] chars) {
    char[] sorted = Util.sortedCopy(chars);
    int[] result = new int[chars.length];
    boolean[] used = new boolean[chars.length];
    for (int i = 0; i < chars.length; i += 1) {
      int idx = Arrays.binarySearch(sorted, chars[i]);
      int offset = 0;
      int direction = 1;
      while (used[idx + offset]) {
        offset += direction;
        if (idx + offset >= sorted.length || sorted[idx + offset] != chars[i]) {
          offset = -1;
          direction = -1;
        }
      }
      result[i] = idx + offset;
      used[idx + offset] = true;
    }
    return new Permutation(result);
  }

  /**
   * @param ints an array of integers, not necessarily distinct.
   * @return a permutation that sorts {@code ints}. There is no guarantee which one will be chosen if there is more
   * than one permutation that sorts {@code ints}, i.e. if {@code ints} contains duplicates.
   * @see Permutation#sort(java.lang.Object[], java.util.Comparator)
   */
  static public Permutation sort(int[] ints) {
    int[] sorted = Util.sortedCopy(ints);
    int[] result = new int[ints.length];
    boolean[] used = new boolean[ints.length];
    for (int i = 0; i < ints.length; i += 1) {
      int idx = Arrays.binarySearch(sorted, ints[i]);
      int offset = 0;
      int direction = 1;
      while (used[idx + offset]) {
        offset += direction;
        if (idx + offset >= sorted.length || sorted[idx + offset] != ints[i]) {
          offset = -1;
          direction = -1;
        }
      }
      result[i] = idx + offset;
      used[idx + offset] = true;
    }
    return new Permutation(result);
  }

  /**
   * @param s any string
   * @return a permutation that sorts {@code s}. There is no guarantee which one will be chosen if there is more
   * than one permutation that sorts {@code s}, i.e. if {@code s} contains duplicates.
   * @see Permutation#sort(java.lang.Object[], java.util.Comparator)
   */
  static public Permutation sort(String s) {
    char[] chars = new char[s.length()];
    s.getChars(0, chars.length, chars, 0);
    return sort(chars);
  }

}
