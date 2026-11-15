#!/usr/bin/env bash
# Compiles and runs every Java solution in the repo. Each file is self-contained.
set -u
cd "$(dirname "$0")"
pass=0; fail=0; failed=()
for f in $(find . -name '*.java' | sort); do
  out=$(mktemp -d)
  if javac -d "$out" "$f" 2>/tmp/javac.err; then
    cls=$(basename "$f" .java)
    if timeout 20 java -cp "$out" "$cls" > /tmp/run.out 2>&1; then
      pass=$((pass+1))
    else
      fail=$((fail+1)); failed+=("$f (runtime)"); cat /tmp/run.out
    fi
  else
    fail=$((fail+1)); failed+=("$f (compile)"); cat /tmp/javac.err
  fi
  rm -rf "$out"
done
echo "Passed: $pass  Failed: $fail"
for x in "${failed[@]:-}"; do [ -n "$x" ] && echo "  FAILED: $x"; done
[ "$fail" -eq 0 ]
