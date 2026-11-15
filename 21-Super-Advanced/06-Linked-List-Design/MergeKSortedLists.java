import java.util.*;

/**
 * LC 23 — Merge k Sorted Lists. Explanation: MergeKSortedLists.md
 * 1. Collect + sort                 O(N log N)
 * 2. Merge one list at a time       O(k N)
 * 3. Scan k heads each step         O(k N)
 * 4. Min-heap of heads              O(N log k)
 * 5. Divide & conquer pairing       O(N log k), O(1) extra
 */
public class MergeKSortedLists {

    static class ListNode { int val; ListNode next; ListNode(int v) { val = v; } }

    static ListNode collectSort(ListNode[] lists) {
        List<Integer> v = new ArrayList<>();
        for (ListNode l : lists) for (; l != null; l = l.next) v.add(l.val);
        Collections.sort(v);
        ListNode d = new ListNode(0), t = d;
        for (int x : v) { t.next = new ListNode(x); t = t.next; }
        return d.next;
    }

    static ListNode mergeTwo(ListNode a, ListNode b) {
        ListNode d = new ListNode(0), t = d;
        while (a != null && b != null) { if (a.val <= b.val) { t.next = a; a = a.next; } else { t.next = b; b = b.next; } t = t.next; }
        t.next = a != null ? a : b;
        return d.next;
    }

    static ListNode oneByOne(ListNode[] lists) {
        ListNode res = null;
        for (ListNode l : lists) res = mergeTwo(res, l);
        return res;
    }

    static ListNode scanHeads(ListNode[] in) {
        ListNode[] lists = in.clone();
        ListNode d = new ListNode(0), t = d;
        while (true) {
            int best = -1;
            for (int i = 0; i < lists.length; i++) if (lists[i] != null && (best == -1 || lists[i].val < lists[best].val)) best = i;
            if (best == -1) break;
            t.next = lists[best]; t = t.next; lists[best] = lists[best].next;
        }
        return d.next;
    }

    static ListNode heap(ListNode[] lists) {
        PriorityQueue<ListNode> pq = new PriorityQueue<>(Comparator.comparingInt(n -> n.val));
        for (ListNode l : lists) if (l != null) pq.offer(l);
        ListNode d = new ListNode(0), t = d;
        while (!pq.isEmpty()) {
            ListNode n = pq.poll();
            t.next = n; t = n;
            if (n.next != null) pq.offer(n.next);
        }
        return d.next;
    }

    static ListNode divideConquer(ListNode[] in) {
        if (in.length == 0) return null;
        ListNode[] lists = in.clone();
        for (int step = 1; step < lists.length; step *= 2)
            for (int i = 0; i + step < lists.length; i += 2 * step) lists[i] = mergeTwo(lists[i], lists[i + step]);
        return lists[0];
    }

    static ListNode[] make(int[][] arrs) {
        ListNode[] res = new ListNode[arrs.length];
        for (int i = 0; i < arrs.length; i++) {
            ListNode d = new ListNode(0), t = d;
            for (int x : arrs[i]) { t.next = new ListNode(x); t = t.next; }
            res[i] = d.next;
        }
        return res;
    }

    static List<Integer> list(ListNode h) { List<Integer> l = new ArrayList<>(); for (; h != null; h = h.next) l.add(h.val); return l; }

    public static void main(String[] args) {
        int[][] ex = {{1, 4, 5}, {1, 3, 4}, {2, 6}};
        check(list(heap(make(ex))), List.of(1, 1, 2, 3, 4, 4, 5, 6)); check(list(divideConquer(new ListNode[0])), List.of());
        Random r = new Random(24);
        for (int t = 0; t < 1500; t++) {
            int k = r.nextInt(6);
            int[][] arrs = new int[k][];
            for (int i = 0; i < k; i++) {
                arrs[i] = new int[r.nextInt(5)];
                for (int j = 0; j < arrs[i].length; j++) arrs[i][j] = r.nextInt(10);
                Arrays.sort(arrs[i]);
            }
            List<Integer> e = list(collectSort(make(arrs)));
            check(list(oneByOne(make(arrs))), e); check(list(scanHeads(make(arrs))), e);
            check(list(heap(make(arrs))), e); check(list(divideConquer(make(arrs))), e);
        }
        System.out.println("MergeKSortedLists: all tests passed");
    }

    static void check(Object got, Object exp) {
        if (!got.equals(exp)) throw new AssertionError("expected " + exp + " but got " + got);
    }
}
