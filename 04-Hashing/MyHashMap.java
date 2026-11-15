/**
 * LC 706 — Design HashMap (and the conceptual "how does HashMap work?" question).
 *
 * Design: array of buckets, each a singly linked list (separate chaining).
 *   index = (hash ^ (hash >>> 16)) & (capacity - 1)   // capacity is a power of 2, mix high bits like Java
 *   resize to 2x when size > capacity * 0.75, rehashing every entry.
 * Average O(1) for put/get/remove, worst O(n) if every key collides.
 */
public class MyHashMap {
    private static class Node {
        int key, val; Node next;
        Node(int k, int v, Node n) { key = k; val = v; next = n; }
    }

    private Node[] buckets = new Node[16];
    private int size = 0;

    private int index(int key, int cap) {
        int h = Integer.hashCode(key);
        h ^= (h >>> 16);
        return h & (cap - 1);
    }

    public void put(int key, int value) {
        int i = index(key, buckets.length);
        for (Node n = buckets[i]; n != null; n = n.next) if (n.key == key) { n.val = value; return; }
        buckets[i] = new Node(key, value, buckets[i]);
        if (++size > buckets.length * 0.75) resize();
    }

    public int get(int key) {
        for (Node n = buckets[index(key, buckets.length)]; n != null; n = n.next) if (n.key == key) return n.val;
        return -1;
    }

    public void remove(int key) {
        int i = index(key, buckets.length);
        Node dummy = new Node(0, 0, buckets[i]), prev = dummy;
        while (prev.next != null) {
            if (prev.next.key == key) { prev.next = prev.next.next; size--; break; }
            prev = prev.next;
        }
        buckets[i] = dummy.next;
    }

    private void resize() {
        Node[] old = buckets;
        buckets = new Node[old.length * 2];
        for (Node head : old)
            for (Node n = head; n != null; n = n.next) {
                int i = index(n.key, buckets.length);
                buckets[i] = new Node(n.key, n.val, buckets[i]);
            }
    }

    public static void main(String[] args) {
        MyHashMap m = new MyHashMap();
        m.put(1, 1); m.put(2, 2);
        check(m.get(1), 1); check(m.get(3), -1);
        m.put(2, 1); check(m.get(2), 1);
        m.remove(2); check(m.get(2), -1);
        for (int i = 0; i < 1000; i++) m.put(i, i * 10);   // forces several resizes
        check(m.get(999), 9990); check(m.size, 1000);
        System.out.println("MyHashMap: all tests passed");
    }

    static void check(Object got, Object exp) {
        if (!got.equals(exp)) throw new AssertionError("expected " + exp + " but got " + got);
    }
}
