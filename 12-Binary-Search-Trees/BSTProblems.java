import java.util.*;

/**
 * LC 98 Validate: brute = inorder to list + check sorted O(n) space | bounds (long min/max) O(h)
 * LC 230 Kth smallest: inorder list O(n) | iterative inorder stop at k O(h + k)
 * LC 235 LCA in BST: first node where p and q split. O(h)
 * LC 108 Sorted array -> balanced BST: mid as root. O(n)
 * LC 1008 BST from preorder: brute insert each O(n^2) worst | upper-bound recursion O(n)
 * Inorder successor: smallest node > key. O(h)
 * LC 173 BST iterator: push left spine; next() pops and pushes right's left spine. avg O(1), O(h) space
 * LC 653 Two sum in BST: hash set O(n) | two iterators (next smallest, next largest) O(h) space
 * LC 99 Recover BST: inorder finds first and second violation; swap values. O(n) (Morris -> O(1) space)
 * Largest BST subtree: brute validate every node O(n^2) | postorder returning {isBST, min, max, size} O(n)
 */
public class BSTProblems {

    static class TreeNode { int val; TreeNode left, right; TreeNode(int v) { val = v; } }

    static boolean isValidBSTInorder(TreeNode root) {
        List<Integer> v = new ArrayList<>(); inorder(root, v);
        for (int i = 1; i < v.size(); i++) if (v.get(i) <= v.get(i - 1)) return false;
        return true;
    }

    static boolean isValidBST(TreeNode n) { return valid(n, Long.MIN_VALUE, Long.MAX_VALUE); }
    static boolean valid(TreeNode n, long lo, long hi) {
        if (n == null) return true;
        if (n.val <= lo || n.val >= hi) return false;
        return valid(n.left, lo, n.val) && valid(n.right, n.val, hi);
    }

    static int kthSmallest(TreeNode root, int k) {
        Deque<TreeNode> st = new ArrayDeque<>();
        TreeNode cur = root;
        while (true) {
            while (cur != null) { st.push(cur); cur = cur.left; }
            cur = st.pop();
            if (--k == 0) return cur.val;
            cur = cur.right;
        }
    }

    static TreeNode lcaBST(TreeNode n, int p, int q) {
        while (n != null) {
            if (p < n.val && q < n.val) n = n.left;
            else if (p > n.val && q > n.val) n = n.right;
            else return n;
        }
        return null;
    }

    static TreeNode sortedArrayToBST(int[] a) { return sab(a, 0, a.length - 1); }
    static TreeNode sab(int[] a, int l, int r) {
        if (l > r) return null;
        int m = (l + r) / 2;
        TreeNode n = new TreeNode(a[m]);
        n.left = sab(a, l, m - 1); n.right = sab(a, m + 1, r);
        return n;
    }

    static int idx;
    static TreeNode bstFromPreorder(int[] pre) { idx = 0; return bfp(pre, Integer.MAX_VALUE); }
    static TreeNode bfp(int[] pre, int bound) {
        if (idx == pre.length || pre[idx] > bound) return null;
        TreeNode n = new TreeNode(pre[idx++]);
        n.left = bfp(pre, n.val);
        n.right = bfp(pre, bound);
        return n;
    }

    static TreeNode successor(TreeNode root, int key) {
        TreeNode ans = null;
        while (root != null) {
            if (root.val > key) { ans = root; root = root.left; } else root = root.right;
        }
        return ans;
    }

    static class BSTIterator {
        private final Deque<TreeNode> st = new ArrayDeque<>();
        private final boolean reverse;
        BSTIterator(TreeNode root) { this(root, false); }
        BSTIterator(TreeNode root, boolean reverse) { this.reverse = reverse; pushAll(root); }
        boolean hasNext() { return !st.isEmpty(); }
        int next() { TreeNode n = st.pop(); pushAll(reverse ? n.left : n.right); return n.val; }
        private void pushAll(TreeNode n) { while (n != null) { st.push(n); n = reverse ? n.right : n.left; } }
    }

    static boolean findTarget(TreeNode root, int k) {
        BSTIterator lo = new BSTIterator(root, false), hi = new BSTIterator(root, true);
        int a = lo.next(), b = hi.next();
        while (a < b) {
            if (a + b == k) return true;
            if (a + b < k) a = lo.next(); else b = hi.next();
        }
        return false;
    }

    static TreeNode first, second, prev;
    static void recoverTree(TreeNode root) {
        first = second = prev = null;
        rec(root);
        int t = first.val; first.val = second.val; second.val = t;
    }
    static void rec(TreeNode n) {
        if (n == null) return;
        rec(n.left);
        if (prev != null && prev.val > n.val) { if (first == null) first = prev; second = n; }
        prev = n;
        rec(n.right);
    }

    /** returns {isBST(1/0), min, max, size} ; tracks best in field. */
    static int largest;
    static int largestBST(TreeNode root) { largest = 0; lb(root); return largest; }
    static int[] lb(TreeNode n) {
        if (n == null) return new int[]{1, Integer.MAX_VALUE, Integer.MIN_VALUE, 0};
        int[] l = lb(n.left), r = lb(n.right);
        if (l[0] == 1 && r[0] == 1 && l[2] < n.val && n.val < r[1]) {
            int size = l[3] + r[3] + 1;
            largest = Math.max(largest, size);
            return new int[]{1, Math.min(l[1], n.val), Math.max(r[2], n.val), size};
        }
        return new int[]{0, 0, 0, 0};
    }

    static void inorder(TreeNode n, List<Integer> o) { if (n == null) return; inorder(n.left, o); o.add(n.val); inorder(n.right, o); }

    static TreeNode build(Integer... v) {
        if (v.length == 0 || v[0] == null) return null;
        TreeNode root = new TreeNode(v[0]);
        Queue<TreeNode> q = new ArrayDeque<>(); q.offer(root);
        for (int i = 1; i < v.length; ) {
            TreeNode n = q.poll();
            if (i < v.length && v[i] != null) { n.left = new TreeNode(v[i]); q.offer(n.left); } i++;
            if (i < v.length && v[i] != null) { n.right = new TreeNode(v[i]); q.offer(n.right); } i++;
        }
        return root;
    }

    public static void main(String[] args) {
        TreeNode good = build(5, 3, 7, 2, 4, 6, 8), bad = build(5, 4, 6, null, null, 3, 7);
        check(isValidBST(good), true); check(isValidBST(bad), false);
        check(isValidBSTInorder(good), true); check(isValidBSTInorder(bad), false);
        check(isValidBST(build(2147483647)), true);
        check(kthSmallest(good, 3), 4);
        check(lcaBST(good, 2, 4).val, 3); check(lcaBST(good, 2, 8).val, 5);
        List<Integer> in = new ArrayList<>(); inorder(sortedArrayToBST(new int[]{-10, -3, 0, 5, 9}), in);
        check(in, List.of(-10, -3, 0, 5, 9));
        in.clear(); inorder(bstFromPreorder(new int[]{8, 5, 1, 7, 10, 12}), in);
        check(in, List.of(1, 5, 7, 8, 10, 12));
        check(successor(good, 4).val, 5); check(successor(good, 8) == null, true);
        BSTIterator it = new BSTIterator(good); StringBuilder sb = new StringBuilder();
        while (it.hasNext()) sb.append(it.next());
        check(sb.toString(), "2345678");
        check(findTarget(good, 9), true); check(findTarget(good, 28), false);
        TreeNode swapped = build(3, 1, 4, null, null, 2);
        recoverTree(swapped); in.clear(); inorder(swapped, in); check(in, List.of(1, 2, 3, 4));
        check(largestBST(build(10, 5, 15, 1, 8, null, 7)), 3);
        System.out.println("BSTProblems: all tests passed");
    }

    static void check(Object got, Object exp) {
        if (!got.equals(exp)) throw new AssertionError("expected " + exp + " but got " + got);
    }
}
