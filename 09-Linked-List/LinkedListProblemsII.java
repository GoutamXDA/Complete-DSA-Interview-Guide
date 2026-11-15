import java.util.*;

/**
 * LC 24   Swap nodes in pairs: brute swap values (often disallowed) | rewire with dummy (iterative) | recursion.
 * LC 82   Remove duplicates from sorted list II (drop every value that repeats): dummy + skip whole runs. O(n)
 * LC 86   Partition list around x (stable): two dummy chains, join. O(n)
 * LC 2095 Delete the middle node: count length (2 passes) | fast/slow with fast starting two ahead (1 pass).
 * LC 92   Reverse linked list II (positions left..right): copy to array O(n) space | in place "front insertion". O(n), O(1)
 * LC 143  Reorder list L0 Ln L1 Ln-1 ...: array of nodes O(n) space | middle + reverse second half + merge O(1)
 */
public class LinkedListProblemsII {

    static class ListNode { int val; ListNode next; ListNode(int v) { val = v; } ListNode(int v, ListNode n) { val = v; next = n; } }

    static ListNode swapPairsValues(ListNode head) {
        for (ListNode c = head; c != null && c.next != null; c = c.next.next) { int t = c.val; c.val = c.next.val; c.next.val = t; }
        return head;
    }

    static ListNode swapPairs(ListNode head) {
        ListNode dummy = new ListNode(0, head), prev = dummy;
        while (prev.next != null && prev.next.next != null) {
            ListNode a = prev.next, b = a.next;
            a.next = b.next; b.next = a; prev.next = b;
            prev = a;
        }
        return dummy.next;
    }

    static ListNode swapPairsRecursive(ListNode head) {
        if (head == null || head.next == null) return head;
        ListNode second = head.next;
        head.next = swapPairsRecursive(second.next);
        second.next = head;
        return second;
    }

    static ListNode deleteDuplicates(ListNode head) {
        ListNode dummy = new ListNode(0, head), prev = dummy;
        while (prev.next != null) {
            ListNode cur = prev.next;
            if (cur.next != null && cur.next.val == cur.val) {
                int v = cur.val;
                while (cur != null && cur.val == v) cur = cur.next;
                prev.next = cur;              // drop the whole run
            } else prev = cur;
        }
        return dummy.next;
    }

    static ListNode partition(ListNode head, int x) {
        ListNode lessD = new ListNode(0), moreD = new ListNode(0), less = lessD, more = moreD;
        for (ListNode c = head; c != null; c = c.next) {
            if (c.val < x) { less.next = c; less = c; } else { more.next = c; more = c; }
        }
        more.next = null;                     // avoid a cycle
        less.next = moreD.next;
        return lessD.next;
    }

    static ListNode deleteMiddleCount(ListNode head) {
        int n = 0; for (ListNode c = head; c != null; c = c.next) n++;
        if (n == 1) return null;
        ListNode c = head; for (int i = 0; i < n / 2 - 1; i++) c = c.next;
        c.next = c.next.next;
        return head;
    }

    static ListNode deleteMiddle(ListNode head) {
        if (head.next == null) return null;
        ListNode slow = head, fast = head.next.next;   // slow stops just before the middle
        while (fast != null && fast.next != null) { slow = slow.next; fast = fast.next.next; }
        slow.next = slow.next.next;
        return head;
    }

    static ListNode reverseBetweenArray(ListNode head, int left, int right) {
        List<ListNode> nodes = new ArrayList<>();
        for (ListNode c = head; c != null; c = c.next) nodes.add(c);
        for (int i = left - 1, j = right - 1; i < j; i++, j--) { int t = nodes.get(i).val; nodes.get(i).val = nodes.get(j).val; nodes.get(j).val = t; }
        return head;
    }

    static ListNode reverseBetween(ListNode head, int left, int right) {
        ListNode dummy = new ListNode(0, head), prev = dummy;
        for (int i = 1; i < left; i++) prev = prev.next;
        ListNode cur = prev.next;
        for (int i = 0; i < right - left; i++) {     // move cur.next to the front of the sublist
            ListNode move = cur.next;
            cur.next = move.next;
            move.next = prev.next;
            prev.next = move;
        }
        return dummy.next;
    }

    static void reorderListArray(ListNode head) {
        List<ListNode> n = new ArrayList<>();
        for (ListNode c = head; c != null; c = c.next) n.add(c);
        int i = 0, j = n.size() - 1;
        while (i < j) { n.get(i).next = n.get(j); i++; if (i == j) break; n.get(j).next = n.get(i); j--; }
        n.get(i).next = null;
    }

    static void reorderList(ListNode head) {
        if (head == null || head.next == null) return;
        ListNode slow = head, fast = head;
        while (fast.next != null && fast.next.next != null) { slow = slow.next; fast = fast.next.next; }
        ListNode second = null, cur = slow.next;
        slow.next = null;
        while (cur != null) { ListNode nx = cur.next; cur.next = second; second = cur; cur = nx; }   // reverse
        for (ListNode a = head, b = second; b != null; ) {
            ListNode an = a.next, bn = b.next;
            a.next = b; b.next = an;
            a = an; b = bn;
        }
    }

    static ListNode build(int... v) { ListNode d = new ListNode(0), t = d; for (int x : v) { t.next = new ListNode(x); t = t.next; } return d.next; }
    static String str(ListNode h) { List<Integer> l = new ArrayList<>(); for (; h != null; h = h.next) l.add(h.val); return l.toString(); }

    public static void main(String[] args) {
        check(str(swapPairs(build(1, 2, 3, 4))), "[2, 1, 4, 3]"); check(str(swapPairsRecursive(build(1, 2, 3))), "[2, 1, 3]");
        check(str(swapPairsValues(build(1, 2, 3, 4))), "[2, 1, 4, 3]");
        check(str(deleteDuplicates(build(1, 2, 3, 3, 4, 4, 5))), "[1, 2, 5]"); check(str(deleteDuplicates(build(1, 1, 1, 2, 3))), "[2, 3]");
        check(str(partition(build(1, 4, 3, 2, 5, 2), 3)), "[1, 2, 2, 4, 3, 5]");
        check(str(deleteMiddle(build(1, 3, 4, 7, 1, 2, 6))), "[1, 3, 4, 1, 2, 6]"); check(str(deleteMiddleCount(build(1, 2, 3, 4))), "[1, 2, 4]");
        check(str(deleteMiddle(build(2, 1))), "[2]"); check(deleteMiddle(build(1)) == null, true);
        check(str(reverseBetween(build(1, 2, 3, 4, 5), 2, 4)), "[1, 4, 3, 2, 5]"); check(str(reverseBetweenArray(build(1, 2, 3, 4, 5), 2, 4)), "[1, 4, 3, 2, 5]");
        check(str(reverseBetween(build(5), 1, 1)), "[5]");
        ListNode r1 = build(1, 2, 3, 4, 5); reorderList(r1); check(str(r1), "[1, 5, 2, 4, 3]");
        ListNode r2 = build(1, 2, 3, 4); reorderListArray(r2); check(str(r2), "[1, 4, 2, 3]");
        System.out.println("LinkedListProblemsII: all tests passed");
    }

    static void check(Object got, Object exp) {
        if (!got.equals(exp)) throw new AssertionError("expected " + exp + " but got " + got);
    }
}
