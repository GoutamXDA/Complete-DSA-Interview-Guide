import java.util.*;

/**
 * LC 135 — Candy. Explanation: Candy.md
 * 1. Repeated relaxation until stable     O(n^2)
 * 2. Two passes                           O(n) time, O(n) space
 * 3. One pass counting slopes             O(n) time, O(1) space
 */
public class Candy {

    static long relax(int[] r) {
        int n = r.length;
        int[] c = new int[n];
        Arrays.fill(c, 1);
        for (boolean changed = true; changed; ) {
            changed = false;
            for (int i = 0; i < n; i++) {
                if (i > 0 && r[i] > r[i - 1] && c[i] <= c[i - 1]) { c[i] = c[i - 1] + 1; changed = true; }
                if (i + 1 < n && r[i] > r[i + 1] && c[i] <= c[i + 1]) { c[i] = c[i + 1] + 1; changed = true; }
            }
        }
        long s = 0; for (int x : c) s += x; return s;
    }

    static long twoPass(int[] r) {
        int n = r.length;
        int[] c = new int[n];
        Arrays.fill(c, 1);
        for (int i = 1; i < n; i++) if (r[i] > r[i - 1]) c[i] = c[i - 1] + 1;
        for (int i = n - 2; i >= 0; i--) if (r[i] > r[i + 1]) c[i] = Math.max(c[i], c[i + 1] + 1);
        long s = 0; for (int x : c) s += x; return s;
    }

    static long onePass(int[] r) {
        int n = r.length;
        if (n == 0) return 0;
        long total = 1;
        int up = 0, down = 0, peak = 0;          // peak = length of the last up-run (candies at the peak - 1)
        for (int i = 1; i < n; i++) {
            if (r[i] > r[i - 1]) { up++; down = 0; peak = up; total += up + 1; }
            else if (r[i] == r[i - 1]) { up = down = peak = 0; total += 1; }
            else {
                up = 0; down++;
                total += down + 1 - (down <= peak ? 1 : 0);   // new child gets 1; every child in the descent shifts up by 1; the peak only grows once down exceeds it
            }
        }
        return total;
    }

    public static void main(String[] args) {
        check(twoPass(new int[]{1, 0, 2}), 5L); check(twoPass(new int[]{1, 2, 2}), 4L); check(onePass(new int[]{1, 0, 2}), 5L);
        check(onePass(new int[]{1, 3, 4, 5, 2}), 11L); check(onePass(new int[]{1, 2, 87, 87, 87, 2, 1}), 13L);
        Random rnd = new Random(62);
        for (int t = 0; t < 5000; t++) {
            int[] r = new int[rnd.nextInt(15) + 1];
            for (int i = 0; i < r.length; i++) r[i] = rnd.nextInt(5);
            long e = relax(r);
            check(twoPass(r), e); check(onePass(r), e);
        }
        System.out.println("Candy: all tests passed");
    }

    static void check(Object got, Object exp) {
        if (!got.equals(exp)) throw new AssertionError("expected " + exp + " but got " + got);
    }
}
