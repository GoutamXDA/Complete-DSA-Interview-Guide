/**
 * Rotated sorted array family. Brute force for all: linear scan O(n).
 *
 * LC 33 search (distinct): one half around mid is always sorted; check if target is inside it. O(log n)
 * LC 81 search (duplicates): if a[lo]==a[mid]==a[hi], can't tell -> lo++, hi--. O(log n) avg, O(n) worst
 * LC 153 find min: if a[mid] > a[hi], min is right of mid; else min is at mid or left. O(log n)
 * Rotation count = index of the minimum.
 */
public class RotatedSortedArray {

    static int search(int[] a, int t) {
        int lo = 0, hi = a.length - 1;
        while (lo <= hi) {
            int mid = lo + (hi - lo) / 2;
            if (a[mid] == t) return mid;
            if (a[lo] <= a[mid]) {                       // left half sorted
                if (a[lo] <= t && t < a[mid]) hi = mid - 1; else lo = mid + 1;
            } else {                                      // right half sorted
                if (a[mid] < t && t <= a[hi]) lo = mid + 1; else hi = mid - 1;
            }
        }
        return -1;
    }

    static boolean searchWithDuplicates(int[] a, int t) {
        int lo = 0, hi = a.length - 1;
        while (lo <= hi) {
            int mid = lo + (hi - lo) / 2;
            if (a[mid] == t) return true;
            if (a[lo] == a[mid] && a[mid] == a[hi]) { lo++; hi--; continue; }
            if (a[lo] <= a[mid]) {
                if (a[lo] <= t && t < a[mid]) hi = mid - 1; else lo = mid + 1;
            } else {
                if (a[mid] < t && t <= a[hi]) lo = mid + 1; else hi = mid - 1;
            }
        }
        return false;
    }

    static int findMinIndex(int[] a) {
        int lo = 0, hi = a.length - 1;
        while (lo < hi) {
            int mid = lo + (hi - lo) / 2;
            if (a[mid] > a[hi]) lo = mid + 1; else hi = mid;
        }
        return lo;
    }

    static int findMin(int[] a) { return a[findMinIndex(a)]; }

    public static void main(String[] args) {
        int[] a = {4, 5, 6, 7, 0, 1, 2};
        check(search(a, 0), 4); check(search(a, 3), -1); check(search(new int[]{1}, 1), 0);
        check(searchWithDuplicates(new int[]{2, 5, 6, 0, 0, 1, 2}, 0), true);
        check(searchWithDuplicates(new int[]{1, 0, 1, 1, 1}, 0), true);
        check(searchWithDuplicates(new int[]{2, 5, 6, 0, 0, 1, 2}, 3), false);
        check(findMin(new int[]{3, 4, 5, 1, 2}), 1); check(findMin(new int[]{11, 13, 15, 17}), 11);
        check(findMinIndex(a), 4);
        System.out.println("RotatedSortedArray: all tests passed");
    }

    static void check(Object got, Object exp) {
        if (!got.equals(exp)) throw new AssertionError("expected " + exp + " but got " + got);
    }
}
