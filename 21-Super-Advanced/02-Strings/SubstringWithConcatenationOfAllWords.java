import java.util.*;

/**
 * LC 30 — Substring with Concatenation of All Words. Explanation: SubstringWithConcatenationOfAllWords.md
 * 1. Check every start index             O(n * k * w)
 * 2. Sliding window over word slots      O(n * w), one window per offset in [0, w)
 */
public class SubstringWithConcatenationOfAllWords {

    static List<Integer> brute(String s, String[] words) {
        List<Integer> res = new ArrayList<>();
        if (words.length == 0) return res;
        int w = words[0].length(), k = words.length, total = w * k;
        Map<String, Integer> need = new HashMap<>();
        for (String x : words) need.merge(x, 1, Integer::sum);
        for (int i = 0; i + total <= s.length(); i++) {
            Map<String, Integer> seen = new HashMap<>();
            int j = 0;
            for (; j < k; j++) {
                String word = s.substring(i + j * w, i + (j + 1) * w);
                if (seen.merge(word, 1, Integer::sum) > need.getOrDefault(word, 0)) break;
            }
            if (j == k) res.add(i);
        }
        return res;
    }

    static List<Integer> findSubstring(String s, String[] words) {
        List<Integer> res = new ArrayList<>();
        if (words.length == 0) return res;
        int w = words[0].length(), k = words.length, n = s.length();
        Map<String, Integer> need = new HashMap<>();
        for (String x : words) need.merge(x, 1, Integer::sum);
        for (int off = 0; off < w; off++) {
            Map<String, Integer> seen = new HashMap<>();
            int l = off, count = 0;
            for (int r = off; r + w <= n; r += w) {
                String word = s.substring(r, r + w);
                if (!need.containsKey(word)) { seen.clear(); count = 0; l = r + w; continue; }
                seen.merge(word, 1, Integer::sum);
                count++;
                while (seen.get(word) > need.get(word)) {          // over-used: shrink from the left
                    seen.merge(s.substring(l, l + w), -1, Integer::sum);
                    count--; l += w;
                }
                if (count == k) {
                    res.add(l);
                    seen.merge(s.substring(l, l + w), -1, Integer::sum);   // slide by one word
                    count--; l += w;
                }
            }
        }
        Collections.sort(res);
        return res;
    }

    public static void main(String[] args) {
        check(findSubstring("barfoothefoobarman", new String[]{"foo", "bar"}), List.of(0, 9));
        check(findSubstring("wordgoodgoodgoodbestword", new String[]{"word", "good", "best", "word"}), List.of());
        check(findSubstring("barfoofoobarthefoobarman", new String[]{"bar", "foo", "the"}), List.of(6, 9, 12));
        check(findSubstring("aaaaaaaa", new String[]{"aa", "aa"}), List.of(0, 1, 2, 3, 4));
        Random r = new Random(7);
        for (int t = 0; t < 2000; t++) {
            int w = r.nextInt(2) + 1, k = r.nextInt(3) + 1;
            String[] words = new String[k];
            for (int i = 0; i < k; i++) words[i] = rand(r, w);
            String s = rand(r, r.nextInt(14));
            check(findSubstring(s, words), brute(s, words));
        }
        System.out.println("SubstringWithConcatenationOfAllWords: all tests passed");
    }

    static String rand(Random r, int n) { StringBuilder sb = new StringBuilder(); for (int i = 0; i < n; i++) sb.append((char) ('a' + r.nextInt(2))); return sb.toString(); }

    static void check(Object got, Object exp) {
        if (!got.equals(exp)) throw new AssertionError("expected " + exp + " but got " + got);
    }
}
