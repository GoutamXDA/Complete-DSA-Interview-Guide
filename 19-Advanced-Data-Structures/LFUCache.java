import java.util.*;

/**
 * LC 460 — LFU Cache, O(1) get / put.
 * keys:  key -> {value, freq}
 * freqs: freq -> LinkedHashSet of keys (insertion order = LRU order within the same frequency)
 * minFreq: smallest frequency present; evict the first key of freqs[minFreq].
 * On access: move key from freq f to f+1; if freqs[f] became empty and f == minFreq, minFreq++.
 * On insert of a new key: minFreq = 1.
 */
public class LFUCache {
    private final int cap;
    private int minFreq = 0;
    private final Map<Integer, int[]> keys = new HashMap<>();              // {value, freq}
    private final Map<Integer, LinkedHashSet<Integer>> freqs = new HashMap<>();

    LFUCache(int capacity) { cap = capacity; }

    int get(int key) {
        int[] e = keys.get(key);
        if (e == null) return -1;
        touch(key, e);
        return e[0];
    }

    void put(int key, int value) {
        if (cap == 0) return;
        int[] e = keys.get(key);
        if (e != null) { e[0] = value; touch(key, e); return; }
        if (keys.size() == cap) {
            LinkedHashSet<Integer> bucket = freqs.get(minFreq);
            int evict = bucket.iterator().next();
            bucket.remove(evict);
            keys.remove(evict);
        }
        keys.put(key, new int[]{value, 1});
        freqs.computeIfAbsent(1, f -> new LinkedHashSet<>()).add(key);
        minFreq = 1;
    }

    private void touch(int key, int[] e) {
        int f = e[1];
        LinkedHashSet<Integer> bucket = freqs.get(f);
        bucket.remove(key);
        if (bucket.isEmpty() && f == minFreq) minFreq++;
        e[1] = f + 1;
        freqs.computeIfAbsent(f + 1, x -> new LinkedHashSet<>()).add(key);
    }

    public static void main(String[] args) {
        LFUCache c = new LFUCache(2);
        c.put(1, 1); c.put(2, 2);
        check(c.get(1), 1);
        c.put(3, 3);                  // evicts 2 (freq 1)
        check(c.get(2), -1); check(c.get(3), 3);
        c.put(4, 4);                  // 1 and 3 both freq 2 -> evict LRU among them = 1
        check(c.get(1), -1); check(c.get(3), 3); check(c.get(4), 4);
        System.out.println("LFUCache: all tests passed");
    }

    static void check(Object got, Object exp) {
        if (!got.equals(exp)) throw new AssertionError("expected " + exp + " but got " + got);
    }
}
