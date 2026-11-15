import java.util.*;

/**
 * LC 1707 — Maximum XOR With an Element From Array. Explanation: MaximumXorWithElementFromArray.md
 * 1. Brute force                                   O(n q)
 * 2. Offline: sort by limit, progressive trie      O((n + q) 30 + sorting)
 * 3. Online: trie storing subtree minimum          O((n + q) 30)
 */
public class MaximumXorWithElementFromArray {

    static final int BITS = 30;

    static int[] brute(int[] nums, int[][] q) {
        int[] r = new int[q.length];
        for (int i = 0; i < q.length; i++) { r[i] = -1; for (int x : nums) if (x <= q[i][1]) r[i] = Math.max(r[i], x ^ q[i][0]); }
        return r;
    }

    static class Node { Node[] ch = new Node[2]; int min = Integer.MAX_VALUE; }

    static void insert(Node root, int v) {
        Node c = root;
        c.min = Math.min(c.min, v);
        for (int b = BITS - 1; b >= 0; b--) {
            int bit = (v >> b) & 1;
            if (c.ch[bit] == null) c.ch[bit] = new Node();
            c = c.ch[bit];
            c.min = Math.min(c.min, v);
        }
    }

    static int bestXor(Node root, int x) {
        Node c = root; int res = 0;
        for (int b = BITS - 1; b >= 0; b--) {
            int want = ((x >> b) & 1) ^ 1;
            if (c.ch[want] != null) { res |= 1 << b; c = c.ch[want]; } else c = c.ch[want ^ 1];
        }
        return res;
    }

    static int[] maximizeXor(int[] nums, int[][] queries) {
        int[] a = nums.clone(); Arrays.sort(a);
        Integer[] order = new Integer[queries.length];
        for (int i = 0; i < order.length; i++) order[i] = i;
        Arrays.sort(order, Comparator.comparingInt(i -> queries[i][1]));
        Node root = new Node();
        int[] ans = new int[queries.length];
        int p = 0;
        for (int qi : order) {
            while (p < a.length && a[p] <= queries[qi][1]) insert(root, a[p++]);
            ans[qi] = p == 0 ? -1 : bestXor(root, queries[qi][0]);
        }
        return ans;
    }

    static int[] online(int[] nums, int[][] queries) {
        Node root = new Node();
        for (int x : nums) insert(root, x);
        int[] ans = new int[queries.length];
        for (int i = 0; i < queries.length; i++) {
            int x = queries[i][0], m = queries[i][1];
            if (root.min > m) { ans[i] = -1; continue; }
            Node c = root; int res = 0;
            for (int b = BITS - 1; b >= 0; b--) {
                int want = ((x >> b) & 1) ^ 1;
                if (c.ch[want] != null && c.ch[want].min <= m) { res |= 1 << b; c = c.ch[want]; }
                else c = c.ch[want ^ 1];          // exists: root.min <= m guarantees a valid path
            }
            ans[i] = res;
        }
        return ans;
    }

    public static void main(String[] args) {
        int[] nums = {0, 1, 2, 3, 4};
        int[][] q = {{3, 1}, {1, 3}, {5, 6}};
        check(Arrays.toString(maximizeXor(nums, q)), "[3, 3, 7]"); check(Arrays.toString(online(nums, q)), "[3, 3, 7]");
        check(Arrays.toString(maximizeXor(new int[]{5, 2, 4, 6, 6, 3}, new int[][]{{12, 4}, {8, 1}, {6, 3}})), "[15, -1, 5]");
        Random r = new Random(69);
        for (int t = 0; t < 1500; t++) {
            int[] a = new int[r.nextInt(10) + 1];
            for (int i = 0; i < a.length; i++) a[i] = r.nextInt(1 << 10);
            int[][] qs = new int[r.nextInt(10) + 1][];
            for (int i = 0; i < qs.length; i++) qs[i] = new int[]{r.nextInt(1 << 10), r.nextInt(1 << 10)};
            String e = Arrays.toString(brute(a, qs));
            check(Arrays.toString(maximizeXor(a, qs)), e); check(Arrays.toString(online(a, qs)), e);
        }
        System.out.println("MaximumXorWithElementFromArray: all tests passed");
    }

    static void check(Object got, Object exp) {
        if (!got.equals(exp)) throw new AssertionError("expected " + exp + " but got " + got);
    }
}
