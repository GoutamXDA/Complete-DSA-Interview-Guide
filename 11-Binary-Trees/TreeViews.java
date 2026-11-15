import java.util.*;

/**
 * LC 199 Right view: BFS last of each level | DFS (root, right, left) — first node seen at each new depth.
 * Left view: mirror.
 * Top view: BFS with horizontal distance (hd); first node per hd. Bottom view: last node per hd.
 *   (BFS, not DFS — DFS can see a deeper node first.)
 * LC 987 Vertical order: collect (col, row, val), sort, group by col.
 * Boundary traversal (anti-clockwise): root, left boundary (no leaves), all leaves, right boundary reversed.
 * LC 662 Max width: index children 2i+1 / 2i+2; normalise by subtracting level's first index to avoid overflow.
 */
public class TreeViews {

    static class TreeNode { int val; TreeNode left, right; TreeNode(int v) { val = v; } }

    static List<Integer> rightViewBFS(TreeNode root) {
        List<Integer> res = new ArrayList<>();
        Queue<TreeNode> q = new ArrayDeque<>();
        if (root != null) q.offer(root);
        while (!q.isEmpty()) {
            int size = q.size();
            for (int i = 0; i < size; i++) {
                TreeNode n = q.poll();
                if (i == size - 1) res.add(n.val);
                if (n.left != null) q.offer(n.left);
                if (n.right != null) q.offer(n.right);
            }
        }
        return res;
    }

    static List<Integer> rightViewDFS(TreeNode root) { List<Integer> r = new ArrayList<>(); rv(root, 0, r, true); return r; }
    static List<Integer> leftViewDFS(TreeNode root) { List<Integer> r = new ArrayList<>(); rv(root, 0, r, false); return r; }

    static void rv(TreeNode n, int depth, List<Integer> res, boolean right) {
        if (n == null) return;
        if (depth == res.size()) res.add(n.val);
        rv(right ? n.right : n.left, depth + 1, res, right);
        rv(right ? n.left : n.right, depth + 1, res, right);
    }

    static List<Integer> topOrBottomView(TreeNode root, boolean top) {
        TreeMap<Integer, Integer> map = new TreeMap<>();
        Queue<Object[]> q = new ArrayDeque<>();
        if (root != null) q.offer(new Object[]{root, 0});
        while (!q.isEmpty()) {
            Object[] cur = q.poll();
            TreeNode n = (TreeNode) cur[0]; int hd = (int) cur[1];
            if (top) map.putIfAbsent(hd, n.val); else map.put(hd, n.val);
            if (n.left != null) q.offer(new Object[]{n.left, hd - 1});
            if (n.right != null) q.offer(new Object[]{n.right, hd + 1});
        }
        return new ArrayList<>(map.values());
    }

    static List<List<Integer>> verticalTraversal(TreeNode root) {
        List<int[]> nodes = new ArrayList<>(); // col, row, val
        collect(root, 0, 0, nodes);
        nodes.sort((a, b) -> a[0] != b[0] ? a[0] - b[0] : a[1] != b[1] ? a[1] - b[1] : a[2] - b[2]);
        List<List<Integer>> res = new ArrayList<>();
        Integer prevCol = null;
        for (int[] x : nodes) {
            if (prevCol == null || x[0] != prevCol) { res.add(new ArrayList<>()); prevCol = x[0]; }
            res.get(res.size() - 1).add(x[2]);
        }
        return res;
    }

    static void collect(TreeNode n, int row, int col, List<int[]> out) {
        if (n == null) return;
        out.add(new int[]{col, row, n.val});
        collect(n.left, row + 1, col - 1, out); collect(n.right, row + 1, col + 1, out);
    }

    static List<Integer> boundary(TreeNode root) {
        List<Integer> res = new ArrayList<>();
        if (root == null) return res;
        if (!isLeaf(root)) res.add(root.val);
        for (TreeNode c = root.left; c != null; c = c.left != null ? c.left : c.right) if (!isLeaf(c)) res.add(c.val);
        leaves(root, res);
        Deque<Integer> right = new ArrayDeque<>();
        for (TreeNode c = root.right; c != null; c = c.right != null ? c.right : c.left) if (!isLeaf(c)) right.push(c.val);
        res.addAll(right);
        return res;
    }

    static boolean isLeaf(TreeNode n) { return n.left == null && n.right == null; }
    static void leaves(TreeNode n, List<Integer> res) {
        if (n == null) return;
        if (isLeaf(n)) { res.add(n.val); return; }
        leaves(n.left, res); leaves(n.right, res);
    }

    static int widthOfBinaryTree(TreeNode root) {
        int best = 0;
        Queue<Object[]> q = new ArrayDeque<>();
        q.offer(new Object[]{root, 0L});
        while (!q.isEmpty()) {
            int size = q.size(); long first = (long) q.peek()[1], last = first;
            for (int i = 0; i < size; i++) {
                Object[] cur = q.poll();
                TreeNode n = (TreeNode) cur[0]; long idx = (long) cur[1] - first;
                last = idx;
                if (n.left != null) q.offer(new Object[]{n.left, 2 * idx + 1});
                if (n.right != null) q.offer(new Object[]{n.right, 2 * idx + 2});
            }
            best = (int) Math.max(best, last + 1);
        }
        return best;
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
        TreeNode t = build(1, 2, 3, null, 5, null, 4);
        check(rightViewBFS(t), List.of(1, 3, 4)); check(rightViewDFS(t), List.of(1, 3, 4));
        check(leftViewDFS(t), List.of(1, 2, 5));
        TreeNode v = build(1, 2, 3, 4, 5, 6, 7);
        check(topOrBottomView(v, true), List.of(4, 2, 1, 3, 7));
        check(topOrBottomView(v, false), List.of(4, 2, 6, 3, 7)); // 5 and 6 share hd 0; 6 is later in BFS
        check(verticalTraversal(build(3, 9, 20, null, null, 15, 7)).toString(), "[[9], [3, 15], [20], [7]]");
        check(verticalTraversal(v).toString(), "[[4], [2], [1, 5, 6], [3], [7]]");
        TreeNode b = build(1, 2, 3, 4, 5, 6, 7, null, null, 8, 9);
        check(boundary(b), List.of(1, 2, 4, 8, 9, 6, 7, 3));
        check(widthOfBinaryTree(build(1, 3, 2, 5, 3, null, 9)), 4);
        System.out.println("TreeViews: all tests passed");
    }

    static void check(Object got, Object exp) {
        if (!got.equals(exp)) throw new AssertionError("expected " + exp + " but got " + got);
    }
}
