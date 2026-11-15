import java.util.*;

/**
 * LC 632 — Smallest Range Covering Elements from K Lists. Explanation: SmallestRangeCoveringKLists.md
 * 1. Brute force over value pairs + binary search per list     O(N^2 k log n)
 * 2. Merge + sliding window over k "colours"                   O(N log N)
 * 3. Heap of heads + running max                               O(N log k)
 */
public class SmallestRangeCoveringKLists {

    static boolean better(int a, int b, int[] best) { return b - a < best[1] - best[0] || (b - a == best[1] - best[0] && a < best[0]); }

    static int[] brute(List<List<Integer>> lists) {
        TreeSet<Integer> vals = new TreeSet<>();
        for (List<Integer> l : lists) vals.addAll(l);
        int[] best = null;
        for (int a : vals) for (int b : vals.tailSet(a, true)) {
            boolean ok = true;
            for (List<Integer> l : lists) {
                int i = Collections.binarySearch(l, a);
                if (i < 0) i = -i - 1;
                while (i > 0 && l.get(i - 1) >= a) i--;           // first index with value >= a
                if (i == l.size() || l.get(i) > b) { ok = false; break; }
            }
            if (ok && (best == null || better(a, b, best))) best = new int[]{a, b};
        }
        return best;
    }

    static int[] sweep(List<List<Integer>> lists) {
        List<int[]> all = new ArrayList<>();
        for (int i = 0; i < lists.size(); i++) for (int v : lists.get(i)) all.add(new int[]{v, i});
        all.sort((x, y) -> x[0] != y[0] ? x[0] - y[0] : x[1] - y[1]);
        int k = lists.size(), covered = 0;
        int[] cnt = new int[k], best = null;
        for (int l = 0, r = 0; r < all.size(); r++) {
            if (cnt[all.get(r)[1]]++ == 0) covered++;
            while (covered == k) {
                int a = all.get(l)[0], b = all.get(r)[0];
                if (best == null || better(a, b, best)) best = new int[]{a, b};
                if (--cnt[all.get(l)[1]] == 0) covered--;
                l++;
            }
        }
        return best;
    }

    static int[] smallestRange(List<List<Integer>> lists) {
        PriorityQueue<int[]> pq = new PriorityQueue<>(Comparator.comparingInt(x -> x[0]));   // {value, list, index}
        int max = Integer.MIN_VALUE;
        for (int i = 0; i < lists.size(); i++) { int v = lists.get(i).get(0); pq.offer(new int[]{v, i, 0}); max = Math.max(max, v); }
        int[] best = null;
        while (true) {
            int[] c = pq.poll();
            if (best == null || better(c[0], max, best)) best = new int[]{c[0], max};
            List<Integer> l = lists.get(c[1]);
            if (c[2] + 1 == l.size()) return best;
            int nv = l.get(c[2] + 1);
            pq.offer(new int[]{nv, c[1], c[2] + 1});
            max = Math.max(max, nv);
        }
    }

    public static void main(String[] args) {
        List<List<Integer>> ex = List.of(List.of(4, 10, 15, 24, 26), List.of(0, 9, 12, 20), List.of(5, 18, 22, 30));
        check(Arrays.toString(smallestRange(ex)), "[20, 24]"); check(Arrays.toString(sweep(ex)), "[20, 24]");
        check(Arrays.toString(smallestRange(List.of(List.of(1, 2, 3), List.of(1, 2, 3), List.of(1, 2, 3)))), "[1, 1]");
        Random r = new Random(40);
        for (int t = 0; t < 1500; t++) {
            int k = r.nextInt(4) + 1;
            List<List<Integer>> lists = new ArrayList<>();
            for (int i = 0; i < k; i++) {
                List<Integer> l = new ArrayList<>();
                for (int j = r.nextInt(5) + 1; j > 0; j--) l.add(r.nextInt(20));
                Collections.sort(l);
                lists.add(l);
            }
            String e = Arrays.toString(brute(lists));
            check(Arrays.toString(smallestRange(lists)), e); check(Arrays.toString(sweep(lists)), e);
        }
        System.out.println("SmallestRangeCoveringKLists: all tests passed");
    }

    static void check(Object got, Object exp) {
        if (!got.equals(exp)) throw new AssertionError("expected " + exp + " but got " + got);
    }
}
