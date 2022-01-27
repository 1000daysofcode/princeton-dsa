/* *****************************************************************************
 *  Name:              Ada Lovelace
 *  Coursera User ID:  123456
 *  Last modified:     October 16, 1842
 **************************************************************************** */

import java.util.ArrayList;

public class HTSC {
    private ArrayList<HashNode> buckets;
    private int size, cap;

    public HTSC() {
        size = 0;
        cap = 16;
        buckets = new ArrayList<>();
        for (int i = 0; i < cap; i++) buckets.add(null);
    }

    public String get(int key) {
        int hashedKey = hash(key);
        if (buckets.get(hashedKey) == null) return "null spot";
        if (buckets.get(hashedKey) != null) {
            if (buckets.get(hashedKey).key() == key) return buckets.get(hashedKey).value();
            else if (buckets.get(hashedKey).next != null) {
                HashNode pointer = buckets.get(hashedKey).next;
                while (pointer != null) {
                    if (pointer.key() == key) return pointer.value();
                    pointer = pointer.next;
                }
            }
        }
        return "null spot";
    }

    public void add(int key, String value) {
        if (mustResize()) resize();
        int hashedKey = hash(key);
        System.out.println("Key for " + key + " is " + hashedKey);
        size++;
        if (buckets.get(hashedKey) == null) buckets.add((hashedKey), new HashNode(key, value));
            // Walk until null for separate chaining
        else {
            HashNode pointer = buckets.get(hashedKey);
            while (pointer.next != null) pointer = pointer.next;
            pointer.next = new HashNode(key, value);
        }
    }

    public void remove(int key) {
        int hashedKey = hash(key);
        if (buckets.get(hashedKey) == null) return;
        if (buckets.get(hashedKey) != null) {
            if (buckets.get(hashedKey).key() == key) {
                buckets.set(hashedKey, null);
                size--;
            }
            else if (buckets.get(hashedKey).next != null) {
                HashNode previous = buckets.get(hashedKey);
                HashNode pointer = buckets.get(hashedKey).next;
                while (pointer != null) {
                    if (pointer.key() == key) {
                        previous.next = pointer.next;
                        // pointer = null;
                        size--;
                        break;
                    }
                    previous = pointer;
                    pointer = pointer.next;
                }
            }
        }
    }

    private int hash(int key) {
        return (int) (((double) key * 3.14) % cap);
    }

    private void resize() {
        cap *= 2;
        ArrayList<HashNode> temp = new ArrayList<>();
        for (int i = 0; i < cap; i++) temp.add(null);
        for (HashNode each : buckets) if (each != null) temp.set(hash(each.key()), each);
        buckets = temp;
    }


    private boolean mustResize() {
        // if (isEmpty()) return false;
        double num = (double) size / cap;
        if (num > 0.5) return true;
        return false;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    private class HashNode {
        private int k;
        private String v;
        private HashNode next = null;

        public HashNode(int key, String value) {
            k = key;
            v = value;
        }

        public int key() {
            return k;
        }

        public String value() {
            return v;
        }

    }

    public static void main(String[] args) {
        HTSC ht = new HTSC();

        System.out.println("Is the hashtable empty? " + ht.isEmpty());

        ht.add(12, "Good");
        ht.add(38, "Luck");
        ht.add(79, "With");
        ht.add(95, "This");
        ht.add(192, "Boyo");

        System.out.println("Print for key 12 " + ht.get(12));
        System.out.println("Print for key 38 " + ht.get(38));
        System.out.println("Print for key 79 " + ht.get(79));
        System.out.println("Print for key 95 " + ht.get(95));
        System.out.println("Print for key 192 " + ht.get(192));

        System.out.println("Remove key 192");
        ht.remove(192);

        System.out.println("Print for key 192 " + ht.get(192));

        System.out.print("Is the hashtable empty? " + ht.isEmpty());
        System.out.print(" | What is the hashtable size? " + ht.size() + "\n");
    }
}
