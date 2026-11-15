import java.util.*;
import java.util.stream.*;

/**
 * Java Collections cheat sheet for DSA interviews.
 * Run: java JavaDSACheatSheet.java
 *
 * Complexities (typical):
 *  ArrayList     get/set O(1), add O(1) amortised, add(i)/remove(i) O(n)
 *  LinkedList    add/remove at ends O(1), get(i) O(n)
 *  ArrayDeque    push/pop/offer/poll at both ends O(1)  <-- use for Stack AND Queue
 *  HashMap/Set   get/put/remove O(1) average, O(n) worst (Java 8+: O(log n) tree bins)
 *  TreeMap/Set   get/put/remove/floor/ceiling O(log n), sorted order
 *  PriorityQueue offer/poll O(log n), peek O(1), min-heap by default
 */
public class JavaDSACheatSheet {

    public static void main(String[] args) {
        // ---------- Arrays ----------
        int[] arr = {5, 2, 9, 1};
        Arrays.sort(arr);                                   // [1,2,5,9]  dual-pivot quicksort for primitives
        int idx = Arrays.binarySearch(arr, 5);              // 2
        int[] filled = new int[5]; Arrays.fill(filled, -1);
        int[] copy = Arrays.copyOfRange(arr, 1, 3);         // [2,5]
        Integer[] boxed = {5, 2, 9, 1};
        Arrays.sort(boxed, Collections.reverseOrder());     // descending needs boxed type
        int[][] intervals = {{3, 4}, {1, 2}};
        Arrays.sort(intervals, (a, b) -> Integer.compare(a[0], b[0])); // avoid a[0]-b[0] overflow
        System.out.println("Arrays: " + Arrays.toString(arr) + " idx=" + idx + " copy=" + Arrays.toString(copy));

        // ---------- ArrayList ----------
        List<Integer> list = new ArrayList<>(List.of(3, 1, 2));
        list.add(4); list.remove(Integer.valueOf(1));      // remove by value (not index!)
        Collections.sort(list); Collections.reverse(list);
        System.out.println("List: " + list);

        // ---------- Stack & Queue via ArrayDeque ----------
        Deque<Integer> stack = new ArrayDeque<>();
        stack.push(1); stack.push(2);
        System.out.println("Stack pop: " + stack.pop() + " peek: " + stack.peek());
        Deque<Integer> queue = new ArrayDeque<>();
        queue.offer(1); queue.offer(2);
        System.out.println("Queue poll: " + queue.poll() + " peek: " + queue.peek());

        // ---------- HashMap ----------
        Map<String, Integer> freq = new HashMap<>();
        for (String w : "a b a c b a".split(" ")) freq.merge(w, 1, Integer::sum);
        freq.getOrDefault("z", 0);
        Map<Integer, List<Integer>> graph = new HashMap<>();
        graph.computeIfAbsent(1, k -> new ArrayList<>()).add(2);
        for (Map.Entry<String, Integer> e : freq.entrySet()) { /* e.getKey(), e.getValue() */ }
        System.out.println("Freq: " + freq + " graph: " + graph);

        // ---------- HashSet ----------
        Set<Integer> set = new HashSet<>();
        boolean added = set.add(5);            // false if already present -> duplicate check
        System.out.println("Set add: " + added + " contains 5: " + set.contains(5));

        // ---------- TreeMap / TreeSet (sorted) ----------
        TreeMap<Integer, String> tm = new TreeMap<>(Map.of(10, "a", 20, "b", 30, "c"));
        System.out.println("floorKey(25)=" + tm.floorKey(25) + " ceilingKey(25)=" + tm.ceilingKey(25)
                + " first=" + tm.firstKey() + " last=" + tm.lastKey());
        TreeSet<Integer> ts = new TreeSet<>(List.of(5, 1, 9));
        System.out.println("TreeSet lower(5)=" + ts.lower(5) + " higher(5)=" + ts.higher(5));

        // ---------- PriorityQueue ----------
        PriorityQueue<Integer> minHeap = new PriorityQueue<>();
        PriorityQueue<Integer> maxHeap = new PriorityQueue<>(Collections.reverseOrder());
        PriorityQueue<int[]> byDist = new PriorityQueue<>((a, b) -> Integer.compare(a[1], b[1]));
        minHeap.addAll(List.of(5, 1, 3)); maxHeap.addAll(List.of(5, 1, 3));
        byDist.offer(new int[]{0, 7}); byDist.offer(new int[]{1, 2});
        System.out.println("minHeap.peek=" + minHeap.peek() + " maxHeap.peek=" + maxHeap.peek()
                + " byDist.peek node=" + byDist.peek()[0]);

        // ---------- Strings ----------
        String s = "hello";
        char[] cs = s.toCharArray(); Arrays.sort(cs);
        String sorted = new String(cs);
        StringBuilder sb = new StringBuilder(s).reverse();   // never use += in loops: O(n^2)
        int[] count = new int[26];
        for (char c : s.toCharArray()) count[c - 'a']++;
        System.out.println("sorted=" + sorted + " reversed=" + sb + " count['l']=" + count['l' - 'a']);
        System.out.println("substring(1,3)=" + s.substring(1, 3) + " indexOf('l')=" + s.indexOf('l')
                + " charAt(0)=" + s.charAt(0) + " digit='7'-'0'=" + ('7' - '0'));

        // ---------- Integer gotchas ----------
        Integer a = 1000, b = 1000;
        System.out.println("Integer == trap: " + (a == b) + " equals: " + a.equals(b)); // false / true
        long big = (long) Integer.MAX_VALUE * 2;            // cast BEFORE multiply to avoid overflow
        int mid = 0 + (Integer.MAX_VALUE - 0) / 2;          // safe mid: lo + (hi - lo) / 2
        System.out.println("big=" + big + " mid=" + mid);

        // ---------- Streams (handy, but loops are faster in interviews) ----------
        int sum = IntStream.of(1, 2, 3).sum();
        List<Integer> evens = Stream.of(1, 2, 3, 4).filter(x -> x % 2 == 0).collect(Collectors.toList());
        System.out.println("sum=" + sum + " evens=" + evens);
    }
}
