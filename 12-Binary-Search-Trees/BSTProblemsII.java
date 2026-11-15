import java.util.*;

/**
 * LC 285  Inorder successor (+ predecessor) in BST: brute inorder list + scan O(n) | walk from root tracking candidate O(h)
 * LC 538  Convert BST to greater tree: inorder list + suffix sums O(n) space | reverse inorder with running sum O(h)
 * LC 669  Trim a BST to [low, high]: if node < low return trim(right); if > high return trim(left). O(n)
 * LC 1382 Balance a BST: inorder to sorted list, rebuild from middle. O(n)
 * LC 96   Unique BSTs (count): recursion O(exponential) | DP dp[n] = sum dp[i-1]*dp[n-i] O(n^2) | Catalan C(2n,n)/(n+1) O(n)
 * LC 95   Unique BSTs II (generate): for each root i, combine all left trees of [lo, i-1] with right trees of [i+1, hi] (memo by range).
 */
public class BSTProblemsII {

    static class TreeNode { int val; TreeNode left, right; TreeNode(int v) { val = v; } TreeNode(int v, TreeNode l, TreeNode r) { val = v; left = l; right = r; } }

    static Integer successorBrute(TreeNode root, int key) {
        List<Integer> in = new ArrayList<>(); inorder(root, in);
        for (int v : in) if (v > key) return v;
        return null;
    }

    static Integer successor(TreeNode root, int key) {
        Integer ans = null;
        while (root != null) { if (root.val > key) { ans = root.val; root = root.left; } else root = root.right; }
        return ans;
    }

    static Integer predecessor(TreeNode root, int key) {
        Integer ans = null;
        while (root != null) { if (root.val < key) { ans = root.val; root = root.right; } else root = root.left; }
        return ans;
    }

    static TreeNode convertBSTBrute(TreeNode root) {
        List<TreeNode> nodes = new ArrayList<>(); inorderNodes(root, nodes);
        int suffix = 0;
        for (int i = nodes.size() - 1; i >= 0; i--) { suffix += nodes.get(i).val; nodes.get(i).val = suffix; }
        return root;
    }

    static int running;
    static TreeNode convertBST(TreeNode root) { running = 0; revIn(root); return root; }
    static void revIn(TreeNode n) {
        if (n == null) return;
        revIn(n.right);
        running += n.val; n.val = running;
        revIn(n.left);
    }

    static TreeNode trimBST(TreeNode n, int lo, int hi) {
        if (n == null) return null;
        if (n.val < lo) return trimBST(n.right, lo, hi);
        if (n.val > hi) return trimBST(n.left, lo, hi);
        n.left = trimBST(n.left, lo, hi); n.right = trimBST(n.right, lo, hi);
        return n;
    }

    static TreeNode balanceBST(TreeNode root) {
        List<TreeNode> nodes = new ArrayList<>(); inorderNodes(root, nodes);
        return rebuild(nodes, 0, nodes.size() - 1);
    }

    static TreeNode rebuild(List<TreeNode> n, int l, int r) {
        if (l > r) return null;
        int m = (l + r) / 2;
        TreeNode root = n.get(m);
        root.left = rebuild(n, l, m - 1); root.right = rebuild(n, m + 1, r);
        return root;
    }

    static int numTreesRec(int n) {
        if (n <= 1) return 1;
        int s = 0;
        for (int i = 1; i <= n; i++) s += numTreesRec(i - 1) * numTreesRec(n - i);
        return s;
    }

    static int numTrees(int n) {
        int[] dp = new int[n + 1]; dp[0] = dp[1] = 1;
        for (int k = 2; k <= n; k++) for (int root = 1; root <= k; root++) dp[k] += dp[root - 1] * dp[k - root];
        return dp[n];
    }

    static long numTreesCatalan(int n) {
        long c = 1;
        for (int i = 0; i < n; i++) c = c * 2 * (2 * i + 1) / (i + 2);   // C(i+1) = C(i) * 2(2i+1)/(i+2)
        return c;
    }

    static List<TreeNode> generateTrees(int n) { return gen(1, n, new HashMap<>()); }

    static List<TreeNode> gen(int lo, int hi, Map<String, List<TreeNode>> memo) {
        String key = lo + "," + hi;
        if (memo.containsKey(key)) return memo.get(key);
        List<TreeNode> res = new ArrayList<>();
        if (lo > hi) { res.add(null); return res; }
        for (int i = lo; i <= hi; i++)
            for (TreeNode l : gen(lo, i - 1, memo))
                for (TreeNode r : gen(i + 1, hi, memo)) res.add(new TreeNode(i, l, r));  // subtrees are shared (fine for read-only use)
        memo.put(key, res);
        return res;
    }

    static void inorder(TreeNode n, List<Integer> o) { if (n == null) return; inorder(n.left, o); o.add(n.val); inorder(n.right, o); }
    static void inorderNodes(TreeNode n, List<TreeNode> o) { if (n == null) return; inorderNodes(n.left, o); o.add(n); inorderNodes(n.right, o); }
    static int height(TreeNode n) { return n == null ? 0 : 1 + Math.max(height(n.left), height(n.right)); }
    static String pre(TreeNode n) { return n == null ? "#" : n.val + "(" + pre(n.left) + "," + pre(n.right) + ")"; }

    static TreeNode insert(TreeNode r, int v) {
        if (r == null) return new TreeNode(v);
        if (v < r.val) r.left = insert(r.left, v); else r.right = insert(r.right, v);
        return r;
    }

    static TreeNode bst(int... vals) { TreeNode r = null; for (int v : vals) r = insert(r, v); return r; }

    public static void main(String[] args) {
        TreeNode t = bst(20, 8, 22, 4, 12, 10, 14);
        check(successor(t, 8), 10); check(successorBrute(t, 8), 10); check(successor(t, 14), 20); check(successor(t, 22) == null, true);
        check(predecessor(t, 10), 8); check(predecessor(t, 20), 14); check(predecessor(t, 4) == null, true);
        List<Integer> a = new ArrayList<>(), b = new ArrayList<>();
        inorder(convertBST(bst(4, 1, 6, 0, 2, 5, 7, 3, 8)), a); inorder(convertBSTBrute(bst(4, 1, 6, 0, 2, 5, 7, 3, 8)), b);
        check(a, List.of(36, 36, 35, 33, 30, 26, 21, 15, 8)); check(b, a);
        List<Integer> tr = new ArrayList<>(); inorder(trimBST(bst(3, 0, 4, 2, 1), 1, 3), tr); check(tr, List.of(1, 2, 3));
        TreeNode skew = bst(1, 2, 3, 4, 5, 6, 7);
        TreeNode bal = balanceBST(skew);
        List<Integer> bi = new ArrayList<>(); inorder(bal, bi);
        check(bi, List.of(1, 2, 3, 4, 5, 6, 7)); check(height(bal), 3);
        for (int n = 1; n <= 10; n++) { check(numTrees(n), numTreesRec(n)); check(numTreesCatalan(n), (long) numTrees(n)); }
        check(numTrees(3), 5);
        List<TreeNode> all = generateTrees(3);
        check(all.size(), 5);
        Set<String> shapes = new HashSet<>();
        for (TreeNode r : all) { List<Integer> in = new ArrayList<>(); inorder(r, in); check(in, List.of(1, 2, 3)); shapes.add(pre(r)); }
        check(shapes.size(), 5);
        check(generateTrees(4).size(), 14);
        System.out.println("BSTProblemsII: all tests passed");
    }

    static void check(Object got, Object exp) {
        if (!got.equals(exp)) throw new AssertionError("expected " + exp + " but got " + got);
    }
}
