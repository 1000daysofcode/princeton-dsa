/* *****************************************************************************
 *  Name:              Ada Lovelace
 *  Coursera User ID:  123456
 *  Last modified:     October 16, 1842
 **************************************************************************** */

public class MSort {
    private final char[] ranArr, ordArr;

    public MSort(int s) {
        ranArr = new char[s];
        ordArr = new char[s];

        for (int i = 0; i < s; i++) {
            int ran = (int) (Math.random() * s);
            ranArr[i] = (char) ran;
            ordArr[i] = (char) ran;
        }
        mergesort(0, s - 1);
    }

    private void mergesort(int l, int r) {
        if (!(l < r)) return;

        int mid = (l + r) / 2;

        mergesort(l, mid);
        mergesort(mid + 1, r);

        merge(l, mid, r);
    }

    private void merge(int l, int m, int r) {
        char[] left = new char[m - l + 1];
        char[] right = new char[r - m];

        for (int i = 0; i < left.length; i++) left[i] = ordArr[l + i];
        for (int i = 0; i < right.length; i++) right[i] = ordArr[m + 1 + i];

        int lP = 0, rP = 0, tP = l;

        while (lP < left.length && rP < right.length) {
            if (left[lP] <= right[rP]) ordArr[tP++] = left[lP++];
            else ordArr[tP++] = right[rP++];
        }

        while (lP < left.length) ordArr[tP++] = left[lP++];
        while (rP < right.length) ordArr[tP++] = right[rP++];
    }

    public char[] uA() {
        return ranArr;
    }

    public char[] oA() {
        return ordArr;
    }

    public static void main(String[] args) {
        int n = 255;
        MSort msObject = new MSort(n);
        char[] rA = msObject.uA(), oA = msObject.oA();

        System.out.println("Starting array:");
        for (int i = 0; i < n; i++) System.out.print(rA[i] + " ");
        System.out.println("\n");
        System.out.println("Ordered array:");
        for (int i = 0; i < n; i++) System.out.print(oA[i] + " ");
        System.out.println();
    }
}
