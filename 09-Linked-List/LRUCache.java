import java.util.*;

/**
 * LC 146 — LRU Cache with O(1) get and put.
 *
 * Approach 1 (library): LinkedHashMap with accessOrder = true + removeEldestEntry. Know it, but
 *                       interviewers usually want approach 2.
 * Approach 2 (manual) : HashMap<key, Node> + doubly linked list (head = most recent, tail = least recent).
 *   get: move node to front. put: update/move, or insert at front; evict tail.prev if over capacity.
 */
public class LRUCache {

    // ---------- Approach 2 ----------
    private static class Node { int key, val; Node prev, next; Node(int k, int v) { key = k; val = v; } }

    private final int capacity;
    private final Map<Integer, Node> map = new HashMap<>();
    private final Node head = new Node(0, 0), tail = new Node(0, 0);  // sentinels

    LRUCache(int capacity) {
        this.capacity = capacity;
        head.next = tail; tail.prev = head;
    }

    int get(int key) {
        Node n = map.get(key);
        if (n == null) return -1;
        remove(n); addFront(n);
        return n.val;
    }

    void put(int key, int value) {
        Node n = map.get(key);
        if (n != null) { n.val = value; remove(n); addFront(n); return; }
        if (map.size() == capacity) { Node lru = tail.prev; remove(lru); map.remove(lru.key); }
        n = new Node(key, value);
        map.put(key, n); addFront(n);
    }

    private void remove(Node n) { n.prev.next = n.next; n.next.prev = n.prev; }

    private void addFront(Node n) { n.next = head.next; n.prev = head; head.next.prev = n; head.next = n; }

    // ---------- Approach 1 ----------
    static class LRULinkedHashMap extends LinkedHashMap<Integer, Integer> {
        private final int cap;
        LRULinkedHashMap(int cap) { super(cap, 0.75f, true); this.cap = cap; }
        int getVal(int k) { return getOrDefault(k, -1); }
        @Override protected boolean removeEldestEntry(Map.Entry<Integer, Integer> e) { return size() > cap; }
    }

    public static void main(String[] args) {
        LRUCache c = new LRUCache(2);
        c.put(1, 1); c.put(2, 2);
        check(c.get(1), 1);
        c.put(3, 3);               // evicts 2
        check(c.get(2), -1);
        c.put(4, 4);               // evicts 1
        check(c.get(1), -1); check(c.get(3), 3); check(c.get(4), 4);

        LRULinkedHashMap l = new LRULinkedHashMap(2);
        l.put(1, 1); l.put(2, 2); l.getVal(1); l.put(3, 3);
        check(l.getVal(2), -1); check(l.getVal(1), 1);
        System.out.println("LRUCache: all tests passed");
    }

    static void check(Object got, Object exp) {
        if (!got.equals(exp)) throw new AssertionError("expected " + exp + " but got " + got);
    }
}
