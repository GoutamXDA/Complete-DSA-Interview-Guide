import java.util.*;

/**
 * LC 992 — Subarrays with K Different Integers. Explanation: SubarraysWithKDistinct.md
 * 1. Brute force from each start                    O(n^2)
 * 2. atMost(K) - atMost(K-1)                        O(n)
 * 3. One pass with two left pointers                O(n)
 */
public class SubarraysWithKDistinct {

    static long brute(int[] a, int k) {
        long c = 0;
        for (int i = 0; i < a.length; i++) {
            Set<Integer> s = new HashSet<>();
            for (int j = i; j < a.length; j++) { s.add(a[j]); if (s.size() == k) c++; else if (s.size() > k) break; }
        }
        return c;
    }

    static long atMost(int[] a, int k) {
        if (k <= 0) return 0;
        Map<Integer, Integer> cnt = new HashMap<>();
        long total = 0;
        for (int l = 0, r = 0; r < a.length; r++) {
            cnt.merge(a[r], 1, Integer::sum);
            while (cnt.size() > k) { if (cnt.merge(a[l], -1, Integer::sum) == 0) cnt.remove(a[l]); l++; }
            total += r - l + 1;
        }
        return total;
    }

    static long subarraysWithKDistinct(int[] a, int k) { return atMost(a, k) - atMost(a, k - 1); }

    static long onePass(int[] a, int k) {
        Map<Integer, Integer> c1 = new HashMap<>(), c2 = new HashMap<>();
        int l1 = 0, l2 = 0;
        long total = 0;
        for (int r = 0; r < a.length; r++) {
            c1.merge(a[r], 1, Integer::sum); c2.merge(a[r], 1, Integer::sum);
            while (c1.size() > k) { if (c1.merge(a[l1], -1, Integer::sum) == 0) c1.remove(a[l1]); l1++; }
            while (c2.size() > k - 1) { if (c2.merge(a[l2], -1, Integer::sum) == 0) c2.remove(a[l2]); l2++; }
            total += l2 - l1;                       // starts in [l1, l2) have exactly k distinct
        }
        return total;
    }

    public static void main(String[] args) {
        check(subarraysWithKDistinct(new int[]{1, 2, 1, 2, 3}, 2), 7L); check(subarraysWithKDistinct(new int[]{1, 2, 1, 3, 4}, 3), 3L);
        Random r = new Random(12);
        for (int t = 0; t < 3000; t++) {
            int[] a = new int[r.nextInt(15) + 1];
            for (int i = 0; i < a.length; i++) a[i] = r.nextInt(4) + 1;
            int k = r.nextInt(4) + 1;
            long e = brute(a, k);
            check(subarraysWithKDistinct(a, k), e); check(onePass(a, k), e);
        }
        System.out.println("SubarraysWithKDistinct: all tests passed");
    }

    static void check(Object got, Object exp) {
        if (!got.equals(exp)) throw new AssertionError("expected " + exp + " but got " + got);
    }
}
