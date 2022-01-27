/* *****************************************************************************
 *  Name:              Ada Lovelace
 *  Coursera User ID:  123456
 *  Last modified:     October 16, 1842
 **************************************************************************** */

public class DumbST {
    private static final int M = 26;
    private Node root;
    private final int n;

    public DumbST(String word) {
        n = word.length();
        // Create all possible suffixes
        for (int i = 0; i < word.length(); i++) insert(word.substring(i));
    }

    public void insert(String word) {
        if (root == null) root = new Node(false);
        Node pointer = root;
        int len = word.length();
        for (int i = 0, j = len; i < len; i++, j--) {
            if (i == len - 1) {
                pointer.isWord = true;
                pointer.index = n - len;
            }
            if (j > 1) if (pointer.children[r(word.charAt(i))] == null) {
                if (j == 2) {
                    pointer.children[r(word.charAt(i))] = new Node(true);
                    pointer = pointer.children[r(word.charAt(i))];
                }
                else {
                    pointer.children[r(word.charAt(i))] = new Node(false);
                    pointer = pointer.children[r(word.charAt(i))];
                }
            }
            else pointer = pointer.children[r(word.charAt(i))];
        }
    }

    public boolean contains(String word) {
        Node pointer = root;
        int len = word.length();
        for (int i = 0, j = len; i < len; i++, j--) {
            // if more rem and index not null
            if (j == 1 && pointer.isWord()) return true;
            if (pointer.children[r(word.charAt(i))] == null) return false;
            pointer = pointer.children[r(word.charAt(i))];
        }
        return false;
    }

    private class Node {
        private Node[] children = new Node[M];
        private boolean isWord;
        private int index;

        public Node(boolean endOfWord) {
            if (endOfWord) isWord = true;
            else isWord = false;
        }

        public boolean isWord() {
            return isWord;
        }
    }

    private int indexInPattern(String word) {
        Node pointer = root;
        int len = word.length();
        for (int i = 0, j = len; i < len; i++, j--) {
            // if more rem and index not null
            if (j == 1 && pointer.isWord()) return pointer.index;
            if (pointer.children[r(word.charAt(i))] == null)
                throw new IllegalArgumentException();
            pointer = pointer.children[r(word.charAt(i))];
        }
        throw new IllegalArgumentException();
    }

    private int r(int num) {
        return num - 65;
    }

    public static void main(String[] args) {
        System.out.println("Add word 'BANANARAMA' to trie");

        DumbST dumbST = new DumbST("BANANARAMA");

        System.out.println("Is 'RAMA' in trie? " + dumbST.contains("RAMA")
                                   + " | Index is: " + dumbST.indexInPattern("RAMA"));
        System.out.println("Is 'GOOB' in trie? " + dumbST.contains("GOOB"));
        System.out.println("Is 'ARAMA' in trie? " + dumbST.contains("ARAMA")
                                   + " | Index is: " + dumbST.indexInPattern("ARAMA"));
        System.out.println("Is 'BARAMA' in trie? " + dumbST.contains("BARAMA"));
        System.out.println("Is 'BANANARAMA' in trie? " + dumbST.contains("BANANARAMA")
                                   + " | Index is: " + dumbST.indexInPattern("BANANARAMA"));
        System.out.println("Is 'A' in trie? " + dumbST.contains("A")
                                   + " | Index is: " + dumbST.indexInPattern("A"));
    }
}
