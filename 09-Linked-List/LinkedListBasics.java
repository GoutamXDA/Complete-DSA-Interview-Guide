import java.util.*;

/**
 * LC 707 Design linked list (dummy head).
 * LC 206 Reverse: stack O(n) space | iterative O(1) | recursive O(n) stack
 * LC 876 Middle: count then walk (2 passes) | fast/slow (1 pass). Returns second middle for even length.
 * LC 19 Remove nth from end: length pass O(2n) | gap of n, one pass.
 * LC 237 Delete node without head: copy next value, skip next.
 */
public class LinkedListBasics {

    static class ListNode {
        int val; ListNode next;
        ListNode(int v) { val = v; }
        ListNode(int v, ListNode n) { val = v; next = n; }
    }

    /** LC 707 */
    static class MyLinkedList {
        private final ListNode dummy = new ListNode(0);
        private int size = 0;

        int get(int index) {
            if (index < 0 || index >= size) return -1;
            ListNode cur = dummy.next;
            while (index-- > 0) cur = cur.next;
            return cur.val;
        }

        void addAtHead(int v) { addAtIndex(0, v); }
        void addAtTail(int v) { addAtIndex(size, v); }

        void addAtIndex(int index, int v) {
            if (index < 0 || index > size) return;
            ListNode prev = dummy;
            while (index-- > 0) prev = prev.next;
            prev.next = new ListNode(v, prev.next);
            size++;
        }

        void deleteAtIndex(int index) {
            if (index < 0 || index >= size) return;
            ListNode prev = dummy;
            while (index-- > 0) prev = prev.next;
            prev.next = prev.next.next;
            size--;
        }
    }

    static ListNode reverseStack(ListNode head) {
        Deque<ListNode> st = new ArrayDeque<>();
        for (ListNode c = head; c != null; c = c.next) st.push(c);
        ListNode dummy = new ListNode(0), tail = dummy;
        while (!st.isEmpty()) { tail.next = st.pop(); tail = tail.next; }
        tail.next = null;
        return dummy.next;
    }

    static ListNode reverseIterative(ListNode head) {
        ListNode prev = null, cur = head;
        while (cur != null) {
            ListNode next = cur.next;
            cur.next = prev;
            prev = cur;
            cur = next;
        }
        return prev;
    }

    static ListNode reverseRecursive(ListNode head) {
        if (head == null || head.next == null) return head;
        ListNode newHead = reverseRecursive(head.next);
        head.next.next = head;   // the node after head now points back to head
        head.next = null;
        return newHead;
    }

    static ListNode middleCount(ListNode head) {
        int n = 0; for (ListNode c = head; c != null; c = c.next) n++;
        ListNode c = head; for (int i = 0; i < n / 2; i++) c = c.next;
        return c;
    }

    static ListNode middle(ListNode head) {
        ListNode slow = head, fast = head;
        while (fast != null && fast.next != null) { slow = slow.next; fast = fast.next.next; }
        return slow;
    }

    static ListNode removeNthFromEnd(ListNode head, int n) {
        ListNode dummy = new ListNode(0, head), fast = dummy, slow = dummy;
        for (int i = 0; i <= n; i++) fast = fast.next;   // gap of n+1 so slow stops BEFORE the target
        while (fast != null) { fast = fast.next; slow = slow.next; }
        slow.next = slow.next.next;
        return dummy.next;
    }

    static void deleteNode(ListNode node) { node.val = node.next.val; node.next = node.next.next; }

    // ---------- helpers ----------
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
        MyLinkedList ll = new MyLinkedList();
        ll.addAtHead(1); ll.addAtTail(3); ll.addAtIndex(1, 2);
        check(ll.get(1), 2); ll.deleteAtIndex(1); check(ll.get(1), 3);
        check(str(reverseStack(build(1, 2, 3, 4, 5))), "[5, 4, 3, 2, 1]");
        check(str(reverseIterative(build(1, 2, 3, 4, 5))), "[5, 4, 3, 2, 1]");
        check(str(reverseRecursive(build(1, 2, 3, 4, 5))), "[5, 4, 3, 2, 1]");
        check(middle(build(1, 2, 3, 4, 5)).val, 3); check(middle(build(1, 2, 3, 4, 5, 6)).val, 4);
        check(middleCount(build(1, 2, 3, 4, 5, 6)).val, 4);
        check(str(removeNthFromEnd(build(1, 2, 3, 4, 5), 2)), "[1, 2, 3, 5]");
        check(str(removeNthFromEnd(build(1), 1)), "[]");
        ListNode h = build(4, 5, 1, 9); deleteNode(h.next); check(str(h), "[4, 1, 9]");
        System.out.println("LinkedListBasics: all tests passed");
    }

    static void check(Object got, Object exp) {
        if (!got.equals(exp)) throw new AssertionError("expected " + exp + " but got " + got);
    }
}
