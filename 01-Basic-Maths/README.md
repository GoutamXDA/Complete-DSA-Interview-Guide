# 01 — Basic Maths

Maths shows up as building blocks: digit extraction, GCD, primes, modular arithmetic, and fast exponentiation.

## Key notes

### Digit extraction
```
while (n > 0) { int d = n % 10; n /= 10; }   // O(log10 n) = number of digits
count of digits = (int) Math.log10(n) + 1      // for n > 0
```

### GCD (Euclid)
`gcd(a, b) = gcd(b, a % b)`, `gcd(a, 0) = a`. Time **O(log min(a,b))**.
`lcm(a, b) = a / gcd(a, b) * b` (divide first to avoid overflow).

### Primes
- Check prime: test divisors up to `√n` → **O(√n)**.
- All primes ≤ n: **Sieve of Eratosthenes** → O(n log log n). Start crossing from `i*i`.
- Prime factorisation: trial divide up to √n → O(√n).
- All divisors: pair `i` with `n/i` for `i ≤ √n` → O(√n).

### Modular arithmetic (MOD = 1_000_000_007)
- `(a + b) % M`, `(a * b) % M` — cast to `long` before multiplying.
- `(a - b) % M` can be negative in Java → use `((a - b) % M + M) % M`.
- Division: multiply by modular inverse `b^(M-2) % M` (Fermat, M prime).

### Fast power (binary exponentiation)
`x^n`: square the base, halve n; multiply into result when the current bit is 1 → **O(log n)**.

## Problems

| # | Problem | Level | Approach | Solution |
|---|---|---|---|---|
| 1 | Count digits | Easy | loop %10 or log10 | [DigitProblems](DigitProblems.java) |
| 2 | Reverse integer (LC 7) | Easy | build reversed, check overflow before multiply | [DigitProblems](DigitProblems.java) |
| 3 | Palindrome number (LC 9) | Easy | reverse half the number | [DigitProblems](DigitProblems.java) |
| 4 | Armstrong number | Easy | sum of digit^k | [DigitProblems](DigitProblems.java) |
| 5 | GCD / LCM | Easy | brute → Euclid | [GcdLcm](GcdLcm.java) |
| 6 | Check prime | Easy | √n trial division | [Primes](Primes.java) |
| 7 | Count primes (LC 204) | Medium | sieve | [Primes](Primes.java) |
| 8 | Prime factorisation | Medium | trial division / SPF sieve | [Primes](Primes.java) |
| 9 | All divisors | Easy | pair i and n/i | [Primes](Primes.java) |
| 10 | Pow(x, n) (LC 50) | Medium | binary exponentiation, handle n = MIN_VALUE | [FastPower](FastPower.java) |
| 11 | Modular exponentiation / inverse | Medium | fast pow with mod, Fermat | [FastPower](FastPower.java) |
| 12 | Factorial trailing zeroes (LC 172) | Medium | count factors of 5: n/5 + n/25 + … | [DigitProblems](DigitProblems.java) |
| 13 | Sqrt(x) (LC 69) | Easy | binary search / Newton | see 06-Binary-Search |
| 14 | Excel column number (LC 171) | Easy | base-26 | _(todo)_ |
| 15 | Happy number (LC 202) | Easy | Floyd cycle on digit-square sum | _(todo)_ |
| 16 | nCr % p | Hard | factorial + inverse factorial arrays | _(todo)_ |
