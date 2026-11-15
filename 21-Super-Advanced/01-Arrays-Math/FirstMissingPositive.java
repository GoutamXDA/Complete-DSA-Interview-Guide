import java.util.*;

/**
 * LC 41 — First Missing Positive. Explanation: FirstMissingPositive.md
 *
 * 1. Sort + scan            O(n log n)  O(1)
 * 2. HashSet                O(n)        O(n)
 * 3. Boolean seen[n+2]      O(n)        O(n)
 * 4. Cyclic sort            O(n)        O(1)   (each swap fixes one value)
 * 5. Sign marking           O(n)        O(1)
 */
public class FirstMissingPositive {

    static int sortScan(int[] nums) {
        int[] a = nums.clone(); Arrays.sort(a);
        int want = 1;
        for (int x : a) if (x == want) want++;
        return want;
    }

    static int hashSet(int[] a) {
        Set<Integer> s = new HashSet<>();
        for (int x : a) s.add(x);
        int v = 1;
        while (s.contains(v)) v++;
        return v;
    }

    static int seenArray(int[] a) {
        int n = a.length;
        boolean[] seen = new boolean[n + 2];
        for (int x : a) if (x >= 1 && x <= n) seen[x] = true;
        for (int v = 1; v <= n + 1; v++) if (!seen[v]) return v;
        return n + 1;
    }

    static int cyclicSort(int[] nums) {
        int[] a = nums.clone();
        int n = a.length;
        for (int i = 0; i < n; i++)
            while (a[i] >= 1 && a[i] <= n && a[a[i] - 1] != a[i]) {
                int j = a[i] - 1;
                int t = a[i]; a[i] = a[j]; a[j] = t;
            }
        for (int i = 0; i < n; i++) if (a[i] != i + 1) return i + 1;
        return n + 1;
    }

    static int signMarking(int[] nums) {
        int[] a = nums.clone();
        int n = a.length;
        for (int i = 0; i < n; i++) if (a[i] <= 0 || a[i] > n) a[i] = n + 1;      // filler
        for (int i = 0; i < n; i++) {
            int v = Math.abs(a[i]);
            if (v <= n && a[v - 1] > 0) a[v - 1] = -a[v - 1];                      // mark v present
        }
        for (int i = 0; i < n; i++) if (a[i] > 0) return i + 1;
        return n + 1;
    }

    public static void main(String[] args) {
        int[][] t = {{3, 4, -1, 1}, {7, 8, 9, 11, 12}, {1, 2, 0}, {1, 1}, {1}, {2}, {2, 1}, {}};
        int[] e = {2, 1, 3, 2, 2, 1, 3, 1};
        for (int i = 0; i < t.length; i++) {
            check(sortScan(t[i]), e[i]); check(hashSet(t[i]), e[i]); check(seenArray(t[i]), e[i]);
            check(cyclicSort(t[i]), e[i]); check(signMarking(t[i]), e[i]);
        }
        Random r = new Random(1);
        for (int k = 0; k < 2000; k++) {
            int[] a = new int[r.nextInt(12)];
            for (int i = 0; i < a.length; i++) a[i] = r.nextInt(16) - 3;
            int exp = sortScan(a);
            check(cyclicSort(a), exp); check(signMarking(a), exp); check(seenArray(a), exp);
        }
        System.out.println("FirstMissingPositive: all tests passed");
    }

    static void check(Object got, Object exp) {
        if (!got.equals(exp)) throw new AssertionError("expected " + exp + " but got " + got);
    }
}
