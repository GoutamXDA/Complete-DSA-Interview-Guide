import java.util.*;

/**
 * LC 369  Add one to a number in a linked list (most significant digit first):
 *   reverse, add, reverse back O(n) | recursion returning carry O(n) stack | "last non-9" trick O(1) space
 * Sort a linked list of 0s, 1s and 2s (GFG): count and overwrite values | three dummy chains, relink (no value writes).
 * LC 1472 Design browser history: array list + current index + logical end (O(1) visit/back/forward)
 *   or a doubly linked list.
 * LC 430  Flatten a multilevel doubly linked list: DFS returning the tail of each flattened level | explicit stack.
 * Flatten a linked list (GFG): each node has next (sorted heads) and bottom (sorted list);
 *   merge lists pairwise along bottom pointers O(N * k) | min-heap of heads O(N log k).
 */
public class LinkedListProblemsIII {

    static class ListNode { int val; ListNode next; ListNode(int v) { val = v; } }

    static ListNode plusOneRecursive(ListNode head) {
        if (carry(head) == 0) return head;
        ListNode h = new ListNode(1); h.next = head; return h;
    }

    static int carry(ListNode n) {
        if (n == null) return 1;
        int s = n.val + carry(n.next);
        n.val = s % 10;
        return s / 10;
    }

    static ListNode plusOne(ListNode head) {
        ListNode dummy = new ListNode(0); dummy.next = head;
        ListNode lastNotNine = dummy;
        for (ListNode c = head; c != null; c = c.next) if (c.val != 9) lastNotNine = c;
        lastNotNine.val++;
        for (ListNode c = lastNotNine.next; c != null; c = c.next) c.val = 0;   // trailing 9s become 0
        return dummy.val == 1 ? dummy : head;
    }

    static ListNode sort012Count(ListNode head) {
        int[] c = new int[3];
        for (ListNode n = head; n != null; n = n.next) c[n.val]++;
        int v = 0;
        for (ListNode n = head; n != null; n = n.next) { while (c[v] == 0) v++; n.val = v; c[v]--; }
        return head;
    }

    static ListNode sort012(ListNode head) {
        ListNode[] dummy = {new ListNode(0), new ListNode(0), new ListNode(0)};
        ListNode[] tail = dummy.clone();
        for (ListNode n = head; n != null; n = n.next) { tail[n.val].next = n; tail[n.val] = n; }
        tail[2].next = null;
        tail[1].next = dummy[2].next;
        tail[0].next = dummy[1].next != null ? dummy[1].next : dummy[2].next;
        return dummy[0].next;
    }

    static class BrowserHistory {
        final List<String> pages = new ArrayList<>();
        int cur = 0, end = 0;   // valid history is pages[0..end]
        BrowserHistory(String home) { pages.add(home); }
        void visit(String url) {
            cur++;
            if (cur < pages.size()) pages.set(cur, url); else pages.add(url);
            end = cur;            // forward history is discarded logically
        }
        String back(int steps) { cur = Math.max(0, cur - steps); return pages.get(cur); }
        String forward(int steps) { cur = Math.min(end, cur + steps); return pages.get(cur); }
    }

    static class Node { int val; Node prev, next, child; Node(int v) { val = v; } }

    static Node flattenDFS(Node head) { flat(head); return head; }

    /** flattens the list starting at n, returns its tail */
    static Node flat(Node n) {
        Node cur = n, tail = n;
        while (cur != null) {
            Node next = cur.next;
            if (cur.child != null) {
                Node childTail = flat(cur.child);
                cur.next = cur.child; cur.child.prev = cur; cur.child = null;
                childTail.next = next;
                if (next != null) next.prev = childTail;
                tail = childTail;
            } else tail = cur;
            cur = next;
        }
        return tail;
    }

    static Node flattenStack(Node head) {
        Deque<Node> st = new ArrayDeque<>();
        Node cur = head;
        while (cur != null) {
            if (cur.child != null) {
                if (cur.next != null) st.push(cur.next);
                cur.next = cur.child; cur.child.prev = cur; cur.child = null;
            } else if (cur.next == null && !st.isEmpty()) {
                Node n = st.pop(); cur.next = n; n.prev = cur;
            }
            cur = cur.next;
        }
        return head;
    }

    static class BNode { int val; BNode next, bottom; BNode(int v) { val = v; } }

    static BNode flattenBottom(BNode root) {
        if (root == null || root.next == null) return root;
        BNode rest = flattenBottom(root.next);
        root.next = null;
        return mergeBottom(root, rest);
    }

    static BNode mergeBottom(BNode a, BNode b) {
        BNode d = new BNode(0), t = d;
        while (a != null && b != null) {
            if (a.val <= b.val) { t.bottom = a; a = a.bottom; } else { t.bottom = b; b = b.bottom; }
            t = t.bottom;
        }
        t.bottom = a != null ? a : b;
        return d.bottom;
    }

    static BNode flattenBottomHeap(BNode root) {
        PriorityQueue<BNode> pq = new PriorityQueue<>(Comparator.comparingInt(n -> n.val));
        for (BNode h = root; h != null; h = h.next) pq.offer(h);
        BNode d = new BNode(0), t = d;
        while (!pq.isEmpty()) {
            BNode n = pq.poll();
            t.bottom = n; t = n; n.next = null;
            if (n.bottom != null) pq.offer(n.bottom);
        }
        return d.bottom;
    }

    static ListNode build(int... v) { ListNode d = new ListNode(0), t = d; for (int x : v) { t.next = new ListNode(x); t = t.next; } return d.next; }
    static String str(ListNode h) { List<Integer> l = new ArrayList<>(); for (; h != null; h = h.next) l.add(h.val); return l.toString(); }

    static BNode buildBottom(int[][] cols) {
        BNode head = null, prevHead = null;
        for (int[] col : cols) {
            BNode h = new BNode(col[0]), t = h;
            for (int i = 1; i < col.length; i++) { t.bottom = new BNode(col[i]); t = t.bottom; }
            if (head == null) head = h; else prevHead.next = h;
            prevHead = h;
        }
        return head;
    }

    static String strBottom(BNode n) { List<Integer> l = new ArrayList<>(); for (; n != null; n = n.bottom) l.add(n.val); return l.toString(); }

    public static void main(String[] args) {
        check(str(plusOne(build(1, 2, 3))), "[1, 2, 4]"); check(str(plusOne(build(9, 9))), "[1, 0, 0]");
        check(str(plusOneRecursive(build(1, 9, 9))), "[2, 0, 0]"); check(str(plusOneRecursive(build(9))), "[1, 0]");
        check(str(sort012(build(1, 2, 0, 2, 1, 0))), "[0, 0, 1, 1, 2, 2]"); check(str(sort012Count(build(2, 2, 0))), "[0, 2, 2]");
        check(str(sort012(build(2, 0, 2))), "[0, 2, 2]"); check(str(sort012(build(1, 1))), "[1, 1]");
        BrowserHistory b = new BrowserHistory("leetcode.com");
        b.visit("google.com"); b.visit("facebook.com"); b.visit("youtube.com");
        check(b.back(1), "facebook.com"); check(b.back(1), "google.com"); check(b.forward(1), "facebook.com");
        b.visit("linkedin.com");
        check(b.forward(2), "linkedin.com"); check(b.back(2), "google.com"); check(b.back(7), "leetcode.com");
        for (int v = 0; v < 2; v++) {
            Node[] n = new Node[13];
            for (int i = 1; i <= 12; i++) n[i] = new Node(i);
            int[][] links = {{1, 2}, {2, 3}, {3, 4}, {4, 5}, {5, 6}, {7, 8}, {8, 9}, {9, 10}, {11, 12}};
            for (int[] l : links) { n[l[0]].next = n[l[1]]; n[l[1]].prev = n[l[0]]; }
            n[3].child = n[7]; n[8].child = n[11];
            Node h = v == 0 ? flattenDFS(n[1]) : flattenStack(n[1]);
            StringBuilder sb = new StringBuilder();
            Node last = null;
            for (Node c = h; c != null; c = c.next) { sb.append(c.val).append(' '); if (c.child != null) throw new AssertionError(); last = c; }
            check(sb.toString().trim(), "1 2 3 7 8 11 12 9 10 4 5 6");
            check(last.prev.val, 5);
        }
        int[][] cols = {{5, 7, 8, 30}, {10, 20}, {19, 22, 50}, {28, 35, 40, 45}};
        check(strBottom(flattenBottom(buildBottom(cols))), "[5, 7, 8, 10, 19, 20, 22, 28, 30, 35, 40, 45, 50]");
        check(strBottom(flattenBottomHeap(buildBottom(cols))), "[5, 7, 8, 10, 19, 20, 22, 28, 30, 35, 40, 45, 50]");
        System.out.println("LinkedListProblemsIII: all tests passed");
    }

    static void check(Object got, Object exp) {
        if (!got.equals(exp)) throw new AssertionError("expected " + exp + " but got " + got);
    }
}
