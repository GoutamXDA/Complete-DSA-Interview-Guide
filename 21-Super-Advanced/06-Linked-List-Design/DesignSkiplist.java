import java.util.*;

/**
 * LC 1206 — Design Skiplist. Explanation: DesignSkiplist.md
 * 1. SortedListSet : sorted singly linked list, O(n)
 * 2. ArrayListSet  : sorted ArrayList + binary search, search O(log n), add/erase O(n)
 * 3. Skiplist      : randomised levels, O(log n) expected
 */
public class DesignSkiplist {

    interface Api { boolean search(int t); void add(int x); boolean erase(int x); }

    static class SortedListSet implements Api {
        static class N { int v; N next; N(int v, N n) { this.v = v; next = n; } }
        final N head = new N(Integer.MIN_VALUE, null);
        public boolean search(int t) { N c = head.next; while (c != null && c.v < t) c = c.next; return c != null && c.v == t; }
        public void add(int x) { N c = head; while (c.next != null && c.next.v < x) c = c.next; c.next = new N(x, c.next); }
        public boolean erase(int x) { N c = head; while (c.next != null && c.next.v < x) c = c.next; if (c.next == null || c.next.v != x) return false; c.next = c.next.next; return true; }
    }

    static class ArrayListSet implements Api {
        final List<Integer> a = new ArrayList<>();
        int lower(int x) { int lo = 0, hi = a.size(); while (lo < hi) { int m = (lo + hi) / 2; if (a.get(m) < x) lo = m + 1; else hi = m; } return lo; }
        public boolean search(int t) { int i = lower(t); return i < a.size() && a.get(i) == t; }
        public void add(int x) { a.add(lower(x), x); }
        public boolean erase(int x) { int i = lower(x); if (i == a.size() || a.get(i) != x) return false; a.remove(i); return true; }
    }

    static class Skiplist implements Api {
        static final int MAX = 16;
        static class Node { final int val; final Node[] next; Node(int v, int lvl) { val = v; next = new Node[lvl]; } }
        final Node head = new Node(Integer.MIN_VALUE, MAX);
        final Random rnd;
        int level = 1;                                     // number of levels currently in use
        Skiplist(long seed) { rnd = new Random(seed); }

        int randomLevel() { int l = 1; while (l < MAX && rnd.nextBoolean()) l++; return l; }

        Node[] predecessors(int x) {
            Node[] update = new Node[MAX];
            Node cur = head;
            for (int l = level - 1; l >= 0; l--) {
                while (cur.next[l] != null && cur.next[l].val < x) cur = cur.next[l];
                update[l] = cur;
            }
            return update;
        }

        public boolean search(int t) {
            Node c = predecessors(t)[0].next[0];
            return c != null && c.val == t;
        }

        public void add(int x) {
            Node[] update = predecessors(x);
            int lvl = randomLevel();
            if (lvl > level) { for (int l = level; l < lvl; l++) update[l] = head; level = lvl; }
            Node n = new Node(x, lvl);
            for (int l = 0; l < lvl; l++) { n.next[l] = update[l].next[l]; update[l].next[l] = n; }
        }

        public boolean erase(int x) {
            Node[] update = predecessors(x);
            Node target = update[0].next[0];
            if (target == null || target.val != x) return false;
            for (int l = 0; l < level && update[l].next[l] == target; l++) update[l].next[l] = target.next[l];
            while (level > 1 && head.next[level - 1] == null) level--;
            return true;
        }

        int height() { return level; }
    }

    public static void main(String[] args) {
        Skiplist s = new Skiplist(1);
        s.add(1); s.add(2); s.add(3);
        check(s.search(0), false); s.add(4); check(s.search(1), true);
        check(s.erase(0), false); check(s.erase(1), true); check(s.search(1), false);
        Random r = new Random(27);
        for (int t = 0; t < 200; t++) {
            Api[] impls = {new SortedListSet(), new ArrayListSet(), new Skiplist(t)};
            for (int op = 0; op < 300; op++) {
                int x = r.nextInt(20), kind = r.nextInt(3);
                if (kind == 0) for (Api a : impls) a.add(x);
                else if (kind == 1) { boolean e = impls[0].search(x); check(impls[1].search(x), e); check(impls[2].search(x), e); }
                else { boolean e = impls[0].erase(x); check(impls[1].erase(x), e); check(impls[2].erase(x), e); }
            }
        }
        Skiplist big = new Skiplist(7);
        for (int i = 0; i < 100000; i++) big.add(i);
        check(big.height() <= 16 && big.height() >= 10, true);     // ~log2(100000) = 17, capped at 16
        for (int i = 0; i < 100000; i += 997) check(big.search(i), true);
        System.out.println("DesignSkiplist: all tests passed");
    }

    static void check(Object got, Object exp) {
        if (!got.equals(exp)) throw new AssertionError("expected " + exp + " but got " + got);
    }
}
