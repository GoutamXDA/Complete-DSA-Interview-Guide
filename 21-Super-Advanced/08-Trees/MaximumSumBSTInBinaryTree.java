import java.util.*;

/**
 * LC 1373 — Maximum Sum BST in Binary Tree. Explanation: MaximumSumBSTInBinaryTree.md
 * 1. Validate + sum every subtree     O(n^2)
 * 2. Post-order summary tuple         O(n)
 */
public class MaximumSumBSTInBinaryTree {

    static class TreeNode { int val; TreeNode left, right; TreeNode(int v) { val = v; } }

    static int brute(TreeNode root) {
        int best = 0;
        Deque<TreeNode> st = new ArrayDeque<>();
        if (root != null) st.push(root);
        while (!st.isEmpty()) {
            TreeNode n = st.pop();
            if (valid(n, Long.MIN_VALUE, Long.MAX_VALUE)) best = Math.max(best, sum(n));
            if (n.left != null) st.push(n.left);
            if (n.right != null) st.push(n.right);
        }
        return best;
    }

    static boolean valid(TreeNode n, long lo, long hi) {
        if (n == null) return true;
        return n.val > lo && n.val < hi && valid(n.left, lo, n.val) && valid(n.right, n.val, hi);
    }

    static int sum(TreeNode n) { return n == null ? 0 : n.val + sum(n.left) + sum(n.right); }

    static int best;

    static int maxSumBST(TreeNode root) { best = 0; post(root); return best; }

    /** returns {isBST (1/0), min, max, sum} using longs for safe sentinels */
    static long[] post(TreeNode n) {
        if (n == null) return new long[]{1, Long.MAX_VALUE, Long.MIN_VALUE, 0};
        long[] L = post(n.left), R = post(n.right);
        if (L[0] == 1 && R[0] == 1 && L[2] < n.val && n.val < R[1]) {
            long s = L[3] + R[3] + n.val;
            best = (int) Math.max(best, s);
            return new long[]{1, Math.min(L[1], n.val), Math.max(R[2], n.val), s};
        }
        return new long[]{0, 0, 0, 0};
    }

    static TreeNode build(Integer... v) {
        TreeNode root = new TreeNode(v[0]);
        Queue<TreeNode> q = new ArrayDeque<>(List.of(root));
        for (int i = 1; i < v.length; ) {
            TreeNode n = q.poll();
            if (i < v.length && v[i] != null) { n.left = new TreeNode(v[i]); q.offer(n.left); } i++;
            if (i < v.length && v[i] != null) { n.right = new TreeNode(v[i]); q.offer(n.right); } i++;
        }
        return root;
    }

    static TreeNode random(Random r, int n) {
        TreeNode root = new TreeNode(r.nextInt(21) - 10);
        List<TreeNode> all = new ArrayList<>(List.of(root));
        while (all.size() < n) {
            TreeNode p = all.get(r.nextInt(all.size())), c = new TreeNode(r.nextInt(21) - 10);
            if (r.nextBoolean()) { if (p.left == null) { p.left = c; all.add(c); } } else if (p.right == null) { p.right = c; all.add(c); }
        }
        return root;
    }

    public static void main(String[] args) {
        check(maxSumBST(build(1, 4, 3, 2, 4, 2, 5, null, null, null, null, null, null, 4, 6)), 20);
        check(maxSumBST(build(4, 3, null, 1, 2)), 2); check(maxSumBST(build(-4, -2, -5)), 0);
        Random r = new Random(36);
        for (int t = 0; t < 2000; t++) {
            TreeNode root = random(r, r.nextInt(14) + 1);
            check(maxSumBST(root), brute(root));
        }
        System.out.println("MaximumSumBSTInBinaryTree: all tests passed");
    }

    static void check(Object got, Object exp) {
        if (!got.equals(exp)) throw new AssertionError("expected " + exp + " but got " + got);
    }
}
