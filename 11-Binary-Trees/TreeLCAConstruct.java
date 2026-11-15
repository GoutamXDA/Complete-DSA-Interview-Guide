import java.util.*;

/**
 * LC 236 LCA
 *   Brute : store root->p and root->q paths, compare. O(n), O(n)
 *   Optimal: if node is p or q return it; if both sides return non-null, node is LCA. O(n), O(h)
 * LC 105 Build from preorder + inorder: preorder[0] is root; split inorder at root via index map. O(n)
 * LC 106 Build from inorder + postorder: postorder from the back; build right subtree first.
 * LC 297 Serialize / deserialize: preorder with "#" for null.
 * LC 863 Distance K: parent pointers via DFS, then BFS from target. O(n)
 * LC 222 Count complete tree nodes: O(n) DFS | if leftmost height == rightmost height -> 2^h - 1, else recurse. O(log^2 n)
 */
public class TreeLCAConstruct {

    static class TreeNode { int val; TreeNode left, right; TreeNode(int v) { val = v; } }

    static TreeNode lcaBrute(TreeNode root, TreeNode p, TreeNode q) {
        List<TreeNode> a = new ArrayList<>(), b = new ArrayList<>();
        path(root, p, a); path(root, q, b);
        TreeNode lca = null;
        for (int i = 0; i < Math.min(a.size(), b.size()) && a.get(i) == b.get(i); i++) lca = a.get(i);
        return lca;
    }

    static boolean path(TreeNode n, TreeNode target, List<TreeNode> out) {
        if (n == null) return false;
        out.add(n);
        if (n == target || path(n.left, target, out) || path(n.right, target, out)) return true;
        out.remove(out.size() - 1);
        return false;
    }

    static TreeNode lca(TreeNode n, TreeNode p, TreeNode q) {
        if (n == null || n == p || n == q) return n;
        TreeNode l = lca(n.left, p, q), r = lca(n.right, p, q);
        if (l != null && r != null) return n;
        return l != null ? l : r;
    }

    static int preIdx;
    static TreeNode buildPreIn(int[] pre, int[] in) {
        Map<Integer, Integer> pos = new HashMap<>();
        for (int i = 0; i < in.length; i++) pos.put(in[i], i);
        preIdx = 0;
        return bpi(pre, 0, in.length - 1, pos);
    }

    static TreeNode bpi(int[] pre, int lo, int hi, Map<Integer, Integer> pos) {
        if (lo > hi) return null;
        TreeNode root = new TreeNode(pre[preIdx++]);
        int m = pos.get(root.val);
        root.left = bpi(pre, lo, m - 1, pos);
        root.right = bpi(pre, m + 1, hi, pos);
        return root;
    }

    static int postIdx;
    static TreeNode buildInPost(int[] in, int[] post) {
        Map<Integer, Integer> pos = new HashMap<>();
        for (int i = 0; i < in.length; i++) pos.put(in[i], i);
        postIdx = post.length - 1;
        return bip(post, 0, in.length - 1, pos);
    }

    static TreeNode bip(int[] post, int lo, int hi, Map<Integer, Integer> pos) {
        if (lo > hi) return null;
        TreeNode root = new TreeNode(post[postIdx--]);
        int m = pos.get(root.val);
        root.right = bip(post, m + 1, hi, pos);   // right first when consuming postorder backwards
        root.left = bip(post, lo, m - 1, pos);
        return root;
    }

    static String serialize(TreeNode root) { StringBuilder sb = new StringBuilder(); ser(root, sb); return sb.toString(); }
    static void ser(TreeNode n, StringBuilder sb) {
        if (n == null) { sb.append("#,"); return; }
        sb.append(n.val).append(','); ser(n.left, sb); ser(n.right, sb);
    }

    static TreeNode deserialize(String data) {
        Deque<String> tokens = new ArrayDeque<>(Arrays.asList(data.split(",")));
        return des(tokens);
    }
    static TreeNode des(Deque<String> t) {
        String s = t.poll();
        if (s.equals("#")) return null;
        TreeNode n = new TreeNode(Integer.parseInt(s));
        n.left = des(t); n.right = des(t);
        return n;
    }

    static List<Integer> distanceK(TreeNode root, TreeNode target, int k) {
        Map<TreeNode, TreeNode> parent = new HashMap<>();
        Deque<TreeNode> st = new ArrayDeque<>(List.of(root));
        while (!st.isEmpty()) {
            TreeNode n = st.pop();
            if (n.left != null) { parent.put(n.left, n); st.push(n.left); }
            if (n.right != null) { parent.put(n.right, n); st.push(n.right); }
        }
        Set<TreeNode> seen = new HashSet<>(List.of(target));
        Queue<TreeNode> q = new ArrayDeque<>(List.of(target));
        for (int d = 0; d < k && !q.isEmpty(); d++)
            for (int i = q.size(); i > 0; i--) {
                TreeNode n = q.poll();
                for (TreeNode nb : new TreeNode[]{n.left, n.right, parent.get(n)})
                    if (nb != null && seen.add(nb)) q.offer(nb);
            }
        List<Integer> res = new ArrayList<>();
        for (TreeNode n : q) res.add(n.val);
        Collections.sort(res);
        return res;
    }

    static int countNodesBrute(TreeNode n) { return n == null ? 0 : 1 + countNodesBrute(n.left) + countNodesBrute(n.right); }

    static int countNodes(TreeNode n) {
        if (n == null) return 0;
        int lh = 0, rh = 0;
        for (TreeNode c = n; c != null; c = c.left) lh++;
        for (TreeNode c = n; c != null; c = c.right) rh++;
        if (lh == rh) return (1 << lh) - 1;   // perfect tree
        return 1 + countNodes(n.left) + countNodes(n.right);
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

    static TreeNode find(TreeNode n, int v) {
        if (n == null || n.val == v) return n;
        TreeNode l = find(n.left, v); return l != null ? l : find(n.right, v);
    }

    public static void main(String[] args) {
        TreeNode t = build(3, 5, 1, 6, 2, 0, 8, null, null, 7, 4);
        TreeNode p = find(t, 5), q = find(t, 1), r = find(t, 4);
        check(lca(t, p, q).val, 3); check(lca(t, p, r).val, 5);
        check(lcaBrute(t, p, q).val, 3); check(lcaBrute(t, p, r).val, 5);
        TreeNode b1 = buildPreIn(new int[]{3, 9, 20, 15, 7}, new int[]{9, 3, 15, 20, 7});
        TreeNode b2 = buildInPost(new int[]{9, 3, 15, 20, 7}, new int[]{9, 15, 7, 20, 3});
        check(serialize(b1), "3,9,#,#,20,15,#,#,7,#,#,"); check(serialize(b2), serialize(b1));
        check(serialize(deserialize(serialize(t))), serialize(t));
        check(distanceK(t, p, 2), List.of(1, 4, 7));
        TreeNode c = build(1, 2, 3, 4, 5, 6);
        check(countNodes(c), 6); check(countNodesBrute(c), 6);
        System.out.println("TreeLCAConstruct: all tests passed");
    }

    static void check(Object got, Object exp) {
        if (!got.equals(exp)) throw new AssertionError("expected " + exp + " but got " + got);
    }
}
