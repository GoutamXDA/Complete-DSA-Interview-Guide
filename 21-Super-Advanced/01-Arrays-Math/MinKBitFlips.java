import java.util.*;

/**
 * LC 995 — Minimum Number of K Consecutive Bit Flips. Explanation: MinKBitFlips.md
 * 1. BFS over bitmask states (oracle)   O(2^n * n)
 * 2. Greedy with real flipping          O(n k)
 * 3. Greedy with flip parity + diff     O(n), O(n) diff array
 * 4. Same with in-place marking         O(n), O(1) extra
 */
public class MinKBitFlips {

    static int bfs(int[] a, int k) {
        int n = a.length, start = 0, goal = (1 << n) - 1;
        for (int i = 0; i < n; i++) if (a[i] == 1) start |= 1 << i;
        int[] dist = new int[1 << n];
        Arrays.fill(dist, -1);
        dist[start] = 0;
        Deque<Integer> q = new ArrayDeque<>(List.of(start));
        while (!q.isEmpty()) {
            int s = q.poll();
            if (s == goal) return dist[s];
            for (int i = 0; i + k <= n; i++) {
                int t = s ^ (((1 << k) - 1) << i);
                if (dist[t] == -1) { dist[t] = dist[s] + 1; q.offer(t); }
            }
        }
        return -1;
    }

    static int greedyReal(int[] nums, int k) {
        int[] a = nums.clone();
        int flips = 0;
        for (int i = 0; i < a.length; i++) {
            if (a[i] == 1) continue;
            if (i + k > a.length) return -1;
            for (int j = i; j < i + k; j++) a[j] ^= 1;
            flips++;
        }
        return flips;
    }

    static int greedyDiff(int[] a, int k) {
        int n = a.length, active = 0, flips = 0;
        int[] endAt = new int[n + 1];
        for (int i = 0; i < n; i++) {
            active -= endAt[i];                          // flips that stopped covering i
            if ((a[i] ^ (active & 1)) == 0) {
                if (i + k > n) return -1;
                flips++; active++; endAt[i + k]++;
            }
        }
        return flips;
    }

    static int minKBitFlips(int[] nums, int k) {
        int[] a = nums.clone();
        int n = a.length, active = 0, flips = 0;
        for (int i = 0; i < n; i++) {
            if (i >= k && a[i - k] == 2) active--;       // the flip that started at i-k no longer covers i
            if ((a[i] ^ (active & 1)) == 0) {
                if (i + k > n) return -1;
                a[i] = 2;                                // mark "a flip started here"
                active++; flips++;
            }
        }
        return flips;
    }

    public static void main(String[] args) {
        check(minKBitFlips(new int[]{0, 1, 0}, 1), 2); check(minKBitFlips(new int[]{1, 1, 0}, 2), -1);
        check(minKBitFlips(new int[]{0, 0, 0, 1, 0, 1, 1, 0}, 3), 3);
        Random r = new Random(5);
        for (int t = 0; t < 3000; t++) {
            int n = r.nextInt(12) + 1, k = r.nextInt(n) + 1;
            int[] a = new int[n];
            for (int i = 0; i < n; i++) a[i] = r.nextInt(2);
            int e = bfs(a, k);
            check(greedyReal(a, k), e); check(greedyDiff(a, k), e); check(minKBitFlips(a, k), e);
        }
        System.out.println("MinKBitFlips: all tests passed");
    }

    static void check(Object got, Object exp) {
        if (!got.equals(exp)) throw new AssertionError("expected " + exp + " but got " + got);
    }
}
