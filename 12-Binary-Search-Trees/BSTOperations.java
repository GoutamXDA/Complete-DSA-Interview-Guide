import java.util.*;

/**
 * Core BST operations, all O(h).
 * LC 700 search (recursive + iterative), LC 701 insert, LC 450 delete, floor/ceil, min/max.
 */
public class BSTOperations {

    static class TreeNode { int val; TreeNode left, right; TreeNode(int v) { val = v; } }

    static TreeNode searchRec(TreeNode n, int k) {
        if (n == null || n.val == k) return n;
        return k < n.val ? searchRec(n.left, k) : searchRec(n.right, k);
    }

    static TreeNode search(TreeNode n, int k) {
        while (n != null && n.val != k) n = k < n.val ? n.left : n.right;
        return n;
    }

    static TreeNode insert(TreeNode root, int k) {
        if (root == null) return new TreeNode(k);
        TreeNode cur = root;
        while (true) {
            if (k < cur.val) { if (cur.left == null) { cur.left = new TreeNode(k); break; } cur = cur.left; }
            else { if (cur.right == null) { cur.right = new TreeNode(k); break; } cur = cur.right; }
        }
        return root;
    }

    static TreeNode delete(TreeNode n, int k) {
        if (n == null) return null;
        if (k < n.val) n.left = delete(n.left, k);
        else if (k > n.val) n.right = delete(n.right, k);
        else {
            if (n.left == null) return n.right;          // 0 or 1 child
            if (n.right == null) return n.left;
            TreeNode succ = n.right;                      // 2 children: inorder successor
            while (succ.left != null) succ = succ.left;
            n.val = succ.val;
            n.right = delete(n.right, succ.val);
        }
        return n;
    }

    static int floor(TreeNode n, int k) {
        int ans = -1;
        while (n != null) {
            if (n.val == k) return k;
            if (n.val < k) { ans = n.val; n = n.right; } else n = n.left;
        }
        return ans;
    }

    static int ceil(TreeNode n, int k) {
        int ans = -1;
        while (n != null) {
            if (n.val == k) return k;
            if (n.val > k) { ans = n.val; n = n.left; } else n = n.right;
        }
        return ans;
    }

    static int min(TreeNode n) { while (n.left != null) n = n.left; return n.val; }
    static int max(TreeNode n) { while (n.right != null) n = n.right; return n.val; }

    static void inorder(TreeNode n, List<Integer> o) { if (n == null) return; inorder(n.left, o); o.add(n.val); inorder(n.right, o); }

    public static void main(String[] args) {
        TreeNode root = null;
        for (int v : new int[]{8, 3, 10, 1, 6, 14, 4, 7, 13}) root = insert(root, v);
        List<Integer> in = new ArrayList<>(); inorder(root, in);
        check(in, List.of(1, 3, 4, 6, 7, 8, 10, 13, 14));
        check(search(root, 6).val, 6); check(searchRec(root, 5) == null, true);
        check(floor(root, 5), 4); check(ceil(root, 5), 6); check(floor(root, 0), -1); check(ceil(root, 15), -1);
        check(min(root), 1); check(max(root), 14);
        root = delete(root, 3);   // two children
        root = delete(root, 14);  // one child
        root = delete(root, 1);   // leaf
        in.clear(); inorder(root, in);
        check(in, List.of(4, 6, 7, 8, 10, 13));
        System.out.println("BSTOperations: all tests passed");
    }

    static void check(Object got, Object exp) {
        if (!got.equals(exp)) throw new AssertionError("expected " + exp + " but got " + got);
    }
}
