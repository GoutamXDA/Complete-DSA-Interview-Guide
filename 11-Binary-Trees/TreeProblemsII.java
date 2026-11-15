import java.util.*;

/**
 * LC 257  Binary tree paths: DFS carrying the path. O(n * h)
 * LC 572  Subtree of another tree: brute isSame at every node O(m*n) | serialize both with null markers + substring/KMP O(m+n)
 * LC 129  Sum root to leaf numbers: pass cur*10 + val down. O(n)
 * LC 1448 Count good nodes (no greater value on the path from root): pass max so far. O(n)
 * LC 437  Path sum III (any downward path): brute DFS from every node O(n^2) | prefix sums on the root path + map O(n)
 * LC 116  Populating next right pointers (perfect tree): BFS queue O(n) space | use built next pointers level by level O(1)
 * LC 114  Flatten binary tree to linked list (preorder): preorder list then relink O(n) space
 *         | reverse postorder (right, left, root) with prev pointer O(h) | Morris-style O(1) space
 * Children sum property (GFG): make every node equal the sum of its children by only increasing values.
 *         Push the larger of (node, children sum) down, then set node = sum of children on the way up. O(n)
 * LC 2385 Amount of time to burn the tree from start: parent map + BFS O(n)
 * LC 889  Construct from preorder and postorder: pre[i+1] is the left child's root; its post index gives left size.
 * LC 968  Binary tree cameras: greedy postorder states 0 = not covered, 1 = has camera, 2 = covered. O(n)
 */
public class TreeProblemsII {

    static class TreeNode { int val; TreeNode left, right, next; TreeNode(int v) { val = v; } }

    static List<String> binaryTreePaths(TreeNode root) {
        List<String> res = new ArrayList<>();
        paths(root, "", res);
        return res;
    }

    static void paths(TreeNode n, String path, List<String> res) {
        if (n == null) return;
        String p = path.isEmpty() ? String.valueOf(n.val) : path + "->" + n.val;
        if (n.left == null && n.right == null) { res.add(p); return; }
        paths(n.left, p, res); paths(n.right, p, res);
    }

    static boolean isSubtreeBrute(TreeNode root, TreeNode sub) {
        if (root == null) return false;
        return same(root, sub) || isSubtreeBrute(root.left, sub) || isSubtreeBrute(root.right, sub);
    }

    static boolean same(TreeNode a, TreeNode b) {
        if (a == null || b == null) return a == b;
        return a.val == b.val && same(a.left, b.left) && same(a.right, b.right);
    }

    static boolean isSubtree(TreeNode root, TreeNode sub) {
        // leading comma so "2" doesn't match inside "12"; contains() could be swapped for KMP for guaranteed O(m+n)
        return ser(root, new StringBuilder()).toString().contains(ser(sub, new StringBuilder()).toString());
    }

    static StringBuilder ser(TreeNode n, StringBuilder sb) {
        if (n == null) return sb.append(",#");
        sb.append(',').append(n.val);
        ser(n.left, sb); ser(n.right, sb);
        return sb;
    }

    static int sumNumbers(TreeNode n) { return sumNum(n, 0); }
    static int sumNum(TreeNode n, int cur) {
        if (n == null) return 0;
        cur = cur * 10 + n.val;
        if (n.left == null && n.right == null) return cur;
        return sumNum(n.left, cur) + sumNum(n.right, cur);
    }

    static int goodNodes(TreeNode n) { return good(n, Integer.MIN_VALUE); }
    static int good(TreeNode n, int max) {
        if (n == null) return 0;
        int self = n.val >= max ? 1 : 0;
        max = Math.max(max, n.val);
        return self + good(n.left, max) + good(n.right, max);
    }

    static int pathSumBrute(TreeNode n, int target) {
        if (n == null) return 0;
        return from(n, target) + pathSumBrute(n.left, target) + pathSumBrute(n.right, target);
    }

    static int from(TreeNode n, long rem) {
        if (n == null) return 0;
        return (n.val == rem ? 1 : 0) + from(n.left, rem - n.val) + from(n.right, rem - n.val);
    }

    static int pathSum(TreeNode root, int target) {
        Map<Long, Integer> prefix = new HashMap<>(Map.of(0L, 1));
        return ps(root, 0, target, prefix);
    }

    static int ps(TreeNode n, long sum, int target, Map<Long, Integer> prefix) {
        if (n == null) return 0;
        sum += n.val;
        int cnt = prefix.getOrDefault(sum - target, 0);
        prefix.merge(sum, 1, Integer::sum);
        cnt += ps(n.left, sum, target, prefix) + ps(n.right, sum, target, prefix);
        prefix.merge(sum, -1, Integer::sum);     // backtrack: only ancestors count
        return cnt;
    }

    static TreeNode connectBFS(TreeNode root) {
        Queue<TreeNode> q = new ArrayDeque<>();
        if (root != null) q.offer(root);
        while (!q.isEmpty()) {
            TreeNode prev = null;
            for (int i = q.size(); i > 0; i--) {
                TreeNode n = q.poll();
                if (prev != null) prev.next = n;
                prev = n;
                if (n.left != null) { q.offer(n.left); q.offer(n.right); }
            }
        }
        return root;
    }

    static TreeNode connect(TreeNode root) {
        for (TreeNode level = root; level != null && level.left != null; level = level.left)
            for (TreeNode n = level; n != null; n = n.next) {
                n.left.next = n.right;
                if (n.next != null) n.right.next = n.next.left;
            }
        return root;
    }

    static void flattenList(TreeNode root) {
        List<TreeNode> pre = new ArrayList<>();
        preorder(root, pre);
        for (int i = 0; i < pre.size(); i++) { pre.get(i).left = null; pre.get(i).right = i + 1 < pre.size() ? pre.get(i + 1) : null; }
    }

    static void preorder(TreeNode n, List<TreeNode> out) { if (n == null) return; out.add(n); preorder(n.left, out); preorder(n.right, out); }

    static TreeNode prev;
    static void flattenRecursive(TreeNode root) { prev = null; revPost(root); }
    static void revPost(TreeNode n) {
        if (n == null) return;
        revPost(n.right); revPost(n.left);
        n.right = prev; n.left = null; prev = n;
    }

    static void flatten(TreeNode root) {
        for (TreeNode cur = root; cur != null; cur = cur.right) {
            if (cur.left != null) {
                TreeNode pre = cur.left;
                while (pre.right != null) pre = pre.right;    // rightmost of left subtree
                pre.right = cur.right;
                cur.right = cur.left;
                cur.left = null;
            }
        }
    }

    static void childrenSum(TreeNode n) {
        if (n == null) return;
        int child = (n.left != null ? n.left.val : 0) + (n.right != null ? n.right.val : 0);
        if (child >= n.val) n.val = child;
        else { if (n.left != null) n.left.val = n.val; if (n.right != null) n.right.val = n.val; }
        childrenSum(n.left); childrenSum(n.right);
        int total = (n.left != null ? n.left.val : 0) + (n.right != null ? n.right.val : 0);
        if (n.left != null || n.right != null) n.val = total;
    }

    static boolean holdsChildrenSum(TreeNode n) {
        if (n == null || (n.left == null && n.right == null)) return true;
        int s = (n.left != null ? n.left.val : 0) + (n.right != null ? n.right.val : 0);
        return s == n.val && holdsChildrenSum(n.left) && holdsChildrenSum(n.right);
    }

    static int amountOfTime(TreeNode root, int start) {
        Map<TreeNode, TreeNode> parent = new HashMap<>();
        TreeNode src = null;
        Deque<TreeNode> st = new ArrayDeque<>(List.of(root));
        while (!st.isEmpty()) {
            TreeNode n = st.pop();
            if (n.val == start) src = n;
            if (n.left != null) { parent.put(n.left, n); st.push(n.left); }
            if (n.right != null) { parent.put(n.right, n); st.push(n.right); }
        }
        Set<TreeNode> seen = new HashSet<>(List.of(src));
        Queue<TreeNode> q = new ArrayDeque<>(List.of(src));
        int minutes = -1;
        while (!q.isEmpty()) {
            minutes++;
            for (int i = q.size(); i > 0; i--) {
                TreeNode n = q.poll();
                for (TreeNode nb : new TreeNode[]{n.left, n.right, parent.get(n)}) if (nb != null && seen.add(nb)) q.offer(nb);
            }
        }
        return minutes;
    }

    static int preI;
    static TreeNode constructFromPrePost(int[] pre, int[] post) {
        Map<Integer, Integer> pos = new HashMap<>();
        for (int i = 0; i < post.length; i++) pos.put(post[i], i);
        preI = 0;
        return cpp(pre, 0, post.length - 1, pos);
    }

    static TreeNode cpp(int[] pre, int lo, int hi, Map<Integer, Integer> pos) {   // lo..hi = range in postorder
        if (lo > hi) return null;
        TreeNode root = new TreeNode(pre[preI++]);
        if (lo == hi) return root;
        int leftRootPost = pos.get(pre[preI]);       // left subtree ends at its root in postorder
        root.left = cpp(pre, lo, leftRootPost, pos);
        root.right = cpp(pre, leftRootPost + 1, hi - 1, pos);
        return root;
    }

    static int cameras;
    static int minCameraCover(TreeNode root) { cameras = 0; if (cam(root) == 0) cameras++; return cameras; }
    static int cam(TreeNode n) {
        if (n == null) return 2;                      // null counts as covered
        int l = cam(n.left), r = cam(n.right);
        if (l == 0 || r == 0) { cameras++; return 1; } // a child needs coverage -> camera here
        if (l == 1 || r == 1) return 2;                // covered by a child's camera
        return 0;                                      // leave it for the parent
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

    static String rightChain(TreeNode n) { List<Integer> l = new ArrayList<>(); for (; n != null; n = n.right) { if (n.left != null) return "bad"; l.add(n.val); } return l.toString(); }
    static String serStr(TreeNode n) { return ser(n, new StringBuilder()).toString(); }

    public static void main(String[] args) {
        check(binaryTreePaths(build(1, 2, 3, null, 5)), List.of("1->2->5", "1->3"));
        TreeNode big = build(3, 4, 5, 1, 2), small = build(4, 1, 2), bad = build(3, 4, 5, 1, 2, null, null, null, null, 0);
        check(isSubtree(big, small), true); check(isSubtreeBrute(big, small), true);
        check(isSubtree(bad, small), false); check(isSubtreeBrute(bad, small), false);
        check(isSubtree(build(12), build(2)), false);
        check(sumNumbers(build(4, 9, 0, 5, 1)), 1026);
        check(goodNodes(build(3, 1, 4, 3, null, 1, 5)), 4);
        TreeNode p = build(10, 5, -3, 3, 2, null, 11, 3, -2, null, 1);
        check(pathSum(p, 8), 3); check(pathSumBrute(p, 8), 3);
        TreeNode q = build(1000000000, 1000000000, null, 294967296, null, 1000000000, null, 1000000000, null, 1000000000);
        check(pathSum(q, 0), 0);
        for (int v = 0; v < 2; v++) {
            TreeNode t = build(1, 2, 3, 4, 5, 6, 7);
            if (v == 0) connect(t); else connectBFS(t);
            check(t.left.right.next.val, 6); check(t.left.left.next.next.next.val, 7); check(t.right.next == null, true);
        }
        for (int v = 0; v < 3; v++) {
            TreeNode t = build(1, 2, 5, 3, 4, null, 6);
            if (v == 0) flatten(t); else if (v == 1) flattenRecursive(t); else flattenList(t);
            check(rightChain(t), "[1, 2, 3, 4, 5, 6]");
        }
        TreeNode cs = build(50, 7, 2, 3, 5, 1, 30);
        childrenSum(cs); check(holdsChildrenSum(cs), true);
        TreeNode cs2 = build(2, 35, 10, 2, 3, 5, 2); childrenSum(cs2); check(holdsChildrenSum(cs2), true);
        check(amountOfTime(build(1, 5, 3, null, 4, 10, 6, 9, 2), 3), 4); check(amountOfTime(build(1), 1), 0);
        TreeNode c = constructFromPrePost(new int[]{1, 2, 4, 5, 3, 6, 7}, new int[]{4, 5, 2, 6, 7, 3, 1});
        check(serStr(c), serStr(build(1, 2, 3, 4, 5, 6, 7)));
        check(minCameraCover(build(0, 0, null, 0, 0)), 1); check(minCameraCover(build(0, 0, null, 0, null, 0, null, null, 0)), 2);
        check(minCameraCover(build(0)), 1);
        System.out.println("TreeProblemsII: all tests passed");
    }

    static void check(Object got, Object exp) {
        if (!got.equals(exp)) throw new AssertionError("expected " + exp + " but got " + got);
    }
}
