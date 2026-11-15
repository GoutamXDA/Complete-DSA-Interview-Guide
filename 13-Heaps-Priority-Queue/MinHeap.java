import java.util.*;

/**
 * Min-heap from scratch (asked frequently: "implement a priority queue").
 * insert   : append at end, sift up        O(log n)
 * extractMin: move last to root, sift down O(log n)
 * heapify  : sift down from n/2-1 to 0     O(n)  (most nodes are near the bottom and move little)
 */
public class MinHeap {
    private int[] a;
    private int size;

    MinHeap(int capacity) { a = new int[Math.max(1, capacity)]; }

    MinHeap(int[] arr) { a = arr.clone(); size = arr.length; for (int i = size / 2 - 1; i >= 0; i--) siftDown(i); }

    void insert(int x) {
        if (size == a.length) a = Arrays.copyOf(a, a.length * 2);
        a[size] = x;
        siftUp(size++);
    }

    int peek() { if (size == 0) throw new NoSuchElementException(); return a[0]; }

    int extractMin() {
        int min = peek();
        a[0] = a[--size];
        siftDown(0);
        return min;
    }

    /** Decrease key at index i to value v (used in Dijkstra with an indexed heap). */
    void decreaseKey(int i, int v) { a[i] = v; siftUp(i); }

    int size() { return size; }

    private void siftUp(int i) {
        while (i > 0 && a[(i - 1) / 2] > a[i]) { swap(i, (i - 1) / 2); i = (i - 1) / 2; }
    }

    private void siftDown(int i) {
        while (true) {
            int s = i, l = 2 * i + 1, r = 2 * i + 2;
            if (l < size && a[l] < a[s]) s = l;
            if (r < size && a[r] < a[s]) s = r;
            if (s == i) return;
            swap(i, s); i = s;
        }
    }

    private void swap(int i, int j) { int t = a[i]; a[i] = a[j]; a[j] = t; }

    public static void main(String[] args) {
        MinHeap h = new MinHeap(2);
        for (int x : new int[]{5, 3, 8, 1, 9, 2}) h.insert(x);
        StringBuilder sb = new StringBuilder();
        while (h.size() > 0) sb.append(h.extractMin()).append(' ');
        check(sb.toString().trim(), "1 2 3 5 8 9");
        MinHeap built = new MinHeap(new int[]{7, 4, 6, 1, 3});
        check(built.extractMin(), 1); check(built.peek(), 3);
        System.out.println("MinHeap: all tests passed");
    }

    static void check(Object got, Object exp) {
        if (!got.equals(exp)) throw new AssertionError("expected " + exp + " but got " + got);
    }
}
