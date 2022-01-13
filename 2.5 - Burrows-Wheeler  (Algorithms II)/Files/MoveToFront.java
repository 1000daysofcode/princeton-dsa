/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

public class MoveToFront {
    private static final int M = 256;

    // apply move-to-front encoding, reading from standard input and writing to standard output
    public static void encode() {
        // Using an array of primitive chars, create ordered char array
        char[] ascii = new char[M];
        for (int i = 0; i < ascii.length; i++) ascii[i] = (char) i;

        // Read binary data from standard input stream
        while (!BinaryStdIn.isEmpty()) {
            char c = BinaryStdIn.readChar();
            int cI = -1; // Char's index

            for (int i = 0; i < M; i++) if (ascii[i] == c) cI = i; // Find char's index
            for (int i = cI; i > 0; i--) ascii[i] = ascii[i - 1]; // Replace relevant chars

            ascii[0] = c; // Move char to front of array

            BinaryStdOut.write(cI, 8); // Write 8 significant bits of index
        }
        BinaryStdOut.close();
    }

    // apply move-to-front decoding, reading from standard input and writing to standard output
    public static void decode() {
        // Using an array of primitive chars, create ordered char array
        char[] ascii = new char[M];
        for (int i = 0; i < ascii.length; i++) ascii[i] = (char) i;

        // Read binary data from standard input stream
        while (!BinaryStdIn.isEmpty()) {
            int cI = BinaryStdIn.readChar(); // Char index from input stream
            char c = ascii[cI]; // Char is current location in array of cI

            BinaryStdOut.write(c, 8); // Write 8 significant bits of char

            for (int i = cI; i >= 1; i--) ascii[i] = ascii[i - 1]; // Replace relevant chars
            ascii[0] = c;
        }
        BinaryStdOut.close();
    }

    // if args[0] is "-", apply move-to-front encoding
    // if args[0] is "+", apply move-to-front decoding
    public static void main(String[] args) {
        if (args[0].equals("-")) encode();
        else if (args[0].equals("+")) decode();
        else throw new IllegalArgumentException();
    }
}
