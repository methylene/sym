# sym

## Permutations for Java

A permutation is an object that encapsulates certain operations on arrays
such as sorting, shuffling, swapping two elements, or moving an element by deleting and inserting.

This library is available as a maven artifact:

````xml
<dependency>
  <groupId>com.github.methylene</groupId>
  <artifactId>sym</artifactId>
  <version>1.9.1</version>
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

Our client now gives us "rearrangement" of the header fields, and asks us to rearrange the
arrays accordingly. Fortunately, we can use `Permutation.from` 
to find a permutation that performs the rearranging

````java
String[] newHeader = new String[]{"country", "pop", "gdp", "area"};
Permutation rearrange = Permutation.factory().from(header, newHeader);
````

Now we can use the this permutation on the rows

````java
rearrange.apply(uk);
// => [UK, 255.6, 38309, 243610]
rearrange.apply(lt);
// => [Lithuania, 45, 28245, 65300]
````

### Searching an array

This example shows how to find the index of a given string in an unsorted array.
Get a permutation that sorts `a`

````java
String[] a = new String[]{"a", "f", "v", "x", "x", "n"};
Permutation sort = Permutation.factory().sort(a);
````

Sort `a`, and get the permutation that undoes the sorting

````java
String[] sorted = sort.apply(a);
Permutation unsort = sort.invert();
````
Now we can find the index of `"x"` using 
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
Permutation t02 = Permutation.swap(0, 2);
Permutation t01 = Permutation.swap(0, 1);
System.out.println(t02.comp(t01).apply(bca));
// = > abc
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

Consider the following

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

This finds a permutation that sorts the string `"Hello world!"` (there are several)
and then prints its decomposition into <a href="http://en.wikipedia.org/wiki/Cyclic_permutation">cycles</a>:

    [7, 9]
    [1, 4, 8, 10, 3, 6, 11]
    [0, 2, 5]

This can be verified by the following code:

````java
Permutation c = Permutation.prod(cycle(9, 7),
                                 cycle(11, 6, 3, 10, 8, 4, 1),
                                 cycle(5, 2, 0));
System.out.println(c.apply(" !Hdellloorw"));
// => Hello world!
````

For more ideas, see the [javadoc](http://methylene.github.io/sym/current/com/github/methylene/sym/package-summary.html).
