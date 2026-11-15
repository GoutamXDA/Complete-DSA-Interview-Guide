import java.util.*;

/**
 * LC 169 — element appearing > n/2 times (guaranteed to exist).
 *   Brute   : count each element               O(n^2)
 *   Sort    : answer is a[n/2]                 O(n log n)
 *   HashMap : frequencies                      O(n), O(n)
 *   Boyer–Moore voting: candidate + count; different elements cancel. O(n), O(1)
 *
 * LC 229 — all elements appearing > n/3 times: at most 2 candidates.
 *   Extended Boyer–Moore with 2 candidates, then verify counts. O(n), O(1)
 */
public class MajorityElement {

    static int brute(int[] a) {
        for (int x : a) {
            int c = 0;
            for (int y : a) if (y == x) c++;
            if (c > a.length / 2) return x;
        }
        return -1;
    }

    static int sorting(int[] a) { int[] s = a.clone(); Arrays.sort(s); return s[s.length / 2]; }

    static int hashMap(int[] a) {
        Map<Integer, Integer> f = new HashMap<>();
        for (int x : a) if (f.merge(x, 1, Integer::sum) > a.length / 2) return x;
        return -1;
    }

    static int boyerMoore(int[] a) {
        int cand = 0, count = 0;
        for (int x : a) {
            if (count == 0) cand = x;
            count += (x == cand) ? 1 : -1;
        }
        return cand; // verify with a second pass if existence isn't guaranteed
    }

    static List<Integer> majorityNBy3(int[] a) {
        int c1 = 0, c2 = 1, n1 = 0, n2 = 0; // c1 != c2 initially
        for (int x : a) {
            if (x == c1) n1++;
            else if (x == c2) n2++;
            else if (n1 == 0) { c1 = x; n1 = 1; }
            else if (n2 == 0) { c2 = x; n2 = 1; }
            else { n1--; n2--; }
        }
        n1 = 0; n2 = 0;
        for (int x : a) { if (x == c1) n1++; else if (x == c2) n2++; }
        List<Integer> res = new ArrayList<>();
        if (n1 > a.length / 3) res.add(c1);
        if (n2 > a.length / 3) res.add(c2);
        return res;
    }

    public static void main(String[] args) {
        int[] a = {2, 2, 1, 1, 1, 2, 2};
        check(brute(a), 2); check(sorting(a), 2); check(hashMap(a), 2); check(boyerMoore(a), 2);
        List<Integer> r = majorityNBy3(new int[]{1, 1, 1, 3, 3, 2, 2, 2});
        Collections.sort(r); check(r, List.of(1, 2));
        check(majorityNBy3(new int[]{3, 2, 3}), List.of(3));
        System.out.println("MajorityElement: all tests passed");
    }

    static void check(Object got, Object exp) {
        if (!got.equals(exp)) throw new AssertionError("expected " + exp + " but got " + got);
    }
}
