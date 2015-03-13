package com.github.methylene.sym;

import org.junit.Test;

import static org.junit.Assert.*;

public class SearchableTest {

  @Test
  public void testNonstrict() throws Exception {
    for (int i = 0; i < 1000; i += 1) {
      int maxNumber = (int) (Math.random() * 1000);
      int[] ints = Util.randomNumbers(maxNumber, (int) (Math.random() * 1000));
      Searchable.IntArray a = Searchable.searchable().array(ints);
      int search = (int) (maxNumber * Math.random());
      assertEquals(Util.indexOf(ints, search), a.indexOf(search));
    }
  }

  @Test
  public void testStrict() throws Exception {
    for (int i = 0; i < 1000; i += 1) {
      int maxNumber = (int) (Math.random() * 100);
      int[] ints = Util.distinctInts(maxNumber, (int) (Math.random() * 10 + 2));
      Searchable.IntArray a = Searchable.searchable().array(ints);
      int search = (int) (maxNumber * Math.random());
      assertEquals(Util.indexOf(ints, search), a.indexOf(search));
    }
  }

  @Test(expected = IllegalArgumentException.class)
  public void testStrictFail() throws Exception {
      int[] ints = Util.randomNumbers(100, 105);
      Searchable.strictSearchable().array(ints);
  }

}