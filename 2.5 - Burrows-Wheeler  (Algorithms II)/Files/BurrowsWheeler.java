/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;
import edu.princeton.cs.algs4.Stack;

import java.util.ArrayList;

public class BurrowsWheeler {
    // apply Burrows-Wheeler transform,
    // reading from standard input and writing to standard output
    public static void transform() {
        // Create string of chars
        StringBuilder s = new StringBuilder();
        while (!BinaryStdIn.isEmpty()) {
            s.append(BinaryStdIn.readString());
        }
        String st = s.toString();

        int first = -1;

        CircularSuffixArray csa = new CircularSuffixArray(st);
        
        for (int i = 0; i < csa.length(); i++)
            if (csa.index(i) == 0) {
                first = i;
                BinaryStdOut.write(first);
                break;
            }

        if (first < 0) throw new RuntimeException("first not updated in loop");

        for (int i = 0; i < csa.length(); i++) {
            int ia = csa.index(i);
            if (ia == 0) BinaryStdOut.write(st.charAt(st.length() - 1));
            else BinaryStdOut.write(st.charAt(ia - 1));
        }

        BinaryStdOut.close();
    }

    // apply Burrows-Wheeler inverse transform,
    // reading from standard input and writing to standard output
    public static void inverseTransform() {
        Stack<Character> revDecomp;
        int[] counts, next;

        int first = BinaryStdIn.readInt();

        ArrayList<Character> s = new ArrayList<>();
        while (!BinaryStdIn.isEmpty()) {
            s.add(BinaryStdIn.readChar());
        }

        Character[] t = s.toArray(new Character[s.size()]);

        next = new int[t.length];

        revDecomp = new Stack<>();
        counts = keyIndexedCount(t);

        int counter;

        for (int i = 0; i < t.length; i++) {
            counter = counts[t[i]]++;
            next[i] = counter;
        }

        int pointer = first;
        for (int i = 0; i < t.length; i++) {
            revDecomp.push(t[pointer]);
            pointer = next[pointer];
        }

        for (Character c : revDecomp) {
            BinaryStdOut.write(c);
        }
        BinaryStdOut.close();
    }

    private static int[] keyIndexedCount(Character[] t) {
        int r = 256, n = t.length;
        int[] count = new int[r + 1];

        for (int i = 0; i < n; i++) count[t[i] + 1]++;
        for (int i = 0; i < r; i++) count[i + 1] += count[i];

        return count;
    }

    // if args[0] is "-", apply Burrows-Wheeler transform
    // if args[0] is "+", apply Burrows-Wheeler inverse transform
    public static void main(String[] args) {
        if (args[0] == null) throw new IllegalArgumentException();

        if (args[0].equals("-")) transform();
        if (args[0].equals("+")) inverseTransform();
    }
}
