import java.util.*;

/**
 * LC 88 Merge sorted array in place: brute = copy + sort O((m+n)log) | optimal = fill from back O(m+n), O(1)
 * Count inversions (i<j, a[i]>a[j]): brute O(n^2) | merge sort: when right element goes first,
 *   it's smaller than all remaining left elements -> add (mid - i + 1). O(n log n)
 * LC 493 Reverse pairs (a[i] > 2*a[j]): merge sort, count with a separate two-pointer pass. O(n log n)
 * LC 215 Kth largest: sort O(n log n) | min-heap of size k O(n log k) | quickselect O(n) avg
 * LC 179 Largest number: sort strings by (b+a) vs (a+b).
 */
public class SortingProblems {

    static void mergeSortedArrays(int[] a, int m, int[] b, int n) {
        int i = m - 1, j = n - 1, k = m + n - 1;
        while (j >= 0) a[k--] = (i >= 0 && a[i] > b[j]) ? a[i--] : b[j--];
    }

    static long inversionsBrute(int[] a) {
        long c = 0;
        for (int i = 0; i < a.length; i++) for (int j = i + 1; j < a.length; j++) if (a[i] > a[j]) c++;
        return c;
    }

    static long inversions(int[] a) { return sortCount(a.clone(), 0, a.length - 1, new int[a.length]); }

    static long sortCount(int[] a, int l, int r, int[] tmp) {
        if (l >= r) return 0;
        int m = (l + r) / 2;
        long c = sortCount(a, l, m, tmp) + sortCount(a, m + 1, r, tmp);
        int i = l, j = m + 1, k = l;
        while (i <= m && j <= r) {
            if (a[i] <= a[j]) tmp[k++] = a[i++];
            else { c += m - i + 1; tmp[k++] = a[j++]; }
        }
        while (i <= m) tmp[k++] = a[i++];
        while (j <= r) tmp[k++] = a[j++];
        System.arraycopy(tmp, l, a, l, r - l + 1);
        return c;
    }

    static int reversePairs(int[] nums) { return rp(nums.clone(), 0, nums.length - 1); }

    static int rp(int[] a, int l, int r) {
        if (l >= r) return 0;
        int m = (l + r) / 2, c = rp(a, l, m) + rp(a, m + 1, r);
        for (int i = l, j = m + 1; i <= m; i++) {
            while (j <= r && a[i] > 2L * a[j]) j++;
            c += j - (m + 1);
        }
        Arrays.sort(a, l, r + 1); // simple merge; both halves sorted so a real merge would be O(n)
        return c;
    }

    static int kthLargestSort(int[] a, int k) { int[] s = a.clone(); Arrays.sort(s); return s[s.length - k]; }

    static int kthLargestHeap(int[] a, int k) {
        PriorityQueue<Integer> pq = new PriorityQueue<>();
        for (int x : a) { pq.offer(x); if (pq.size() > k) pq.poll(); }
        return pq.peek();
    }

    static int kthLargestQuickSelect(int[] nums, int k) {
        int[] a = nums.clone();
        int target = a.length - k, l = 0, r = a.length - 1;
        Random rng = new Random(1);
        while (true) {
            int p = l + rng.nextInt(r - l + 1);
            int t = a[p]; a[p] = a[r]; a[r] = t;
            int store = l;
            for (int i = l; i < r; i++) if (a[i] < a[r]) { t = a[i]; a[i] = a[store]; a[store] = t; store++; }
            t = a[store]; a[store] = a[r]; a[r] = t;
            if (store == target) return a[store];
            if (store < target) l = store + 1; else r = store - 1;
        }
    }

    static String largestNumber(int[] nums) {
        String[] s = Arrays.stream(nums).mapToObj(String::valueOf).toArray(String[]::new);
        Arrays.sort(s, (a, b) -> (b + a).compareTo(a + b));
        if (s[0].equals("0")) return "0";
        return String.join("", s);
    }

    public static void main(String[] args) {
        int[] a = {1, 2, 3, 0, 0, 0}; mergeSortedArrays(a, 3, new int[]{2, 5, 6}, 3);
        check(Arrays.toString(a), "[1, 2, 2, 3, 5, 6]");
        int[] inv = {8, 4, 2, 1};
        check(inversionsBrute(inv), 6L); check(inversions(inv), 6L); check(inversions(new int[]{2, 4, 1, 3, 5}), 3L);
        check(reversePairs(new int[]{1, 3, 2, 3, 1}), 2); check(reversePairs(new int[]{2147483647, 2147483647, 2147483647}), 0);
        int[] k = {3, 2, 3, 1, 2, 4, 5, 5, 6};
        check(kthLargestSort(k, 4), 4); check(kthLargestHeap(k, 4), 4); check(kthLargestQuickSelect(k, 4), 4);
        check(largestNumber(new int[]{3, 30, 34, 5, 9}), "9534330"); check(largestNumber(new int[]{0, 0}), "0");
        System.out.println("SortingProblems: all tests passed");
    }

    static void check(Object got, Object exp) {
        if (!got.equals(exp)) throw new AssertionError("expected " + exp + " but got " + got);
    }
}
