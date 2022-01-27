/* *****************************************************************************
 *  Name:              Ada Lovelace
 *  Coursera User ID:  123456
 *  Last modified:     October 16, 1842
 **************************************************************************** */

public class BS {
    private int[] ranArr;
    private int[] ordArr;
    private int s;

    public BS(int size) {
        s = size;
        ranArr = new int[s];
        ordArr = new int[1000000];
        for (int i = 0; i < s; i++) {
            ranArr[i] = (int) (Math.random() * s);
            ordArr[i] = i;
        }
        for (int i = s; i < 1000000; i++) {
            ordArr[i] = i;
        }
    }

    public boolean contains(boolean recursive, int value) {
        int[] defRange = { 0, 1000000 };
        if (recursive) {
            return binSearch(defRange, value);
        }
        return binSearchIt(defRange, value);
    }

    private boolean binSearchIt(int[] index, int value) {
        int checker, l = index[0], r = index[1];
        while (l <= r) {
            checker = (l + r) / 2;
            if (ordArr[checker] == value) return true;
            if (value < ordArr[checker]) r = checker - 1;
            else l = checker + 1;
            if (l > r) break;
        }
        return false;
    }

    private boolean binSearch(int[] index, int value) {
        int checker = (index[1] + index[0]) / 2;
        if (index[0] > index[1]) return false;
        if (ordArr[checker] == value) return true;
        if (value < ordArr[checker]) {
            index[1] = checker - 1;
            return binSearch(index, value);
        }
        else {
            index[0] = checker + 1;
            return binSearch(index, value);
        }
    }

    public int[] ran() {
        return ranArr;
    }

    public int[] ord() {
        return ordArr;
    }

    public static void main(String[] args) {
        BSIt arr = new BSIt(250);
        int[] r = arr.ran();
        int[] o = arr.ord();
        // for (int i = 0; i < 250; i++) System.out.print(r[i] + " ");
        // System.out.println("\n");
        System.out.println("Does array contain 250? (rec) " + arr.contains(true, 250));
        System.out.println("Does array contain 200? (it) " + arr.contains(false, 200));
        System.out.println("Does array contain 19854? (rec) " + arr.contains(true, 19854));
        System.out.println("Does array contain 100? (rec) " + arr.contains(true, 100));
        System.out.println("Does array contain 439494? (it) " + arr.contains(false, 439494));
        System.out.println("Does array contain -50? (it) " + arr.contains(false, -50));
        System.out.println("Does array contain -34549? (rec) " + arr.contains(true, -34549));
    }
}
