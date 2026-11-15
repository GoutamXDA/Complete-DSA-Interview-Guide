import java.util.*;

/**
 * Every sorting algorithm interviewers ask you to write by hand.
 * Run: each is tested against Arrays.sort on random input.
 */
public class SortingAlgorithms {

    /** Bubble: largest bubbles to the end each pass. Early exit -> O(n) on sorted input. */
    static void bubble(int[] a) {
        for (int i = a.length - 1; i > 0; i--) {
            boolean swapped = false;
            for (int j = 0; j < i; j++) if (a[j] > a[j + 1]) { swap(a, j, j + 1); swapped = true; }
            if (!swapped) return;
        }
    }

    /** Selection: pick min of unsorted part, put it at the front. */
    static void selection(int[] a) {
        for (int i = 0; i < a.length - 1; i++) {
            int min = i;
            for (int j = i + 1; j < a.length; j++) if (a[j] < a[min]) min = j;
            swap(a, i, min);
        }
    }

    /** Insertion: grow a sorted prefix, shift bigger elements right. */
    static void insertion(int[] a) {
        for (int i = 1; i < a.length; i++) {
            int key = a[i], j = i - 1;
            while (j >= 0 && a[j] > key) { a[j + 1] = a[j]; j--; }
            a[j + 1] = key;
        }
    }

    /** Merge sort: T(n) = 2T(n/2) + O(n) = O(n log n), O(n) extra. */
    static void mergeSort(int[] a, int l, int r, int[] tmp) {
        if (l >= r) return;
        int m = l + (r - l) / 2;
        mergeSort(a, l, m, tmp); mergeSort(a, m + 1, r, tmp);
        int i = l, j = m + 1, k = l;
        while (i <= m && j <= r) tmp[k++] = a[i] <= a[j] ? a[i++] : a[j++]; // <= keeps it stable
        while (i <= m) tmp[k++] = a[i++];
        while (j <= r) tmp[k++] = a[j++];
        System.arraycopy(tmp, l, a, l, r - l + 1);
    }

    /** Quick sort with random pivot + Lomuto partition. Avg O(n log n), worst O(n^2). */
    static final Random RNG = new Random(42);
    static void quickSort(int[] a, int l, int r) {
        if (l >= r) return;
        int p = partition(a, l, r);
        quickSort(a, l, p - 1); quickSort(a, p + 1, r);
    }

    static int partition(int[] a, int l, int r) {
        swap(a, l + RNG.nextInt(r - l + 1), r);  // random pivot moved to end
        int pivot = a[r], i = l;
        for (int j = l; j < r; j++) if (a[j] < pivot) swap(a, i++, j);
        swap(a, i, r);
        return i;
    }

    /** Heap sort: build max-heap O(n), then extract max n times O(n log n). In place. */
    static void heapSort(int[] a) {
        int n = a.length;
        for (int i = n / 2 - 1; i >= 0; i--) siftDown(a, i, n);
        for (int end = n - 1; end > 0; end--) { swap(a, 0, end); siftDown(a, 0, end); }
    }

    static void siftDown(int[] a, int i, int n) {
        while (true) {
            int largest = i, l = 2 * i + 1, r = 2 * i + 2;
            if (l < n && a[l] > a[largest]) largest = l;
            if (r < n && a[r] > a[largest]) largest = r;
            if (largest == i) return;
            swap(a, i, largest); i = largest;
        }
    }

    /** Counting sort for values in [min, max]. O(n + k). */
    static void countingSort(int[] a) {
        if (a.length == 0) return;
        int min = Arrays.stream(a).min().getAsInt(), max = Arrays.stream(a).max().getAsInt();
        int[] c = new int[max - min + 1];
        for (int x : a) c[x - min]++;
        int k = 0;
        for (int v = 0; v < c.length; v++) while (c[v]-- > 0) a[k++] = v + min;
    }

    /** LSD radix sort for non-negative ints, base 10, using stable counting per digit. */
    static void radixSort(int[] a) {
        int max = Arrays.stream(a).max().orElse(0);
        int[] out = new int[a.length];
        for (long exp = 1; max / exp > 0; exp *= 10) {
            int[] c = new int[10];
            for (int x : a) c[(int) (x / exp % 10)]++;
            for (int d = 1; d < 10; d++) c[d] += c[d - 1];
            for (int i = a.length - 1; i >= 0; i--) out[--c[(int) (a[i] / exp % 10)]] = a[i]; // backwards = stable
            System.arraycopy(out, 0, a, 0, a.length);
        }
    }

    static void swap(int[] a, int i, int j) { int t = a[i]; a[i] = a[j]; a[j] = t; }

    public static void main(String[] args) {
        Random r = new Random(7);
        for (int trial = 0; trial < 50; trial++) {
            int n = r.nextInt(60);
            int[] base = new int[n];
            for (int i = 0; i < n; i++) base[i] = r.nextInt(200) - (trial % 2 == 0 ? 0 : 100);
            int[] expected = base.clone(); Arrays.sort(expected);
            String e = Arrays.toString(expected);
            int[] t;
            t = base.clone(); bubble(t); check(Arrays.toString(t), e);
            t = base.clone(); selection(t); check(Arrays.toString(t), e);
            t = base.clone(); insertion(t); check(Arrays.toString(t), e);
            t = base.clone(); mergeSort(t, 0, n - 1, new int[n]); check(Arrays.toString(t), e);
            t = base.clone(); quickSort(t, 0, n - 1); check(Arrays.toString(t), e);
            t = base.clone(); heapSort(t); check(Arrays.toString(t), e);
            t = base.clone(); countingSort(t); check(Arrays.toString(t), e);
            if (trial % 2 == 0) { t = base.clone(); radixSort(t); check(Arrays.toString(t), e); }
        }
        System.out.println("SortingAlgorithms: all tests passed");
    }

    static void check(Object got, Object exp) {
        if (!got.equals(exp)) throw new AssertionError("expected " + exp + " but got " + got);
    }
}
