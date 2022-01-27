/* *****************************************************************************
 *  Name:              Ada Lovelace
 *  Coursera User ID:  123456
 *  Last modified:     October 16, 1842
 **************************************************************************** */

public class KP {
    private int cap;
    private int[][] dp;
    private int[] w, v;

    public KP(int capacity, int[] weights, int[] values) {
        cap = capacity;
        w = weights;
        v = values;
    }

    public void buildKP() {
        dp = new int[w.length + 1][cap + 1];

        // Nested for loop to add in weights

        for (int i = 1; i <= w.length; i++) {
            for (int j = 1; j <= cap; j++) {
                dp[i][j] = dp[i - 1][j]; // Above item
                int aJ = j - w[i - 1]; // Diagonal item
                if (j >= w[i - 1] && dp[i - 1][aJ] + w[i - 1] > dp[i][j]) {
                    dp[i][j] = dp[i - 1][aJ] + w[i - 1];
                }
            }
        }

        DLL stack = new DLL();

        int j = cap, totalVal = 0;

        // Nested for loop to get contained items
        for (int i = w.length; i > 0; i--) {
            if (dp[i][j] != dp[i - 1][j]) {
                stack.putFirst(i - 1);
                totalVal += v[i - 1];
                j -= w[i - 1];
            }
        }

        System.out.println("The maximum weight the knapsack can hold is: " + dp[w.length][cap]);
        System.out.print("The items the knapsack can hold are:");
        for (int each : stack.toArray()) System.out.print(" " + each);

        System.out.print(" for a value of " + totalVal);
        System.out.println();
    }

    public static void main(String[] args) {
        int[] w = { 3, 1, 3, 4, 2 };
        int[] v = { 2, 2, 4, 5, 3 };

        KP kp = new KP(7, w, v);
        kp.buildKP();
    }
}
