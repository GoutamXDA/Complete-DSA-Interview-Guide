/**
 * LC 162 Peak element: linear O(n) | binary search toward the rising side O(log n)
 *   If a[mid] < a[mid+1], a peak exists to the right (the slope must come down eventually).
 * LC 540 Single element in sorted array (others appear twice):
 *   XOR O(n) | binary search: before the single element, pairs start at even indices. O(log n)
 * LC 74 Search 2D matrix (rows sorted & row[i][0] > row[i-1][last]): treat as 1D array of m*n. O(log mn)
 * LC 240 Search 2D matrix II (rows & cols sorted): staircase from top-right. O(m + n)
 */
public class PeakAndMatrix {

    static int findPeakLinear(int[] a) {
        for (int i = 0; i < a.length - 1; i++) if (a[i] > a[i + 1]) return i;
        return a.length - 1;
    }

    static int findPeak(int[] a) {
        int lo = 0, hi = a.length - 1;
        while (lo < hi) {
            int mid = lo + (hi - lo) / 2;
            if (a[mid] < a[mid + 1]) lo = mid + 1; else hi = mid;
        }
        return lo;
    }

    static int singleNonDuplicateXor(int[] a) { int x = 0; for (int v : a) x ^= v; return x; }

    static int singleNonDuplicate(int[] a) {
        int lo = 0, hi = a.length - 1;
        while (lo < hi) {
            int mid = lo + (hi - lo) / 2;
            if (mid % 2 == 1) mid--;                 // make mid the start of a pair
            if (a[mid] == a[mid + 1]) lo = mid + 2;  // pairing intact -> single is to the right
            else hi = mid;
        }
        return a[lo];
    }

    static boolean searchMatrix(int[][] m, int t) {
        int R = m.length, C = m[0].length, lo = 0, hi = R * C - 1;
        while (lo <= hi) {
            int mid = lo + (hi - lo) / 2, v = m[mid / C][mid % C];
            if (v == t) return true;
            if (v < t) lo = mid + 1; else hi = mid - 1;
        }
        return false;
    }

    static boolean searchMatrixII(int[][] m, int t) {
        int r = 0, c = m[0].length - 1;
        while (r < m.length && c >= 0) {
            if (m[r][c] == t) return true;
            if (m[r][c] > t) c--; else r++;          // eliminate a column or a row each step
        }
        return false;
    }

    public static void main(String[] args) {
        int[] p = {1, 2, 1, 3, 5, 6, 4};
        int i1 = findPeak(p), i2 = findPeakLinear(p);
        check(i1 == 1 || i1 == 5, true); check(i2 == 1 || i2 == 5, true);
        int[] s = {1, 1, 2, 3, 3, 4, 4, 8, 8};
        check(singleNonDuplicateXor(s), 2); check(singleNonDuplicate(s), 2);
        check(singleNonDuplicate(new int[]{3, 3, 7, 7, 10, 11, 11}), 10);
        int[][] m = {{1, 3, 5, 7}, {10, 11, 16, 20}, {23, 30, 34, 60}};
        check(searchMatrix(m, 3), true); check(searchMatrix(m, 13), false);
        int[][] m2 = {{1, 4, 7, 11}, {2, 5, 8, 12}, {3, 6, 9, 16}, {10, 13, 14, 17}};
        check(searchMatrixII(m2, 5), true); check(searchMatrixII(m2, 20), false);
        System.out.println("PeakAndMatrix: all tests passed");
    }

    static void check(Object got, Object exp) {
        if (!got.equals(exp)) throw new AssertionError("expected " + exp + " but got " + got);
    }
}
