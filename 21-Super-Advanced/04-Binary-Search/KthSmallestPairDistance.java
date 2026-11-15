import java.util.*;

/**
 * LC 719 — Find K-th Smallest Pair Distance. Explanation: KthSmallestPairDistance.md
 * 1. Enumerate + sort                   O(n^2 log n)
 * 2. Bucket count over distances        O(n^2 + W)
 * 3. Heap frontier (k smallest pairs)   O((n + k) log n)
 * 4. Binary search on distance + count  O(n log n + n log W)
 */
public class KthSmallestPairDistance {

    static int sortAll(int[] a, int k) {
        List<Integer> d = new ArrayList<>();
        for (int i = 0; i < a.length; i++) for (int j = i + 1; j < a.length; j++) d.add(Math.abs(a[i] - a[j]));
        Collections.sort(d);
        return d.get(k - 1);
    }

    static int bucket(int[] a, int k) {
        int max = Arrays.stream(a).max().getAsInt(), min = Arrays.stream(a).min().getAsInt();
        int[] cnt = new int[max - min + 1];
        for (int i = 0; i < a.length; i++) for (int j = i + 1; j < a.length; j++) cnt[Math.abs(a[i] - a[j])]++;
        for (int d = 0; ; d++) { k -= cnt[d]; if (k <= 0) return d; }
    }

    static int heap(int[] nums, int k) {
        int[] a = nums.clone(); Arrays.sort(a);
        PriorityQueue<int[]> pq = new PriorityQueue<>(Comparator.comparingInt(p -> a[p[1]] - a[p[0]]));
        for (int i = 0; i + 1 < a.length; i++) pq.offer(new int[]{i, i + 1});
        int[] p = null;
        while (k-- > 0) {
            p = pq.poll();
            if (p[1] + 1 < a.length) pq.offer(new int[]{p[0], p[1] + 1});
        }
        return a[p[1]] - a[p[0]];
    }

    static int smallestDistancePair(int[] nums, int k) {
        int[] a = nums.clone(); Arrays.sort(a);
        int lo = 0, hi = a[a.length - 1] - a[0];
        while (lo < hi) {
            int mid = (lo + hi) >>> 1;
            if (countAtMost(a, mid) >= k) hi = mid; else lo = mid + 1;
        }
        return lo;
    }

    static long countAtMost(int[] a, int d) {
        long cnt = 0;
        for (int i = 0, j = 0; j < a.length; j++) {
            while (a[j] - a[i] > d) i++;
            cnt += j - i;
        }
        return cnt;
    }

    public static void main(String[] args) {
        check(smallestDistancePair(new int[]{1, 3, 1}, 1), 0); check(smallestDistancePair(new int[]{1, 1, 1}, 2), 0);
        check(smallestDistancePair(new int[]{1, 6, 1}, 3), 5);
        Random r = new Random(17);
        for (int t = 0; t < 2000; t++) {
            int n = r.nextInt(9) + 2;
            int[] a = new int[n];
            for (int i = 0; i < n; i++) a[i] = r.nextInt(15);
            int k = r.nextInt(n * (n - 1) / 2) + 1;
            int e = sortAll(a, k);
            check(bucket(a, k), e); check(heap(a, k), e); check(smallestDistancePair(a, k), e);
        }
        System.out.println("KthSmallestPairDistance: all tests passed");
    }

    static void check(Object got, Object exp) {
        if (!got.equals(exp)) throw new AssertionError("expected " + exp + " but got " + got);
    }
}
