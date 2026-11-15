import java.util.*;

/**
 * LC 1027 Longest arithmetic subsequence: brute fix first two elements and extend O(n^3) | dp[i] = map diff -> length O(n^2)
 * Longest bitonic subsequence (GFG): LIS ending at i (left) + LDS starting at i (right) - 1. O(n^2)
 * LC 97   Interleaving string: recursion on (i, j) O(2^(m+n)) -> dp[i][j] O(mn) -> 1 row O(n)
 * LC 1092 Shortest common supersequence: build the LCS table, walk back taking matched chars once. O(mn)
 * LC 329  Longest increasing path in a matrix: brute DFS from each cell (exponential) | DFS + memo (matrix is a DAG) O(RC)
 * LC 174  Dungeon game: forward DP can't decide (needs both min health and current health); go backwards:
 *         need[r][c] = max(1, min(need[r+1][c], need[r][c+1]) - d[r][c]). O(RC)
 * LC 403  Frog jump: brute recursion (pos, k) | memo on (stone, k) / map stone -> set of jump sizes. O(n^2)
 * Boolean parenthesization (GFG): count ways an expression of T/F with & | ^ evaluates to true.
 *         recursion over split operator returning (true, false) counts -> interval DP. O(n^3)
 * LC 2035 Partition array into two arrays (n each) minimising |sum1 - sum2|:
 *         brute all C(2n, n) choices | meet in the middle: subset sums of each half grouped by size, binary search. O(2^n * n)
 */
public class DPProblemsIII {

    static int longestArithBrute(int[] a) {
        int n = a.length, best = Math.min(n, 2);
        for (int i = 0; i < n; i++) for (int j = i + 1; j < n; j++) {
            int d = a[j] - a[i], len = 2, last = a[j];
            for (int k = j + 1; k < n; k++) if (a[k] - last == d) { len++; last = a[k]; }
            best = Math.max(best, len);
        }
        return best;
    }

    static int longestArithSeqLength(int[] a) {
        int n = a.length, best = 2;
        List<Map<Integer, Integer>> dp = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            Map<Integer, Integer> m = new HashMap<>();
            for (int j = 0; j < i; j++) {
                int d = a[i] - a[j];
                int len = dp.get(j).getOrDefault(d, 1) + 1;
                m.put(d, Math.max(m.getOrDefault(d, 0), len));
                best = Math.max(best, len);
            }
            dp.add(m);
        }
        return best;
    }

    static int longestBitonic(int[] a) {
        int n = a.length;
        int[] inc = new int[n], dec = new int[n];
        for (int i = 0; i < n; i++) { inc[i] = 1; for (int j = 0; j < i; j++) if (a[j] < a[i]) inc[i] = Math.max(inc[i], inc[j] + 1); }
        for (int i = n - 1; i >= 0; i--) { dec[i] = 1; for (int j = n - 1; j > i; j--) if (a[j] < a[i]) dec[i] = Math.max(dec[i], dec[j] + 1); }
        int best = 0;
        for (int i = 0; i < n; i++) best = Math.max(best, inc[i] + dec[i] - 1);
        return best;
    }

    static boolean isInterleaveRec(String a, String b, String c, int i, int j) {
        if (i + j == c.length()) return i == a.length() && j == b.length();
        char x = c.charAt(i + j);
        return (i < a.length() && a.charAt(i) == x && isInterleaveRec(a, b, c, i + 1, j))
                || (j < b.length() && b.charAt(j) == x && isInterleaveRec(a, b, c, i, j + 1));
    }

    static boolean isInterleave(String a, String b, String c) {
        int m = a.length(), n = b.length();
        if (m + n != c.length()) return false;
        boolean[] dp = new boolean[n + 1];
        for (int i = 0; i <= m; i++)
            for (int j = 0; j <= n; j++) {
                if (i == 0 && j == 0) dp[j] = true;
                else dp[j] = (i > 0 && dp[j] && a.charAt(i - 1) == c.charAt(i + j - 1))
                        || (j > 0 && dp[j - 1] && b.charAt(j - 1) == c.charAt(i + j - 1));
            }
        return dp[n];
    }

    static String shortestCommonSupersequence(String a, String b) {
        int m = a.length(), n = b.length();
        int[][] dp = new int[m + 1][n + 1];
        for (int i = 1; i <= m; i++) for (int j = 1; j <= n; j++)
            dp[i][j] = a.charAt(i - 1) == b.charAt(j - 1) ? dp[i - 1][j - 1] + 1 : Math.max(dp[i - 1][j], dp[i][j - 1]);
        StringBuilder sb = new StringBuilder();
        int i = m, j = n;
        while (i > 0 && j > 0) {
            if (a.charAt(i - 1) == b.charAt(j - 1)) { sb.append(a.charAt(i - 1)); i--; j--; }
            else if (dp[i - 1][j] >= dp[i][j - 1]) sb.append(a.charAt(--i));
            else sb.append(b.charAt(--j));
        }
        while (i > 0) sb.append(a.charAt(--i));
        while (j > 0) sb.append(b.charAt(--j));
        return sb.reverse().toString();
    }

    static boolean isSupersequence(String s, String t) {   // is t a subsequence of s?
        int j = 0;
        for (int i = 0; i < s.length() && j < t.length(); i++) if (s.charAt(i) == t.charAt(j)) j++;
        return j == t.length();
    }

    static final int[][] D4 = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};

    static int longestIncreasingPathBrute(int[][] m) {
        int best = 0;
        for (int r = 0; r < m.length; r++) for (int c = 0; c < m[0].length; c++) best = Math.max(best, lip(m, r, c, null));
        return best;
    }

    static int longestIncreasingPath(int[][] m) {
        int[][] memo = new int[m.length][m[0].length];
        int best = 0;
        for (int r = 0; r < m.length; r++) for (int c = 0; c < m[0].length; c++) best = Math.max(best, lip(m, r, c, memo));
        return best;
    }

    static int lip(int[][] m, int r, int c, int[][] memo) {
        if (memo != null && memo[r][c] != 0) return memo[r][c];
        int best = 1;
        for (int[] d : D4) {
            int nr = r + d[0], nc = c + d[1];
            if (nr >= 0 && nc >= 0 && nr < m.length && nc < m[0].length && m[nr][nc] > m[r][c]) best = Math.max(best, 1 + lip(m, nr, nc, memo));
        }
        if (memo != null) memo[r][c] = best;
        return best;
    }

    static int calculateMinimumHP(int[][] d) {
        int R = d.length, C = d[0].length;
        int[][] need = new int[R + 1][C + 1];
        for (int[] row : need) Arrays.fill(row, Integer.MAX_VALUE);
        need[R][C - 1] = need[R - 1][C] = 1;        // virtual cells next to the princess
        for (int r = R - 1; r >= 0; r--)
            for (int c = C - 1; c >= 0; c--) need[r][c] = Math.max(1, Math.min(need[r + 1][c], need[r][c + 1]) - d[r][c]);
        return need[0][0];
    }

    static boolean canCrossBrute(int[] stones) {
        Set<Integer> set = new HashSet<>();
        for (int s : stones) set.add(s);
        return jump(stones[0], 0, stones[stones.length - 1], set, null);
    }

    static boolean canCrossMemo(int[] stones) {
        Set<Integer> set = new HashSet<>();
        for (int s : stones) set.add(s);
        return jump(stones[0], 0, stones[stones.length - 1], set, new HashMap<>());
    }

    static boolean jump(int pos, int k, int last, Set<Integer> set, Map<Long, Boolean> memo) {
        if (pos == last) return true;
        long key = (long) pos * 4000 + k;
        if (memo != null && memo.containsKey(key)) return memo.get(key);
        boolean ok = false;
        for (int nk = k - 1; nk <= k + 1 && !ok; nk++)
            if (nk > 0 && set.contains(pos + nk)) ok = jump(pos + nk, nk, last, set, memo);
        if (memo != null) memo.put(key, ok);
        return ok;
    }

    static boolean canCross(int[] stones) {
        Map<Integer, Set<Integer>> jumps = new HashMap<>();
        for (int s : stones) jumps.put(s, new HashSet<>());
        jumps.get(stones[0]).add(0);
        for (int s : stones)
            for (int k : jumps.get(s))
                for (int nk = k - 1; nk <= k + 1; nk++)
                    if (nk > 0 && jumps.containsKey(s + nk)) jumps.get(s + nk).add(nk);
        return !jumps.get(stones[stones.length - 1]).isEmpty();
    }

    static final int MOD = 1003;

    static int booleanParenthesizationRec(String s) { return bp(s, 0, s.length() - 1)[0]; }

    /** returns {ways true, ways false} for s[i..j] (i, j on operands) */
    static int[] bp(String s, int i, int j) {
        if (i == j) return s.charAt(i) == 'T' ? new int[]{1, 0} : new int[]{0, 1};
        int t = 0, f = 0;
        for (int k = i + 1; k < j; k += 2) {
            int[] l = bp(s, i, k - 1), r = bp(s, k + 1, j);
            int[] res = combine(s.charAt(k), l, r);
            t = (t + res[0]) % MOD; f = (f + res[1]) % MOD;
        }
        return new int[]{t, f};
    }

    static int[] combine(char op, int[] l, int[] r) {
        int tt = l[0] * r[0], tf = l[0] * r[1], ft = l[1] * r[0], ff = l[1] * r[1];
        switch (op) {
            case '&': return new int[]{tt % MOD, (tf + ft + ff) % MOD};
            case '|': return new int[]{(tt + tf + ft) % MOD, ff % MOD};
            default:  return new int[]{(tf + ft) % MOD, (tt + ff) % MOD};   // '^'
        }
    }

    static int booleanParenthesization(String s) {
        int n = s.length();
        int[][] T = new int[n][n], F = new int[n][n];
        for (int i = 0; i < n; i += 2) { T[i][i] = s.charAt(i) == 'T' ? 1 : 0; F[i][i] = 1 - T[i][i]; }
        for (int len = 3; len <= n; len += 2)
            for (int i = 0; i + len - 1 < n; i += 2) {
                int j = i + len - 1;
                for (int k = i + 1; k < j; k += 2) {
                    int[] res = combine(s.charAt(k), new int[]{T[i][k - 1], F[i][k - 1]}, new int[]{T[k + 1][j], F[k + 1][j]});
                    T[i][j] = (T[i][j] + res[0]) % MOD; F[i][j] = (F[i][j] + res[1]) % MOD;
                }
            }
        return T[0][n - 1];
    }

    static int minimumDifferenceBrute(int[] a) {
        int m = a.length, n = m / 2, total = Arrays.stream(a).sum(), best = Integer.MAX_VALUE;
        for (int mask = 0; mask < 1 << m; mask++) {
            if (Integer.bitCount(mask) != n) continue;
            int s = 0;
            for (int i = 0; i < m; i++) if ((mask >> i & 1) == 1) s += a[i];
            best = Math.min(best, Math.abs(total - 2 * s));
        }
        return best;
    }

    static int minimumDifference(int[] a) {
        int n = a.length / 2, total = 0;
        for (int x : a) total += x;
        List<List<Integer>> left = sums(a, 0, n), right = sums(a, n, n);
        for (List<Integer> l : right) Collections.sort(l);
        int best = Integer.MAX_VALUE;
        for (int k = 0; k <= n; k++)
            for (int sl : left.get(k)) {
                List<Integer> r = right.get(n - k);
                // want sl + sr as close as possible to total / 2  ->  minimise |total - 2(sl + sr)|
                double want = total / 2.0 - sl;
                int idx = Collections.binarySearch(r, (int) Math.ceil(want));
                if (idx < 0) idx = -idx - 1;
                for (int t = idx - 1; t <= idx; t++)
                    if (t >= 0 && t < r.size()) best = Math.min(best, Math.abs(total - 2 * (sl + r.get(t))));
            }
        return best;
    }

    /** subset sums of a[off .. off+len), grouped by subset size */
    static List<List<Integer>> sums(int[] a, int off, int len) {
        List<List<Integer>> res = new ArrayList<>();
        for (int i = 0; i <= len; i++) res.add(new ArrayList<>());
        for (int mask = 0; mask < 1 << len; mask++) {
            int s = 0;
            for (int i = 0; i < len; i++) if ((mask >> i & 1) == 1) s += a[off + i];
            res.get(Integer.bitCount(mask)).add(s);
        }
        return res;
    }

    public static void main(String[] args) {
        for (int[] t : new int[][]{{3, 6, 9, 12}, {9, 4, 7, 2, 10}, {20, 1, 15, 3, 10, 5, 8}, {83, 20, 17, 43, 52, 78, 68, 45}})
            check(longestArithSeqLength(t), longestArithBrute(t));
        check(longestArithSeqLength(new int[]{20, 1, 15, 3, 10, 5, 8}), 4);
        check(longestBitonic(new int[]{1, 11, 2, 10, 4, 5, 2, 1}), 6); check(longestBitonic(new int[]{12, 11, 40, 5, 3, 1}), 5);
        check(isInterleave("aabcc", "dbbca", "aadbbcbcac"), true); check(isInterleave("aabcc", "dbbca", "aadbbbaccc"), false);
        check(isInterleaveRec("aabcc", "dbbca", "aadbbcbcac", 0, 0), true);
        String scs = shortestCommonSupersequence("abac", "cab");
        check(scs.length(), 5); check(isSupersequence(scs, "abac") && isSupersequence(scs, "cab"), true);
        check(shortestCommonSupersequence("aaaaaaaa", "aaaaaaaa"), "aaaaaaaa");
        int[][] lm = {{9, 9, 4}, {6, 6, 8}, {2, 1, 1}};
        check(longestIncreasingPath(lm), 4); check(longestIncreasingPathBrute(lm), 4); check(longestIncreasingPath(new int[][]{{3, 4, 5}, {3, 2, 6}, {2, 2, 1}}), 4);
        check(calculateMinimumHP(new int[][]{{-2, -3, 3}, {-5, -10, 1}, {10, 30, -5}}), 7); check(calculateMinimumHP(new int[][]{{0}}), 1);
        int[] fr = {0, 1, 3, 5, 6, 8, 12, 17}, fr2 = {0, 1, 2, 3, 4, 8, 9, 11};
        check(canCross(fr), true); check(canCrossMemo(fr), true); check(canCrossBrute(fr), true);
        check(canCross(fr2), false); check(canCrossMemo(fr2), false); check(canCrossBrute(fr2), false);
        check(booleanParenthesization("T|T&F^T"), 4); check(booleanParenthesizationRec("T|T&F^T"), 4);
        check(booleanParenthesization("T^F|F"), 2); check(booleanParenthesizationRec("T^F&T"), 2);
        check(minimumDifference(new int[]{3, 9, 7, 3}), 2); check(minimumDifference(new int[]{-36, 36}), 72);
        check(minimumDifference(new int[]{2, -1, 0, 4, -2, -9}), 0);
        Random rnd = new Random(4);
        for (int t = 0; t < 100; t++) {
            int[] a = new int[2 * (rnd.nextInt(5) + 1)];
            for (int i = 0; i < a.length; i++) a[i] = rnd.nextInt(41) - 20;
            check(minimumDifference(a), minimumDifferenceBrute(a));
        }
        System.out.println("DPProblemsIII: all tests passed");
    }

    static void check(Object got, Object exp) {
        if (!got.equals(exp)) throw new AssertionError("expected " + exp + " but got " + got);
    }
}
