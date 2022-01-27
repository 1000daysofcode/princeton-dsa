/* *****************************************************************************
 *  Name:              Ada Lovelace
 *  Coursera User ID:  123456
 *  Last modified:     October 16, 1842
 **************************************************************************** */

import java.util.ArrayList;

public class DLL {
    private Node head;
    private int count = 0;

    public DLL() {
        head = null;
    }

    public DLL(int value) {
        head = new Node(null, value);
        count++;
    }

    private class Node {
        private Node next = null;
        private Node prev;
        private int val;

        public Node(Node previous, int value) {
            prev = previous;
            val = value;
        }

        public Node(Node previous, Node nxt, int value) {
            prev = previous;
            next = nxt;
            val = value;
        }

        private int value() {
            return val;
        }

        private Node p() {
            return prev;
        }

        private Node n() {
            return next;
        }
    }

    public Node get(int location) {
        if (location > size() - 1 || location < 0)
            throw new IllegalArgumentException("location out of bounds");

        Node pointer = head;

        for (int i = location; i > 0; i--) pointer = pointer.next;

        return pointer;
    }


    public void putFirst(int value) {
        if (head == null) {
            head = new Node(null, value);
            count++;
        }
        else {
            Node temp = head;
            head = new Node(null, temp, value);
            count++;
        }
    }

    public void putLast(int value) {
        if (head == null) {
            head = new Node(null, value);
            count++;
        }
        else head.next = putAtEnd(null, head, value);
    }

    private Node putAtEnd(Node previous, Node current, int value) {
        if (current.next == null) {
            Node finalNode = new Node(current, value);
            count++;
            return current = new Node(current.prev, finalNode, current.value());
        }
        current = putAtEnd(current, current.next, value);
        return current;
    }

    public int remFirst() {
        if (head.next == null) {
            Node temp = head;
            head = null;
            return temp.value();
        }
        Node temp = head;
        head = new Node(null, head.next.next, head.next.value());
        count--;
        return temp.value();
    }

    public int remLast() {
        if (head.next == null) {
            Node temp = head;
            head = null;
            count--;
            return temp.value();
        }
        Node pointer = head, previous = head;
        while (pointer.next.next != null) {
            previous = pointer;
            pointer = pointer.next;
        }
        Node temp = pointer.next;
        previous.next = new Node(pointer.prev, null, pointer.value());
        count--;
        return temp.value();
    }

    public Iterable<Integer> toArray() {
        ArrayList<Integer> llToArray = new ArrayList<>();
        Node pointer = head;
        while (pointer != null) {
            llToArray.add(pointer.value());
            pointer = pointer.n();
        }
        return llToArray;
    }

    public int size() {
        return count;
    }

    public boolean isEmpty() {
        return count == 0;
    }

    public static void main(String[] args) {
        DLL dll = new DLL();

        System.out.println("Is DLL empty? " + dll.isEmpty());

        dll.putFirst(1);
        dll.putLast(2);
        dll.putLast(3);
        dll.putFirst(4);
        dll.putFirst(5);

        System.out.println("The value of element 2 is " + dll.get(2).value());
        System.out.println("Current size is " + dll.size());

        for (int each : dll.toArray()) System.out.println(each);

        System.out.println("\nRemoving 3\n");

        for (int i = 0; i < 3; i++) System.out.println(dll.remFirst());

        System.out.println("\nRemoved 3\n");

        for (int each : dll.toArray()) System.out.println(each);

        System.out.println("\nRemoving last\n");
        System.out.println(dll.remLast());
        System.out.println("Current size is " + dll.size());
        System.out.println("Is DLL empty? " + dll.isEmpty());

        System.out.println("\nRemoving last\n");
        System.out.println(dll.remLast());
        System.out.println("Current size is " + dll.size());
        System.out.println("Is DLL empty? " + dll.isEmpty());
    }
}
