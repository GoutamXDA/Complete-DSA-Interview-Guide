import java.util.*;

/**
 * Sliding window problems, each with brute and window versions.
 *
 * Max sum of size-K subarray: brute O(nk) | fixed window O(n)
 * LC 3 Longest substring w/o repeats: brute all substrings O(n^3) | window+set O(2n) | window+lastIndex O(n)
 * LC 424 Char replacement: window valid while (len - maxFreq) <= k. maxFreq never needs decreasing. O(n)
 * LC 1004 Max consecutive ones III: window with at most k zeros. O(n)
 * LC 992 Exactly K distinct: atMost(k) - atMost(k-1). O(n)
 * LC 209 Min size subarray sum >= target: shrink while valid. O(n)  (binary search on prefix: O(n log n))
 */
public class SlidingWindowBasics {

    static int maxSumKBrute(int[] a, int k) {
        int best = Integer.MIN_VALUE;
        for (int i = 0; i + k <= a.length; i++) {
            int s = 0; for (int j = i; j < i + k; j++) s += a[j];
            best = Math.max(best, s);
        }
        return best;
    }

    static int maxSumK(int[] a, int k) {
        int s = 0, best = Integer.MIN_VALUE;
        for (int r = 0; r < a.length; r++) {
            s += a[r];
            if (r >= k) s -= a[r - k];
            if (r >= k - 1) best = Math.max(best, s);
        }
        return best;
    }

    static int lengthOfLongestSubstringBrute(String s) {
        int best = 0;
        for (int i = 0; i < s.length(); i++) {
            Set<Character> seen = new HashSet<>();
            for (int j = i; j < s.length() && seen.add(s.charAt(j)); j++) best = Math.max(best, j - i + 1);
        }
        return best;
    }

    static int lengthOfLongestSubstringSet(String s) {
        Set<Character> win = new HashSet<>();
        int l = 0, best = 0;
        for (int r = 0; r < s.length(); r++) {
            while (win.contains(s.charAt(r))) win.remove(s.charAt(l++));
            win.add(s.charAt(r));
            best = Math.max(best, r - l + 1);
        }
        return best;
    }

    static int lengthOfLongestSubstring(String s) {
        int[] last = new int[128]; Arrays.fill(last, -1);
        int l = 0, best = 0;
        for (int r = 0; r < s.length(); r++) {
            char c = s.charAt(r);
            if (last[c] >= l) l = last[c] + 1;   // jump directly past the previous occurrence
            last[c] = r;
            best = Math.max(best, r - l + 1);
        }
        return best;
    }

    static int characterReplacement(String s, int k) {
        int[] f = new int[26];
        int l = 0, maxF = 0, best = 0;
        for (int r = 0; r < s.length(); r++) {
            maxF = Math.max(maxF, ++f[s.charAt(r) - 'A']);
            while (r - l + 1 - maxF > k) f[s.charAt(l++) - 'A']--;
            best = Math.max(best, r - l + 1);
        }
        return best;
    }

    static int longestOnes(int[] a, int k) {
        int l = 0, zeros = 0, best = 0;
        for (int r = 0; r < a.length; r++) {
            if (a[r] == 0) zeros++;
            while (zeros > k) if (a[l++] == 0) zeros--;
            best = Math.max(best, r - l + 1);
        }
        return best;
    }

    static int subarraysWithKDistinct(int[] a, int k) { return atMost(a, k) - atMost(a, k - 1); }

    static int atMost(int[] a, int k) {
        Map<Integer, Integer> f = new HashMap<>();
        int l = 0, count = 0;
        for (int r = 0; r < a.length; r++) {
            f.merge(a[r], 1, Integer::sum);
            while (f.size() > k) {
                if (f.merge(a[l], -1, Integer::sum) == 0) f.remove(a[l]);
                l++;
            }
            count += r - l + 1;   // all subarrays ending at r with start in [l, r]
        }
        return count;
    }

    static int minSubArrayLen(int target, int[] a) {
        int l = 0, s = 0, best = Integer.MAX_VALUE;
        for (int r = 0; r < a.length; r++) {
            s += a[r];
            while (s >= target) { best = Math.min(best, r - l + 1); s -= a[l++]; }
        }
        return best == Integer.MAX_VALUE ? 0 : best;
    }

    public static void main(String[] args) {
        int[] a = {2, 1, 5, 1, 3, 2};
        check(maxSumKBrute(a, 3), 9); check(maxSumK(a, 3), 9);
        for (String s : new String[]{"abcabcbb", "bbbbb", "pwwkew", "abba"}) {
            int e = lengthOfLongestSubstringBrute(s);
            check(lengthOfLongestSubstringSet(s), e); check(lengthOfLongestSubstring(s), e);
        }
        check(lengthOfLongestSubstring("abcabcbb"), 3); check(lengthOfLongestSubstring("abba"), 2);
        check(characterReplacement("AABABBA", 1), 4);
        check(longestOnes(new int[]{1, 1, 1, 0, 0, 0, 1, 1, 1, 1, 0}, 2), 6);
        check(subarraysWithKDistinct(new int[]{1, 2, 1, 2, 3}, 2), 7);
        check(minSubArrayLen(7, new int[]{2, 3, 1, 2, 4, 3}), 2);
        System.out.println("SlidingWindowBasics: all tests passed");
    }

    static void check(Object got, Object exp) {
        if (!got.equals(exp)) throw new AssertionError("expected " + exp + " but got " + got);
    }
}
