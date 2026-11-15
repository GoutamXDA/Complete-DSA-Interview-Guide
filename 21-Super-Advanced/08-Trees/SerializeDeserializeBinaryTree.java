import java.util.*;

/**
 * LC 297 — Serialize and Deserialize Binary Tree (+ LC 449 BST variant). Explanation: SerializeDeserializeBinaryTree.md
 * 1. Preorder DFS with null markers
 * 2. Level-order BFS with null markers (iterative both ways)
 * 3. BST only: preorder without nulls, rebuilt with value bounds
 */
public class SerializeDeserializeBinaryTree {

    static class TreeNode { int val; TreeNode left, right; TreeNode(int v) { val = v; } }

    // ---------- 1. Preorder ----------
    static String serializeDFS(TreeNode root) { StringBuilder sb = new StringBuilder(); pre(root, sb); return sb.toString(); }

    static void pre(TreeNode n, StringBuilder sb) {
        if (n == null) { sb.append("#,"); return; }
        sb.append(n.val).append(','); pre(n.left, sb); pre(n.right, sb);
    }

    static int idx;
    static TreeNode deserializeDFS(String data) { idx = 0; return build(data.split(",")); }

    static TreeNode build(String[] t) {
        String s = t[idx++];
        if (s.equals("#")) return null;
        TreeNode n = new TreeNode(Integer.parseInt(s));
        n.left = build(t); n.right = build(t);
        return n;
    }

    // ---------- 2. Level order ----------
    static String serializeBFS(TreeNode root) {
        if (root == null) return "";
        List<String> out = new ArrayList<>();
        Queue<TreeNode> q = new LinkedList<>();          // LinkedList allows null elements
        q.offer(root);
        while (!q.isEmpty()) {
            TreeNode n = q.poll();
            if (n == null) { out.add("null"); continue; }
            out.add(String.valueOf(n.val));
            q.offer(n.left); q.offer(n.right);
        }
        int end = out.size();
        while (end > 0 && out.get(end - 1).equals("null")) end--;   // trim trailing nulls
        return String.join(",", out.subList(0, end));
    }

    static TreeNode deserializeBFS(String data) {
        if (data.isEmpty()) return null;
        String[] t = data.split(",");
        TreeNode root = new TreeNode(Integer.parseInt(t[0]));
        Queue<TreeNode> q = new ArrayDeque<>(List.of(root));
        for (int i = 1; i < t.length; ) {
            TreeNode n = q.poll();
            if (i < t.length && !t[i].equals("null")) { n.left = new TreeNode(Integer.parseInt(t[i])); q.offer(n.left); } i++;
            if (i < t.length && !t[i].equals("null")) { n.right = new TreeNode(Integer.parseInt(t[i])); q.offer(n.right); } i++;
        }
        return root;
    }

    // ---------- 3. BST without nulls ----------
    static String serializeBST(TreeNode root) {
        StringBuilder sb = new StringBuilder();
        Deque<TreeNode> st = new ArrayDeque<>();
        if (root != null) st.push(root);
        while (!st.isEmpty()) {
            TreeNode n = st.pop();
            sb.append(n.val).append(',');
            if (n.right != null) st.push(n.right);
            if (n.left != null) st.push(n.left);
        }
        return sb.toString();
    }

    static TreeNode deserializeBST(String data) {
        if (data.isEmpty()) return null;
        int[] v = Arrays.stream(data.split(",")).mapToInt(Integer::parseInt).toArray();
        idx = 0;
        return bst(v, Long.MIN_VALUE, Long.MAX_VALUE);
    }

    static TreeNode bst(int[] v, long lo, long hi) {
        if (idx == v.length || v[idx] < lo || v[idx] > hi) return null;
        TreeNode n = new TreeNode(v[idx++]);
        n.left = bst(v, lo, n.val - 1L);          // distinct keys assumed for BSTs
        n.right = bst(v, n.val + 1L, hi);
        return n;
    }

    static boolean same(TreeNode a, TreeNode b) {
        if (a == null || b == null) return a == b;
        return a.val == b.val && same(a.left, b.left) && same(a.right, b.right);
    }

    static TreeNode random(Random r, int n) {
        if (n == 0) return null;
        TreeNode root = new TreeNode(r.nextInt(41) - 20);
        List<TreeNode> all = new ArrayList<>(List.of(root));
        while (all.size() < n) {
            TreeNode p = all.get(r.nextInt(all.size())), c = new TreeNode(r.nextInt(41) - 20);
            if (r.nextBoolean()) { if (p.left == null) { p.left = c; all.add(c); } } else if (p.right == null) { p.right = c; all.add(c); }
        }
        return root;
    }

    static TreeNode insert(TreeNode n, int v) {
        if (n == null) return new TreeNode(v);
        if (v < n.val) n.left = insert(n.left, v); else n.right = insert(n.right, v);
        return n;
    }

    public static void main(String[] args) {
        TreeNode t = deserializeBFS("1,2,3,null,null,4,5");
        check(serializeBFS(t), "1,2,3,null,null,4,5"); check(serializeDFS(t), "1,2,#,#,3,4,#,#,5,#,#,");
        check(deserializeDFS(serializeDFS(null)) == null, true); check(deserializeBFS(serializeBFS(null)) == null, true);
        Random r = new Random(34);
        for (int k = 0; k < 1500; k++) {
            TreeNode x = random(r, r.nextInt(15));
            check(same(deserializeDFS(serializeDFS(x)), x), true);
            check(same(deserializeBFS(serializeBFS(x)), x), true);
            TreeNode b = null;
            Set<Integer> used = new HashSet<>();
            for (int i = r.nextInt(12); i > 0; i--) { int v = r.nextInt(50) - 25; if (used.add(v)) b = insert(b, v); }
            check(same(deserializeBST(serializeBST(b)), b), true);
        }
        System.out.println("SerializeDeserializeBinaryTree: all tests passed");
    }

    static void check(Object got, Object exp) {
        if (!got.equals(exp)) throw new AssertionError("expected " + exp + " but got " + got);
    }
}
