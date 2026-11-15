import java.util.*;

/**
 * LC 460 — LFU Cache. Explanation: LFUCache.md
 * 1. ScanLFU        : map + O(capacity) scan on eviction
 * 2. TreeSetLFU     : (freq, time) ordering, O(log c)
 * 3. LFUCache       : frequency buckets as explicit doubly linked lists, O(1)
 * 4. LinkedSetLFU   : frequency buckets as LinkedHashSets, O(1)
 * All four are run against the same random operation stream.
 */
public class LFUCache {

    interface Cache { int get(int k); void put(int k, int v); }

    // ---------- 1. Scan ----------
    static class ScanLFU implements Cache {
        final int cap; int time = 0;
        final Map<Integer, int[]> m = new HashMap<>();   // {value, freq, lastUsed}
        ScanLFU(int c) { cap = c; }
        public int get(int k) { int[] e = m.get(k); if (e == null) return -1; e[1]++; e[2] = time++; return e[0]; }
        public void put(int k, int v) {
            if (cap == 0) return;
            int[] e = m.get(k);
            if (e != null) { e[0] = v; e[1]++; e[2] = time++; return; }
            if (m.size() == cap) {
                int victim = -1; int[] ve = null;
                for (var en : m.entrySet()) {
                    int[] x = en.getValue();
                    if (ve == null || x[1] < ve[1] || (x[1] == ve[1] && x[2] < ve[2])) { ve = x; victim = en.getKey(); }
                }
                m.remove(victim);
            }
            m.put(k, new int[]{v, 1, time++});
        }
    }

    // ---------- 2. TreeSet ----------
    static class TreeSetLFU implements Cache {
        final int cap; int time = 0;
        final Map<Integer, int[]> m = new HashMap<>();   // {value, freq, time}
        final TreeSet<int[]> order = new TreeSet<>((a, b) -> a[0] != b[0] ? a[0] - b[0] : a[1] - b[1]);   // {freq, time, key}
        final Map<Integer, int[]> pos = new HashMap<>();
        TreeSetLFU(int c) { cap = c; }
        void use(int k) {
            int[] e = m.get(k);
            order.remove(pos.get(k));
            e[1]++; e[2] = time++;
            int[] p = {e[1], e[2], k}; order.add(p); pos.put(k, p);
        }
        public int get(int k) { if (!m.containsKey(k)) return -1; use(k); return m.get(k)[0]; }
        public void put(int k, int v) {
            if (cap == 0) return;
            if (m.containsKey(k)) { m.get(k)[0] = v; use(k); return; }
            if (m.size() == cap) { int[] victim = order.pollFirst(); m.remove(victim[2]); pos.remove(victim[2]); }
            m.put(k, new int[]{v, 1, time});
            int[] p = {1, time++, k}; order.add(p); pos.put(k, p);
        }
    }

    // ---------- 3. Explicit DLL buckets ----------
    static class Node { int key, val, freq = 1; Node prev, next; Node(int k, int v) { key = k; val = v; } }

    static class DList {
        final Node head = new Node(0, 0), tail = new Node(0, 0); int size = 0;
        DList() { head.next = tail; tail.prev = head; }
        void addFirst(Node n) { n.next = head.next; n.prev = head; head.next.prev = n; head.next = n; size++; }
        void remove(Node n) { n.prev.next = n.next; n.next.prev = n.prev; size--; }
        Node removeLast() { Node n = tail.prev; remove(n); return n; }
    }

    static class O1LFU implements Cache {
        final int cap; int minFreq = 0;
        final Map<Integer, Node> nodes = new HashMap<>();
        final Map<Integer, DList> buckets = new HashMap<>();
        O1LFU(int c) { cap = c; }
        void touch(Node n) {
            DList b = buckets.get(n.freq);
            b.remove(n);
            if (b.size == 0 && n.freq == minFreq) minFreq++;
            n.freq++;
            buckets.computeIfAbsent(n.freq, f -> new DList()).addFirst(n);
        }
        public int get(int k) { Node n = nodes.get(k); if (n == null) return -1; touch(n); return n.val; }
        public void put(int k, int v) {
            if (cap == 0) return;
            Node n = nodes.get(k);
            if (n != null) { n.val = v; touch(n); return; }
            if (nodes.size() == cap) nodes.remove(buckets.get(minFreq).removeLast().key);
            n = new Node(k, v);
            nodes.put(k, n);
            buckets.computeIfAbsent(1, f -> new DList()).addFirst(n);
            minFreq = 1;
        }
    }

    // ---------- 4. LinkedHashSet buckets ----------
    static class LinkedSetLFU implements Cache {
        final int cap; int minFreq = 0;
        final Map<Integer, int[]> m = new HashMap<>();                  // {value, freq}
        final Map<Integer, LinkedHashSet<Integer>> buckets = new HashMap<>();
        LinkedSetLFU(int c) { cap = c; }
        void touch(int k) {
            int f = m.get(k)[1];
            LinkedHashSet<Integer> b = buckets.get(f);
            b.remove(k);
            if (b.isEmpty() && f == minFreq) minFreq++;
            m.get(k)[1] = f + 1;
            buckets.computeIfAbsent(f + 1, x -> new LinkedHashSet<>()).add(k);
        }
        public int get(int k) { if (!m.containsKey(k)) return -1; touch(k); return m.get(k)[0]; }
        public void put(int k, int v) {
            if (cap == 0) return;
            if (m.containsKey(k)) { m.get(k)[0] = v; touch(k); return; }
            if (m.size() == cap) {
                Iterator<Integer> it = buckets.get(minFreq).iterator();
                int victim = it.next(); it.remove(); m.remove(victim);
            }
            m.put(k, new int[]{v, 1});
            buckets.computeIfAbsent(1, x -> new LinkedHashSet<>()).add(k);
            minFreq = 1;
        }
    }

    public static void main(String[] args) {
        O1LFU c = new O1LFU(2);
        c.put(1, 1); c.put(2, 2); check(c.get(1), 1);
        c.put(3, 3); check(c.get(2), -1); check(c.get(3), 3);
        c.put(4, 4); check(c.get(1), -1); check(c.get(3), 3); check(c.get(4), 4);
        Random r = new Random(25);
        for (int t = 0; t < 300; t++) {
            int cap = r.nextInt(4);
            Cache[] cs = {new ScanLFU(cap), new TreeSetLFU(cap), new O1LFU(cap), new LinkedSetLFU(cap)};
            for (int op = 0; op < 200; op++) {
                int k = r.nextInt(6), v = r.nextInt(100);
                if (r.nextBoolean()) { int e = cs[0].get(k); for (int i = 1; i < 4; i++) check(cs[i].get(k), e); }
                else for (Cache x : cs) x.put(k, v);
            }
        }
        System.out.println("LFUCache: all tests passed");
    }

    static void check(Object got, Object exp) {
        if (!got.equals(exp)) throw new AssertionError("expected " + exp + " but got " + got);
    }
}
