import java.util.*;

/**
 * LC 1944 — Number of Visible People in a Queue. Explanation: NumberOfVisiblePeopleInQueue.md
 * 1. Brute force with running max between       O(n^2)
 * 2. Monotonic stack from the right             O(n)
 */
public class NumberOfVisiblePeopleInQueue {

    static int[] brute(int[] h) {
        int n = h.length;
        int[] ans = new int[n];
        for (int i = 0; i < n; i++) {
            int maxBetween = 0;
            for (int j = i + 1; j < n; j++) {
                if (maxBetween < Math.min(h[i], h[j])) ans[i]++;
                maxBetween = Math.max(maxBetween, h[j]);
                if (h[j] > h[i]) break;                      // nobody further right is visible
            }
        }
        return ans;
    }

    static int[] canSeePersonsCount(int[] h) {
        int n = h.length;
        int[] ans = new int[n];
        Deque<Integer> st = new ArrayDeque<>();          // heights, decreasing toward the top
        for (int i = n - 1; i >= 0; i--) {
            int count = 0;
            while (!st.isEmpty() && st.peek() < h[i]) { st.pop(); count++; }
            if (!st.isEmpty()) count++;                      // first taller person
            ans[i] = count;
            st.push(h[i]);
        }
        return ans;
    }

    public static void main(String[] args) {
        check(Arrays.toString(canSeePersonsCount(new int[]{10, 6, 8, 5, 11, 9})), "[3, 1, 2, 1, 1, 0]");
        check(Arrays.toString(canSeePersonsCount(new int[]{5, 1, 2, 3, 10})), "[4, 1, 1, 1, 0]");
        Random r = new Random(32);
        for (int t = 0; t < 3000; t++) {
            int n = r.nextInt(12) + 1;
            List<Integer> perm = new ArrayList<>();
            for (int i = 1; i <= n; i++) perm.add(i);
            Collections.shuffle(perm, r);
            int[] h = perm.stream().mapToInt(Integer::intValue).toArray();   // distinct heights
            check(Arrays.toString(canSeePersonsCount(h)), Arrays.toString(brute(h)));
        }
        System.out.println("NumberOfVisiblePeopleInQueue: all tests passed");
    }

    static void check(Object got, Object exp) {
        if (!got.equals(exp)) throw new AssertionError("expected " + exp + " but got " + got);
    }
}
