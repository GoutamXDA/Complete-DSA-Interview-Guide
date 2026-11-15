import java.util.*;

/**
 * LC 432 — All O`one Data Structure. Explanation: AllOOneDataStructure.md
 * 1. ScanAllOne    : HashMap + O(n) scan for max/min
 * 2. TreeAllOne    : HashMap + TreeMap<count, keys>, O(log n)
 * 3. AllOne        : doubly linked list of count buckets, O(1)
 * Verified by checking that every max/min answer has the true max/min count.
 */
public class AllOOneDataStructure {

    interface Api { void inc(String k); void dec(String k); String getMaxKey(); String getMinKey(); }

    static class ScanAllOne implements Api {
        final Map<String, Integer> c = new HashMap<>();
        public void inc(String k) { c.merge(k, 1, Integer::sum); }
        public void dec(String k) { if (c.merge(k, -1, Integer::sum) == 0) c.remove(k); }
        public String getMaxKey() { return c.entrySet().stream().max(Map.Entry.comparingByValue()).map(Map.Entry::getKey).orElse(""); }
        public String getMinKey() { return c.entrySet().stream().min(Map.Entry.comparingByValue()).map(Map.Entry::getKey).orElse(""); }
    }

    static class TreeAllOne implements Api {
        final Map<String, Integer> c = new HashMap<>();
        final TreeMap<Integer, Set<String>> byCount = new TreeMap<>();
        void move(String k, int from, int to) {
            if (from > 0) { Set<String> s = byCount.get(from); s.remove(k); if (s.isEmpty()) byCount.remove(from); }
            if (to > 0) { byCount.computeIfAbsent(to, x -> new HashSet<>()).add(k); c.put(k, to); } else c.remove(k);
        }
        public void inc(String k) { int f = c.getOrDefault(k, 0); move(k, f, f + 1); }
        public void dec(String k) { int f = c.get(k); move(k, f, f - 1); }
        public String getMaxKey() { return byCount.isEmpty() ? "" : byCount.lastEntry().getValue().iterator().next(); }
        public String getMinKey() { return byCount.isEmpty() ? "" : byCount.firstEntry().getValue().iterator().next(); }
    }

    static class Bucket {
        final int count; final Set<String> keys = new HashSet<>(); Bucket prev, next;
        Bucket(int c) { count = c; }
    }

    static class AllOne implements Api {
        final Bucket head = new Bucket(0), tail = new Bucket(Integer.MAX_VALUE);
        final Map<String, Bucket> where = new HashMap<>();
        AllOne() { head.next = tail; tail.prev = head; }

        Bucket insertAfter(Bucket b, int count) {
            Bucket n = new Bucket(count);
            n.prev = b; n.next = b.next; b.next.prev = n; b.next = n;
            return n;
        }

        void removeIfEmpty(Bucket b) {
            if (b == head || b == tail || !b.keys.isEmpty()) return;
            b.prev.next = b.next; b.next.prev = b.prev;
        }

        public void inc(String k) {
            Bucket cur = where.getOrDefault(k, head);
            Bucket dst = cur.next.count == cur.count + 1 ? cur.next : insertAfter(cur, cur.count + 1);
            dst.keys.add(k); where.put(k, dst);
            if (cur != head) { cur.keys.remove(k); removeIfEmpty(cur); }
        }

        public void dec(String k) {
            Bucket cur = where.get(k);
            if (cur.count == 1) where.remove(k);
            else {
                Bucket dst = cur.prev.count == cur.count - 1 ? cur.prev : insertAfter(cur.prev, cur.count - 1);
                dst.keys.add(k); where.put(k, dst);
            }
            cur.keys.remove(k); removeIfEmpty(cur);
        }

        public String getMaxKey() { return tail.prev == head ? "" : tail.prev.keys.iterator().next(); }
        public String getMinKey() { return head.next == tail ? "" : head.next.keys.iterator().next(); }
    }

    public static void main(String[] args) {
        AllOne a = new AllOne();
        a.inc("hello"); a.inc("hello");
        check(a.getMaxKey(), "hello"); check(a.getMinKey(), "hello");
        a.inc("leet");
        check(a.getMaxKey(), "hello"); check(a.getMinKey(), "leet");
        Random r = new Random(26);
        for (int t = 0; t < 300; t++) {
            Api[] impls = {new ScanAllOne(), new TreeAllOne(), new AllOne()};
            Map<String, Integer> truth = new HashMap<>();
            for (int op = 0; op < 200; op++) {
                String k = "k" + r.nextInt(5);
                if (r.nextInt(3) > 0 || !truth.containsKey(k)) { truth.merge(k, 1, Integer::sum); for (Api x : impls) x.inc(k); }
                else { if (truth.merge(k, -1, Integer::sum) == 0) truth.remove(k); for (Api x : impls) x.dec(k); }
                int max = truth.values().stream().max(Integer::compare).orElse(0), min = truth.values().stream().min(Integer::compare).orElse(0);
                for (Api x : impls) {
                    String mx = x.getMaxKey(), mn = x.getMinKey();
                    check(mx.isEmpty() ? 0 : truth.get(mx), max); check(mn.isEmpty() ? 0 : truth.get(mn), min);
                }
            }
        }
        System.out.println("AllOOneDataStructure: all tests passed");
    }

    static void check(Object got, Object exp) {
        if (!got.equals(exp)) throw new AssertionError("expected " + exp + " but got " + got);
    }
}
