import java.util.*;

/**
 * LC 772 (also covers LC 224 unary minus and LC 227). Explanation: BasicCalculatorIII.md
 * 1. Recursion on parentheses + sum/term scanning     O(n)
 * 2. Two stacks (shunting-yard evaluation)             O(n)
 * 3. Recursive descent grammar                         O(n)
 * Cross-checked on random expressions against each other and hand-computed cases.
 */
public class BasicCalculatorIII {

    // ---------- 1. Recursion on parentheses ----------
    static int pos;

    static long calcRecursive(String s) { pos = 0; return scan(s.replace(" ", "")); }

    /** Evaluates from `pos` until ')' or end. A leading '-' works as 0 - x, so unary minus at the start or after '(' is free. */
    static long scan(String s) {
        long sum = 0, term = 0, num = 0;
        char op = '+';
        while (true) {
            char c = pos < s.length() ? s.charAt(pos) : '#';          // '#' = end of input
            pos++;
            if (Character.isDigit(c)) { num = num * 10 + (c - '0'); continue; }
            if (c == '(') { num = scan(s); continue; }                 // the bracket's value acts as one number
            switch (op) {                                              // apply the pending operator to num
                case '+' -> { sum += term; term = num; }
                case '-' -> { sum += term; term = -num; }
                case '*' -> term *= num;
                case '/' -> term /= num;
            }
            if (c == ')' || c == '#') return sum + term;
            op = c; num = 0;
        }
    }

    // ---------- 2. Two stacks ----------
    static int prec(char op) { return op == '+' || op == '-' ? 1 : op == '*' || op == '/' ? 2 : 0; }

    static void apply(Deque<Long> nums, char op) {
        long b = nums.pop(), a = nums.pop();
        nums.push(switch (op) { case '+' -> a + b; case '-' -> a - b; case '*' -> a * b; default -> a / b; });
    }

    static long calcStacks(String str) {
        String s = str.replace(" ", "");
        Deque<Long> nums = new ArrayDeque<>();
        Deque<Character> ops = new ArrayDeque<>();
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (Character.isDigit(c)) {
                long v = 0;
                while (i < s.length() && Character.isDigit(s.charAt(i))) v = v * 10 + (s.charAt(i++) - '0');
                i--;
                nums.push(v);
            } else if (c == '(') ops.push(c);
            else if (c == ')') { while (ops.peek() != '(') apply(nums, ops.pop()); ops.pop(); }
            else {
                if (c == '-' && (i == 0 || s.charAt(i - 1) == '(')) nums.push(0L);   // unary minus -> 0 - x
                while (!ops.isEmpty() && prec(ops.peek()) >= prec(c)) apply(nums, ops.pop());   // '(' has precedence 0, so it stops here
                ops.push(c);
            }
        }
        while (!ops.isEmpty()) apply(nums, ops.pop());
        return nums.pop();
    }

    // ---------- 3. Recursive descent ----------
    static String src;
    static int at;

    static long calculate(String s) { src = s.replace(" ", ""); at = 0; return expr(); }

    static long expr() {
        long v = term();
        while (at < src.length() && (src.charAt(at) == '+' || src.charAt(at) == '-')) {
            char op = src.charAt(at++);
            long t = term();
            v = op == '+' ? v + t : v - t;
        }
        return v;
    }

    static long term() {
        long v = factor();
        while (at < src.length() && (src.charAt(at) == '*' || src.charAt(at) == '/')) {
            char op = src.charAt(at++);
            long f = factor();
            v = op == '*' ? v * f : v / f;
        }
        return v;
    }

    static long factor() {
        char c = src.charAt(at);
        if (c == '-') { at++; return -factor(); }                      // unary minus
        if (c == '(') { at++; long v = expr(); at++; return v; }        // skip ')'
        long v = 0;
        while (at < src.length() && Character.isDigit(src.charAt(at))) v = v * 10 + (src.charAt(at++) - '0');
        return v;
    }

    // ---------- random expression generator (no unary minus, no division by zero) ----------
    static String gen(Random r, int depth) {
        if (depth == 0 || r.nextInt(3) == 0) return String.valueOf(r.nextInt(10) + 1);
        String a = gen(r, depth - 1), b = gen(r, depth - 1);
        String op = String.valueOf("+-*/".charAt(r.nextInt(4)));
        String e = a + op + b;
        return r.nextBoolean() ? "(" + e + ")" : e;
    }

    public static void main(String[] args) {
        String[] cases = {"1+1", "6-4/2", "2*(5+5*2)/3+(6/2+8)", "(2+6*3+5-(3*14/7+2)*5)+3", " 3+5 / 2 ", "(1+(4+5+2)-3)+(6+8)"};
        long[] exp = {2, 4, 21, -12, 5, 23};
        for (int i = 0; i < cases.length; i++) {
            check(calculate(cases[i]), exp[i]); check(calcStacks(cases[i]), exp[i]); check(calcRecursive(cases[i]), exp[i]);
        }
        check(calculate("- (3 + (4 + 5))"), -12L); check(calcStacks("- (3 + (4 + 5))"), -12L); check(calcRecursive("- (3 + (4 + 5))"), -12L);
        check(calculate("1-(-2)"), 3L); check(calcStacks("1-(-2)"), 3L); check(calcRecursive("1-(-2)"), 3L);
        Random r = new Random(30);
        int checked = 0;
        for (int t = 0; t < 3000; t++) {
            String e = gen(r, 4);
            long v;
            try { v = calculate(e); } catch (ArithmeticException ex) { continue; }   // a sub-expression divided by zero
            check(calcStacks(e), v); check(calcRecursive(e), v);
            checked++;
        }
        check(checked > 2000, true);
        System.out.println("BasicCalculatorIII: all tests passed");
    }

    static void check(Object got, Object exp) {
        if (!got.equals(exp)) throw new AssertionError("expected " + exp + " but got " + got);
    }
}
