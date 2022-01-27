/* *****************************************************************************
 *  Name:              Ada Lovelace
 *  Coursera User ID:  123456
 *  Last modified:     October 16, 1842
 **************************************************************************** */

public class MH {
    private int[] da;
    private int size;
    private int cap;

    public MH() {
        cap = 16;
        size = 0;
        da = new int[cap];
    }

    public MH(int value) {
        cap = 16;
        size = 1;
        da = new int[cap];
        da[0] = value;
    }

    public int getMin() {
        return da[0];
    }

    public int removeMin() {
        if (size - 1 > 16 && needToResize(false)) resize(false);
        int min = da[0];
        int last = da[size - 1];
        size--;
        da[0] = last;
        sink(0);
        // sink(0);
        return min;
    }

    public void insert(int value) {
        if (size + 1 > 16 && needToResize(true)) resize(true);
        da[size++] = value;
        swim(size - 1);
    }

    private void swim(int i) {
        int parent = (i - 1) / 2;
        if (da[i] < da[parent]) {
            exchange(i, parent);
            swim(parent);
        }
    }

    // sink
    private void sink(int i) {
        int child1 = (2 * i) + 1, child2 = (2 * i) + 2;
        int child = da[child1] < da[child2] ? child1 : child2;
        if (da[i] > da[child]) exchange(child, i);
        if ((child + 1) * 2 < size) sink(child);
    }

    private void exchange(int index, int parent) {
        int temp = da[parent];
        da[parent] = da[index];
        da[index] = temp;
    }

    private boolean needToResize(boolean inserting) {
        if (inserting) return (double) (size + 1) / cap >= 0.75;
        return (double) (size - 1) / cap <= 0.25;
    }

    private void resize(boolean growing) {
        if (growing) {
            cap *= 2;
            int[] temp = new int[cap];
            for (int i = 0; i < size; i++) temp[i] = da[i];
            da = temp;
        }
        else {
            cap /= 2;
            int[] temp = new int[cap];
            for (int i = 0; i < size; i++) temp[i] = da[i];
            da = temp;
        }
    }

    public static void main(String[] args) {
        System.out.println("Creating structure (inserting 10)");
        MH mh = new MH(10);
        System.out.println("Inserting integers 1 - 10 into MH");
        for (int i = 0; i < 50; i++) mh.insert(i * 3);
        System.out.println("Removing all integers and printing:");
        for (int i = 0; i < 51; i++) System.out.print(mh.removeMin() + " ");
        System.out.println("\nTest complete.");
    }
}
