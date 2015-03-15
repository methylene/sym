package com.github.methylene.sym;

import java.util.AbstractList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.RandomAccess;

/**
 * <p>This class contains immutable array based implementations of {@link java.util.List}
 * that have efficient {@code indexOf}, {@code lastIndexOf} and {@code contains} methods.</p>
 *
 * <p>Per default it is not possible to construct a list that contains {@code null} values.
 * This can be configured via the underlying {@link com.github.methylene.sym.PermutationFactory}.</p>
 *
 * <p>Notes:</p>
 * <ul>
 *   <li>Performance gains over {@code java.util.ArrayList} are more significant when the return value of
 *   {@code .indexOf} is large, if the list is big and {@code indexOf} returns {@code -1},
 *   or if the {@code equals} method of the list elements is expensive.
 *   <li>Binary search is used for quick lookup, so all values in the lists must be Comparables,
 *   or a Comparator must be provided. If nulls are allowed, ony the Comparator makes sense, otherwise
 *   {@link java.util.Arrays#binarySearch} will throw an Exception.</li>
 *   <li>Some of the implementations below use arrays of primitives directly and avoid boxing.</li>
 * </ul>
 *
 * @see com.github.methylene.sym.PermutationFactory#getNullPolicy
 */
public class Lists {

  /**
   * Marker interface for those {@link java.util.List} implementations that are backed by primitive arrays.
   */
  public static interface PrimitiveList {}

  /* The PermutationFactory that is used for sorting */
  private final PermutationFactory factory;

  /**
   * Creates a Searchable that uses the given PermutationFactory for sorting and unsorting.
   * @param permutationFactory used to sort the input arrays
   */
  public Lists(PermutationFactory permutationFactory) {
    this.factory = permutationFactory;
  }

  private static final Lists DEFAULT = new Lists(Permutation.factory());
  private static final Lists UNIQUE = new Lists(Permutation.duplicateRejectingFactory());
  private static final Lists ALLOW_NULL = new Lists(PermutationFactory.builder()
      .setNullPolicy(PermutationFactory.NullPolicy.ALLOW_NULL).build());

  /**
   * Obtain the uniqueness enforcing variant of Searchable. This will refuse to create lists that
   * contain duplicates, and throw an IllegalArgumentException instead.
   * @return a lists object that can not be used to construct lists that contain duplicate elements
   */
  public static Lists uniqueLists() {
    return UNIQUE;
  }

  public static Lists allowNull() {
    return ALLOW_NULL;
  }

  public abstract class LookupList<E> extends AbstractList<E> implements RandomAccess {

    protected final Permutation unsort;
    protected final Permutation sort;

    protected LookupList(Permutation sort) {
      this.sort = sort;
      this.unsort = sort.invert();
    }

    /**
     * Get the inverse of a particular permutation which sorts this list.
     * @return the unsort permutation
     */
    public final Permutation getUnsort() {
      return unsort;
    }

    /**
     * Get a particular permutation which sorts this list.
     * @return the sort permutation
     */
    public final Permutation getSort() {
      return sort;
    }


    /**
     * Get the permutation factory that was used to create this list.
     * @return the permutation factory
     */
    public final PermutationFactory getPermutationFactory() {
      return factory;
    }

    /**
     * Find all indexes {@code i} where
     * <pre><code>
     *   this.get(i).equals(el)
     * </code></pre>
     * If {@code el} is not in the list, this returns an empty list.
     * @param el an object
     * @return an increasing list of all indexes where the value equals {@code el}
     */
    public abstract List<Integer> indexesOf(E el);

  }

  /**
   * Creates a list from the given array.
   * @param a an array
   * @return a list
   */
  public static IntList asList(int... a) {
    return DEFAULT.newList(a);
  }

  /**
   * Creates a list from the given array.
   * @param a an array
   * @return a list
   */
  public static LongList asList(long... a) {
    return DEFAULT.newList(a);
  }

  /**
   * Creates a list from the given array.
   * @param a an array
   * @return a list
   */
  public static ByteList asList(byte... a) {
    return DEFAULT.newList(a);
  }

  /**
   * Creates a list from the given array.
   * @param a an array
   * @return a list
   */
  public static CharList asList(char... a) {
    return DEFAULT.newList(a);
  }

  /**
   * Creates a list from the given array.
   * @param a an array
   * @return a list
   */
  public static FloatList asList(float... a) {
    return DEFAULT.newList(a);
  }

  /**
   * Creates a list from the given array.
   * @param a an array
   * @return a list
   */
  public static DoubleList asList(double... a) {
    return DEFAULT.newList(a);
  }

  /**
   * Creates a list from the given array.
   * @param a an array
   * @return a list
   */
  public static ShortList asList(short... a) {
    return DEFAULT.newList(a);
  }

  /**
   * Creates a list from the given input, using default settings (forbid null, allow duplicates)
   * @param a an array
   * @return a list
   * @throws java.lang.NullPointerException if the input contains a null element
   */
  public static <E extends Comparable> ComparableList<E> asList(E... a) {
    return DEFAULT.newList(a);
  }

  /**
   * Creates a list from the given input, using default settings (forbid null, allow duplicates)
   * @param a an array
   * @param comparator a Comparator
   * @return a searchable version of the input
   * @throws java.lang.NullPointerException if the input contains a null element
   */
  public static <E> ComparatorList<E> asList(Comparator<E> comparator, E... a) {
    return DEFAULT.newList(comparator, a);
  }

  /**
   * Creates a list from the given input.
   * @param a an array
   * @return a searchable version of the input
   * @throws java.lang.IllegalArgumentException if the PermutationFactory's uniqueness policy does not allow duplicates
   * and the input contains a duplicate element
   * @see Lists#getPermutationFactory
   */
  public IntList newList(int[] a) {
    return new IntList(a, factory.sort(a));
  }

  /**
   * Creates a list from the given input.
   * @param a an array
   * @return a searchable version of the input
   * @throws java.lang.IllegalArgumentException if the PermutationFactory's uniqueness policy does not allow duplicates
   * and the input contains a duplicate element
   * @see Lists#getPermutationFactory
   */
  public LongList newList(long[] a) {
    return new LongList(a, factory.sort(a));
  }

  /**
   * Creates a list from the given input.
   * @param a an array
   * @return a searchable version of the input
   * @throws java.lang.IllegalArgumentException if the PermutationFactory's uniqueness policy does not allow duplicates
   * and the input contains a duplicate element
   * @see Lists#getPermutationFactory
   */
  public ByteList newList(byte[] a) {
    return new ByteList(a, factory.sort(a));
  }

  /**
   * Creates a list from the given input.
   * @param a an array
   * @return a searchable version of the input
   * @throws java.lang.IllegalArgumentException if the PermutationFactory's uniqueness policy does not allow duplicates
   * and the input contains a duplicate element
   * @see Lists#getPermutationFactory
   */
  public CharList newList(char[] a) {
    return new CharList(a, factory.sort(a));
  }

  /**
   * Creates a list from the given input.
   * @param a an array
   * @return a searchable version of the input
   * @throws java.lang.IllegalArgumentException if the PermutationFactory's uniqueness policy does not allow duplicates
   * and the input contains a duplicate element
   * @see Lists#getPermutationFactory
   */
  public FloatList newList(float[] a) {
    return new FloatList(a, factory.sort(a));
  }

  /**
   * Creates a list from the given input.
   * @param a an array
   * @return a searchable version of the input
   * @throws java.lang.IllegalArgumentException if the PermutationFactory's uniqueness policy does not allow duplicates
   * and the input contains a duplicate element
   * @see Lists#getPermutationFactory
   */
  public DoubleList newList(double[] a) {
    return new DoubleList(a, factory.sort(a));
  }

  /**
   * Creates a list from the given input.
   * @param a an array
   * @return a searchable version of the input
   * @throws java.lang.IllegalArgumentException if the PermutationFactory's uniqueness policy does not allow duplicates
   * and the input contains a duplicate element
   * @see Lists#getPermutationFactory
   */
  public ShortList newList(short[] a) {
    return new ShortList(a, factory.sort(a));
  }

  /**
   * Creates a list from the given input.
   * @param a an array
   * @return a list
   * @throws java.lang.IllegalArgumentException if the PermutationFactory's uniqueness policy does not allow duplicates
   * and the input contains a duplicate element
   * @throws java.lang.NullPointerException if  the PermutationFactory's null policy forbids null elements,
   * and the input contains a null element
   * @see Lists#getPermutationFactory
   */
  public <E extends Comparable> ComparableList<E> newList(E[] a) {
    return new ComparableList<E>(a, factory.sort(a));
  }

  /**
   * Creates a list from the given input.
   * @param a an array
   * @param comparator a Comparator
   * @return a list
   * @throws java.lang.IllegalArgumentException if underlying the PermutationFactory's uniqueness constraint forbids
   * duplicates, and the input contains duplicates
   * @throws java.lang.NullPointerException if underlying the PermutationFactory's null policy forbids null elements,
   * and the input contains a null element
   * @see Lists#getPermutationFactory
   */
  public <E> ComparatorList<E> newList(Comparator<E> comparator, E[] a) {
    if (comparator == null)
      throw new IllegalArgumentException("comparator can not be null");
    return new ComparatorList<E>(a, comparator, factory.sort(a, comparator));
  }

  public static <E> ListBuilder<E> builder(Comparator<E> comparator) {
    return DEFAULT.newBuilder(comparator);
  }

  public static <E extends Comparable> ListBuilder<E> builder() {
    return DEFAULT.newBuilder();
  }

  public <E> ListBuilder<E> newBuilder(Comparator<E> comparator) {
    return new ComparatorBuilder<E>(comparator);
  }

  public <E extends Comparable> ListBuilder<E> newBuilder() {
    return new ComparableBuilder<E>();
  }


  public final class ByteList extends LookupList<Byte> implements PrimitiveList {
    private final byte[] sorted;

    private ByteList(byte[] a, Permutation sort) {
      super(sort);
      this.sorted = sort.apply(a);
    }

    @Override
    public int indexOf(Object el) {
      int i = Arrays.binarySearch(sorted, (Byte) el);
      return i < 0 ? -1 : unsort.apply(i);
    }

    @Override
    public int lastIndexOf(Object el) {
      byte b = (Byte) el;
      int start = Arrays.binarySearch(sorted, b);
      if (start < 0) {return -1;}
      int direction = start > 0 && sorted[start - 1] == b ? -1 : 1;
      int peek = start + direction;
      while (peek >= 0 && peek < sorted.length && sorted[peek] == b) {
        start = peek;
        peek += direction;
      }
      return unsort.apply(start);
    }

    @Override
    public boolean contains(Object el) {
      return indexOf(el) >= 0;
    }

    @Override
    public Byte get(int i) {
      return sorted[sort.apply(i)];
    }

    @Override
    public int size() {
      return sorted.length;
    }

    @Override
    public LookupList<Integer> indexesOf(Byte el) {
      byte b = el;
      ComparableBuilder<Integer> builder = new ComparableBuilder<Integer>();
      int pos = Arrays.binarySearch(sorted, b);
      if (pos < 0) {return builder.build();}
      int offset = 0;
      int direction = 1;
      int current;
      while (sorted[current = pos + offset] == b) {
        builder.add(unsort.apply(current));
        if (direction == 1) {
          if (pos + offset + direction >= sorted.length
              || sorted[pos + offset + 1] != b) {
            if (pos > 0) {
              offset = -1;
              direction = -1;
            } else {
              break;
            }
          } else {
            offset += 1;
          }
        } else {
          if (pos + offset == 0) break;
          offset -= 1;
        }
      }
      return builder.build();
    }

  }


  public final class LongList extends LookupList<Long> implements PrimitiveList {
    private final long[] sorted;

    private LongList(long[] a, Permutation sort) {
      super(sort);
      this.sorted = sort.apply(a);
    }

    @Override
    public int indexOf(Object el) {
      int i = Arrays.binarySearch(sorted, (Long) el);
      return i < 0 ? -1 : unsort.apply(i);
    }

    @Override
    public int lastIndexOf(Object el) {
      long n = (Long) el;
      int start = Arrays.binarySearch(sorted, n);
      if (start < 0) {return -1;}
      int direction = start > 0 && sorted[start - 1] == n ? -1 : 1;
      int peek = start + direction;
      while (peek >= 0 && peek < sorted.length && sorted[peek] == n) {
        start = peek;
        peek += direction;
      }
      return unsort.apply(start);
    }

    @Override
    public boolean contains(Object el) {
      return indexOf(el) >= 0;
    }

    @Override
    public Long get(int i) {
      return sorted[sort.apply(i)];
    }

    @Override
    public int size() {
      return sorted.length;
    }

    @Override
    public LookupList<Integer> indexesOf(Long el) {
      long n = el;
      ComparableBuilder<Integer> builder = new ComparableBuilder<Integer>();
      int pos = Arrays.binarySearch(sorted, n);
      if (pos < 0) {return builder.build();}
      int offset = 0;
      int direction = 1;
      int current;
      while (sorted[current = pos + offset] == n) {
        builder.add(unsort.apply(current));
        if (direction == 1) {
          if (pos + offset + direction >= sorted.length
              || sorted[pos + offset + 1] != n) {
            if (pos > 0) {
              offset = -1;
              direction = -1;
            } else {
              break;
            }
          } else {
            offset += 1;
          }
        } else {
          if (pos + offset == 0) break;
          offset -= 1;
        }
      }
      return builder.build();
    }

  }


  public final class CharList extends LookupList<Character> implements PrimitiveList {
    private final char[] sorted;

    private CharList(char[] a, Permutation sort) {
      super(sort);
      this.sorted = sort.apply(a);
    }

    @Override
    public int indexOf(Object el) {
      int i = Arrays.binarySearch(sorted, (Character) el);
      return i < 0 ? -1 : unsort.apply(i);
    }

    @Override
    public int lastIndexOf(Object el) {
      char c = (Character) el;
      int start = Arrays.binarySearch(sorted, c);
      if (start < 0) {return -1;}
      int direction = start > 0 && sorted[start - 1] == c ? -1 : 1;
      int peek = start + direction;
      while (peek >= 0 && peek < sorted.length && sorted[peek] == c) {
        start = peek;
        peek += direction;
      }
      return unsort.apply(start);
    }

    @Override
    public boolean contains(Object el) {
      return indexOf(el) >= 0;
    }

    @Override
    public Character get(int i) {
      return sorted[sort.apply(i)];
    }

    @Override
    public int size() {
      return sorted.length;
    }

    @Override
    public List<Integer> indexesOf(Character el) {
      char c = el;
      ComparableBuilder<Integer> builder = new ComparableBuilder<Integer>();
      int pos = Arrays.binarySearch(sorted, c);
      if (pos < 0) {return builder.build();}
      int offset = 0;
      int direction = 1;
      int current;
      while (sorted[current = pos + offset] == c) {
        builder.add(unsort.apply(current));
        if (direction == 1) {
          if (pos + offset + direction >= sorted.length
              || sorted[pos + offset + 1] != c) {
            if (pos > 0) {
              offset = -1;
              direction = -1;
            } else {
              break;
            }
          } else {
            offset += 1;
          }
        } else {
          if (pos + offset == 0) break;
          offset -= 1;
        }
      }
      return builder.build();
    }

  }


  public final class IntList extends LookupList<Integer> implements PrimitiveList {
    private final int[] sorted;

    private IntList(int[] a, Permutation sort) {
      super(sort);
      this.sorted = sort.apply(a);
    }

    @Override
    public int indexOf(Object el) {
      int i = Arrays.binarySearch(sorted, (Integer) el);
      return i < 0 ? -1 : unsort.apply(i);
    }

    @Override
    public int lastIndexOf(Object el) {
      int n = (Integer) el;
      int start = Arrays.binarySearch(sorted, n);
      if (start < 0) {return -1;};
      int direction = start > 0 && sorted[start - 1] == n ? -1 : 1;
      int peek = start + direction;
      while (peek >= 0 && peek < sorted.length && sorted[peek] == n) {
        start = peek;
        peek += direction;
      }
      return unsort.apply(start);
    }

    @Override
    public boolean contains(Object el) {
      return indexOf(el) >= 0;
    }

    @Override
    public Integer get(int i) {
      return sorted[sort.apply(i)];
    }

    @Override
    public int size() {
      return sorted.length;
    }

    @Override
    public List<Integer> indexesOf(Integer el) {
      int n = el;
      ComparableBuilder<Integer> builder = new ComparableBuilder<Integer>();
      int pos = Arrays.binarySearch(sorted, n);
      if (pos < 0) {return builder.build();}
      int offset = 0;
      int direction = 1;
      int current;
      while (sorted[current = pos + offset] == n) {
        builder.add(unsort.apply(current));
        if (direction == 1) {
          if (pos + offset + direction >= sorted.length
              || sorted[pos + offset + 1] != n) {
            if (pos > 0) {
              offset = -1;
              direction = -1;
            } else {
              break;
            }
          } else {
            offset += 1;
          }
        } else {
          if (pos + offset == 0) break;
          offset -= 1;
        }
      }
      return builder.build();
    }
  }


  public final class FloatList extends LookupList<Float> implements PrimitiveList {
    private final float[] sorted;

    private FloatList(float[] a, Permutation sort) {
      super(sort);
      this.sorted = sort.apply(a);
    }

    @Override
    public int indexOf(Object el) {
      int i = Arrays.binarySearch(sorted, (Float) el);
      return i < 0 ? -1 : unsort.apply(i);
    }

    @Override
    public int lastIndexOf(Object el) {
      float f = (Float) el;
      int start = Arrays.binarySearch(sorted, f);
      if (start < 0) {return -1;}
      int direction = start > 0 && sorted[start - 1] == f ? -1 : 1;
      int peek = start + direction;
      while (peek >= 0 && peek < sorted.length && sorted[peek] == f) {
        start = peek;
        peek += direction;
      }
      return unsort.apply(start);
    }


    @Override
    public boolean contains(Object el) {
      return indexOf(el) >= 0;
    }

    @Override
    public Float get(int i) {
      return sorted[sort.apply(i)];
    }

    @Override
    public int size() {
      return sorted.length;
    }

    @Override
    public List<Integer> indexesOf(Float el) {
      float f = el;
      ComparableBuilder<Integer> builder = new ComparableBuilder<Integer>();
      int pos = Arrays.binarySearch(sorted, f);
      if (pos < 0) {return builder.build();}
      int offset = 0;
      int direction = 1;
      int current;
      while (sorted[current = pos + offset] == f) {
        builder.add(unsort.apply(current));
        if (direction == 1) {
          if (pos + offset + direction >= sorted.length
              || sorted[pos + offset + 1] != f) {
            if (pos > 0) {
              offset = -1;
              direction = -1;
            } else {
              break;
            }
          } else {
            offset += 1;
          }
        } else {
          if (pos + offset == 0) break;
          offset -= 1;
        }
      }
      return builder.build();
    }


  }


  public final class DoubleList extends LookupList<Double> implements PrimitiveList {
    private final double[] sorted;

    private DoubleList(double[] a, Permutation sort) {
      super(sort);
      this.sorted = sort.apply(a);
    }

    @Override
    public int indexOf(Object el) {
      int i = Arrays.binarySearch(sorted, (Double) el);
      return i < 0 ? -1 : unsort.apply(i);
    }

    @Override
    public int lastIndexOf(Object el) {
      double d = (Double) el;
      int start = Arrays.binarySearch(sorted, d);
      if (start < 0) {return -1;}
      int direction = start > 0 && sorted[start - 1] == d ? -1 : 1;
      int peek = start + direction;
      while (peek >= 0 && peek < sorted.length && sorted[peek] == d) {
        start = peek;
        peek += direction;
      }
      return unsort.apply(start);
    }


    @Override
    public boolean contains(Object el) {
      return indexOf(el) >= 0;
    }

    @Override
    public Double get(int i) {
      return sorted[sort.apply(i)];
    }

    @Override
    public int size() {
      return sorted.length;
    }

    @Override
    public List<Integer> indexesOf(Double el) {
      double d = el;
      ComparableBuilder<Integer> builder = new ComparableBuilder<Integer>();
      int pos = Arrays.binarySearch(sorted, d);
      if (pos < 0) {return builder.build();}
      int offset = 0;
      int direction = 1;
      int current;
      while (sorted[current = pos + offset] == d) {
        builder.add(unsort.apply(current));
        if (direction == 1) {
          if (pos + offset + direction >= sorted.length
              || sorted[pos + offset + 1] != d) {
            if (pos > 0) {
              offset = -1;
              direction = -1;
            } else {
              break;
            }
          } else {
            offset += 1;
          }
        } else {
          if (pos + offset == 0) break;
          offset -= 1;
        }
      }
      return builder.build();
    }

  }


  public final class ShortList extends LookupList<Short> implements PrimitiveList {
    private final short[] sorted;

    private ShortList(short[] a, Permutation sort) {
      super(sort);
      this.sorted = sort.apply(a);
    }

    @Override
    public int indexOf(Object el) {
      int i = Arrays.binarySearch(sorted, (Short) el);
      return i < 0 ? -1 : unsort.apply(i);
    }

    @Override
    public int lastIndexOf(Object el) {
      short n = (Short) el;
      int start = Arrays.binarySearch(sorted, n);
      if (start < 0) {return -1;}
      int direction = start > 0 && sorted[start - 1] == n ? -1 : 1;
      int peek = start + direction;
      while (peek >= 0 && peek < sorted.length && sorted[peek] == n) {
        start = peek;
        peek += direction;
      }
      return unsort.apply(start);
    }

    @Override
    public boolean contains(Object el) {
      return indexOf(el) >= 0;
    }

    @Override
    public Short get(int i) {
      return sorted[sort.apply(i)];
    }

    @Override
    public int size() {
      return sorted.length;
    }

    @Override
    public List<Integer> indexesOf(Short el) {
      short n = el;
      ComparableBuilder<Integer> builder = new ComparableBuilder<Integer>();
      int pos = Arrays.binarySearch(sorted, n);
      if (pos < 0) {return builder.build();}
      int offset = 0;
      int direction = 1;
      int current;
      while (sorted[current = pos + offset] == n) {
        builder.add(unsort.apply(current));
        if (direction == 1) {
          if (pos + offset + direction >= sorted.length
              || sorted[pos + offset + 1] != n) {
            if (pos > 0) {
              offset = -1;
              direction = -1;
            } else {
              break;
            }
          } else {
            offset += 1;
          }
        } else {
          if (pos + offset == 0) break;
          offset -= 1;
        }
      }
      return builder.build();
    }

  }


  public final class ComparableList<E extends Comparable> extends LookupList<E> {
    private final Comparable[] sorted;

    private ComparableList(Comparable[] a, Permutation sort) {
      super(sort);
      this.sorted = sort.apply(a);
    }

    @Override
    public int indexOf(Object el) {
      int i = Arrays.binarySearch(sorted, el);
      return i < 0 ? -1 : unsort.apply(i);
    }

    @Override
    public int lastIndexOf(Object el) {
      Comparable comparable = (Comparable) el;
      int start = Arrays.binarySearch(sorted, comparable);
      if (start < 0) {return -1;}
      int direction = start > 0 && Objects.equals(sorted[start - 1], comparable) ? -1 : 1;
      int peek = start + direction;
      while (peek >= 0 && peek < sorted.length && Objects.equals(sorted[peek], comparable)) {
        start = peek;
        peek += direction;
      }
      return unsort.apply(start);
    }

    @Override
    public boolean contains(Object el) {
      return indexOf(el) >= 0;
    }

    @Override
    @SuppressWarnings("unchecked")
    public E get(int i) {
      return (E) sorted[sort.apply(i)];
    }

    @Override
    public int size() {
      return sorted.length;
    }

    @Override
    public List<Integer> indexesOf(E el) {
      ComparableBuilder<Integer> builder = new ComparableBuilder<Integer>();
      int pos = Arrays.binarySearch(sorted, el);
      if (pos < 0) {return builder.build();}
      int offset = 0;
      int direction = 1;
      int current;
      while (Objects.equals(sorted[current = pos + offset], el)) {
        builder.add(unsort.apply(current));
        if (direction == 1) {
          if (pos + offset + direction >= sorted.length
              || !Objects.equals(sorted[pos + offset + 1], el)) {
            if (pos > 0) {
              offset = -1;
              direction = -1;
            } else {
              break;
            }
          } else {
            offset += 1;
          }
        } else {
          if (pos + offset == 0) break;
          offset -= 1;
        }
      }
      return builder.build();
    }


  }

  public final class ComparatorList<E> extends LookupList<E> {
    private final Object[] sorted;
    private final Comparator<E> comparator;

    private ComparatorList(Object[] a, Comparator<E> comparator, Permutation sort) {
      super(sort);
      this.sorted = sort.apply(a);
      this.comparator = comparator;
    }

    /**
     * Get the comparator used by this instance.
     * @return the comparator
     */
    public Comparator<E> getComparator() {
      return comparator;
    }

    @Override
    public int indexOf(Object el) {
      @SuppressWarnings("unchecked")
      int i = Arrays.binarySearch(sorted, el, (Comparator) comparator);
      return i < 0 ? -1 : unsort.apply(i);
    }

    @Override
    public int lastIndexOf(Object el) {
      @SuppressWarnings("unchecked")
      int start = Arrays.binarySearch(sorted, el, (Comparator) comparator);
      if (start < 0) {return -1;}
      int direction = start > 0 && Objects.equals(sorted[start - 1], el) ? -1 : 1;
      int peek = start + direction;
      while (peek >= 0 && peek < sorted.length && Objects.equals(sorted[peek], el)) {
        start = peek;
        peek += direction;
      }
      return unsort.apply(start);
    }

    @Override
    public boolean contains(Object el) {
      return indexOf(el) >= 0;
    }

    @Override
    @SuppressWarnings("unchecked")
    public E get(int i) {
      return (E) sorted[sort.apply(i)];
    }

    @Override
    public int size() {
      return sorted.length;
    }

    @Override
    public List<Integer> indexesOf(E el) {
      ComparableBuilder<Integer> builder = new ComparableBuilder<Integer>();
      int pos = Arrays.binarySearch(sorted, el, (Comparator) comparator);
      if (pos < 0) {return builder.build();}
      int offset = 0;
      int direction = 1;
      int current;
      while (Objects.equals(sorted[current = pos + offset], el)) {
        builder.add(unsort.apply(current));
        if (direction == 1) {
          if (pos + offset + direction >= sorted.length
              || !Objects.equals(sorted[pos + offset + 1], el)) {
            if (pos > 0) {
              offset = -1;
              direction = -1;
            } else {
              break;
            }
          } else {
            offset += 1;
          }
        } else {
          if (pos + offset == 0) break;
          offset -= 1;
        }
      }
      return builder.build();
    }


  }

  abstract static class ListBuilder<E> {
    protected int size = 0;

    static int extendedCapacity(int oldCapacity, int minCapacity) {
      if (minCapacity < 0)
        throw new IllegalArgumentException("must not be negative");
      int newCapacity = oldCapacity;
      while (newCapacity < minCapacity) {
        int next = newCapacity + (newCapacity >> 1) + 1;
        if (next < newCapacity)
          next = Integer.MAX_VALUE;
        newCapacity = next;
      }
      return newCapacity;
    }

    protected abstract void ensureCapacity(int minCapacity);

    protected abstract ListBuilder<E> add(E el);

    public ListBuilder<E> addAll(E... els) {
      ensureCapacity(size + els.length);
      for (E el : els) {add(el); }
      return this;
    }

    public ListBuilder<E> addAll(Collection<E> els) {
      ensureCapacity(size + els.size());
      for (E el : els) { add(el); }
      return this;
    }

    public abstract List<E> build();

  }

  public final class ComparableBuilder<E extends Comparable> extends ListBuilder<E> {
    private Comparable[] contents = new Comparable[16];

    @Override
    public ComparableList<E> build() {
      Comparable[] a = Arrays.copyOf(contents, size);
      return new ComparableList<E>(a, factory.sort(a));
    }

    @Override
    protected void ensureCapacity(int minCapacity) {
      if (minCapacity > contents.length)
        contents = Arrays.copyOf(contents, extendedCapacity(contents.length, minCapacity));
    }

    @Override
    public ComparableBuilder<E> add(E element) {
      ensureCapacity(size + 1);
      contents[size++] = element;
      return this;
    }

  }

  public final class ComparatorBuilder<E> extends ListBuilder<E> {
    private final Comparator<E> comparator;
    private Object[] contents = new Object[16];

    private ComparatorBuilder(Comparator<E> comparator) {this.comparator = comparator;}

    @Override
    public ComparatorList<E> build() {
      Object[] a = Arrays.copyOf(contents, size);
      return new ComparatorList<E>(a, comparator, factory.sort(a, comparator));
    }

    @Override
    protected void ensureCapacity(int minCapacity) {
      if (minCapacity > contents.length)
        contents = Arrays.copyOf(contents, extendedCapacity(contents.length, minCapacity));
    }

    @Override
    public ComparatorBuilder<E> add(E el) {
      ensureCapacity(size + 1);
      contents[size++] = el;
      return this;
    }

  }

  /**
   * Get the PermutationFactory that is used to sort the input of the various builder methods.
   * @return the permutation factory
   */
  public PermutationFactory getPermutationFactory() {
    return factory;
  }

}
