/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;

public class CircularSuffixArray {
    private final int n;
    private final String st;
    private final CircularSuffix[] sufArray;

    // circular suffix array of s
    public CircularSuffixArray(String s) {
        if (s == null)
            throw new IllegalArgumentException("null string in CircularSuffixArray constructor");
        st = s;
        n = s.length();

        // Initiate instance variable with length
        sufArray = new CircularSuffix[n];

        // Add suffixes to each index
        for (int i = 0; i < length(); i++) {
            sufArray[i] = new CircularSuffix(i);
        }

        // Sort suffixes using comparable
        Arrays.sort(sufArray);
    }

    private class CircularSuffix implements Comparable<CircularSuffix> {
        private final int indexChar;

        public CircularSuffix(int argInt) {
            indexChar = argInt;
        }

        public int compareTo(CircularSuffix other) {
            for (int i = 0; i < n; i++) {

                // Testing chars for correct value comparison
                // char tC = st.charAt((indexChar + i) % n);
                // char oC = st.charAt((other.indexChar + i) % n);

                if (st.charAt((indexChar + i) % n) > st.charAt((other.indexChar + i) % n)) return 1;
                if (st.charAt((indexChar + i) % n) < st.charAt((other.indexChar + i) % n))
                    return -1;
            }
            return 0;
        }
    }

    // length of s
    public int length() {
        return n;
    }

    // returns index of ith sorted suffix
    public int index(int i) {
        if (i < 0 || i > n - 1)
            throw new IllegalArgumentException("calling index out of String length range");
        return sufArray[i].indexChar;
    }

    // unit testing (required)
    public static void main(String[] args) {
        // Initialize CSA
        CircularSuffixArray csa = new CircularSuffixArray("test!");
        // Test CSA length
        StdOut.println("CSA's String length is: " + csa.length());
        // Test CSA index
        for (int i = 0; i < csa.length(); i++) {
            if (i % 10 == 1)
                StdOut.println("The index of the " + i + "st sorted suffix is " + csa.index(i));
            else if (i % 10 == 2)
                StdOut.println("The index of the " + i + "nd sorted suffix is " + csa.index(i));
            else if (i % 10 == 3)
                StdOut.println("The index of the " + i + "rd sorted suffix is " + csa.index(i));
            else StdOut.println("The index of the " + i + "th sorted suffix is " + csa.index(i));
        }
    }
}
