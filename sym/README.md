# sym

## LookupList

This project contains immutable implementations of the `java.util.List` interface 
that are backed by a sorted array.
Their `.indexOf`, `.lastIndexOf` and `.contains` method use binary search internally.

The abstract class `LookupList` defines an additional arity of the 
`indexOf` method that can be used
to find duplicate elements, or count occurrences.

For example, the following method finds two indexes of an element 
that is at least twice in the list, in roughly `O(n*log(n))` time:

````java
public <E> int[] findDuplicate(LookupList<E> list) {
  for (int i = 0; i < list.size(); i++) {
    int[] indexes = list.indexOf(list.get(i), 2);
    if (indexes.length == 2)
      return indexes;
  }
  // all elements unique
  return null;
}
````

## Permutation

<b>Permutations</b> are objects that encapsulate certain operations on arrays and lists,
such as sorting, shuffling, swapping two elements, or moving an element.

This library is available as a maven artifact:

````xml
<dependency>
  <groupId>com.github.methylene</groupId>
  <artifactId>sym</artifactId>
  <version>1.9.4</version>
</dependency>
````

### Shuffling an array

````java
String[] a = {"Check", "out", "this", "great", "library"};
System.out.println(Permutation.random(a.length).apply(a));
// => [great, library, Check, this, out]
````

A permutation can be applied to arrays, lists or strings, as long as they are not too short.

````java
System.out.println(new Permutation(new int[]{0, 2, 1}).apply("abc"));
// => acb
System.out.println(new Permutation(new int[]{0, 2, 1}).apply("ab"));
// => IllegalArgumentException: input too short: 2, minimum input length is 3
````

It is also possible to apply the permutation to an index.

````java
System.out.println(new Permutation(new int[]{0, 2, 1}).apply(1));
// => 2
System.out.println(new Permutation(new int[]{0, 2, 1}).apply(-1));
// => IllegalArgumentException: negative index: -1
````

### Changing column order

In this use case we have some arrays and a header that gives meaningful names to their contents

````java
String[] header = {"country", "area", "pop", "gdp"};
Object[] uk = {"UK", 243610, 255.6, 38309};
Object[] lt = {"Lithuania", 65300, 45, 28245};
````

Our client hands us a "rearrangement" of the header fields, and asks us to change the columns accordingly.

````java
String[] newHeader = {"country", "pop", "gdp", "area"};
````

Fortunately, we can use the `Permutation.from` method 
to find a permutation that performs this operation,

````java
Permutation rearrange = Permutation.factory().from(header, newHeader);
````

and we are now able to rearrange the rows as required.

````java
System.out.println(Arrays.toString(rearrange.apply(uk)));
// => [UK, 255.6, 38309, 243610]
System.out.println(Arrays.toString(rearrange.apply(lt)));
// => [Lithuania, 45, 28245, 65300]
````

### Searching an array

This example motivates the lookup list `java.util.List` implementation.
Our goal is to find the index of a string in a given string-array `a`.
We obtain a permutation that sorts `a`

````java
String[] a = {"a", "f", "v", "x", "x", "n"};
Permutation sort = Permutation.factory().sort(a);
String[] sorted = sort.apply(a);
````

as well as the the permutation that undoes the sorting

````java
Permutation unsort = sort.invert();
````

and now we can find the index of `"x"` using 
[binary search](http://docs.oracle.com/javase/7/docs/api/java/util/Arrays.html):

````java
int i = Arrays.binarySearch(sorted, "x");
if (i >= 0) {
  System.out.println(unsort.apply(i));
}
// => 3
````

Note that this will always the _least_ index of x if there is a choice, due to the way
the sort permutation is built using binarySearch. So this returns 3, not 4.

The subclasses of `LookupList` use this mechanism internally.

### Composition

Permutations can be composed using the `comp` method.

````java
Permutation s02 = Permutation.swap(0, 2);
Permutation s01 = Permutation.swap(0, 1);
System.out.println(s02.comp(s01).apply("bca"));
// = > abc
````

Applying the composed permutation `s02.comp(s01)` is equivalent to first applying `s01` and then `s02`

````java
String s = s01.apply("bca");
s = s02.apply(s);
System.out.println(s);
// = > abc
````

but doing it the other way round will generally give a different result.

````java
String s = s02.apply("bca");
s = s01.apply(s);
System.out.println(s);
// = > cab
````

### Padding

Padding is a generic way to build longer permutations from shorter ones.

Indexes `i >= p.length(), i < m` are not moved by a padded permutation `p.padding(m)`:

````java
Permutation c = Permutation.cycle(0, 1, 2, 3);
System.out.println(c.apply("1234"));
// => 4123
System.out.println(c.padding(10).apply("1234567890"));
// => 4123567890
````

In version 1.9.0, implicit padding was added to the `comp` and `apply` methods,
so it's rarely necessary to apply padding explicitly.

````java
System.out.println(c.apply("1234567890"));
// => 4123567890
````

### Cycle decomposition, orbits etc

Consider the following code

````java
static int[] findCycle(Permutation p) {
  for (int i = 0; i < p.length(); i += 1)
    if (p.orbit(i).length > 1)
      return p.orbit(i);
  return null;
}

public static void main(String[] args) {
  Permutation s = Permutation.factory().sort("Hello world!");
  for (Permutation p: s.toCycles())
    System.out.println(Arrays.toString(findCycle(p)));
}
````

which finds a permutation that sorts the string `"Hello world!"` (there are several)
and then prints its decomposition into <a href="http://en.wikipedia.org/wiki/Cyclic_permutation">cycles</a>

    [7, 9]
    [1, 4, 8, 10, 3, 6, 11]
    [0, 2, 5]

as can be verified by the following code

````java
Permutation c = Permutation.prod(cycle(9, 7),
                                 cycle(11, 6, 3, 10, 8, 4, 1),
                                 cycle(5, 2, 0));
System.out.println(c.apply(" !Hdellloorw"));
// => Hello world!
````

For more ideas, see the [javadoc](http://methylene.github.io/sym/current/com/github/methylene/sym/package-summary.html).

<!--
Idea: add Kendall tau distance? add rank?
http://rosettacode.org/wiki/Permutations/Rank_of_a_permutation
-->
