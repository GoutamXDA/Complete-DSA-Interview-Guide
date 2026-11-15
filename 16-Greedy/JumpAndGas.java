import java.util.*;

/**
 * LC 55 Jump game: DP O(n^2) | greedy farthest-reach O(n)
 * LC 45 Jump game II: DP O(n^2) | greedy levels: [l, r] window of positions reachable in `jumps` jumps. O(n)
 * LC 134 Gas station: brute try each start O(n^2) | if total >= 0 an answer exists; restart after any negative prefix. O(n)
 * LC 763 Partition labels: last index of each char; cut when i == farthest last index.
 */
public class JumpAndGas {

    static boolean canJumpDP(int[] a) {
        boolean[] ok = new boolean[a.length]; ok[0] = true;
        for (int i = 0; i < a.length; i++) {
            if (!ok[i]) continue;
            for (int j = 1; j <= a[i] && i + j < a.length; j++) ok[i + j] = true;
        }
        return ok[a.length - 1];
    }

    static boolean canJump(int[] a) {
        int far = 0;
        for (int i = 0; i < a.length; i++) {
            if (i > far) return false;
            far = Math.max(far, i + a[i]);
        }
        return true;
    }

    static int jumpDP(int[] a) {
        int n = a.length; int[] dp = new int[n];
        Arrays.fill(dp, Integer.MAX_VALUE); dp[0] = 0;
        for (int i = 0; i < n; i++) {
            if (dp[i] == Integer.MAX_VALUE) continue;
            for (int j = 1; j <= a[i] && i + j < n; j++) dp[i + j] = Math.min(dp[i + j], dp[i] + 1);
        }
        return dp[n - 1];
    }

    static int jump(int[] a) {
        int jumps = 0, curEnd = 0, far = 0;
        for (int i = 0; i < a.length - 1; i++) {
            far = Math.max(far, i + a[i]);
            if (i == curEnd) { jumps++; curEnd = far; }   // must jump to go beyond this window
        }
        return jumps;
    }

    static int canCompleteCircuitBrute(int[] gas, int[] cost) {
        int n = gas.length;
        for (int s = 0; s < n; s++) {
            int tank = 0, k = 0;
            for (; k < n; k++) { tank += gas[(s + k) % n] - cost[(s + k) % n]; if (tank < 0) break; }
            if (k == n) return s;
        }
        return -1;
    }

    static int canCompleteCircuit(int[] gas, int[] cost) {
        int total = 0, tank = 0, start = 0;
        for (int i = 0; i < gas.length; i++) {
            int d = gas[i] - cost[i];
            total += d; tank += d;
            if (tank < 0) { start = i + 1; tank = 0; }   // no start in [old start, i] can work
        }
        return total >= 0 ? start : -1;
    }

    static List<Integer> partitionLabels(String s) {
        int[] last = new int[26];
        for (int i = 0; i < s.length(); i++) last[s.charAt(i) - 'a'] = i;
        List<Integer> res = new ArrayList<>();
        int start = 0, end = 0;
        for (int i = 0; i < s.length(); i++) {
            end = Math.max(end, last[s.charAt(i) - 'a']);
            if (i == end) { res.add(end - start + 1); start = i + 1; }
        }
        return res;
    }

    public static void main(String[] args) {
        check(canJump(new int[]{2, 3, 1, 1, 4}), true); check(canJump(new int[]{3, 2, 1, 0, 4}), false);
        check(canJumpDP(new int[]{3, 2, 1, 0, 4}), false);
        check(jump(new int[]{2, 3, 1, 1, 4}), 2); check(jumpDP(new int[]{2, 3, 0, 1, 4}), 2); check(jump(new int[]{0}), 0);
        int[] gas = {1, 2, 3, 4, 5}, cost = {3, 4, 5, 1, 2};
        check(canCompleteCircuit(gas, cost), 3); check(canCompleteCircuitBrute(gas, cost), 3);
        check(canCompleteCircuit(new int[]{2, 3, 4}, new int[]{3, 4, 3}), -1);
        check(partitionLabels("ababcbacadefegdehijhklij"), List.of(9, 7, 8));
        System.out.println("JumpAndGas: all tests passed");
    }

    static void check(Object got, Object exp) {
        if (!got.equals(exp)) throw new AssertionError("expected " + exp + " but got " + got);
    }
}
