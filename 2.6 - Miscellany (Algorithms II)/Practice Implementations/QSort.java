/* *****************************************************************************
 *  Name:              Ada Lovelace
 *  Coursera User ID:  123456
 *  Last modified:     October 16, 1842
 **************************************************************************** */

public class QSort {
    private int[] ranArr, ordArr;

    public QSort(int s) {
        ranArr = new int[s];
        ordArr = new int[s];
        for (int i = 0; i < s; i++) {
            int ran = (int) (Math.random() * s);
            ranArr[i] = ran;
            ordArr[i] = ran;
        }
        quicksort(0, s - 1);
    }

    private void quicksort(int l, int r) {
        if (!(l <= r)) return;

        int p = partition(l, r);
        quicksort(l, p - 1);
        quicksort(p + 1, r);
    }

    private void exchange(int i, int j) {
        int temp = ordArr[i];
        ordArr[i] = ordArr[j];
        ordArr[j] = temp;
    }

    private int partition(int l, int r) {
        int p = l + (int) (Math.random() * (r - l));
        exchange(l, p);
        int x = l + 1;

        for (int i = x; i <= r; i++) if (ordArr[i] < ordArr[l]) exchange(i, x++);

        exchange(l, x - 1);
        return x - 1;
    }

    public int[] oA() {
        return ordArr;
    }

    public int[] uA() {
        return ranArr;
    }

    public static void main(String[] args) {
        QSort qs = new QSort(Integer.parseInt(args[0]));
        int[] uA = qs.uA(), oA = qs.oA();

        System.out.println("Random array:\n");
        for (int i = 0; i < uA.length; i++) System.out.print(uA[i] + " ");

        System.out.println("\nOrdered array:\n");
        for (int i = 0; i < oA.length; i++) System.out.print(oA[i] + " ");

        System.out.println();
    }
}
