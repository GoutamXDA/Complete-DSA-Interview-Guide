import java.util.*;

/**
 * LC 881  Boats to save people (limit, max 2 per boat): sort; pair heaviest with lightest if it fits. O(n log n)
 * LC 1423 Max points from cards (take k from either end):
 *   Brute try i from left, k-i from right with prefix sums O(k) | equivalently total - min window of size n-k. O(n)
 * LC 904  Fruit into baskets: longest window with <= 2 distinct values. O(n)
 * LC 340  Longest substring with at most K distinct characters: same window with a count map. O(n)
 * LC 930  Binary subarrays with sum = goal: prefix-sum map O(n) space | atMost(goal) - atMost(goal-1) O(1) space
 * LC 1248 Count nice subarrays (exactly k odd numbers): map odd -> 1, even -> 0, reuse atMost.
 * LC 1358 Substrings containing all of a, b, c: brute O(n^2) | for each end r, add 1 + min(last[a], last[b], last[c]). O(n)
 */
public class WindowProblemsII {

    static int numRescueBoats(int[] people, int limit) {
        int[] p = people.clone(); Arrays.sort(p);
        int l = 0, r = p.length - 1, boats = 0;
        while (l <= r) { if (p[l] + p[r] <= limit) l++; r--; boats++; }
        return boats;
    }

    static int maxScoreBrute(int[] c, int k) {
        int n = c.length, left = 0, best;
        for (int i = 0; i < k; i++) left += c[i];
        best = left;
        int right = 0;
        for (int i = 1; i <= k; i++) {          // take k-i from left, i from right
            left -= c[k - i]; right += c[n - i];
            best = Math.max(best, left + right);
        }
        return best;
    }

    static int maxScore(int[] c, int k) {
        int n = c.length, w = n - k, total = 0, win = 0, minWin;
        for (int x : c) total += x;
        for (int i = 0; i < w; i++) win += c[i];
        minWin = win;
        for (int i = w; i < n; i++) { win += c[i] - c[i - w]; minWin = Math.min(minWin, win); }
        return total - minWin;
    }

    static int totalFruit(int[] f) { return longestAtMostKDistinct(f, 2); }

    static int longestAtMostKDistinct(int[] a, int k) {
        Map<Integer, Integer> cnt = new HashMap<>();
        int l = 0, best = 0;
        for (int r = 0; r < a.length; r++) {
            cnt.merge(a[r], 1, Integer::sum);
            while (cnt.size() > k) { if (cnt.merge(a[l], -1, Integer::sum) == 0) cnt.remove(a[l]); l++; }
            best = Math.max(best, r - l + 1);
        }
        return best;
    }

    static int lengthOfLongestSubstringKDistinct(String s, int k) {
        return longestAtMostKDistinct(s.chars().toArray(), k);
    }

    static int numSubarraysWithSumMap(int[] a, int goal) {
        Map<Integer, Integer> freq = new HashMap<>(Map.of(0, 1));
        int sum = 0, ans = 0;
        for (int x : a) { sum += x; ans += freq.getOrDefault(sum - goal, 0); freq.merge(sum, 1, Integer::sum); }
        return ans;
    }

    static int numSubarraysWithSum(int[] a, int goal) { return atMost(a, goal) - atMost(a, goal - 1); }

    /** count of subarrays with sum <= s (non-negative entries) */
    static int atMost(int[] a, int s) {
        if (s < 0) return 0;
        int l = 0, sum = 0, cnt = 0;
        for (int r = 0; r < a.length; r++) {
            sum += a[r];
            while (sum > s) sum -= a[l++];
            cnt += r - l + 1;
        }
        return cnt;
    }

    static int numberOfSubarrays(int[] nums, int k) {
        int[] odd = new int[nums.length];
        for (int i = 0; i < nums.length; i++) odd[i] = nums[i] & 1;
        return atMost(odd, k) - atMost(odd, k - 1);
    }

    static int numberOfSubstringsBrute(String s) {
        int c = 0;
        for (int i = 0; i < s.length(); i++) {
            int[] f = new int[3];
            for (int j = i; j < s.length(); j++) { f[s.charAt(j) - 'a']++; if (f[0] > 0 && f[1] > 0 && f[2] > 0) { c += s.length() - j; break; } }
        }
        return c;
    }

    static int numberOfSubstrings(String s) {
        int[] last = {-1, -1, -1};
        int c = 0;
        for (int r = 0; r < s.length(); r++) {
            last[s.charAt(r) - 'a'] = r;
            c += 1 + Math.min(last[0], Math.min(last[1], last[2]));   // any start in [0, min] works
        }
        return c;
    }

    public static void main(String[] args) {
        check(numRescueBoats(new int[]{3, 2, 2, 1}, 3), 3); check(numRescueBoats(new int[]{3, 5, 3, 4}, 5), 4);
        int[] cards = {1, 2, 3, 4, 5, 6, 1};
        check(maxScoreBrute(cards, 3), 12); check(maxScore(cards, 3), 12); check(maxScore(new int[]{9, 7, 7, 9, 7, 7, 9}, 7), 55);
        check(totalFruit(new int[]{1, 2, 3, 2, 2}), 4); check(totalFruit(new int[]{3, 3, 3, 1, 2, 1, 1, 2, 3, 3, 4}), 5);
        check(lengthOfLongestSubstringKDistinct("eceba", 2), 3); check(lengthOfLongestSubstringKDistinct("aa", 1), 2);
        int[] bin = {1, 0, 1, 0, 1};
        check(numSubarraysWithSumMap(bin, 2), 4); check(numSubarraysWithSum(bin, 2), 4); check(numSubarraysWithSum(new int[]{0, 0, 0, 0, 0}, 0), 15);
        check(numberOfSubarrays(new int[]{1, 1, 2, 1, 1}, 3), 2); check(numberOfSubarrays(new int[]{2, 2, 2, 1, 2, 2, 1, 2, 2, 2}, 2), 16);
        check(numberOfSubstringsBrute("abcabc"), 10); check(numberOfSubstrings("abcabc"), 10); check(numberOfSubstrings("aaacb"), 3);
        System.out.println("WindowProblemsII: all tests passed");
    }

    static void check(Object got, Object exp) {
        if (!got.equals(exp)) throw new AssertionError("expected " + exp + " but got " + got);
    }
}
