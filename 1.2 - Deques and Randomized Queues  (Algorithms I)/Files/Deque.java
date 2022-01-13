/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

// This must be implemented with a linked list.
// Randomized queue must be with a resizing array

import edu.princeton.cs.algs4.StdOut;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {
    private int n; // size of the deque
    private Node first, last; // top, bottom of deque

    // helper linked list class
    private class Node {
        private Item item;
        private Node next;
        private Node prev;
    }

    // construct an empty deque
    public Deque() {
        first = null;
        last = null;
        // current = null;
        n = 0;
    }

    // is the deque empty?
    public boolean isEmpty() {
        return n == 0;
    }

    // return the number of items on the deque
    public int size() {
        return n;
    }

    // add the item to the front
    public void addFirst(Item item) {
        if (item == null) throw new IllegalArgumentException("Item can not be null value");
        Node oldfirst;
        Node newfirst = new Node();
        if (!isEmpty()) {
            oldfirst = first;
            newfirst.item = item;
            newfirst.next = oldfirst;
            oldfirst.prev = newfirst;
            first = newfirst;
            // current = first;
        }
        else {
            first = new Node();
            first.item = item;
            last = first;
            // iterator = first;
        }
        n++;
    }

    // add the item to the back
    public void addLast(Item item) {
        if (item == null) throw new IllegalArgumentException("Item can not be null value");
        Node newlast = new Node();
        newlast.item = item;
        if (!isEmpty()) {
            Node oldlast = last;
            newlast.prev = oldlast;
            oldlast.next = newlast;
            last = newlast;
            if (oldlast.prev == null) first = oldlast;
        }
        else {
            last = newlast;
            first = last;
            // current = first;
        }
        n++;
    }

    // remove and return the item from the front
    public Item removeFirst() {
        if (isEmpty()) throw new NoSuchElementException("List is empty");
        Item item;
        if (n == 1) {
            first.item = last.item;
            first = last;
            // current = first;
            first.prev = null;
            first.next = null;
            last = first;
            item = first.item;
        }
        else {
            item = first.item;
            first = first.next;
            // current = first;
            first.prev = null;
        }
        n--;
        return item;
    }

    // remove and return the item from the back
    public Item removeLast() {
        if (isEmpty()) throw new NoSuchElementException("List is empty");
        Item item;
        if (n == 1) {
            last.item = first.item;
            last = first;
            last.prev = null;
            last.next = null;
            first = last;
            // current = first;
            item = last.item;
        }
        else {
            item = last.item;
            last = last.prev;
            last.next = null;
        }
        n--;
        return item;
    }

    // return an iterator over items in order from front to back
    public Iterator<Item> iterator() {
        return new QueueIterator();
    }

    private class QueueIterator implements Iterator<Item> {
        private Node current = first;

        public boolean hasNext() {
            return current != null;
        }

        public void remove() {
            throw new UnsupportedOperationException(
                    "This operation is not allowed in the iterator");
        }

        public Item next() {
            if (!hasNext()) throw new NoSuchElementException(
                    "There are no more items through which to iterate");
            Item item = current.item;
            current = current.next;

            return item;
        }
    }

    // unit testing (required)
    public static void main(String[] args) {
        String a = args[0], b = args[1], c = args[2], d = args[3], e = args[4];

        Deque<String> deck = new Deque<String>();
        StdOut.println("Is the deque empty? " + deck.isEmpty());

        deck.addFirst(a);
        StdOut.println("Does the first element have more? " + deck.iterator().hasNext());

        deck.addLast(b);
        // StdOut.println("Does the first element have more? " + deck.iterator().hasNext());
        // StdOut.println("What is the element? " + deck.iterator().next());

        deck.addFirst(c);
        // StdOut.println("Does the first element have more? " + deck.iterator().hasNext());
        // StdOut.println("What is the element? " + deck.iterator().next());

        deck.addLast(d);
        // StdOut.println("Does the first element have more? " + deck.iterator().hasNext());
        // StdOut.println("What is the element? " + deck.iterator().next());

        StdOut.println("Test:");
        // for (String item : deck) {
        //     Iterator<String> iterator = deck.iterator();
        //     if (iterator.hasNext()) StdOut.println(iterator.next());
        // }

        StdOut.println("After adding to the front and back, is the deque empty? " + deck.isEmpty());
        StdOut.println("How big is the deque? " + deck.size());

        StdOut.println("Let's remove the first element " + deck.removeFirst());
        StdOut.println(
                "After removing from the front, what is the size of the deque? " + deck.size());

        deck.addFirst(e);
        StdOut.println(
                "After adding (1) to the front, what is the size of the deque? " + deck.size());

        StdOut.println("Remove the remaining items in the list:");
        for (String item : deck) {
            StdOut.println(deck.removeFirst());
        }

        StdOut.println("Checking to see whether all items are removed... is the deck empty? " + deck
                .isEmpty());
        StdOut.println("What is the size of the deque after all of this? " + deck.size());
    }
}
