# sym

Permutations for Java

    <dependency>
      <groupId>com.github.methylene</groupId>
      <artifactId>sym</artifactId>
      <version>1.2</version>
    </dependency>

### Shuffling an array

    String[] sentence = new String[]{"Check", "out", "this", "great", "library"};
    Permutation.random(sentence.length).apply(sentence);
    => [great, library, Check, this, out]

### Changing colum order

Suppose you have a bunch of CSV and a header:

    String[] header = new String[]{"country", "area", "pop", "gdp"};
    Object[] row1 = new Object[]{"UK", 243610, 255.6, 38309};
    Object[] row2 = new Object[]{"Lithuania", 65300, 45, 28245};

The columns are easily rearranged:

    String[] newHeader = new String[]{"country", "pop", "gdp", "area"};
    Permutation reorder = Permutation.from(header, newHeader);
    reorder.apply(row1);
    => [UK, 255.6, 38309, 243610]
    reorder.apply(row2);
    => [Lithuania, 45, 28245, 65300]

### Searching in an array

Finding the index of a given element `e` in an array `a` is a `O(n)` 
operation at first glance, because we need to do an equality test on each element of `a` in sequence.

If we search more than once in the same array, a HashMap 
that maps each element to its position could be used to speed this up.

If the elements of `a` are `Comparable` and _distinct_,
the HashMap is not needed.

We make a sorted copy of `a`, along with the `unsortA` permutation,
which maps indexes in `sortedA` to their original position in `a`:

    String[] a = new String[]{"x", "f", "v", "c", "n"};
    Permutation sortA = Permutation.sort(a);
    Permutation unsortA = sortA.invert();
    String[] sortedA = sortA.apply(a);

`sortedA` is just what we'd get by doing `Arrays.sort(a)`, 
except `apply` always returns a copy and leaves `a` unchanged.

Now we can find the index of `e` in `a` like this:

    public int indexOf(String e) {
      int i = Arrays.binarySearch(sortedA, e);
      return i < 0 ? i : unsortA.apply(i);
    }

Here `unsortA.apply(i)` is just an array lookup, 
so this takes about as long as the `binarySearch` call.

Notice that `Permutation.sort(a)` will throw an `IllegalArgumentException` 
if `a` contains duplicates.

### Composition

The following static import is assumed:

    import static com.github.methylene.sym.Permutation.*;

Permutations can be composed using `prod`, `comp` or `pow`. 
`prod` and `comp` will throw an `IllegalArgumentException` if the arguments differ in `length`.
The `pad` method can be used to get around this restriction.

    char[] bca = new char[]{ 'b', 'c', 'a' };
    Permutation srt = Permutation.sort(bca);
    srt.pow(3).isIdentity();
    => true

    Permutation hardWork = prod(swap(0, 2), swap(0, 1).pad(3));
    hardWork.apply(bca);
    = > abc

    hardWork.equals(srt);
    => true

Indexes `i >= p.length(), i < m` are not moved by a padded permutation `p.pad(m)`:

    random(3).pad(4).apply(3)
    => 3
