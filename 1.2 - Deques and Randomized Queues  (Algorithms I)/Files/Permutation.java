/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class Permutation {
    public static void main(String[] args) {
        int k = Integer.parseInt(args[0]);

        RandomizedQueue<String> strings = new RandomizedQueue<>();

        while (!StdIn.isEmpty()) {
            strings.enqueue(StdIn.readString());
        }

        for (int i = k; k > 0; k--) {
            StdOut.println(strings.dequeue());
        }
    }
}
