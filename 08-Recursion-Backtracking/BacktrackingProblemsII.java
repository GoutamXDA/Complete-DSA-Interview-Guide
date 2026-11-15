import java.util.*;

/**
 * LC 93  Restore IP addresses: choose 4 segments of length 1..3, value <= 255, no leading zero. O(3^4 * n)
 * M-coloring problem (GFG): colour vertices with <= m colours so no edge joins equal colours. O(m^V) worst
 * LC 698 Partition to K equal sum subsets: sort descending, place each number into a bucket; prune
 *        (skip buckets with the same current sum, stop if a number doesn't fit an empty bucket). O(k^n) worst
 * LC 60  Permutation sequence (kth): brute = apply next-permutation k-1 times O(n*k)
 *        | factorial number system: digit index = (k-1) / (n-1)!. O(n^2)
 * LC 140 Word break II: plain backtracking (exponential, recomputes suffixes) | memo on start index.
 * LC 282 Expression add operators: carry (value so far, last operand); for '*': value - last + last*cur.
 */
public class BacktrackingProblemsII {

    static List<String> restoreIpAddresses(String s) {
        List<String> res = new ArrayList<>();
        ip(s, 0, new ArrayList<>(), res);
        return res;
    }

    static void ip(String s, int start, List<String> parts, List<String> res) {
        if (parts.size() == 4) { if (start == s.length()) res.add(String.join(".", parts)); return; }
        int remaining = s.length() - start, slots = 4 - parts.size();
        if (remaining < slots || remaining > slots * 3) return;            // prune impossible lengths
        for (int len = 1; len <= 3 && start + len <= s.length(); len++) {
            String seg = s.substring(start, start + len);
            if ((seg.length() > 1 && seg.charAt(0) == '0') || Integer.parseInt(seg) > 255) continue;
            parts.add(seg); ip(s, start + len, parts, res); parts.remove(parts.size() - 1);
        }
    }

    static boolean graphColoring(boolean[][] g, int m) { return color(g, m, new int[g.length], 0); }

    static boolean color(boolean[][] g, int m, int[] col, int v) {
        if (v == g.length) return true;
        for (int c = 1; c <= m; c++) {
            boolean ok = true;
            for (int u = 0; u < g.length && ok; u++) if (g[v][u] && col[u] == c) ok = false;
            if (!ok) continue;
            col[v] = c;
            if (color(g, m, col, v + 1)) return true;
            col[v] = 0;
        }
        return false;
    }

    static boolean canPartitionKSubsets(int[] nums, int k) {
        int total = Arrays.stream(nums).sum();
        if (total % k != 0) return false;
        int target = total / k;
        Integer[] a = Arrays.stream(nums).boxed().sorted(Collections.reverseOrder()).toArray(Integer[]::new);
        if (a[0] > target) return false;
        return place(a, 0, new int[k], target);
    }

    static boolean place(Integer[] a, int i, int[] bucket, int target) {
        if (i == a.length) return true;
        for (int b = 0; b < bucket.length; b++) {
            if (bucket[b] + a[i] > target) continue;
            if (b > 0 && bucket[b] == bucket[b - 1]) continue;        // same state as previous bucket
            bucket[b] += a[i];
            if (place(a, i + 1, bucket, target)) return true;
            bucket[b] -= a[i];
            if (bucket[b] == 0) break;                                // didn't fit an empty bucket -> won't fit others
        }
        return false;
    }

    static String getPermutationBrute(int n, int k) {
        int[] a = new int[n];
        for (int i = 0; i < n; i++) a[i] = i + 1;
        for (int t = 1; t < k; t++) nextPermutation(a);
        StringBuilder sb = new StringBuilder();
        for (int x : a) sb.append(x);
        return sb.toString();
    }

    static void nextPermutation(int[] a) {
        int i = a.length - 2;
        while (i >= 0 && a[i] >= a[i + 1]) i--;
        if (i >= 0) { int j = a.length - 1; while (a[j] <= a[i]) j--; int t = a[i]; a[i] = a[j]; a[j] = t; }
        for (int l = i + 1, r = a.length - 1; l < r; l++, r--) { int t = a[l]; a[l] = a[r]; a[r] = t; }
    }

    static String getPermutation(int n, int k) {
        List<Integer> nums = new ArrayList<>();
        int fact = 1;
        for (int i = 1; i <= n; i++) { nums.add(i); if (i < n) fact *= i; }   // fact = (n-1)!
        k--;                                                                   // 0-indexed
        StringBuilder sb = new StringBuilder();
        for (int i = n - 1; i >= 0; i--) {
            sb.append(nums.remove(k / fact));
            if (i == 0) break;
            k %= fact;
            fact /= i;
        }
        return sb.toString();
    }

    static List<String> wordBreakBrute(String s, List<String> dict) {
        List<String> res = new ArrayList<>();
        wb(s, 0, new HashSet<>(dict), new ArrayList<>(), res);
        Collections.sort(res);
        return res;
    }

    static void wb(String s, int start, Set<String> dict, List<String> path, List<String> res) {
        if (start == s.length()) { res.add(String.join(" ", path)); return; }
        for (int end = start + 1; end <= s.length(); end++) {
            String w = s.substring(start, end);
            if (dict.contains(w)) { path.add(w); wb(s, end, dict, path, res); path.remove(path.size() - 1); }
        }
    }

    static List<String> wordBreak(String s, List<String> dict) {
        List<String> res = new ArrayList<>(memo(s, 0, new HashSet<>(dict), new HashMap<>()));
        Collections.sort(res);
        return res;
    }

    static List<String> memo(String s, int start, Set<String> dict, Map<Integer, List<String>> cache) {
        if (cache.containsKey(start)) return cache.get(start);
        List<String> res = new ArrayList<>();
        if (start == s.length()) res.add("");
        for (int end = start + 1; end <= s.length(); end++) {
            String w = s.substring(start, end);
            if (!dict.contains(w)) continue;
            for (String rest : memo(s, end, dict, cache)) res.add(rest.isEmpty() ? w : w + " " + rest);
        }
        cache.put(start, res);
        return res;
    }

    static List<String> addOperators(String num, int target) {
        List<String> res = new ArrayList<>();
        ops(num, target, 0, 0, 0, new StringBuilder(), res);
        return res;
    }

    static void ops(String num, int target, int pos, long value, long last, StringBuilder expr, List<String> res) {
        if (pos == num.length()) { if (value == target) res.add(expr.toString()); return; }
        int len = expr.length();
        for (int i = pos; i < num.length(); i++) {
            if (i > pos && num.charAt(pos) == '0') break;               // no multi-digit number with leading zero
            long cur = Long.parseLong(num.substring(pos, i + 1));
            if (pos == 0) {
                expr.append(cur); ops(num, target, i + 1, cur, cur, expr, res); expr.setLength(len);
            } else {
                expr.append('+').append(cur); ops(num, target, i + 1, value + cur, cur, expr, res); expr.setLength(len);
                expr.append('-').append(cur); ops(num, target, i + 1, value - cur, -cur, expr, res); expr.setLength(len);
                expr.append('*').append(cur); ops(num, target, i + 1, value - last + last * cur, last * cur, expr, res); expr.setLength(len);
            }
        }
    }

    public static void main(String[] args) {
        check(restoreIpAddresses("25525511135"), List.of("255.255.11.135", "255.255.111.35"));
        check(restoreIpAddresses("0000"), List.of("0.0.0.0"));
        check(restoreIpAddresses("101023").size(), 5);
        boolean[][] g = new boolean[4][4];
        for (int[] e : new int[][]{{0, 1}, {1, 2}, {2, 3}, {3, 0}, {0, 2}}) { g[e[0]][e[1]] = g[e[1]][e[0]] = true; }
        check(graphColoring(g, 3), true); check(graphColoring(g, 2), false);
        check(canPartitionKSubsets(new int[]{4, 3, 2, 3, 5, 2, 1}, 4), true); check(canPartitionKSubsets(new int[]{1, 2, 3, 4}, 3), false);
        for (int k = 1; k <= 24; k++) check(getPermutation(4, k), getPermutationBrute(4, k));
        check(getPermutation(3, 3), "213"); check(getPermutation(9, 362880), "987654321");
        List<String> d = List.of("cat", "cats", "and", "sand", "dog");
        check(wordBreak("catsanddog", d), List.of("cat sand dog", "cats and dog")); check(wordBreakBrute("catsanddog", d), wordBreak("catsanddog", d));
        check(wordBreak("catsandog", d), List.of());
        List<String> r = addOperators("123", 6); Collections.sort(r); check(r, List.of("1*2*3", "1+2+3"));
        r = addOperators("105", 5); Collections.sort(r); check(r, List.of("1*0+5", "10-5"));
        check(addOperators("3456237490", 9191).size(), 0);
        System.out.println("BacktrackingProblemsII: all tests passed");
    }

    static void check(Object got, Object exp) {
        if (!got.equals(exp)) throw new AssertionError("expected " + exp + " but got " + got);
    }
}
