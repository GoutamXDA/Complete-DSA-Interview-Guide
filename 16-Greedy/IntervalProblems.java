import java.util.*;

/**
 * LC 56 Merge intervals: brute compare all pairs repeatedly O(n^2) | sort by start, merge O(n log n)
 * LC 57 Insert interval (sorted, non-overlapping input): copy left part, merge overlap, copy right. O(n)
 * LC 435 Non-overlapping intervals: sort by END; count kept; removals = n - kept.
 * Activity selection / N meetings: sort by end, take if start > lastEnd (strict for meetings).
 * LC 452 Min arrows: sort by end; shoot at end; new arrow when start > arrow.
 * Min platforms: sort arrivals and departures separately; sweep with two pointers. O(n log n)
 */
public class IntervalProblems {

    static int[][] merge(int[][] intervals) {
        int[][] iv = intervals.clone();
        Arrays.sort(iv, Comparator.comparingInt(x -> x[0]));
        List<int[]> res = new ArrayList<>();
        for (int[] cur : iv) {
            if (res.isEmpty() || res.get(res.size() - 1)[1] < cur[0]) res.add(cur.clone());
            else res.get(res.size() - 1)[1] = Math.max(res.get(res.size() - 1)[1], cur[1]);
        }
        return res.toArray(new int[0][]);
    }

    static int[][] insert(int[][] iv, int[] nw) {
        List<int[]> res = new ArrayList<>();
        int i = 0, n = iv.length;
        int s = nw[0], e = nw[1];
        while (i < n && iv[i][1] < s) res.add(iv[i++]);
        while (i < n && iv[i][0] <= e) { s = Math.min(s, iv[i][0]); e = Math.max(e, iv[i][1]); i++; }
        res.add(new int[]{s, e});
        while (i < n) res.add(iv[i++]);
        return res.toArray(new int[0][]);
    }

    static int eraseOverlapIntervals(int[][] intervals) {
        int[][] iv = intervals.clone();
        Arrays.sort(iv, Comparator.comparingInt(x -> x[1]));
        int kept = 0, lastEnd = Integer.MIN_VALUE;
        for (int[] x : iv) if (x[0] >= lastEnd) { kept++; lastEnd = x[1]; }
        return iv.length - kept;
    }

    static int maxMeetings(int[] start, int[] end) {
        Integer[] idx = new Integer[start.length];
        for (int i = 0; i < idx.length; i++) idx[i] = i;
        Arrays.sort(idx, Comparator.comparingInt(i -> end[i]));
        int count = 0, lastEnd = Integer.MIN_VALUE;
        for (int i : idx) if (start[i] > lastEnd) { count++; lastEnd = end[i]; }
        return count;
    }

    static int findMinArrowShots(int[][] points) {
        int[][] p = points.clone();
        Arrays.sort(p, Comparator.comparingInt(x -> x[1]));
        int arrows = 0; long pos = Long.MIN_VALUE;
        for (int[] x : p) if (x[0] > pos) { arrows++; pos = x[1]; }
        return arrows;
    }

    static int minPlatforms(int[] arr, int[] dep) {
        int[] a = arr.clone(), d = dep.clone(); Arrays.sort(a); Arrays.sort(d);
        int i = 0, j = 0, cur = 0, best = 0;
        while (i < a.length) {
            if (a[i] <= d[j]) { cur++; i++; best = Math.max(best, cur); }  // arrival before (or at) departure needs a platform
            else { cur--; j++; }
        }
        return best;
    }

    public static void main(String[] args) {
        check(Arrays.deepToString(merge(new int[][]{{1, 3}, {2, 6}, {8, 10}, {15, 18}})), "[[1, 6], [8, 10], [15, 18]]");
        check(Arrays.deepToString(merge(new int[][]{{1, 4}, {4, 5}})), "[[1, 5]]");
        check(Arrays.deepToString(insert(new int[][]{{1, 2}, {3, 5}, {6, 7}, {8, 10}, {12, 16}}, new int[]{4, 8})), "[[1, 2], [3, 10], [12, 16]]");
        check(eraseOverlapIntervals(new int[][]{{1, 2}, {2, 3}, {3, 4}, {1, 3}}), 1);
        check(maxMeetings(new int[]{1, 3, 0, 5, 8, 5}, new int[]{2, 4, 6, 7, 9, 9}), 4);
        check(findMinArrowShots(new int[][]{{10, 16}, {2, 8}, {1, 6}, {7, 12}}), 2);
        check(findMinArrowShots(new int[][]{{-2147483646, -2147483645}, {2147483646, 2147483647}}), 2);
        check(minPlatforms(new int[]{900, 940, 950, 1100, 1500, 1800}, new int[]{910, 1200, 1120, 1130, 1900, 2000}), 3);
        System.out.println("IntervalProblems: all tests passed");
    }

    static void check(Object got, Object exp) {
        if (!got.equals(exp)) throw new AssertionError("expected " + exp + " but got " + got);
    }
}
