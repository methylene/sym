# sym

## Permutations for Java

Permutation are objects that encapsulate certain operations on arrays and lists,
such as sorting, shuffling, swapping two elements, or 
[moving](http://methylene.github.io/sym/current/com/github/methylene/sym/Permutation.html#move%28int,%20int%29) 
an element.

This library is available as a maven artifact:

````xml
<dependency>
  <groupId>com.github.methylene</groupId>
  <artifactId>sym</artifactId>
  <version>1.9.2</version>
</dependency>
````

### Shuffling an array

````java
String[] sentence = new String[]{"Check", "out", "this", "great", "library"};
Permutation.random(sentence.length).apply(sentence);
// => [great, library, Check, this, out]
````

### Changing column order

In this use case we have some arrays and a header that gives meaningful names to their contents

````java
String[] header = new String[]{"country", "area", "pop", "gdp"};
Object[] uk = new Object[]{"UK", 243610, 255.6, 38309};
Object[] lt = new Object[]{"Lithuania", 65300, 45, 28245};
````

Our client hands us a "rearrangement" of the header fields, and asks us to rearrange the
arrays accordingly.

````java
String[] newHeader = new String[]{"country", "pop", "gdp", "area"};
````

Fortunately, we can use the `Permutation.from` method 
to find a permutation that performs the rearranging,

````java
Permutation rearrange = Permutation.factory().from(header, newHeader);
````

and we are now able to rearrange the rows as required.

````java
rearrange.apply(uk);
// => [UK, 255.6, 38309, 243610]
rearrange.apply(lt);
// => [Lithuania, 45, 28245, 65300]
````

### Searching an array

This example shows how to find the index of a given string in an unsorted array.
We get a permutation that sorts `a`

````java
String[] a = new String[]{"a", "f", "v", "x", "x", "n"};
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

### Composition

Permutations can be composed using the `comp` method.

````java
char[] bca = new char[]{ 'b', 'c', 'a' };
Permutation s02 = Permutation.swap(0, 2);
Permutation s01 = Permutation.swap(0, 1);
System.out.println(s02.comp(s01).apply(bca));
// = > abc
````

Applying the composed permutation `t02.comp(t01)` is equivalent to first applying `t01` and then `t02`

````java
char[] m = s01.apply(bca);
m = s02.apply(m);
System.out.println(m);
// = > abc
````

but doing it the other way round will generally give a different result.

````java
char[] m = s02.apply(bca);
m = s01.apply(m);
System.out.println(m);
// = > cab
````

### Padding

Padding is a generic way to build longer permutations from shorter ones.

Indexes `i >= p.length(), i < m` are not moved by a padded permutation `p.padding(m)`:

````java
Permutation cycle = Permutation.cycle(0, 1, 2, 3);
System.out.println(cycle.apply(0));
// => 1
System.out.println(cycle.apply(3));
// => 0
System.out.println(cycle.padding(10).apply(6));
// => 6
````

In version 1.9.0, implicit padding was added to the `comp` and `apply` methods,
so it's rarely necessary to apply padding explicitly.

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
