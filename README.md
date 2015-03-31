# sym

## Permutations and LookupList

Maven dependency:

````xml
<dependency>
  <groupId>com.github.methylene</groupId>
  <artifactId>sym</artifactId>
  <version>2.0</version>
</dependency>
````

<i>Permutations</i> 
are objects that act on arrays or lists to perform certain operations,
such as sorting, shuffling, swapping or moving elements.

<i>LookupList</i>
is an immutable implementation of `java.util.List` 
that is optimized for searching. Its implementation uses permutations internally.

The  <i>indexOf</i>, <i>lastIndexOf</i> and <i>contains</i> methods of `LookupList` 
will often perform much better than other array based lists.

Additionally, `LookupList` provides an efficient <i>partition</i> method.

A speed comparison between <i>LookupList</i> and <i>ArrayList</i> can be made as follows:

    $ mvn clean test -f sym/pom.xml -Pbench

This will run a benchmark with a random list of size 10000 and print the results.

    Running com.github.methylene.lists.PerformanceTest
    == list size: 10000
    == repeat: 50
    == return value of .indexOf: 2024
    indexOf:                 2759.76
    indexOfjdK:             97604.78
    lastIndexOf:             2390.68
    lastIndexOfJdk         820767.64
    index_relative:     0.0282748447
    lastIndex_relative: 0.0029127366

Javadoc:

* [Permutation](http://methylene.github.io/sym/current/com/github/methylene/sym/package-summary.html),
* [LookupList](http://methylene.github.io/sym/current/com/github/methylene/lists/package-summary.html)

For more information, see also the [other README](sym).
