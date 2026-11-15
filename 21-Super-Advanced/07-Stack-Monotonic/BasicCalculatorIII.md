# LC 772 / 224 / 227 — Basic Calculator III (Hard)

**Pattern:** expression parsing — recursive descent, operator-precedence stacks, or recursion on parentheses · **Target:** O(n) · **Code:** [BasicCalculatorIII.java](BasicCalculatorIII.java)

## Problem
Evaluate a string with non-negative integers, `+ - * /`, parentheses and spaces. Division truncates toward zero. (LC 224 adds **unary minus**: `-(2+3)`, `1-(-2)`; LC 227 has no parentheses.)

`"2*(5+5*2)/3+(6/2+8)" → 21` · `"(2+6*3+5-(3*14/7+2)*5)+3" → -12` · `"- (3 + (4 + 5))" → -12`

## Why engineers fear it
Precedence, associativity, parentheses and unary minus all at once — with no `eval`. It's a mini-compiler under time pressure.

## Approach 1 — Recursion on parentheses + LC 227 logic
Scan left to right keeping `sum` (finished terms) and `term` (current product chain) and the pending operator. On `(`, **recursively** evaluate up to the matching `)` and treat the result as a number. On `+`/`−` commit the term; on `*`/`/` fold into the term. **O(n)** with a shared index.

## Approach 2 — Two stacks (Dijkstra's shunting-yard evaluation)
`nums` and `ops` stacks. On an operator, first apply every operator on the stack with **≥ precedence** (left-associative), then push. `(` is pushed; `)` applies until `(`. Unary minus: when `-` appears at the start or right after `(` (or another operator), push a `0` first. **O(n)**. The general-purpose technique (works for any precedence table).

## Approach 3 — Recursive descent grammar (cleanest to extend)
```
expr   := term (('+' | '-') term)*
term   := factor (('*' | '/') factor)*
factor := number | '(' expr ')' | '-' factor        # unary minus
```
Each rule is one small function sharing a position index. Precedence falls out of the grammar's structure. **O(n)**. This is what interviewers love when they say "now add exponentiation / functions".

## Why truncation matters
Java's `/` already truncates toward zero for ints, matching the spec. Python's `//` does not — a common bug when translating.

## Pitfalls
- Multi-digit numbers: accumulate `num = num * 10 + d`.
- Spaces anywhere, including between unary `-` and `(`.
- Unary minus vs binary minus — decide by the **previous token**.
- Use `long` for intermediate products.

## Follow-ups
- **Basic calculator IV** (LC 770) — polynomial simplification with variables (very hard).
- **Evaluate reverse Polish notation** (LC 150) — the output format of shunting-yard.
- **Parse boolean expression** (LC 1106), **decode string** (LC 394) — same recursive-descent shape.

## 30-second interview script
"I'd write a recursive-descent parser: expression is terms joined by + and −, a term is factors joined by * and /, and a factor is a number, a parenthesised expression, or a unary minus applied to a factor. Precedence comes directly from the grammar, everything is one left-to-right pass with a shared index, so it's O(n). The two-stack shunting-yard method is the iterative alternative."
