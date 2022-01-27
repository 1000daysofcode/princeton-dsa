/* *****************************************************************************
 *  Name:              Ada Lovelace
 *  Coursera User ID:  123456
 *  Last modified:     October 16, 1842
 **************************************************************************** */

public class LCS {
    private final int c;
    private final String lcs;

    public LCS(String s1, String s2) {
        int s1Len = s1.length(), s2Len = s2.length();

        int[][] dp = new int[s1Len + 1][s2Len + 1];

        for (int i = s1Len - 1; i >= 0; i--) {
            for (int j = s2Len - 1; j >= 0; j--) {
                if (s1.charAt(i) == s2.charAt(j)) {
                    dp[i][j] = 1 + dp[i + 1][j + 1];
                }
                else {
                    dp[i][j] = Math.max(dp[i + 1][j], dp[i][j + 1]);
                }
            }
        }

        c = dp[0][0];

        char[] ca = new char[c];

        int ci = 0, i = s1Len - 1, j = s2Len - 1;

        while (i >= 0 && j >= 0) {
            if (s1.charAt(i) == s2.charAt(j)) {
                ca[c - ci - 1] = s1.charAt(i);
                // System.out.print(s1.charAt(i));
                i--;
                j--;
                ci++;
            }
            else {
                boolean goUp = dp[i - 1][j] > dp[i][j - 1];
                if (goUp) i--;
                else j--; // go left
            }
        }

        StringBuilder s = new StringBuilder();
        for (int k = 0; k < ca.length; k++) s.append(ca[k]);
        lcs = s.toString();

    }

    public int len() {
        return c;
    }

    public String string() {
        return lcs;
    }

    public static void main(String[] args) {
        LCS lcs = new LCS(args[0], args[1]);
        System.out.println("The LCS is \"" + lcs.string() + "\".");
        System.out.println("LCS of these strings is: " + lcs.len() + " letters long.\n");
    }
}
