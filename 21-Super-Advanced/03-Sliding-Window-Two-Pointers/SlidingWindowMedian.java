import java.util.*;

/**
 * LC 480 — Sliding Window Median. Explanation: SlidingWindowMedian.md
 * 1. Sort every window                          O(n k log k)
 * 2. Sorted list with binary-search insert      O(n k)
 * 3. Two TreeSets of indices                    O(n log k)
 * 4. Two heaps + lazy deletion                  O(n log n)
 */
public class SlidingWindowMedian {

    static double med(int[] w, int k) { return k % 2 == 1 ? w[k / 2] : ((double) w[k / 2 - 1] + w[k / 2]) / 2; }

    static double[] sortEach(int[] a, int k) {
        double[] r = new double[a.length - k + 1];
        for (int i = 0; i < r.length; i++) { int[] w = Arrays.copyOfRange(a, i, i + k); Arrays.sort(w); r[i] = med(w, k); }
        return r;
    }

    static double[] sortedList(int[] a, int k) {
        List<Integer> w = new ArrayList<>();
        double[] r = new double[a.length - k + 1];
        for (int i = 0; i < a.length; i++) {
            int pos = Collections.binarySearch(w, a[i]);
            w.add(pos < 0 ? -pos - 1 : pos, a[i]);
            if (i >= k) w.remove(Collections.binarySearch(w, a[i - k]));   // any index holding that value
            if (i >= k - 1) r[i - k + 1] = k % 2 == 1 ? w.get(k / 2) : ((double) w.get(k / 2 - 1) + w.get(k / 2)) / 2;
        }
        return r;
    }

    static double[] twoTreeSets(int[] a, int k) {
        Comparator<Integer> cmp = (x, y) -> a[x] != a[y] ? Integer.compare(a[x], a[y]) : Integer.compare(x, y);
        TreeSet<Integer> low = new TreeSet<>(cmp), high = new TreeSet<>(cmp);
        double[] r = new double[a.length - k + 1];
        for (int i = 0; i < a.length; i++) {
            if (i >= k && !low.remove(i - k)) high.remove(i - k);
            low.add(i);
            high.add(low.pollLast());                          // keep max(low) <= min(high)
            while (high.size() > low.size()) low.add(high.pollFirst());
            if (i >= k - 1) r[i - k + 1] = k % 2 == 1 ? a[low.last()] : ((double) a[low.last()] + a[high.first()]) / 2;
        }
        return r;
    }

    static double[] lazyHeaps(int[] a, int k) {
        PriorityQueue<Integer> low = new PriorityQueue<>(Collections.reverseOrder()), high = new PriorityQueue<>();
        Map<Integer, Integer> delayed = new HashMap<>();
        int lowSize = 0, highSize = 0;
        double[] r = new double[a.length - k + 1];
        for (int i = 0; i < a.length; i++) {
            if (low.isEmpty() || a[i] <= low.peek()) { low.offer(a[i]); lowSize++; } else { high.offer(a[i]); highSize++; }
            if (i >= k) {
                int out = a[i - k];
                delayed.merge(out, 1, Integer::sum);
                if (out <= low.peek()) lowSize--; else highSize--;
                prune(low, delayed); prune(high, delayed);
            }
            if (lowSize > highSize + 1) { high.offer(low.poll()); lowSize--; highSize++; prune(low, delayed); }
            else if (lowSize < highSize) { low.offer(high.poll()); highSize--; lowSize++; prune(high, delayed); }
            if (i >= k - 1) r[i - k + 1] = k % 2 == 1 ? low.peek() : ((double) low.peek() + high.peek()) / 2;
        }
        return r;
    }

    static void prune(PriorityQueue<Integer> h, Map<Integer, Integer> delayed) {
        while (!h.isEmpty() && delayed.getOrDefault(h.peek(), 0) > 0) delayed.merge(h.poll(), -1, Integer::sum);
    }

    public static void main(String[] args) {
        int[] a = {1, 3, -1, -3, 5, 3, 6, 7};
        check(Arrays.toString(twoTreeSets(a, 3)), "[1.0, -1.0, -1.0, 3.0, 5.0, 6.0]");
        int[] big = {2147483647, 2147483647};
        check(Arrays.toString(twoTreeSets(big, 2)), "[2.147483647E9]");
        Random r = new Random(13);
        for (int t = 0; t < 2000; t++) {
            int n = r.nextInt(16) + 1, k = r.nextInt(n) + 1;
            int[] x = new int[n];
            for (int i = 0; i < n; i++) x[i] = r.nextInt(9) - 4;
            String e = Arrays.toString(sortEach(x, k));
            check(Arrays.toString(sortedList(x, k)), e); check(Arrays.toString(twoTreeSets(x, k)), e); check(Arrays.toString(lazyHeaps(x, k)), e);
        }
        System.out.println("SlidingWindowMedian: all tests passed");
    }

    static void check(Object got, Object exp) {
        if (!got.equals(exp)) throw new AssertionError("expected " + exp + " but got " + got);
    }
}
