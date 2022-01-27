/* *****************************************************************************
 *  Name:              Ada Lovelace
 *  Coursera User ID:  123456
 *  Last modified:     October 16, 1842
 **************************************************************************** */

public class PT {
    private static final int M = 26;
    private Node root;
    private int depth;

    public PT() {
        root = null;
        depth = 0;
    }

    public PT(String word) {
        insert(word);
    }

    public void insert(String word) {
        if (root == null) root = new Node(false);
        Node pointer = root;
        int len = word.length();
        if (len > depth) depth = len;
        for (int i = 0, j = len; i < len; i++, j--) {
            if (i == len - 1) {
                pointer.isWord = true;
                pointer.l = word.charAt(len - 1);
            }
            if (j > 1) if (pointer.children[r(word.charAt(i))] == null) {
                if (j == 2) {
                    pointer.children[r(word.charAt(i))] = new Node(true);
                    pointer = pointer.children[r(word.charAt(i))];
                }
                else {
                    pointer.children[r(word.charAt(i))] = new Node(false);
                    pointer.l = word.charAt(i);
                    pointer = pointer.children[r(word.charAt(i))];
                }
            }
            else pointer = pointer.children[r(word.charAt(i))];
        }
    }

    public void remove(String word) {
        if (!contains(word)) throw new IllegalArgumentException();
        Node pointer = root;
        int len = word.length();
        for (int i = 0, j = len; i < len; i++, j--) {
            // if more rem and index not null
            if (j == 2) {
                pointer.children[r(word.charAt(i))].isWord = false;
                break;
            }
            pointer = pointer.children[r(word.charAt(i))];
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
        private char l;

        public Node(boolean endOfWord) {
            if (endOfWord) isWord = true;
            else isWord = false;
        }

        public boolean isWord() {
            return isWord;
        }
    }

    private int depth() {
        return depth;
    }

    private int r(int n) {
        return n - 65;
    }

    public static void main(String[] args) {
        System.out.println("Add word 'WORD' to trie");

        PT trie = new PT("WORD");
        trie.insert("WORLD");
        trie.insert("THAO");

        System.out.println("Does PT contain 'WORD?' " + trie.contains("WORD"));
        System.out.println("Does PT contain 'CHUCK?' " + trie.contains("CHUCK"));
        System.out.println("Does PT contain 'WORLD?' " + trie.contains("WORLD"));
        System.out.println("Does PT contain 'BILL?' " + trie.contains("BILL"));
        System.out.println("Does PT contain 'THAO?' " + trie.contains("THAO"));

        System.out.println("Remove 'WORD'");
        trie.remove("WORD");
        System.out.println("Does PT contain 'WORD?' " + trie.contains("WORD"));
        System.out.println("Does PT contain 'WORLD?' " + trie.contains("WORLD"));
    }
}
