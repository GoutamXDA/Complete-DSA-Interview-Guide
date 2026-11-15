import java.util.*;

/**
 * LC 141 Has cycle: HashSet O(n) space | Floyd fast/slow O(1) space
 * LC 142 Cycle start: HashSet first repeat | Floyd: after meeting, move one pointer to head, step both by 1.
 * Cycle length: from meeting point, walk until back.
 * LC 160 Intersection: HashSet | length difference | pointer switching (a+c+b = b+c+a). O(m+n), O(1)
 */
public class CycleProblems {

    static class ListNode { int val; ListNode next; ListNode(int v) { val = v; } }

    static boolean hasCycleSet(ListNode h) {
        Set<ListNode> seen = new HashSet<>();
        for (; h != null; h = h.next) if (!seen.add(h)) return true;
        return false;
    }

    static boolean hasCycle(ListNode h) {
        ListNode slow = h, fast = h;
        while (fast != null && fast.next != null) {
            slow = slow.next; fast = fast.next.next;
            if (slow == fast) return true;
        }
        return false;
    }

    static ListNode detectCycleSet(ListNode h) {
        Set<ListNode> seen = new HashSet<>();
        for (; h != null; h = h.next) if (!seen.add(h)) return h;
        return null;
    }

    static ListNode detectCycle(ListNode h) {
        ListNode slow = h, fast = h;
        while (fast != null && fast.next != null) {
            slow = slow.next; fast = fast.next.next;
            if (slow == fast) {
                slow = h;
                while (slow != fast) { slow = slow.next; fast = fast.next; }
                return slow;
            }
        }
        return null;
    }

    static int cycleLength(ListNode h) {
        ListNode slow = h, fast = h;
        while (fast != null && fast.next != null) {
            slow = slow.next; fast = fast.next.next;
            if (slow == fast) {
                int len = 1;
                for (ListNode c = slow.next; c != slow; c = c.next) len++;
                return len;
            }
        }
        return 0;
    }

    static ListNode intersectionSet(ListNode a, ListNode b) {
        Set<ListNode> s = new HashSet<>();
        for (; a != null; a = a.next) s.add(a);
        for (; b != null; b = b.next) if (s.contains(b)) return b;
        return null;
    }

    static ListNode intersectionLength(ListNode a, ListNode b) {
        int la = len(a), lb = len(b);
        while (la > lb) { a = a.next; la--; }
        while (lb > la) { b = b.next; lb--; }
        while (a != b) { a = a.next; b = b.next; }
        return a;
    }

    static int len(ListNode h) { int n = 0; for (; h != null; h = h.next) n++; return n; }

    static ListNode intersection(ListNode headA, ListNode headB) {
        ListNode a = headA, b = headB;
        while (a != b) {                    // both null at the same time if no intersection
            a = (a == null) ? headB : a.next;
            b = (b == null) ? headA : b.next;
        }
        return a;
    }

    public static void main(String[] args) {
        ListNode[] n = new ListNode[6];
        for (int i = 0; i < 6; i++) n[i] = new ListNode(i);
        for (int i = 0; i < 5; i++) n[i].next = n[i + 1];
        check(hasCycle(n[0]), false); check(hasCycleSet(n[0]), false);
        n[5].next = n[2]; // cycle 2->3->4->5->2
        check(hasCycle(n[0]), true); check(hasCycleSet(n[0]), true);
        check(detectCycle(n[0]), n[2]); check(detectCycleSet(n[0]), n[2]); check(cycleLength(n[0]), 4);

        ListNode common = new ListNode(8); common.next = new ListNode(4);
        ListNode a = new ListNode(4); a.next = new ListNode(1); a.next.next = common;
        ListNode b = new ListNode(5); b.next = new ListNode(6); b.next.next = new ListNode(1); b.next.next.next = common;
        check(intersectionSet(a, b), common); check(intersectionLength(a, b), common); check(intersection(a, b), common);
        check(intersection(new ListNode(1), new ListNode(2)) == null, true);
        System.out.println("CycleProblems: all tests passed");
    }

    static void check(Object got, Object exp) {
        if (!got.equals(exp)) throw new AssertionError("expected " + exp + " but got " + got);
    }
}
