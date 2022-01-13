/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {
    private static final int INIT_CAPACITY = 8;

    private Item[] rq;
    private int n;

    // resize the queue
    private void resize(int capacity) {
        Item[] copy = (Item[]) new Object[capacity];
        for (int i = 0; i < n; i++) {
            copy[i] = rq[i];
        }
        rq = copy;
    }

    // construct an empty randomized queue
    public RandomizedQueue() {
        rq = (Item[]) new Object[INIT_CAPACITY];
        n = 0;
    }

    // is the randomized queue empty?
    public boolean isEmpty() {
        return n == 0;
    }

    // return the number of items on the randomized queue
    public int size() {
        return n;
    }

    // add the item
    public void enqueue(Item item) {
        if (item == null) throw new IllegalArgumentException("Item cannot be null");
        if (n == rq.length) resize(2 * rq.length);
        rq[n] = item;
        n++;
    }

    // remove and return a random item
    public Item dequeue() {
        if (isEmpty()) throw new NoSuchElementException("There are no more elements to remove");
        int ranNum = StdRandom.uniform(n);
        Item item;
        if (n == 1) {
            item = rq[0];
            rq[0] = null;
        }
        else {
            item = rq[ranNum];
            rq[ranNum] = null;
        }
        n--;
        if (rq[n] == rq[ranNum]) rq[ranNum] = null;
        else {
            rq[ranNum] = rq[n];
            rq[n] = null;
        }
        if (n > 0 && n == rq.length / 4) resize(rq.length / 2);
        return item;
    }

    // return a random item (but do not remove it)
    public Item sample() {
        if (isEmpty()) throw new NoSuchElementException("There are no more elements to remove");
        int ranNum = StdRandom.uniform(n);
        return rq[ranNum];
    }

    // return an independent iterator over items in random order
    public Iterator<Item> iterator() {
        return new QueueIterator();
    }

    private class QueueIterator implements Iterator<Item> {
        private int nR = n - 1;
        int[] indices = new int[n];

        public QueueIterator() {
            for (int i = 0; i < n; i++) indices[i] = i;
            StdRandom.shuffle(indices);
        }

        public boolean hasNext() {
            return nR >= 0;
        }

        public void remove() {
            throw new UnsupportedOperationException(
                    "This operation is not allowed in the iterator");
        }

        public Item next() {
            if (!hasNext()) throw new NoSuchElementException(
                    "There are no more items through which to iterate");
            return rq[indices[nR--]];
        }

    }

    // unit testing (required)
    public static void main(String[] args) {
        // String a = args[0], b = args[1], c = args[2], d = args[3], e = args[4];
        //
        // RandomizedQueue<String> rQ = new RandomizedQueue<>();
        // StdOut.println("Is the deque empty? " + rQ.isEmpty());
        //
        // rQ.enqueue(a);
        // // StdOut.println("Does the first element have more? " + rQ.iterator().hasNext());
        //
        // rQ.enqueue(b);
        // // StdOut.println("Does the first element have more? " + rQ.iterator().hasNext());
        // // StdOut.println("What is the element? " + rQ.iterator().next());
        //
        // rQ.enqueue(c);
        // // StdOut.println("Does the first element have more? " + rQ.iterator().hasNext());
        // // StdOut.println("What is the element? " + rQ.iterator().next());
        //
        // rQ.enqueue(d);
        // // StdOut.println("Does the first element have more? " + rQ.iterator().hasNext());
        // // StdOut.println("What is the element? " + rQ.iterator().next());
        //
        // // StdOut.println("Test:");
        // // for (String item : deck) {
        // //     Iterator<String> iterator = deck.iterator();
        // //     if (iterator.hasNext()) StdOut.println(iterator.next());
        // // }
        //
        // StdOut.println("After adding to the front and back, is the deque empty? " + rQ.isEmpty());
        // StdOut.println("How big is the deque? " + rQ.size());
        //
        // StdOut.println("Let's remove the first element " + rQ.dequeue());
        // StdOut.println(
        //         "After removing from the front, what is the size of the deque? " + rQ.size());
        //
        // rQ.enqueue(e);
        // StdOut.println("Added one more item. Let's now sample randomly: " + rQ.sample());
        //
        // StdOut.println(
        //         "After adding (1) to the front, what is the size of the deque? " + rQ.size());
        //
        // StdOut.println("Remove the remaining items in the list:");
        // while (!rQ.isEmpty()) {
        //     StdOut.println(rQ.dequeue());
        // }
        //
        // StdOut.println("Checking to see whether all items are removed... is the deck empty? " + rQ
        //         .isEmpty());
        // StdOut.println("What is the size of the deque after all of this? " + rQ.size());

        int n = 5;
        RandomizedQueue<Integer> queue = new RandomizedQueue<Integer>();
        for (int i = 0; i < n; i++)
            queue.enqueue(i);
        for (int a : queue) {
            for (int b : queue)
                StdOut.print(a + "-" + b + " ");
            StdOut.println();
        }

    }
}
