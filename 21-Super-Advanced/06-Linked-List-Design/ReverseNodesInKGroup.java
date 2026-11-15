import java.util.*;

/**
 * LC 25 — Reverse Nodes in k-Group. Explanation: ReverseNodesInKGroup.md
 * 1. Values into an array (oracle)     O(n) O(n)
 * 2. Stack per group                   O(n) O(k)
 * 3. Recursion                         O(n) O(n/k) stack
 * 4. Iterative in place                O(n) O(1)
 */
public class ReverseNodesInKGroup {

    static class ListNode { int val; ListNode next; ListNode(int v) { val = v; } }

    static List<Integer> arrayOracle(List<Integer> vals, int k) {
        List<Integer> v = new ArrayList<>(vals);
        for (int i = 0; i + k <= v.size(); i += k) Collections.reverse(v.subList(i, i + k));
        return v;
    }

    static ListNode stack(ListNode head, int k) {
        ListNode dummy = new ListNode(0), tail = dummy, cur = head;
        Deque<ListNode> st = new ArrayDeque<>();
        while (cur != null) {
            ListNode groupStart = cur;
            for (int i = 0; i < k && cur != null; i++) { st.push(cur); cur = cur.next; }
            if (st.size() == k) while (!st.isEmpty()) { tail.next = st.pop(); tail = tail.next; }
            else { st.clear(); tail.next = groupStart; return dummy.next; }   // short tail stays in order
        }
        tail.next = null;
        return dummy.next;
    }

    static ListNode recursive(ListNode head, int k) {
        ListNode node = head;
        for (int i = 0; i < k; i++) { if (node == null) return head; node = node.next; }
        ListNode prev = recursive(node, k), cur = head;      // reversed rest becomes our tail's next
        for (int i = 0; i < k; i++) { ListNode nx = cur.next; cur.next = prev; prev = cur; cur = nx; }
        return prev;
    }

    static ListNode reverseKGroup(ListNode head, int k) {
        ListNode dummy = new ListNode(0); dummy.next = head;
        ListNode groupPrev = dummy;
        while (true) {
            ListNode kth = groupPrev;
            for (int i = 0; i < k && kth != null; i++) kth = kth.next;
            if (kth == null) break;
            ListNode groupNext = kth.next, prev = groupNext, cur = groupPrev.next;
            while (cur != groupNext) { ListNode nx = cur.next; cur.next = prev; prev = cur; cur = nx; }
            ListNode first = groupPrev.next;
            groupPrev.next = kth;
            groupPrev = first;
        }
        return dummy.next;
    }

    static ListNode build(List<Integer> v) { ListNode d = new ListNode(0), t = d; for (int x : v) { t.next = new ListNode(x); t = t.next; } return d.next; }
    static List<Integer> list(ListNode h) { List<Integer> l = new ArrayList<>(); for (; h != null; h = h.next) l.add(h.val); return l; }

    public static void main(String[] args) {
        check(list(reverseKGroup(build(List.of(1, 2, 3, 4, 5)), 2)), List.of(2, 1, 4, 3, 5));
        check(list(reverseKGroup(build(List.of(1, 2, 3, 4, 5)), 3)), List.of(3, 2, 1, 4, 5));
        Random r = new Random(23);
        for (int t = 0; t < 2000; t++) {
            int n = r.nextInt(13), k = r.nextInt(5) + 1;
            List<Integer> v = new ArrayList<>();
            for (int i = 0; i < n; i++) v.add(i);
            List<Integer> e = arrayOracle(v, k);
            check(list(stack(build(v), k)), e); check(list(recursive(build(v), k)), e); check(list(reverseKGroup(build(v), k)), e);
        }
        System.out.println("ReverseNodesInKGroup: all tests passed");
    }

    static void check(Object got, Object exp) {
        if (!got.equals(exp)) throw new AssertionError("expected " + exp + " but got " + got);
    }
}
