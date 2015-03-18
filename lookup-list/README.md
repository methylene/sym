# lookup-list

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
