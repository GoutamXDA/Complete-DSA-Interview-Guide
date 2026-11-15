import java.util.*;

/**
 * LC 104 Max depth: DFS 1 + max(l, r) | BFS count levels
 * LC 111 Min depth: BFS stops at first leaf (better than DFS for wide trees). Careful: a node with one child isn't a leaf.
 * LC 110 Balanced: brute = height() at every node O(n^2) | bottom-up returning -1 if unbalanced O(n)
 * LC 543 Diameter: brute = height(l)+height(r) for every node O(n^2) | one pass O(n)
 * LC 124 Max path sum: gain(n) = n.val + max(0, gain(l), gain(r)); answer through n uses both. O(n)
 * LC 100/101/226 Same / symmetric / invert.
 * LC 113 Path sum II: root-to-leaf paths with target sum, backtracking.
 */
public class TreeProperties {

    static class TreeNode { int val; TreeNode left, right; TreeNode(int v) { val = v; } }

    static int maxDepth(TreeNode n) { return n == null ? 0 : 1 + Math.max(maxDepth(n.left), maxDepth(n.right)); }

    static int maxDepthBFS(TreeNode root) {
        if (root == null) return 0;
        Queue<TreeNode> q = new ArrayDeque<>(List.of(root)); int d = 0;
        while (!q.isEmpty()) {
            d++;
            for (int i = q.size(); i > 0; i--) { TreeNode n = q.poll(); if (n.left != null) q.offer(n.left); if (n.right != null) q.offer(n.right); }
        }
        return d;
    }

    static int minDepth(TreeNode root) {
        if (root == null) return 0;
        Queue<TreeNode> q = new ArrayDeque<>(List.of(root)); int d = 0;
        while (!q.isEmpty()) {
            d++;
            for (int i = q.size(); i > 0; i--) {
                TreeNode n = q.poll();
                if (n.left == null && n.right == null) return d;
                if (n.left != null) q.offer(n.left);
                if (n.right != null) q.offer(n.right);
            }
        }
        return d;
    }

    static boolean isBalancedBrute(TreeNode n) {
        if (n == null) return true;
        return Math.abs(maxDepth(n.left) - maxDepth(n.right)) <= 1 && isBalancedBrute(n.left) && isBalancedBrute(n.right);
    }

    static boolean isBalanced(TreeNode n) { return check(n) != -1; }

    static int check(TreeNode n) {
        if (n == null) return 0;
        int l = check(n.left); if (l == -1) return -1;
        int r = check(n.right); if (r == -1) return -1;
        return Math.abs(l - r) > 1 ? -1 : 1 + Math.max(l, r);
    }

    static int diameterBrute(TreeNode n) {
        if (n == null) return 0;
        return Math.max(maxDepth(n.left) + maxDepth(n.right), Math.max(diameterBrute(n.left), diameterBrute(n.right)));
    }

    static int diameter;
    static int diameterOfBinaryTree(TreeNode root) { diameter = 0; height(root); return diameter; }
    static int height(TreeNode n) {
        if (n == null) return 0;
        int l = height(n.left), r = height(n.right);
        diameter = Math.max(diameter, l + r);  // edges through n
        return 1 + Math.max(l, r);
    }

    static int maxPath;
    static int maxPathSum(TreeNode root) { maxPath = Integer.MIN_VALUE; gain(root); return maxPath; }
    static int gain(TreeNode n) {
        if (n == null) return 0;
        int l = Math.max(0, gain(n.left)), r = Math.max(0, gain(n.right)); // drop negative branches
        maxPath = Math.max(maxPath, n.val + l + r);
        return n.val + Math.max(l, r);
    }

    static boolean isSameTree(TreeNode a, TreeNode b) {
        if (a == null || b == null) return a == b;
        return a.val == b.val && isSameTree(a.left, b.left) && isSameTree(a.right, b.right);
    }

    static boolean isSymmetric(TreeNode root) { return root == null || mirror(root.left, root.right); }
    static boolean mirror(TreeNode a, TreeNode b) {
        if (a == null || b == null) return a == b;
        return a.val == b.val && mirror(a.left, b.right) && mirror(a.right, b.left);
    }

    static TreeNode invertTree(TreeNode n) {
        if (n == null) return null;
        TreeNode t = n.left; n.left = invertTree(n.right); n.right = invertTree(t);
        return n;
    }

    static List<List<Integer>> pathSum(TreeNode root, int target) {
        List<List<Integer>> res = new ArrayList<>();
        paths(root, target, new ArrayList<>(), res);
        return res;
    }

    static void paths(TreeNode n, int rem, List<Integer> path, List<List<Integer>> res) {
        if (n == null) return;
        path.add(n.val);
        if (n.left == null && n.right == null && rem == n.val) res.add(new ArrayList<>(path));
        paths(n.left, rem - n.val, path, res); paths(n.right, rem - n.val, path, res);
        path.remove(path.size() - 1);
    }

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
        TreeNode t = build(3, 9, 20, null, null, 15, 7);
        check(maxDepth(t), 3); check(maxDepthBFS(t), 3); check(minDepth(t), 2);
        check(minDepth(build(2, null, 3, null, 4)), 3);
        check(isBalanced(t), true); check(isBalancedBrute(t), true);
        TreeNode u = build(1, 2, 2, 3, 3, null, null, 4, 4);
        check(isBalanced(u), false); check(isBalancedBrute(u), false);
        TreeNode d = build(1, 2, 3, 4, 5);
        check(diameterOfBinaryTree(d), 3); check(diameterBrute(d), 3);
        check(maxPathSum(build(-10, 9, 20, null, null, 15, 7)), 42); check(maxPathSum(build(-3)), -3);
        check(isSameTree(build(1, 2, 3), build(1, 2, 3)), true); check(isSameTree(build(1, 2), build(1, null, 2)), false);
        check(isSymmetric(build(1, 2, 2, 3, 4, 4, 3)), true); check(isSymmetric(build(1, 2, 2, null, 3, null, 3)), false);
        check(isSameTree(invertTree(build(4, 2, 7, 1, 3, 6, 9)), build(4, 7, 2, 9, 6, 3, 1)), true);
        check(pathSum(build(5, 4, 8, 11, null, 13, 4, 7, 2, null, null, 5, 1), 22).toString(), "[[5, 4, 11, 2], [5, 8, 4, 5]]");
        System.out.println("TreeProperties: all tests passed");
    }

    static void check(Object got, Object exp) {
        if (!got.equals(exp)) throw new AssertionError("expected " + exp + " but got " + got);
    }
}
