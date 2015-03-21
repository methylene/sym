package com.github.methylene.lists;

import static java.lang.System.arraycopy;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;

public final class Partitions {

  private Partitions() {}

  @SuppressWarnings("unchecked")
  public static <E> LinkedHashMap<E, int[]> partition(Object[] sorted, int[] unsort) {
    if (sorted.length == 0)
      return new LinkedHashMap<E, int[]>(0);
    LinkedHashMap<E, int[]> m = new LinkedHashMap<E, int[]>(sorted.length);
    int mark = 0;
    for (int i = 1; i < sorted.length; i++) {
      if (!sorted[i].equals(sorted[mark])) {
        int[] partition = new int[i - mark];
        arraycopy(unsort, mark, partition, 0, partition.length);
        if (partition.length > 1)
          Arrays.sort(partition);
        m.put((E) sorted[mark], partition);
        mark = i;
      }
    }
    int[] partition = new int[sorted.length - mark];
    arraycopy(unsort, mark, partition, 0, partition.length);
    if (partition.length > 1)
      Arrays.sort(partition);
    m.put((E) sorted[mark], partition);
    return m;
  }

  public static LinkedHashMap<Byte, int[]> partition(byte[] sorted, int[] unsort) {
    if (sorted.length == 0)
      return new LinkedHashMap<Byte, int[]>(0);
    LinkedHashMap<Byte, int[]> m = new LinkedHashMap<Byte, int[]>(sorted.length);
    int mark = 0;
    for (int i = 1; i < sorted.length; i++) {
      if (sorted[i] != sorted[mark]) {
        int[] partition = new int[i - mark];
        arraycopy(unsort, mark, partition, 0, partition.length);
        if (partition.length > 1)
          Arrays.sort(partition);
        m.put(sorted[mark], partition);
        mark = i;
      }
    }
    int[] partition = new int[sorted.length - mark];
    arraycopy(unsort, mark, partition, 0, partition.length);
    if (partition.length > 1)
      Arrays.sort(partition);
    m.put(sorted[mark], partition);
    return m;
  }

  public static LinkedHashMap<Short, int[]> partition(short[] sorted, int[] unsort) {
    if (sorted.length == 0)
      return new LinkedHashMap<Short, int[]>(0);
    LinkedHashMap<Short, int[]> m = new LinkedHashMap<Short, int[]>(sorted.length);
    int mark = 0;
    for (int i = 1; i < sorted.length; i++) {
      if (sorted[i] != sorted[mark]) {
        int[] partition = new int[i - mark];
        arraycopy(unsort, mark, partition, 0, partition.length);
        if (partition.length > 1)
          Arrays.sort(partition);
        m.put(sorted[mark], partition);
        mark = i;
      }
    }
    int[] partition = new int[sorted.length - mark];
    arraycopy(unsort, mark, partition, 0, partition.length);
    if (partition.length > 1)
      Arrays.sort(partition);
    m.put(sorted[mark], partition);
    return m;
  }

  public static LinkedHashMap<Integer, int[]> partition(int[] sorted, int[] unsort) {
    if (sorted.length == 0)
      return new LinkedHashMap<Integer, int[]>(0);
    LinkedHashMap<Integer, int[]> m = new LinkedHashMap<Integer, int[]>(sorted.length);
    int mark = 0;
    for (int i = 1; i < sorted.length; i++) {
      if (sorted[i] != sorted[mark]) {
        int[] partition = new int[i - mark];
        arraycopy(unsort, mark, partition, 0, partition.length);
        if (partition.length > 1)
          Arrays.sort(partition);
        m.put(sorted[mark], partition);
        mark = i;
      }
    }
    int[] partition = new int[sorted.length - mark];
    arraycopy(unsort, mark, partition, 0, partition.length);
    if (partition.length > 1)
      Arrays.sort(partition);
    m.put(sorted[mark], partition);
    return m;
  }

  public static LinkedHashMap<Long, int[]> partition(long[] sorted, int[] unsort) {
    if (sorted.length == 0)
      return new LinkedHashMap<Long, int[]>(0);
    LinkedHashMap<Long, int[]> m = new LinkedHashMap<Long, int[]>(sorted.length);
    int mark = 0;
    for (int i = 1; i < sorted.length; i++) {
      if (sorted[i] != sorted[mark]) {
        int[] partition = new int[i - mark];
        arraycopy(unsort, mark, partition, 0, partition.length);
        if (partition.length > 1)
          Arrays.sort(partition);
        m.put(sorted[mark], partition);
        mark = i;
      }
    }
    int[] partition = new int[sorted.length - mark];
    arraycopy(unsort, mark, partition, 0, partition.length);
    if (partition.length > 1)
      Arrays.sort(partition);
    m.put(sorted[mark], partition);
    return m;
  }

  public static LinkedHashMap<Float, int[]> partition(float[] sorted, int[] unsort) {
    if (sorted.length == 0)
      return new LinkedHashMap<Float, int[]>(0);
    LinkedHashMap<Float, int[]> m = new LinkedHashMap<Float, int[]>(sorted.length);
    int mark = 0;
    for (int i = 1; i < sorted.length; i++) {
      if (sorted[i] != sorted[mark]) {
        int[] partition = new int[i - mark];
        arraycopy(unsort, mark, partition, 0, partition.length);
        if (partition.length > 1)
          Arrays.sort(partition);
        m.put(sorted[mark], partition);
        mark = i;
      }
    }
    int[] partition = new int[sorted.length - mark];
    arraycopy(unsort, mark, partition, 0, partition.length);
    if (partition.length > 1)
      Arrays.sort(partition);
    m.put(sorted[mark], partition);
    return m;

  }

  public static LinkedHashMap<Double, int[]> partition(double[] sorted, int[] unsort) {
    if (sorted.length == 0)
      return new LinkedHashMap<Double, int[]>(0);
    LinkedHashMap<Double, int[]> m = new LinkedHashMap<Double, int[]>(sorted.length);
    int mark = 0;
    for (int i = 1; i < sorted.length; i++) {
      if (sorted[i] != sorted[mark]) {
        int[] partition = new int[i - mark];
        arraycopy(unsort, mark, partition, 0, partition.length);
        if (partition.length > 1)
          Arrays.sort(partition);
        m.put(sorted[mark], partition);
        mark = i;
      }
    }
    int[] partition = new int[sorted.length - mark];
    arraycopy(unsort, mark, partition, 0, partition.length);
    if (partition.length > 1)
      Arrays.sort(partition);
    m.put(sorted[mark], partition);
    return m;
  }

  public static LinkedHashMap<Character, int[]> partition(char[] sorted, int[] unsort) {
    if (sorted.length == 0)
      return new LinkedHashMap<Character, int[]>(0);
    LinkedHashMap<Character, int[]> m = new LinkedHashMap<Character, int[]>(sorted.length);
    int mark = 0;
    for (int i = 1; i < sorted.length; i++) {
      if (sorted[i] != sorted[mark]) {
        int[] partition = new int[i - mark];
        arraycopy(unsort, mark, partition, 0, partition.length);
        if (partition.length > 1)
          Arrays.sort(partition);
        m.put(sorted[mark], partition);
        mark = i;
      }
    }
    int[] partition = new int[sorted.length - mark];
    arraycopy(unsort, mark, partition, 0, partition.length);
    if (partition.length > 1)
      Arrays.sort(partition);
    m.put(sorted[mark], partition);
    return m;
  }

}
