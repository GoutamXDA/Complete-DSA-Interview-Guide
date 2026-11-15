import java.util.*;

/**
 * LC 295 — Find Median from Data Stream. Explanation: FindMedianFromDataStream.md
 * 1. SortOnQuery     add O(1), median O(n log n)
 * 2. SortedList      add O(n), median O(1)
 * 3. TwoHeaps        add O(log n), median O(1)
 * 4. BucketMedian    follow-up for values in [0, 100]: add O(1), median O(101)
 */
public class FindMedianFromDataStream {

    interface MF { void addNum(int x); double findMedian(); }

    static class SortOnQuery implements MF {
        final List<Integer> a = new ArrayList<>();
        public void addNum(int x) { a.add(x); }
        public double findMedian() { List<Integer> s = new ArrayList<>(a); Collections.sort(s); int n = s.size(); return n % 2 == 1 ? s.get(n / 2) : ((double) s.get(n / 2 - 1) + s.get(n / 2)) / 2; }
    }

    static class SortedList implements MF {
        final List<Integer> a = new ArrayList<>();
        public void addNum(int x) { int i = Collections.binarySearch(a, x); a.add(i < 0 ? -i - 1 : i, x); }
        public double findMedian() { int n = a.size(); return n % 2 == 1 ? a.get(n / 2) : ((double) a.get(n / 2 - 1) + a.get(n / 2)) / 2; }
    }

    static class TwoHeaps implements MF {
        final PriorityQueue<Integer> low = new PriorityQueue<>(Collections.reverseOrder()), high = new PriorityQueue<>();
        public void addNum(int x) {
            low.offer(x);
            high.offer(low.poll());
            if (high.size() > low.size()) low.offer(high.poll());
        }
        public double findMedian() { return low.size() > high.size() ? low.peek() : ((double) low.peek() + high.peek()) / 2; }
    }

    static class BucketMedian implements MF {
        final int[] cnt = new int[101]; int total = 0;
        public void addNum(int x) { cnt[x]++; total++; }
        int kth(int k) { for (int v = 0; v <= 100; v++) { k -= cnt[v]; if (k < 0) return v; } return -1; }   // 0-based k
        public double findMedian() { return total % 2 == 1 ? kth(total / 2) : ((double) kth(total / 2 - 1) + kth(total / 2)) / 2; }
    }

    public static void main(String[] args) {
        TwoHeaps h = new TwoHeaps();
        h.addNum(1); h.addNum(2); check(h.findMedian(), 1.5); h.addNum(3); check(h.findMedian(), 2.0);
        TwoHeaps big = new TwoHeaps(); big.addNum(Integer.MAX_VALUE); big.addNum(Integer.MAX_VALUE);
        check(big.findMedian(), (double) Integer.MAX_VALUE);
        Random r = new Random(38);
        for (int t = 0; t < 300; t++) {
            MF[] impls = {new SortOnQuery(), new SortedList(), new TwoHeaps(), new BucketMedian()};
            for (int op = 0; op < 60; op++) {
                int x = r.nextInt(101);
                for (MF m : impls) m.addNum(x);
                double e = impls[0].findMedian();
                for (MF m : impls) check(m.findMedian(), e);
            }
        }
        System.out.println("FindMedianFromDataStream: all tests passed");
    }

    static void check(Object got, Object exp) {
        if (!got.equals(exp)) throw new AssertionError("expected " + exp + " but got " + got);
    }
}
