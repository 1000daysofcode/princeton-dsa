/* *****************************************************************************
 *  Name:              Ada Lovelace
 *  Coursera User ID:  123456
 *  Last modified:     October 16, 1842
 **************************************************************************** */

public class DA {
    private int cap;
    private int count;
    private int[] array;

    public DA() {
        cap = 8;
        count = 0;
        array = new int[cap];
    }

    public void push(int data) {
        if ((double) (count + 1) / cap >= 0.75) doubleArray();
        array[count] = data;
        count++;
    }

    public void forcePush(int data, int index) {
        if (index >= cap || (double) (count + 1) / cap >= 0.75) doubleArray();
        array[index] = data;
        count++;
    }

    public void carefulPush(int data, int index) {
        if (index >= cap) {
            doubleArray();
            array[index] = data;
        }
        else {
            if ((double) (count + 1) / cap >= 0.75) doubleArray();
            int temp = array[index];
            array[index] = data;
            for (int i = count; i > index + 1; i--) {
                array[i] = array[i - 1];
            }
            array[index + 1] = temp;
            count++;
        }
    }

    private void doubleArray() {
        cap *= 2;
        int[] temp = new int[cap];
        for (int i = 0; i < count; i++) temp[i] = array[i];
        array = temp;
    }

    public void pop() {
        if ((double) (count - 1) / cap <= 0.25) halveArray();
        array[count - 1] = 0;
        count--;
    }

    private void halveArray() {
        cap /= 2;
        int[] temp = new int[cap];
        for (int i = 0; i < count; i++) temp[i] = array[i];
        array = temp;
    }

    public int get(int index) {
        return array[index];
    }

    public int size() {
        return count;
    }

    public int capacity() {
        return cap;
    }

    public void print() {
        System.out.println();
        for (int i = 0; i < count; i++) System.out.print(array[i] + " ");
        System.out.println();
    }

    public static void main(String[] args) {
        DA da = new DA();

        for (int i = 0; i < 10; i++) da.push(i);

        System.out.println("Untouched array:");
        da.print();

        System.out.println("Array after push:");
        da.carefulPush(69, 3);
        da.forcePush(420, 0);

        System.out.println("Popping 3 items:");
        da.pop();
        da.pop();
        da.pop();

        System.out.println("Array after 3 pops");
        da.print();

        System.out.println("Current size is " + da.size() + ", but capacity is " + da.capacity()
                                   + "\nPopping 8 items");

        for (int i = 0; i < 8; i++) da.pop();

        System.out.println(
                "After popping 8 items, current size is " + da.size() + ", but capacity is " + da
                        .capacity());

        da.print();
    }
}
