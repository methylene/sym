package com.github.methylene.sym;

import org.junit.Test;

import static org.junit.Assert.*;

public class SearchableTest {

  @Test
  public void testCreate() throws Exception {
    for (int i = 0; i < 1000; i += 1) {
      int maxNumber = (int) (Math.random() * 1000);
      int[] ints = Util.randomNumbers(maxNumber, (int) (Math.random() * 1000));
      Searchable.IntArray a = Searchable.create(ints);
      int search = (int) (maxNumber * Math.random());
      assertEquals(Util.indexOf(ints, search), a.indexOf(search));
    }
  }

}