import java.util.*;

/**
 * LC 282 — Expression Add Operators. Explanation: ExpressionAddOperators.md
 * 1. Generate every operator placement, then evaluate with precedence    O(4^n * n)
 * 2. Backtracking with running value + last operand                     O(4^n * n)
 */
public class ExpressionAddOperators {

    static List<String> brute(String num, int target) {
        List<String> res = new ArrayList<>();
        int gaps = num.length() - 1;
        int total = 1;
        for (int i = 0; i < gaps; i++) total *= 4;
        String ops = " +-*";                                  // ' ' = join digits
        for (int code = 0; code < total; code++) {
            StringBuilder sb = new StringBuilder().append(num.charAt(0));
            int c = code;
            for (int i = 0; i < gaps; i++) { char op = ops.charAt(c % 4); c /= 4; if (op != ' ') sb.append(op); sb.append(num.charAt(i + 1)); }
            String e = sb.toString();
            if (noLeadingZeros(e) && eval(e) == target) res.add(e);
        }
        Collections.sort(res);
        return res;
    }

    static boolean noLeadingZeros(String e) {
        for (String t : e.split("[+\\-*]")) if (t.length() > 1 && t.charAt(0) == '0') return false;
        return true;
    }

    static long eval(String e) {                              // + - * with precedence
        long sum = 0, term = 0, num = 0; char op = '+';
        for (int i = 0; i <= e.length(); i++) {
            char ch = i < e.length() ? e.charAt(i) : '+';
            if (Character.isDigit(ch)) { num = num * 10 + (ch - '0'); continue; }
            if (op == '*') term *= num;
            else { sum += term; term = op == '+' ? num : -num; }
            op = ch; num = 0;
        }
        return sum + term;
    }

    static List<String> addOperators(String num, int target) {
        List<String> res = new ArrayList<>();
        go(num, target, 0, 0, 0, new StringBuilder(), res);
        Collections.sort(res);
        return res;
    }

    static void go(String num, long target, int pos, long value, long last, StringBuilder sb, List<String> res) {
        if (pos == num.length()) { if (value == target) res.add(sb.toString()); return; }
        int len = sb.length();
        long cur = 0;
        for (int i = pos; i < num.length(); i++) {
            if (i > pos && num.charAt(pos) == '0') break;    // no leading zeros
            cur = cur * 10 + (num.charAt(i) - '0');
            if (pos == 0) {
                sb.append(cur); go(num, target, i + 1, cur, cur, sb, res); sb.setLength(len);
            } else {
                sb.append('+').append(cur); go(num, target, i + 1, value + cur, cur, sb, res); sb.setLength(len);
                sb.append('-').append(cur); go(num, target, i + 1, value - cur, -cur, sb, res); sb.setLength(len);
                sb.append('*').append(cur); go(num, target, i + 1, value - last + last * cur, last * cur, sb, res); sb.setLength(len);
            }
        }
    }

    public static void main(String[] args) {
        check(addOperators("123", 6), List.of("1*2*3", "1+2+3")); check(addOperators("232", 8), List.of("2*3+2", "2+3*2"));
        check(addOperators("105", 5), List.of("1*0+5", "10-5")); check(addOperators("00", 0), List.of("0*0", "0+0", "0-0"));
        check(addOperators("3456237490", 9191), List.of());
        Random r = new Random(21);
        for (int t = 0; t < 400; t++) {
            StringBuilder sb = new StringBuilder();
            for (int i = r.nextInt(6) + 1; i > 0; i--) sb.append(r.nextInt(4));
            String num = sb.toString();
            int target = r.nextInt(21) - 5;
            check(addOperators(num, target), brute(num, target));
        }
        System.out.println("ExpressionAddOperators: all tests passed");
    }

    static void check(Object got, Object exp) {
        if (!got.equals(exp)) throw new AssertionError("expected " + exp + " but got " + got);
    }
}
