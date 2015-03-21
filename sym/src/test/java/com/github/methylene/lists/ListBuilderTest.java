package com.github.methylene.lists;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import com.github.methylene.sym.Util;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class ListBuilderTest {

  @Test
  public void testExtendedCapacity() {
    int safe = Integer.MAX_VALUE / 3;
    for (int _ = 0; _ < 10000; _ += 1) {
      int oldCapacity = (int) (Math.random() * Integer.MAX_VALUE);
      int minCapacity = oldCapacity + (int) (Math.random() * (Integer.MAX_VALUE - oldCapacity));
      int extended = ListBuilder.extendedCapacity(oldCapacity, minCapacity);
      assertTrue(extended >= minCapacity);
      if (minCapacity < safe) {
        assertTrue(extended >= oldCapacity * 1.4);
        assertTrue(extended < minCapacity * 3);
      }
    }
  }

  @Test
  public void testBuilder() {
    for (int _ = 0; _ < 10000; _ += 1) {
      int maxNumber = 10;
      Integer[] a = Util.box(Util.randomNumbers(maxNumber, maxNumber + 2 + (int) (Math.random() * 20)));
      List<Integer> asList = LookupList.asList(a);
      List<Integer> addAll = LookupList.<Integer>builder().addAll(a).build();
      List<Integer> jdk = Arrays.asList(a);
      assertEquals(jdk, addAll);
      assertEquals(jdk, asList);
    }
  }

}
