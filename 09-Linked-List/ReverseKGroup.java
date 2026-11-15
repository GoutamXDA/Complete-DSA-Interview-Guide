import java.util.*;

/**
 * LC 25 Reverse nodes in k-group
 *   Brute : copy values to array, reverse chunks, write back. O(n) space
 *   Optimal: for each group, check k nodes exist, reverse them in place, connect to previous group. O(n), O(1)
 * LC 61 Rotate list: find length & tail, connect tail->head, break at (len - k % len). O(n)
 * LC 138 Copy list with random pointer
 *   HashMap old->new  O(n) space
 *   Interleave A->A'->B->B', set randoms, then separate. O(1) extra space
 */
public class ReverseKGroup {

    static class ListNode { int val; ListNode next; ListNode(int v) { val = v; } }

    static ListNode reverseKGroupArray(ListNode head, int k) {
        List<Integer> v = new ArrayList<>();
        for (ListNode c = head; c != null; c = c.next) v.add(c.val);
        for (int i = 0; i + k <= v.size(); i += k) Collections.reverse(v.subList(i, i + k));
        ListNode c = head;
        for (int x : v) { c.val = x; c = c.next; }
        return head;
    }

    static ListNode reverseKGroup(ListNode head, int k) {
        ListNode dummy = new ListNode(0); dummy.next = head;
        ListNode groupPrev = dummy;
        while (true) {
            ListNode kth = groupPrev;
            for (int i = 0; i < k && kth != null; i++) kth = kth.next;
            if (kth == null) break;                       // fewer than k nodes left
            ListNode groupNext = kth.next;
            ListNode prev = groupNext, cur = groupPrev.next;  // reverse so the first node links to groupNext
            while (cur != groupNext) { ListNode n = cur.next; cur.next = prev; prev = cur; cur = n; }
            ListNode first = groupPrev.next;               // becomes last of group after reversing
            groupPrev.next = kth;
            groupPrev = first;
        }
        return dummy.next;
    }

    static ListNode rotateRight(ListNode head, int k) {
        if (head == null || head.next == null || k == 0) return head;
        int len = 1; ListNode tail = head;
        while (tail.next != null) { tail = tail.next; len++; }
        k %= len;
        if (k == 0) return head;
        tail.next = head;                                  // make a ring
        ListNode newTail = head;
        for (int i = 0; i < len - k - 1; i++) newTail = newTail.next;
        ListNode newHead = newTail.next; newTail.next = null;
        return newHead;
    }

    static class Node { int val; Node next, random; Node(int v) { val = v; } }

    static Node copyRandomListMap(Node head) {
        Map<Node, Node> m = new HashMap<>();
        for (Node c = head; c != null; c = c.next) m.put(c, new Node(c.val));
        for (Node c = head; c != null; c = c.next) { m.get(c).next = m.get(c.next); m.get(c).random = m.get(c.random); }
        return m.get(head);
    }

    static Node copyRandomList(Node head) {
        if (head == null) return null;
        for (Node c = head; c != null; c = c.next.next) { Node cp = new Node(c.val); cp.next = c.next; c.next = cp; }
        for (Node c = head; c != null; c = c.next.next) if (c.random != null) c.next.random = c.random.next;
        Node newHead = head.next;
        for (Node c = head; c != null; c = c.next) {
            Node cp = c.next;
            c.next = cp.next;
            cp.next = (cp.next != null) ? cp.next.next : null;
        }
        return newHead;
    }

    static ListNode build(int... vals) {
        ListNode d = new ListNode(0), t = d;
        for (int v : vals) { t.next = new ListNode(v); t = t.next; }
        return d.next;
    }

    static String str(ListNode h) {
        List<Integer> l = new ArrayList<>();
        for (; h != null; h = h.next) l.add(h.val);
        return l.toString();
    }

    public static void main(String[] args) {
        check(str(reverseKGroup(build(1, 2, 3, 4, 5), 2)), "[2, 1, 4, 3, 5]");
        check(str(reverseKGroup(build(1, 2, 3, 4, 5), 3)), "[3, 2, 1, 4, 5]");
        check(str(reverseKGroupArray(build(1, 2, 3, 4, 5), 3)), "[3, 2, 1, 4, 5]");
        check(str(rotateRight(build(1, 2, 3, 4, 5), 2)), "[4, 5, 1, 2, 3]");
        check(str(rotateRight(build(0, 1, 2), 4)), "[2, 0, 1]");

        Node a = new Node(7), b = new Node(13), c = new Node(11);
        a.next = b; b.next = c; b.random = a; c.random = c;
        for (Node copy : new Node[]{copyRandomListMap(a), copyRandomList(a)}) {
            check(copy != a && copy.val == 7, true);
            check(copy.next.random == copy, true);
            check(copy.next.next.random == copy.next.next, true);
        }
        check(a.next == b && b.next == c, true); // original restored
        System.out.println("ReverseKGroup: all tests passed");
    }

    static void check(Object got, Object exp) {
        if (!got.equals(exp)) throw new AssertionError("expected " + exp + " but got " + got);
    }
}
