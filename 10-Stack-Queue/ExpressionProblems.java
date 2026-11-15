import java.util.*;

/**
 * LC 20 Valid parentheses: brute = repeatedly remove "()", "[]", "{}" O(n^2) | stack O(n)
 * LC 150 Evaluate RPN: operand stack.
 * Infix -> postfix (Shunting-yard): pop operators with higher/equal precedence (left-assoc) before pushing.
 * LC 227 Calculator II (+-* /, no parens): push +num/-num; for * / combine with top. Sum the stack. O(n)
 *   O(1) space variant: keep `last` term instead of a stack.
 * LC 224 Calculator (+ - and parentheses): running result + sign; push (result, sign) at '('.
 * LC 394 Decode string "3[a2[c]]": stacks for counts and partial strings.
 */
public class ExpressionProblems {

    static boolean isValidBrute(String s) {
        String prev;
        do { prev = s; s = s.replace("()", "").replace("[]", "").replace("{}", ""); } while (!s.equals(prev));
        return s.isEmpty();
    }

    static boolean isValid(String s) {
        Deque<Character> st = new ArrayDeque<>();
        for (char c : s.toCharArray()) {
            if (c == '(') st.push(')');
            else if (c == '[') st.push(']');
            else if (c == '{') st.push('}');
            else if (st.isEmpty() || st.pop() != c) return false;
        }
        return st.isEmpty();
    }

    static int evalRPN(String[] tokens) {
        Deque<Integer> st = new ArrayDeque<>();
        for (String t : tokens) {
            switch (t) {
                case "+" -> st.push(st.pop() + st.pop());
                case "-" -> { int b = st.pop(), a = st.pop(); st.push(a - b); }
                case "*" -> st.push(st.pop() * st.pop());
                case "/" -> { int b = st.pop(), a = st.pop(); st.push(a / b); }
                default -> st.push(Integer.parseInt(t));
            }
        }
        return st.pop();
    }

    static int prec(char c) { return c == '^' ? 3 : (c == '*' || c == '/') ? 2 : (c == '+' || c == '-') ? 1 : -1; }

    static String infixToPostfix(String s) {
        StringBuilder out = new StringBuilder();
        Deque<Character> ops = new ArrayDeque<>();
        for (char c : s.toCharArray()) {
            if (Character.isLetterOrDigit(c)) out.append(c);
            else if (c == '(') ops.push(c);
            else if (c == ')') { while (ops.peek() != '(') out.append(ops.pop()); ops.pop(); }
            else {
                while (!ops.isEmpty() && (prec(ops.peek()) > prec(c) || (prec(ops.peek()) == prec(c) && c != '^')))
                    out.append(ops.pop());
                ops.push(c);
            }
        }
        while (!ops.isEmpty()) out.append(ops.pop());
        return out.toString();
    }

    static int calculateII(String s) {
        int result = 0, last = 0, num = 0; char op = '+';
        for (int i = 0; i <= s.length(); i++) {
            char c = i < s.length() ? s.charAt(i) : '+';
            if (Character.isDigit(c)) { num = num * 10 + (c - '0'); continue; }
            if (c == ' ') continue;
            switch (op) {
                case '+' -> { result += last; last = num; }
                case '-' -> { result += last; last = -num; }
                case '*' -> last *= num;
                case '/' -> last /= num;
            }
            op = c; num = 0;
        }
        return result + last;
    }

    static int calculate(String s) {
        Deque<Integer> st = new ArrayDeque<>();
        int result = 0, num = 0, sign = 1;
        for (char c : s.toCharArray()) {
            if (Character.isDigit(c)) num = num * 10 + (c - '0');
            else if (c == '+' || c == '-') { result += sign * num; num = 0; sign = c == '+' ? 1 : -1; }
            else if (c == '(') { st.push(result); st.push(sign); result = 0; sign = 1; }
            else if (c == ')') { result += sign * num; num = 0; result *= st.pop(); result += st.pop(); }
        }
        return result + sign * num;
    }

    static String decodeString(String s) {
        Deque<Integer> counts = new ArrayDeque<>();
        Deque<StringBuilder> parts = new ArrayDeque<>();
        StringBuilder cur = new StringBuilder(); int k = 0;
        for (char c : s.toCharArray()) {
            if (Character.isDigit(c)) k = k * 10 + (c - '0');
            else if (c == '[') { counts.push(k); parts.push(cur); cur = new StringBuilder(); k = 0; }
            else if (c == ']') { StringBuilder prev = parts.pop(); prev.append(cur.toString().repeat(counts.pop())); cur = prev; }
            else cur.append(c);
        }
        return cur.toString();
    }

    public static void main(String[] args) {
        for (String t : new String[]{"()[]{}", "([)]", "{[]}", "(", "]"}) check(isValid(t), isValidBrute(t));
        check(isValid("{[]}"), true); check(isValid("([)]"), false);
        check(evalRPN(new String[]{"10", "6", "9", "3", "+", "-11", "*", "/", "*", "17", "+", "5", "+"}), 22);
        check(infixToPostfix("a+b*(c^d-e)^(f+g*h)-i"), "abcd^e-fgh*+^*+i-");
        check(calculateII("3+2*2"), 7); check(calculateII(" 3/2 "), 1); check(calculateII(" 3+5 / 2 "), 5);
        check(calculate("(1+(4+5+2)-3)+(6+8)"), 23); check(calculate("- (3 + (4 + 5))"), -12);
        check(decodeString("3[a2[c]]"), "accaccacc"); check(decodeString("2[abc]3[cd]ef"), "abcabccdcdcdef");
        System.out.println("ExpressionProblems: all tests passed");
    }

    static void check(Object got, Object exp) {
        if (!got.equals(exp)) throw new AssertionError("expected " + exp + " but got " + got);
    }
}
