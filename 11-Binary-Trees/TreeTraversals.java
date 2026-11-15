import java.util.*;

/**
 * All traversals. Each O(n) time.
 * Recursive: O(h) stack. Iterative: explicit stack O(h). Morris: O(1) extra space.
 *
 * Iterative postorder trick: do "root, R, L" preorder and reverse it.
 * Morris inorder: for node with a left child, find its inorder predecessor;
 *   if predecessor.right == null -> thread it to node and go left; else remove thread, visit node, go right.
 */
public class TreeTraversals {

    static class TreeNode { int val; TreeNode left, right; TreeNode(int v) { val = v; } }

    static void preRec(TreeNode n, List<Integer> o) { if (n == null) return; o.add(n.val); preRec(n.left, o); preRec(n.right, o); }
    static void inRec(TreeNode n, List<Integer> o) { if (n == null) return; inRec(n.left, o); o.add(n.val); inRec(n.right, o); }
    static void postRec(TreeNode n, List<Integer> o) { if (n == null) return; postRec(n.left, o); postRec(n.right, o); o.add(n.val); }

    static List<Integer> preIter(TreeNode root) {
        List<Integer> o = new ArrayList<>();
        Deque<TreeNode> st = new ArrayDeque<>();
        if (root != null) st.push(root);
        while (!st.isEmpty()) {
            TreeNode n = st.pop(); o.add(n.val);
            if (n.right != null) st.push(n.right);   // right first so left is processed first
            if (n.left != null) st.push(n.left);
        }
        return o;
    }

    static List<Integer> inIter(TreeNode root) {
        List<Integer> o = new ArrayList<>();
        Deque<TreeNode> st = new ArrayDeque<>();
        TreeNode cur = root;
        while (cur != null || !st.isEmpty()) {
            while (cur != null) { st.push(cur); cur = cur.left; }
            cur = st.pop(); o.add(cur.val);
            cur = cur.right;
        }
        return o;
    }

    static List<Integer> postIter(TreeNode root) {
        LinkedList<Integer> o = new LinkedList<>();
        Deque<TreeNode> st = new ArrayDeque<>();
        if (root != null) st.push(root);
        while (!st.isEmpty()) {
            TreeNode n = st.pop(); o.addFirst(n.val);
            if (n.left != null) st.push(n.left);
            if (n.right != null) st.push(n.right);
        }
        return o;
    }

    static List<Integer> morrisInorder(TreeNode root) {
        List<Integer> o = new ArrayList<>();
        TreeNode cur = root;
        while (cur != null) {
            if (cur.left == null) { o.add(cur.val); cur = cur.right; continue; }
            TreeNode pred = cur.left;
            while (pred.right != null && pred.right != cur) pred = pred.right;
            if (pred.right == null) { pred.right = cur; cur = cur.left; }   // create thread
            else { pred.right = null; o.add(cur.val); cur = cur.right; }  // remove thread
        }
        return o;
    }

    static List<List<Integer>> levelOrder(TreeNode root) {
        List<List<Integer>> res = new ArrayList<>();
        Queue<TreeNode> q = new ArrayDeque<>();
        if (root != null) q.offer(root);
        while (!q.isEmpty()) {
            int size = q.size();
            List<Integer> level = new ArrayList<>();
            for (int i = 0; i < size; i++) {
                TreeNode n = q.poll(); level.add(n.val);
                if (n.left != null) q.offer(n.left);
                if (n.right != null) q.offer(n.right);
            }
            res.add(level);
        }
        return res;
    }

    static List<List<Integer>> zigzag(TreeNode root) {
        List<List<Integer>> res = new ArrayList<>();
        Queue<TreeNode> q = new ArrayDeque<>();
        if (root != null) q.offer(root);
        boolean leftToRight = true;
        while (!q.isEmpty()) {
            int size = q.size();
            LinkedList<Integer> level = new LinkedList<>();
            for (int i = 0; i < size; i++) {
                TreeNode n = q.poll();
                if (leftToRight) level.addLast(n.val); else level.addFirst(n.val);
                if (n.left != null) q.offer(n.left);
                if (n.right != null) q.offer(n.right);
            }
            res.add(level); leftToRight = !leftToRight;
        }
        return res;
    }

    /** Build from LeetCode-style level order, null = missing. */
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
        //        1
        //      2   3
        //     4 5   6
        TreeNode t = build(1, 2, 3, 4, 5, null, 6);
        List<Integer> a = new ArrayList<>(), b = new ArrayList<>(), c = new ArrayList<>();
        preRec(t, a); inRec(t, b); postRec(t, c);
        check(a, List.of(1, 2, 4, 5, 3, 6)); check(preIter(t), a);
        check(b, List.of(4, 2, 5, 1, 3, 6)); check(inIter(t), b); check(morrisInorder(t), b);
        check(inIter(t), b); // Morris must restore the tree
        check(c, List.of(4, 5, 2, 6, 3, 1)); check(postIter(t), c);
        check(levelOrder(t).toString(), "[[1], [2, 3], [4, 5, 6]]");
        check(zigzag(t).toString(), "[[1], [3, 2], [4, 5, 6]]");
        System.out.println("TreeTraversals: all tests passed");
    }

    static void check(Object got, Object exp) {
        if (!got.equals(exp)) throw new AssertionError("expected " + exp + " but got " + got);
    }
}
