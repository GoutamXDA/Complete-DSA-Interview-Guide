# 18 — Bit Manipulation

## Notes
| Operation | Code |
|---|---|
| Check i-th bit | `(n >> i) & 1` |
| Set i-th bit | `n \| (1 << i)` |
| Clear i-th bit | `n & ~(1 << i)` |
| Toggle i-th bit | `n ^ (1 << i)` |
| Clear lowest set bit | `n & (n - 1)` |
| Isolate lowest set bit | `n & -n` |
| Power of two? | `n > 0 && (n & (n - 1)) == 0` |
| Count set bits | `Integer.bitCount(n)` or Kernighan loop |
| Multiply / divide by 2^k | `n << k`, `n >> k` (arithmetic), `n >>> k` (logical, fills 0) |

**XOR facts:** `a ^ a = 0`, `a ^ 0 = a`, commutative & associative → pairs cancel out.
**Two's complement:** `-n = ~n + 1`. Java `int` is 32-bit signed; `Integer.MIN_VALUE = -2^31`.

## Problems

| # | Problem | Level | Approach | Solution |
|---|---|---|---|---|
| 1 | Get / set / clear / toggle bit | Easy | masks | [BitBasics](BitBasics.java) |
| 2 | Power of two (LC 231) | Easy | `n & (n-1)` | [BitBasics](BitBasics.java) |
| 3 | Number of 1 bits (LC 191) | Easy | Kernighan | [BitBasics](BitBasics.java) |
| 4 | Counting bits 0..n (LC 338) | Easy | `dp[i] = dp[i >> 1] + (i & 1)` | [BitBasics](BitBasics.java) |
| 5 | Swap two numbers without temp | Easy | XOR swap | [BitBasics](BitBasics.java) |
| 6 | XOR of 1..n | Easy | n % 4 pattern | [BitBasics](BitBasics.java) |
| 7 | Minimum bit flips (LC 2220) | Easy | popcount(a ^ b) | [BitBasics](BitBasics.java) |
| 8 | Reverse bits (LC 190) | Easy | shift in/out | [BitBasics](BitBasics.java) |
| 9 | Single number I (LC 136) | Easy | XOR all | [SingleNumber](SingleNumber.java) |
| 10 | Single number II (LC 137) | Medium | bit counts mod 3 / ones-twos | [SingleNumber](SingleNumber.java) |
| 11 | Single number III (LC 260) | Medium | split by lowest differing bit | [SingleNumber](SingleNumber.java) |
| 12 | Divide two integers (LC 29) | Medium | subtract shifted divisor | [SingleNumber](SingleNumber.java) |
| 13 | Sum of two integers without + (LC 371) | Medium | XOR + carry | [SingleNumber](SingleNumber.java) |
| 14 | Subsets via bitmask (LC 78) | Medium | iterate masks | see 08 |
| 15 | Maximum XOR (LC 421) | Medium | binary trie | see 17 |
| 16 | Gray code (LC 89) | Medium | `i ^ (i >> 1)` | _(todo)_ |
