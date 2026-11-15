import java.util.*;

/**
 * LC 968 — Binary Tree Cameras. Explanation: BinaryTreeCameras.md
 * 1. Brute force over camera subsets (oracle)     O(2^n n)
 * 2. Three-state tree DP                           O(n)
 * 3. Greedy post-order                             O(n)
 */
public class BinaryTreeCameras {

    static class TreeNode { int val; TreeNode left, right, parent; TreeNode(int v) { val = v; } }

    static int brute(List<TreeNode> nodes) {
        int n = nodes.size(), best = n;
        for (int mask = 0; mask < 1 << n; mask++) {
            if (Integer.bitCount(mask) >= best) continue;
            Set<TreeNode> cam = new HashSet<>();
            for (int i = 0; i < n; i++) if ((mask >> i & 1) == 1) cam.add(nodes.get(i));
            boolean ok = true;
            for (TreeNode x : nodes) {
                if (!(cam.contains(x) || cam.contains(x.parent) || cam.contains(x.left) || cam.contains(x.right))) { ok = false; break; }
            }
            if (ok) best = Integer.bitCount(mask);
        }
        return best;
    }

    static final int INF = 1_000_000;

    static int dp(TreeNode root) { int[] s = states(root); return Math.min(s[0], s[1]); }

    /** {camera here, covered by child, not covered} */
    static int[] states(TreeNode n) {
        if (n == null) return new int[]{INF, 0, INF};
        int[] L = states(n.left), R = states(n.right);
        int a = 1 + min3(L) + min3(R);
        int b = Math.min(L[0] + Math.min(R[0], R[1]), R[0] + Math.min(L[0], L[1]));
        int c = L[1] + R[1];
        return new int[]{a, Math.min(b, INF), Math.min(c, INF)};
    }

    static int min3(int[] s) { return Math.min(s[0], Math.min(s[1], s[2])); }

    static int cams;

    static int minCameraCover(TreeNode root) { cams = 0; if (dfs(root) == 0) cams++; return cams; }

    static int dfs(TreeNode n) {
        if (n == null) return 2;
        int l = dfs(n.left), r = dfs(n.right);
        if (l == 0 || r == 0) { cams++; return 1; }
        if (l == 1 || r == 1) return 2;
        return 0;
    }

    static List<TreeNode> random(Random r, int n) {
        TreeNode root = new TreeNode(0);
        List<TreeNode> all = new ArrayList<>(List.of(root));
        while (all.size() < n) {
            TreeNode p = all.get(r.nextInt(all.size())), c = new TreeNode(0);
            if (r.nextBoolean()) { if (p.left == null) { p.left = c; c.parent = p; all.add(c); } }
            else if (p.right == null) { p.right = c; c.parent = p; all.add(c); }
        }
        return all;
    }

    public static void main(String[] args) {
        TreeNode a = new TreeNode(0); a.left = new TreeNode(0); a.left.left = new TreeNode(0); a.left.right = new TreeNode(0);
        check(minCameraCover(a), 1); check(dp(a), 1);
        Random r = new Random(35);
        for (int t = 0; t < 800; t++) {
            List<TreeNode> nodes = random(r, r.nextInt(12) + 1);
            int e = brute(nodes);
            check(minCameraCover(nodes.get(0)), e); check(dp(nodes.get(0)), e);
        }
        System.out.println("BinaryTreeCameras: all tests passed");
    }

    static void check(Object got, Object exp) {
        if (!got.equals(exp)) throw new AssertionError("expected " + exp + " but got " + got);
    }
}
