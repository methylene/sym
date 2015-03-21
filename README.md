# sym

## Permutations and LookupList

Maven dependency:

````xml
<dependency>
  <groupId>com.github.methylene</groupId>
  <artifactId>sym</artifactId>
  <version>1.9.4</version>
</dependency>
````

Permutations are objects that encapsulate certain operations on arrays and lists,
such as sorting, shuffling, swapping two elements, or moving an element.

`LookupList` is an immutable implementation of `java.util.List` 
that is optimized for searching. Its implementation uses permutations internally.

The  <i>indexOf</i>, <i>lastIndexOf</i> and <i>contains</i> methods of `LookupList` 
will often perform much better than other array based lists.

Additionally, `LookupList` provides an efficient <i>partition</i> method.

A speed comparison between <i>LookupList</i> and <i>ArrayList</i> can be made as follows:

    $ mvn clean test -f sym/pom.xml -Pbench

This will print something like
    
    Running com.github.methylene.lists.PerformanceTest
    == list size: 10000
    == repeat: 50
    == return value of .indexOf: -1
    indexOf:                 2626.92
    indexOfjdK:            249112.92
    lastIndexOf:             2224.66
    lastIndexOfJdk         657190.04
    index_relative:     0.0105450973
    lastIndex_relative: 0.0033851091
