package com.github.methylene.lists;

import static java.lang.System.arraycopy;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * This class contains a utility method to group a sorted array.
 */
final class Group {

  private Group() {}

  private static int[] groupIndexes(int mark, int i, int[] unsort) {
    int[] group = new int[i - mark];
    arraycopy(unsort, mark, group, 0, group.length);
    if (group.length > 1)
      Arrays.sort(group);
    return group;
  }

  @SuppressWarnings("unchecked")
  private static <K> Map<K, int[]> build(Object[][] builder, int cnt) {
    LinkedHashMap<K, int[]> m = new LinkedHashMap<K, int[]>(cnt);
    for (int i = 0; i < cnt; i++)
      m.put((K) builder[i][0], (int[]) builder[i][1]);
    return m;
  }

  @SuppressWarnings("unchecked")
  static <E> Map<E, int[]> group(Object[] sorted, int[] unsort) {
    if (sorted.length == 0)
      return Collections.emptyMap();
    Object[][] builder = new Object[sorted.length][];
    int mark = 0;
    int cnt = 0;
    for (int i = 1; i < sorted.length; i++) {
      if (!sorted[i].equals(sorted[mark])) {
        builder[cnt++] = new Object[]{sorted[mark], groupIndexes(mark, i, unsort)};
        mark = i;
      }
    }
    builder[cnt++] = new Object[]{sorted[mark], groupIndexes(mark, sorted.length, unsort)};
    return build(builder, cnt);
  }

  static Map<Byte, int[]> group(byte[] sorted, int[] unsort) {
    if (sorted.length == 0)
      return Collections.emptyMap();
    Object[][] builder = new Object[sorted.length][];
    int mark = 0;
    int cnt = 0;
    for (int i = 1; i < sorted.length; i++) {
      if (sorted[i] != sorted[mark]) {
        builder[cnt++] = new Object[]{sorted[mark], groupIndexes(mark, i, unsort)};
        mark = i;
      }
    }
    builder[cnt++] = new Object[]{sorted[mark], groupIndexes(mark, sorted.length, unsort)};
    return build(builder, cnt);
  }

  static Map<Short, int[]> group(short[] sorted, int[] unsort) {
    if (sorted.length == 0)
      return Collections.emptyMap();
    Object[][] builder = new Object[sorted.length][];
    int mark = 0;
    int cnt = 0;
    for (int i = 1; i < sorted.length; i++) {
      if (sorted[i] != sorted[mark]) {
        builder[cnt++] = new Object[]{sorted[mark], groupIndexes(mark, i, unsort)};
        mark = i;
      }
    }
    builder[cnt++] = new Object[]{sorted[mark], groupIndexes(mark, sorted.length, unsort)};
    return build(builder, cnt);
  }

  static Map<Integer, int[]> group(int[] sorted, int[] unsort) {
    if (sorted.length == 0)
      return Collections.emptyMap();
    Object[][] builder = new Object[sorted.length][];
    int mark = 0;
    int cnt = 0;
    for (int i = 1; i < sorted.length; i++) {
      if (sorted[i] != sorted[mark]) {
        builder[cnt++] = new Object[]{sorted[mark], groupIndexes(mark, i, unsort)};
        mark = i;
      }
    }
    builder[cnt++] = new Object[]{sorted[mark], groupIndexes(mark, sorted.length, unsort)};
    return build(builder, cnt);
  }

  static Map<Long, int[]> group(long[] sorted, int[] unsort) {
    if (sorted.length == 0)
      return Collections.emptyMap();
    Object[][] builder = new Object[sorted.length][];
    int mark = 0;
    int cnt = 0;
    for (int i = 1; i < sorted.length; i++) {
      if (sorted[i] != sorted[mark]) {
        builder[cnt++] = new Object[]{sorted[mark], groupIndexes(mark, i, unsort)};
        mark = i;
      }
    }
    builder[cnt++] = new Object[]{sorted[mark], groupIndexes(mark, sorted.length, unsort)};
    return build(builder, cnt);
  }

  static Map<Float, int[]> group(float[] sorted, int[] unsort) {
    if (sorted.length == 0)
      return Collections.emptyMap();
    Object[][] builder = new Object[sorted.length][];
    int mark = 0;
    int cnt = 0;
    for (int i = 1; i < sorted.length; i++) {
      if (sorted[i] != sorted[mark]) {
        builder[cnt++] = new Object[]{sorted[mark], groupIndexes(mark, i, unsort)};
        mark = i;
      }
    }
    builder[cnt++] = new Object[]{sorted[mark], groupIndexes(mark, sorted.length, unsort)};
    return build(builder, cnt);
  }

  static Map<Double, int[]> group(double[] sorted, int[] unsort) {
    if (sorted.length == 0)
      return Collections.emptyMap();
    Object[][] builder = new Object[sorted.length][];
    int mark = 0;
    int cnt = 0;
    for (int i = 1; i < sorted.length; i++) {
      if (sorted[i] != sorted[mark]) {
        builder[cnt++] = new Object[]{sorted[mark], groupIndexes(mark, i, unsort)};
        mark = i;
      }
    }
    builder[cnt++] = new Object[]{sorted[mark], groupIndexes(mark, sorted.length, unsort)};
    return build(builder, cnt);
  }

  static Map<Character, int[]> group(char[] sorted, int[] unsort) {
    if (sorted.length == 0)
      return Collections.emptyMap();
    Object[][] builder = new Object[sorted.length][];
    int mark = 0;
    int cnt = 0;
    for (int i = 1; i < sorted.length; i++) {
      if (sorted[i] != sorted[mark]) {
        builder[cnt++] = new Object[]{sorted[mark], groupIndexes(mark, i, unsort)};
        mark = i;
      }
    }
    builder[cnt++] = new Object[]{sorted[mark], groupIndexes(mark, sorted.length, unsort)};
    return build(builder, cnt);
  }

}
