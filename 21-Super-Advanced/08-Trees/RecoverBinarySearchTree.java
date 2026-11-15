import java.util.*;

/**
 * LC 99 — Recover Binary Search Tree. Explanation: RecoverBinarySearchTree.md
 * 1. Inorder -> sort values -> write back     O(n log n)  O(n)
 * 2. Inorder with prev pointer (stack)        O(n)        O(h)
 * 3. Morris inorder                           O(n)        O(1)
 */
public class RecoverBinarySearchTree {

    static class TreeNode { int val; TreeNode left, right; TreeNode(int v) { val = v; } }

    static void sortBack(TreeNode root) {
        List<TreeNode> nodes = new ArrayList<>();
        inorder(root, nodes);
        int[] vals = nodes.stream().mapToInt(n -> n.val).sorted().toArray();
        for (int i = 0; i < vals.length; i++) nodes.get(i).val = vals[i];
    }

    static void inorder(TreeNode n, List<TreeNode> out) { if (n == null) return; inorder(n.left, out); out.add(n); inorder(n.right, out); }

    static void stackVersion(TreeNode root) {
        TreeNode first = null, second = null, prev = null, cur = root;
        Deque<TreeNode> st = new ArrayDeque<>();
        while (cur != null || !st.isEmpty()) {
            while (cur != null) { st.push(cur); cur = cur.left; }
            cur = st.pop();
            if (prev != null && prev.val > cur.val) { if (first == null) first = prev; second = cur; }
            prev = cur;
            cur = cur.right;
        }
        int t = first.val; first.val = second.val; second.val = t;
    }

    static void recoverTree(TreeNode root) {
        TreeNode first = null, second = null, prev = null, cur = root;
        while (cur != null) {
            if (cur.left == null) {
                if (prev != null && prev.val > cur.val) { if (first == null) first = prev; second = cur; }
                prev = cur; cur = cur.right;
            } else {
                TreeNode pre = cur.left;
                while (pre.right != null && pre.right != cur) pre = pre.right;
                if (pre.right == null) { pre.right = cur; cur = cur.left; }
                else {
                    pre.right = null;
                    if (prev != null && prev.val > cur.val) { if (first == null) first = prev; second = cur; }
                    prev = cur; cur = cur.right;
                }
            }
        }
        int t = first.val; first.val = second.val; second.val = t;
    }

    static TreeNode insert(TreeNode n, int v) {
        if (n == null) return new TreeNode(v);
        if (v < n.val) n.left = insert(n.left, v); else n.right = insert(n.right, v);
        return n;
    }

    static String shape(TreeNode n) { return n == null ? "#" : "(" + shape(n.left) + n.val + shape(n.right) + ")"; }

    public static void main(String[] args) {
        Random r = new Random(37);
        for (int t = 0; t < 2000; t++) {
            int n = r.nextInt(12) + 2;
            List<Integer> vals = new ArrayList<>();
            for (int i = 0; i < n; i++) vals.add(i * 3 - 10);
            Collections.shuffle(vals, r);
            TreeNode[] roots = new TreeNode[3];
            for (int k = 0; k < 3; k++) for (int v : vals) roots[k] = insert(roots[k], v);
            String correct = shape(roots[0]);
            int a = r.nextInt(n), b = r.nextInt(n - 1);
            if (b >= a) b++;
            for (TreeNode root : roots) {                   // swap the same two inorder positions in each copy
                List<TreeNode> in = new ArrayList<>(); inorder(root, in);
                int tmp = in.get(a).val; in.get(a).val = in.get(b).val; in.get(b).val = tmp;
            }
            sortBack(roots[0]); stackVersion(roots[1]); recoverTree(roots[2]);
            for (TreeNode root : roots) check(shape(root), correct);   // Morris also restores structure
        }
        System.out.println("RecoverBinarySearchTree: all tests passed");
    }

    static void check(Object got, Object exp) {
        if (!got.equals(exp)) throw new AssertionError("expected " + exp + " but got " + got);
    }
}
