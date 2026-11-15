import java.util.*;

/**
 * LC 21 Merge two sorted: iterative with dummy O(1) space | recursive O(m+n) stack
 * LC 148 Sort list: copy to array + sort O(n) space | merge sort (find middle, split, merge) O(n log n)
 * LC 234 Palindrome: copy to array O(n) space | reverse second half & compare O(1) space (restore after)
 * LC 2 Add two numbers (digits stored reversed): add with carry.
 * LC 328 Odd even list: build odd chain and even chain, join.
 */
public class MergeSortList {

    static class ListNode { int val; ListNode next; ListNode(int v) { val = v; } }

    static ListNode mergeTwo(ListNode a, ListNode b) {
        ListNode dummy = new ListNode(0), t = dummy;
        while (a != null && b != null) {
            if (a.val <= b.val) { t.next = a; a = a.next; } else { t.next = b; b = b.next; }
            t = t.next;
        }
        t.next = (a != null) ? a : b;
        return dummy.next;
    }

    static ListNode mergeTwoRecursive(ListNode a, ListNode b) {
        if (a == null) return b;
        if (b == null) return a;
        if (a.val <= b.val) { a.next = mergeTwoRecursive(a.next, b); return a; }
        b.next = mergeTwoRecursive(a, b.next); return b;
    }

    static ListNode sortListArray(ListNode head) {
        List<Integer> vals = new ArrayList<>();
        for (ListNode c = head; c != null; c = c.next) vals.add(c.val);
        Collections.sort(vals);
        ListNode c = head;
        for (int v : vals) { c.val = v; c = c.next; }
        return head;
    }

    static ListNode sortList(ListNode head) {
        if (head == null || head.next == null) return head;
        ListNode slow = head, fast = head.next;           // fast starts ahead -> slow ends at first middle
        while (fast != null && fast.next != null) { slow = slow.next; fast = fast.next.next; }
        ListNode right = slow.next; slow.next = null;
        return mergeTwo(sortList(head), sortList(right));
    }

    static boolean isPalindromeArray(ListNode h) {
        List<Integer> v = new ArrayList<>();
        for (; h != null; h = h.next) v.add(h.val);
        for (int i = 0, j = v.size() - 1; i < j; i++, j--) if (!v.get(i).equals(v.get(j))) return false;
        return true;
    }

    static boolean isPalindrome(ListNode head) {
        ListNode slow = head, fast = head;
        while (fast != null && fast.next != null) { slow = slow.next; fast = fast.next.next; }
        ListNode second = reverse(slow), p = second, q = head;
        boolean ok = true;
        while (p != null) { if (p.val != q.val) { ok = false; break; } p = p.next; q = q.next; }
        reverse(second);  // restore the input (interviewers like this)
        return ok;
    }

    static ListNode reverse(ListNode h) {
        ListNode prev = null;
        while (h != null) { ListNode n = h.next; h.next = prev; prev = h; h = n; }
        return prev;
    }

    static ListNode addTwoNumbers(ListNode a, ListNode b) {
        ListNode dummy = new ListNode(0), t = dummy;
        int carry = 0;
        while (a != null || b != null || carry != 0) {
            int s = carry + (a != null ? a.val : 0) + (b != null ? b.val : 0);
            carry = s / 10;
            t.next = new ListNode(s % 10); t = t.next;
            if (a != null) a = a.next;
            if (b != null) b = b.next;
        }
        return dummy.next;
    }

    static ListNode oddEvenList(ListNode head) {
        if (head == null) return null;
        ListNode odd = head, even = head.next, evenHead = even;
        while (even != null && even.next != null) {
            odd.next = even.next; odd = odd.next;
            even.next = odd.next; even = even.next;
        }
        odd.next = evenHead;
        return head;
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
        check(str(mergeTwo(build(1, 2, 4), build(1, 3, 4))), "[1, 1, 2, 3, 4, 4]");
        check(str(mergeTwoRecursive(build(1, 2, 4), build(1, 3, 4))), "[1, 1, 2, 3, 4, 4]");
        check(str(sortList(build(-1, 5, 3, 4, 0))), "[-1, 0, 3, 4, 5]");
        check(str(sortListArray(build(4, 2, 1, 3))), "[1, 2, 3, 4]");
        ListNode p = build(1, 2, 2, 1);
        check(isPalindrome(p), true); check(str(p), "[1, 2, 2, 1]");
        check(isPalindrome(build(1, 2, 3)), false); check(isPalindromeArray(build(1, 2, 1)), true);
        check(str(addTwoNumbers(build(2, 4, 3), build(5, 6, 4))), "[7, 0, 8]");
        check(str(addTwoNumbers(build(9, 9, 9), build(1))), "[0, 0, 0, 1]");
        check(str(oddEvenList(build(1, 2, 3, 4, 5))), "[1, 3, 5, 2, 4]");
        System.out.println("MergeSortList: all tests passed");
    }

    static void check(Object got, Object exp) {
        if (!got.equals(exp)) throw new AssertionError("expected " + exp + " but got " + got);
    }
}
