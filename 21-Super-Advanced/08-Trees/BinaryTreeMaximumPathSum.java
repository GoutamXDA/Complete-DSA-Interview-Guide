import java.util.*;

/**
 * LC 124 — Binary Tree Maximum Path Sum. Explanation: BinaryTreeMaximumPathSum.md
 * 1. Every node as the bend, separate downward searches     O(n^2)
 * 2. Single post-order DFS (gain + global best)            O(n)
 * 3. Iterative post-order (no recursion)                    O(n)
 * Oracle: brute force over every pair of nodes' connecting path.
 */
public class BinaryTreeMaximumPathSum {

    static class TreeNode { int val; TreeNode left, right; TreeNode(int v) { val = v; } }

    static int bestDown(TreeNode n) {                     // best downward path starting at n (may be just n)
        if (n == null) return Integer.MIN_VALUE;
        int c = Math.max(bestDown(n.left), bestDown(n.right));
        return c == Integer.MIN_VALUE ? n.val : n.val + Math.max(0, c);
    }

    static int everyBend(TreeNode n) {
        if (n == null) return Integer.MIN_VALUE;
        int l = Math.max(0, n.left == null ? 0 : bestDown(n.left)), r = Math.max(0, n.right == null ? 0 : bestDown(n.right));
        return Math.max(n.val + l + r, Math.max(everyBend(n.left), everyBend(n.right)));
    }

    static int best;

    static int maxPathSum(TreeNode root) { best = Integer.MIN_VALUE; gain(root); return best; }

    static int gain(TreeNode n) {
        if (n == null) return 0;
        int l = Math.max(0, gain(n.left)), r = Math.max(0, gain(n.right));
        best = Math.max(best, n.val + l + r);
        return n.val + Math.max(l, r);
    }

    static int iterative(TreeNode root) {
        Map<TreeNode, Integer> g = new HashMap<>();
        Deque<TreeNode> st = new ArrayDeque<>(List.of(root));
        List<TreeNode> order = new ArrayList<>();
        while (!st.isEmpty()) {                            // root, right, left -> reversed = post-order
            TreeNode n = st.pop(); order.add(n);
            if (n.left != null) st.push(n.left);
            if (n.right != null) st.push(n.right);
        }
        int res = Integer.MIN_VALUE;
        for (int i = order.size() - 1; i >= 0; i--) {
            TreeNode n = order.get(i);
            int l = Math.max(0, g.getOrDefault(n.left, 0)), r = Math.max(0, g.getOrDefault(n.right, 0));
            res = Math.max(res, n.val + l + r);
            g.put(n, n.val + Math.max(l, r));
        }
        return res;
    }

    /** Oracle: max over all pairs (u, v) of the sum on the unique u-v path. */
    static int pairsOracle(TreeNode root) {
        List<TreeNode> nodes = new ArrayList<>();
        Map<TreeNode, TreeNode> parent = new HashMap<>();
        Map<TreeNode, Integer> depth = new HashMap<>();
        Deque<TreeNode> st = new ArrayDeque<>(List.of(root));
        depth.put(root, 0);
        while (!st.isEmpty()) {
            TreeNode n = st.pop(); nodes.add(n);
            for (TreeNode c : new TreeNode[]{n.left, n.right}) if (c != null) { parent.put(c, n); depth.put(c, depth.get(n) + 1); st.push(c); }
        }
        int res = Integer.MIN_VALUE;
        for (TreeNode u : nodes) for (TreeNode v : nodes) {
            TreeNode a = u, b = v; int s = 0;
            while (depth.get(a) > depth.get(b)) { s += a.val; a = parent.get(a); }
            while (depth.get(b) > depth.get(a)) { s += b.val; b = parent.get(b); }
            while (a != b) { s += a.val + b.val; a = parent.get(a); b = parent.get(b); }
            res = Math.max(res, s + a.val);
        }
        return res;
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
            TreeNode p = all.get(r.nextInt(all.size()));
            TreeNode c = new TreeNode(r.nextInt(21) - 10);
            if (r.nextBoolean()) { if (p.left == null) { p.left = c; all.add(c); } } else if (p.right == null) { p.right = c; all.add(c); }
        }
        return root;
    }

    public static void main(String[] args) {
        check(maxPathSum(build(1, 2, 3)), 6); check(maxPathSum(build(-10, 9, 20, null, null, 15, 7)), 42); check(maxPathSum(build(-3)), -3);
        check(maxPathSum(build(-2, -1)), -1);
        Random r = new Random(33);
        for (int t = 0; t < 1000; t++) {
            TreeNode root = random(r, r.nextInt(12) + 1);
            int e = pairsOracle(root);
            check(maxPathSum(root), e); check(iterative(root), e); check(everyBend(root), e);
        }
        System.out.println("BinaryTreeMaximumPathSum: all tests passed");
    }

    static void check(Object got, Object exp) {
        if (!got.equals(exp)) throw new AssertionError("expected " + exp + " but got " + got);
    }
}
