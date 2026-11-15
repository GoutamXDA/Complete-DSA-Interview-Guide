import java.util.*;

/**
 * LC 301 — Remove Invalid Parentheses. Explanation: RemoveInvalidParentheses.md
 * 1. Brute force over deletion subsets       O(2^n n)
 * 2. BFS by number of deletions              exponential worst, minimal by construction
 * 3. DFS with exact removal counts + run pruning
 * 4. Two-pass left/right removal (no set needed)
 */
public class RemoveInvalidParentheses {

    static boolean valid(String s) {
        int bal = 0;
        for (char c : s.toCharArray()) { if (c == '(') bal++; else if (c == ')' && --bal < 0) return false; }
        return bal == 0;
    }

    static List<String> brute(String s) {
        int n = s.length(), best = -1;
        Set<String> res = new TreeSet<>();
        for (int mask = 0; mask < 1 << n; mask++) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < n; i++) if ((mask >> i & 1) == 0) sb.append(s.charAt(i));
            String t = sb.toString();
            if (!valid(t)) continue;
            if (t.length() > best) { best = t.length(); res.clear(); }
            if (t.length() == best) res.add(t);
        }
        return new ArrayList<>(res);
    }

    static List<String> bfs(String s) {
        Set<String> level = new HashSet<>(List.of(s));
        while (true) {
            List<String> ok = new ArrayList<>();
            for (String t : level) if (valid(t)) ok.add(t);
            if (!ok.isEmpty()) { Collections.sort(ok); return ok; }
            Set<String> next = new HashSet<>();
            for (String t : level)
                for (int i = 0; i < t.length(); i++)
                    if (t.charAt(i) == '(' || t.charAt(i) == ')') next.add(t.substring(0, i) + t.substring(i + 1));
            level = next;
        }
    }

    static List<String> dfs(String s) {
        int open = 0, close = 0;
        for (char c : s.toCharArray()) {
            if (c == '(') open++;
            else if (c == ')') { if (open > 0) open--; else close++; }
        }
        List<String> res = new ArrayList<>();          // a list: the pruning rule alone guarantees distinct results
        go(s, 0, open, close, 0, false, new StringBuilder(), res);
        Collections.sort(res);
        return res;
    }

    /**
     * Duplicate rule: in a run of identical parens, deleting any j of them gives the same string,
     * so only allow deletions that form a PREFIX of the run: delete s[i] only if it starts a run
     * or the previous char (same symbol) was also deleted.
     */
    static void go(String s, int i, int openRem, int closeRem, int bal, boolean prevDeleted, StringBuilder cur, List<String> res) {
        if (i == s.length()) { if (openRem == 0 && closeRem == 0 && bal == 0) res.add(cur.toString()); return; }
        char c = s.charAt(i);
        boolean canDelete = i == 0 || s.charAt(i - 1) != c || prevDeleted;
        if (c == '(' && openRem > 0 && canDelete) go(s, i + 1, openRem - 1, closeRem, bal, true, cur, res);
        if (c == ')' && closeRem > 0 && canDelete) go(s, i + 1, openRem, closeRem - 1, bal, true, cur, res);
        int len = cur.length();
        cur.append(c);
        if (c == '(') go(s, i + 1, openRem, closeRem, bal + 1, false, cur, res);
        else if (c == ')') { if (bal > 0) go(s, i + 1, openRem, closeRem, bal - 1, false, cur, res); }
        else go(s, i + 1, openRem, closeRem, bal, false, cur, res);
        cur.setLength(len);
    }

    static List<String> twoPass(String s) {
        List<String> res = new ArrayList<>();
        remove(s, res, 0, 0, new char[]{'(', ')'});
        Collections.sort(res);
        return res;
    }

    /** remove extra par[1] scanning left to right; then recurse on the reversed string for par[0]. */
    static void remove(String s, List<String> res, int lastI, int lastJ, char[] par) {
        int bal = 0;
        for (int i = lastI; i < s.length(); i++) {
            if (s.charAt(i) == par[0]) bal++;
            if (s.charAt(i) == par[1]) bal--;
            if (bal >= 0) continue;
            for (int j = lastJ; j <= i; j++)                     // delete one par[1] at the first of each run
                if (s.charAt(j) == par[1] && (j == lastJ || s.charAt(j - 1) != par[1]))
                    remove(s.substring(0, j) + s.substring(j + 1), res, i, j, par);
            return;
        }
        String rev = new StringBuilder(s).reverse().toString();
        if (par[0] == '(') remove(rev, res, 0, 0, new char[]{')', '('});
        else res.add(rev);
    }

    public static void main(String[] args) {
        check(dfs("()())()"), List.of("(())()", "()()()")); check(dfs("(a)())()"), List.of("(a())()", "(a)()()"));
        check(dfs(")("), List.of("")); check(twoPass("()())()"), List.of("(())()", "()()()"));
        Random r = new Random(20);
        String alphabet = "()a";
        for (int t = 0; t < 1500; t++) {
            StringBuilder sb = new StringBuilder();
            for (int i = r.nextInt(11); i > 0; i--) sb.append(alphabet.charAt(r.nextInt(3)));
            String s = sb.toString();
            List<String> e = brute(s);
            check(bfs(s), e); check(dfs(s), e); check(twoPass(s), e);   // lists equal => no duplicates either
        }
        System.out.println("RemoveInvalidParentheses: all tests passed");
    }

    static void check(Object got, Object exp) {
        if (!got.equals(exp)) throw new AssertionError("expected " + exp + " but got " + got);
    }
}
