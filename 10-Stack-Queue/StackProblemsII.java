import java.util.*;

/**
 * LC 71   Simplify path: split on '/', stack of names; ".." pops, "." and "" skip. O(n)
 * LC 1209 Remove all adjacent duplicates II (k in a row): brute repeat scanning O(n^2 / k) | stack of (char, count) O(n)
 * LC 853  Car fleet: sort by position desc; arrival time; a car slower (larger time) than the fleet ahead starts a new fleet. O(n log n)
 * LC 362  Design hit counter (last 300 s): queue of timestamps | 300 circular buckets (time, count) O(1) memory
 * LC 2104 Sum of subarray ranges: brute O(n^2) tracking min/max | sum(max contributions) - sum(min contributions) with monotonic stacks O(n)
 * LC 32   Longest valid parentheses: brute check every even substring O(n^3)
 *         | stack of indices with base -1 O(n) | two counters left-to-right and right-to-left O(n), O(1) | DP O(n)
 * LC 895  Maximum frequency stack: freq map + map freq -> stack; pop from the max-frequency stack. O(1)
 */
public class StackProblemsII {

    static String simplifyPath(String path) {
        Deque<String> st = new ArrayDeque<>();
        for (String p : path.split("/")) {
            if (p.isEmpty() || p.equals(".")) continue;
            if (p.equals("..")) { if (!st.isEmpty()) st.pollLast(); }
            else st.offerLast(p);
        }
        return "/" + String.join("/", st);
    }

    static String removeDuplicatesBrute(String s, int k) {
        boolean changed = true;
        while (changed) {
            changed = false;
            for (int i = 0; i + k <= s.length(); i++) {
                int j = i;
                while (j < s.length() && s.charAt(j) == s.charAt(i)) j++;
                if (j - i >= k) { s = s.substring(0, i) + s.substring(i + k); changed = true; break; }
            }
        }
        return s;
    }

    static String removeDuplicates(String s, int k) {
        Deque<int[]> st = new ArrayDeque<>();   // {char, count}
        for (char c : s.toCharArray()) {
            if (!st.isEmpty() && st.peek()[0] == c) { if (++st.peek()[1] == k) st.pop(); }
            else st.push(new int[]{c, 1});
        }
        StringBuilder sb = new StringBuilder();
        Iterator<int[]> it = st.descendingIterator();
        while (it.hasNext()) { int[] e = it.next(); sb.append(String.valueOf((char) e[0]).repeat(e[1])); }
        return sb.toString();
    }

    static int carFleet(int target, int[] pos, int[] speed) {
        int n = pos.length;
        Integer[] idx = new Integer[n];
        for (int i = 0; i < n; i++) idx[i] = i;
        Arrays.sort(idx, (a, b) -> pos[b] - pos[a]);
        int fleets = 0; double slowest = 0;
        for (int i : idx) {
            double t = (double) (target - pos[i]) / speed[i];
            if (t > slowest) { fleets++; slowest = t; }   // can't catch the fleet ahead
        }
        return fleets;
    }

    static class HitCounterQueue {
        final Deque<Integer> q = new ArrayDeque<>();
        void hit(int t) { q.offer(t); }
        int getHits(int t) { while (!q.isEmpty() && q.peek() <= t - 300) q.poll(); return q.size(); }
    }

    static class HitCounter {
        final int[] times = new int[300], counts = new int[300];
        void hit(int t) {
            int i = t % 300;
            if (times[i] != t) { times[i] = t; counts[i] = 1; } else counts[i]++;
        }
        int getHits(int t) {
            int total = 0;
            for (int i = 0; i < 300; i++) if (t - times[i] < 300) total += counts[i];
            return total;
        }
    }

    static long subArrayRangesBrute(int[] a) {
        long s = 0;
        for (int i = 0; i < a.length; i++) {
            int mn = a[i], mx = a[i];
            for (int j = i; j < a.length; j++) { mn = Math.min(mn, a[j]); mx = Math.max(mx, a[j]); s += mx - mn; }
        }
        return s;
    }

    static long subArrayRanges(int[] a) { return contribution(a, true) - contribution(a, false); }

    /** sum over subarrays of max (isMax) or min. Ties: strict on one side, non-strict on the other. */
    static long contribution(int[] a, boolean isMax) {
        int n = a.length;
        int[] left = new int[n], right = new int[n];
        Deque<Integer> st = new ArrayDeque<>();
        for (int i = 0; i < n; i++) {
            while (!st.isEmpty() && (isMax ? a[st.peek()] < a[i] : a[st.peek()] > a[i])) st.pop();
            left[i] = st.isEmpty() ? i + 1 : i - st.peek();
            st.push(i);
        }
        st.clear();
        for (int i = n - 1; i >= 0; i--) {
            while (!st.isEmpty() && (isMax ? a[st.peek()] <= a[i] : a[st.peek()] >= a[i])) st.pop();
            right[i] = st.isEmpty() ? n - i : st.peek() - i;
            st.push(i);
        }
        long s = 0;
        for (int i = 0; i < n; i++) s += (long) a[i] * left[i] * right[i];
        return s;
    }

    static int longestValidParenthesesBrute(String s) {
        int best = 0;
        for (int i = 0; i < s.length(); i++)
            for (int j = i + 2; j <= s.length(); j += 2) {
                int bal = 0; boolean ok = true;
                for (int k = i; k < j && ok; k++) { bal += s.charAt(k) == '(' ? 1 : -1; if (bal < 0) ok = false; }
                if (ok && bal == 0) best = Math.max(best, j - i);
            }
        return best;
    }

    static int longestValidParenthesesStack(String s) {
        Deque<Integer> st = new ArrayDeque<>();
        st.push(-1);                                  // index before the current valid run
        int best = 0;
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == '(') st.push(i);
            else {
                st.pop();
                if (st.isEmpty()) st.push(i);         // unmatched ')' becomes the new base
                else best = Math.max(best, i - st.peek());
            }
        }
        return best;
    }

    static int longestValidParentheses(String s) {
        int best = 0, open = 0, close = 0;
        for (char c : s.toCharArray()) {
            if (c == '(') open++; else close++;
            if (open == close) best = Math.max(best, 2 * close); else if (close > open) open = close = 0;
        }
        open = close = 0;
        for (int i = s.length() - 1; i >= 0; i--) {
            if (s.charAt(i) == '(') open++; else close++;
            if (open == close) best = Math.max(best, 2 * open); else if (open > close) open = close = 0;
        }
        return best;
    }

    static class FreqStack {
        final Map<Integer, Integer> freq = new HashMap<>();
        final Map<Integer, Deque<Integer>> group = new HashMap<>();
        int maxFreq = 0;
        void push(int x) {
            int f = freq.merge(x, 1, Integer::sum);
            maxFreq = Math.max(maxFreq, f);
            group.computeIfAbsent(f, k -> new ArrayDeque<>()).push(x);
        }
        int pop() {
            int x = group.get(maxFreq).pop();
            freq.merge(x, -1, Integer::sum);
            if (group.get(maxFreq).isEmpty()) maxFreq--;
            return x;
        }
    }

    public static void main(String[] args) {
        check(simplifyPath("/home//foo/"), "/home/foo"); check(simplifyPath("/../"), "/"); check(simplifyPath("/a/./b/../../c/"), "/c");
        check(simplifyPath("/.../a/../b/c/../d/./"), "/.../b/d");
        check(removeDuplicates("deeedbbcccbdaa", 3), "aa"); check(removeDuplicatesBrute("deeedbbcccbdaa", 3), "aa");
        check(removeDuplicates("pbbcggttciiippooaais", 2), "ps");
        check(carFleet(12, new int[]{10, 8, 0, 5, 3}, new int[]{2, 4, 1, 1, 3}), 3); check(carFleet(100, new int[]{0, 2, 4}, new int[]{4, 2, 1}), 1);
        HitCounter h = new HitCounter(); HitCounterQueue hq = new HitCounterQueue();
        for (int t : new int[]{1, 2, 3}) { h.hit(t); hq.hit(t); }
        check(h.getHits(4), 3); check(hq.getHits(4), 3);
        h.hit(300); hq.hit(300);
        check(h.getHits(300), 4); check(hq.getHits(300), 4); check(h.getHits(301), 3); check(hq.getHits(301), 3);
        for (int[] t : new int[][]{{1, 2, 3}, {1, 3, 3}, {4, -2, -3, 4, 1}})
            check(subArrayRanges(t), subArrayRangesBrute(t));
        check(subArrayRanges(new int[]{4, -2, -3, 4, 1}), 59L);
        for (String t : new String[]{"(()", ")()())", "", "()(()", "(()())", "())((())"}) {
            int b = longestValidParenthesesBrute(t);
            check(longestValidParenthesesStack(t), b); check(longestValidParentheses(t), b);
        }
        check(longestValidParentheses(")()())"), 4);
        FreqStack fs = new FreqStack();
        for (int x : new int[]{5, 7, 5, 7, 4, 5}) fs.push(x);
        check(fs.pop(), 5); check(fs.pop(), 7); check(fs.pop(), 5); check(fs.pop(), 4);
        System.out.println("StackProblemsII: all tests passed");
    }

    static void check(Object got, Object exp) {
        if (!got.equals(exp)) throw new AssertionError("expected " + exp + " but got " + got);
    }
}
