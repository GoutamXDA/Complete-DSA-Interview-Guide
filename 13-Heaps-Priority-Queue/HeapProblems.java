import java.util.*;

/**
 * LC 215 Kth largest: sort O(n log n) | size-k min-heap O(n log k) | quickselect O(n) avg (see 07)
 * LC 703 Kth largest in stream: keep size-k min-heap; top is the answer.
 * LC 347 Top K frequent: count + sort O(n log n) | heap O(n log k) | bucket sort by frequency O(n)
 * LC 973 K closest points: sort O(n log n) | size-k max-heap by distance O(n log k)
 * LC 1046 Last stone weight: max-heap simulation.
 * LC 23 Merge K sorted lists: merge one by one O(kN) | heap of heads O(N log k) | divide & conquer O(N log k)
 * LC 295 Median stream: sorted insert O(n) | two heaps O(log n) add, O(1) median
 * LC 621 Task scheduler: (maxFreq-1)*(n+1) + countOfMaxFreq, at least tasks.length.
 * LC 253 Meeting rooms II: sort by start; min-heap of end times; size = rooms. (Also: sweep line.)
 * LC 767 Reorganize string: always place the most frequent char different from the previous one.
 */
public class HeapProblems {

    static int kthLargest(int[] a, int k) {
        PriorityQueue<Integer> pq = new PriorityQueue<>();
        for (int x : a) { pq.offer(x); if (pq.size() > k) pq.poll(); }
        return pq.peek();
    }

    static class KthLargest {
        final PriorityQueue<Integer> pq = new PriorityQueue<>(); final int k;
        KthLargest(int k, int[] nums) { this.k = k; for (int x : nums) add(x); }
        int add(int v) { pq.offer(v); if (pq.size() > k) pq.poll(); return pq.peek(); }
    }

    static int[] topKFrequentHeap(int[] a, int k) {
        Map<Integer, Integer> f = new HashMap<>();
        for (int x : a) f.merge(x, 1, Integer::sum);
        PriorityQueue<Integer> pq = new PriorityQueue<>(Comparator.comparingInt(f::get));
        for (int key : f.keySet()) { pq.offer(key); if (pq.size() > k) pq.poll(); }
        int[] r = new int[k];
        for (int i = k - 1; i >= 0; i--) r[i] = pq.poll();
        return r;
    }

    static int[] topKFrequentBucket(int[] a, int k) {
        Map<Integer, Integer> f = new HashMap<>();
        for (int x : a) f.merge(x, 1, Integer::sum);
        List<Integer>[] buckets = new List[a.length + 1];
        for (var e : f.entrySet()) {
            if (buckets[e.getValue()] == null) buckets[e.getValue()] = new ArrayList<>();
            buckets[e.getValue()].add(e.getKey());
        }
        int[] r = new int[k]; int idx = 0;
        for (int freq = a.length; freq > 0 && idx < k; freq--)
            if (buckets[freq] != null) for (int x : buckets[freq]) if (idx < k) r[idx++] = x;
        return r;
    }

    static int[][] kClosest(int[][] pts, int k) {
        PriorityQueue<int[]> pq = new PriorityQueue<>((p, q) -> Integer.compare(q[0] * q[0] + q[1] * q[1], p[0] * p[0] + p[1] * p[1]));
        for (int[] p : pts) { pq.offer(p); if (pq.size() > k) pq.poll(); }
        return pq.toArray(new int[0][]);
    }

    static int lastStoneWeight(int[] stones) {
        PriorityQueue<Integer> pq = new PriorityQueue<>(Collections.reverseOrder());
        for (int s : stones) pq.offer(s);
        while (pq.size() > 1) { int y = pq.poll(), x = pq.poll(); if (y != x) pq.offer(y - x); }
        return pq.isEmpty() ? 0 : pq.peek();
    }

    static class ListNode { int val; ListNode next; ListNode(int v) { val = v; } }

    static ListNode mergeKLists(ListNode[] lists) {
        PriorityQueue<ListNode> pq = new PriorityQueue<>(Comparator.comparingInt(n -> n.val));
        for (ListNode l : lists) if (l != null) pq.offer(l);
        ListNode dummy = new ListNode(0), t = dummy;
        while (!pq.isEmpty()) {
            ListNode n = pq.poll();
            t.next = n; t = n;
            if (n.next != null) pq.offer(n.next);
        }
        return dummy.next;
    }

    static class MedianFinder {
        final PriorityQueue<Integer> low = new PriorityQueue<>(Collections.reverseOrder()); // max-heap
        final PriorityQueue<Integer> high = new PriorityQueue<>();                          // min-heap
        void addNum(int x) {
            low.offer(x);
            high.offer(low.poll());                    // ensure every low <= every high
            if (high.size() > low.size()) low.offer(high.poll()); // low may hold one extra
        }
        double findMedian() { return low.size() > high.size() ? low.peek() : (low.peek() + high.peek()) / 2.0; }
    }

    static int leastInterval(char[] tasks, int n) {
        int[] f = new int[26];
        for (char c : tasks) f[c - 'A']++;
        int max = Arrays.stream(f).max().getAsInt(), countMax = 0;
        for (int x : f) if (x == max) countMax++;
        return Math.max(tasks.length, (max - 1) * (n + 1) + countMax);
    }

    static int minMeetingRooms(int[][] intervals) {
        int[][] iv = intervals.clone();
        Arrays.sort(iv, Comparator.comparingInt(x -> x[0]));
        PriorityQueue<Integer> ends = new PriorityQueue<>();
        for (int[] m : iv) {
            if (!ends.isEmpty() && ends.peek() <= m[0]) ends.poll(); // reuse room
            ends.offer(m[1]);
        }
        return ends.size();
    }

    static String reorganizeString(String s) {
        int[] f = new int[26];
        for (char c : s.toCharArray()) f[c - 'a']++;
        PriorityQueue<Integer> pq = new PriorityQueue<>((a, b) -> f[b] != f[a] ? f[b] - f[a] : a - b);
        for (int i = 0; i < 26; i++) if (f[i] > 0) pq.offer(i);
        StringBuilder sb = new StringBuilder();
        while (pq.size() >= 2) {
            int a = pq.poll(), b = pq.poll();
            sb.append((char) ('a' + a)).append((char) ('a' + b));
            if (--f[a] > 0) pq.offer(a);
            if (--f[b] > 0) pq.offer(b);
        }
        if (!pq.isEmpty()) {
            int a = pq.poll();
            if (f[a] > 1) return "";
            sb.append((char) ('a' + a));
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        check(kthLargest(new int[]{3, 2, 1, 5, 6, 4}, 2), 5);
        KthLargest kl = new KthLargest(3, new int[]{4, 5, 8, 2});
        check(kl.add(3), 4); check(kl.add(5), 5); check(kl.add(10), 5);
        int[] f = {1, 1, 1, 2, 2, 3};
        check(Arrays.toString(topKFrequentHeap(f, 2)), "[1, 2]"); check(Arrays.toString(topKFrequentBucket(f, 2)), "[1, 2]");
        check(Arrays.deepToString(kClosest(new int[][]{{1, 3}, {-2, 2}}, 1)), "[[-2, 2]]");
        check(lastStoneWeight(new int[]{2, 7, 4, 1, 8, 1}), 1);
        ListNode a = new ListNode(1); a.next = new ListNode(4); a.next.next = new ListNode(5);
        ListNode b = new ListNode(1); b.next = new ListNode(3); b.next.next = new ListNode(4);
        ListNode c = new ListNode(2); c.next = new ListNode(6);
        StringBuilder sb = new StringBuilder();
        for (ListNode n = mergeKLists(new ListNode[]{a, b, c}); n != null; n = n.next) sb.append(n.val);
        check(sb.toString(), "11234456");
        MedianFinder mf = new MedianFinder();
        mf.addNum(1); mf.addNum(2); check(mf.findMedian(), 1.5); mf.addNum(3); check(mf.findMedian(), 2.0);
        check(leastInterval("AAABBB".toCharArray(), 2), 8); check(leastInterval("AAABBB".toCharArray(), 0), 6);
        check(minMeetingRooms(new int[][]{{0, 30}, {5, 10}, {15, 20}}), 2); check(minMeetingRooms(new int[][]{{7, 10}, {2, 4}}), 1);
        String r = reorganizeString("aab");
        check(r, "aba"); check(reorganizeString("aaab"), "");
        System.out.println("HeapProblems: all tests passed");
    }

    static void check(Object got, Object exp) {
        if (!got.equals(exp)) throw new AssertionError("expected " + exp + " but got " + got);
    }
}
