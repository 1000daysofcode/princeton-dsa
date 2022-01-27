/* *****************************************************************************
 *  Name:              Ada Lovelace
 *  Coursera User ID:  123456
 *  Last modified:     October 16, 1842
 **************************************************************************** */

import java.util.HashMap;
import java.util.Map;

public class LRUC {
    private int cap;
    private Node head;
    private Node tail;
    private Map<Integer, Node> nodes;

    public LRUC(int cap) {
        this.cap = cap;
        head = new Node();
        tail = new Node();
        nodes = new HashMap<>(cap);
    }

    public int get(int key) {
        int value = -1;
        if (nodes.containsKey(key)) {
            value = nodes.get(key).value;
            remove(key);
            add(key, value);
        }
        return value;
    }

    public void put(int key, int value) {
        if (nodes.containsKey(key)) {
            remove(key);
            add(key, value);
        }
        add(key, value);
        if (nodes.size() == cap) remove(tail.prev.key);
    }

    private void add(int key, int value) {
        Node newFirst = new Node();
        newFirst.next = head.next;
        newFirst.prev = head;
        newFirst.key = key;
        newFirst.value = value;
        head.next = newFirst;
    }

    private void remove(int key) {
        Node pointer = head.next;
        while (pointer.key != key) pointer = pointer.next;
        pointer.prev = pointer.next;
        nodes.remove(key);
    }

    private class Node {
        private int key;
        private int value;
        private Node next;
        private Node prev;
    }

    public static void main(String[] args) {

    }
}
