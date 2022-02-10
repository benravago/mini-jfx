package fx.print.ipp;

import java.util.Iterator;

class Vector<T> implements Iterable<T> {

  class Item {
    Item next;
    T value;
  }

  Item head, tail;
  int count;
  
  int size() {
    return count;
  }

  void add(T value) {
    var i = new Item();
    i.value = value;
    if (tail != null) {
      tail.next = i;
    } else {
      head = i;
    }
    tail = i;
    count++;
  }

  @Override
  public Iterator<T> iterator() {
    return new Iterator<>() {
      @Override
      public boolean hasNext() {
        return next != null;
      }
      @Override
      public T next() {
        var value = next.value;
        next = next.next;
        return value;
      }
      Item next = head;
    };
  }

}
