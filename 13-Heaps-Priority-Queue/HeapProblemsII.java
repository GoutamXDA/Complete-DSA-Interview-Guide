import java.util.*;

/**
 * LC 1167 Min cost to connect ropes: brute re-sort after every join O(n^2 log n) | min-heap O(n log n)
 * LC 846  Hand of straights (groups of W consecutive): sort + remove from list O(n^2) | TreeMap counts O(n log n)
 * LC 373  K pairs with smallest sums: brute all pairs + sort O(mn log mn) | heap seeded with (i, 0), push (i, j+1) O(k log k)
 * Maximum sum combinations (GFG): top K sums a[i] + b[j]: brute all sums O(n^2 log n) | sort desc + max-heap + visited O(K log K)
 * LC 355  Design Twitter: per-user tweet list (global timestamp) + follow sets; feed = k-way merge of 10 newest.
 * LC 502  IPO (k projects, initial capital w): brute pick best affordable each round O(kn)
 *         | sort by capital + max-heap of affordable profits O((n + k) log n)
 * LC 480  Sliding window median: brute sort each window O(n k log k) | two heaps with lazy deletion O(n log k)
 * LC 632  Smallest range covering K lists: merge all (value, list) + sliding window over K-coverage O(N log N)
 *         | min-heap of current heads + track current max O(N log k)
 * LC 857  Min cost to hire K workers: brute each worker as the ratio-setter O(n^2 log n)
 *         | sort by wage/quality, max-heap keeps the K smallest qualities O(n log n)
 */
public class HeapProblemsII {

    static int connectRopesBrute(int[] r) {
        List<Integer> l = new ArrayList<>(); for (int x : r) l.add(x);
        int cost = 0;
        while (l.size() > 1) { Collections.sort(l); int s = l.remove(0) + l.remove(0); cost += s; l.add(s); }
        return cost;
    }

    static int connectSticks(int[] r) {
        PriorityQueue<Integer> pq = new PriorityQueue<>();
        for (int x : r) pq.offer(x);
        int cost = 0;
        while (pq.size() > 1) { int s = pq.poll() + pq.poll(); cost += s; pq.offer(s); }
        return cost;
    }

    static boolean isNStraightHandBrute(int[] hand, int w) {
        List<Integer> l = new ArrayList<>(); for (int x : hand) l.add(x);
        Collections.sort(l);
        while (!l.isEmpty()) {
            int start = l.get(0);
            for (int v = start; v < start + w; v++) if (!l.remove(Integer.valueOf(v))) return false;
        }
        return true;
    }

    static boolean isNStraightHand(int[] hand, int w) {
        if (hand.length % w != 0) return false;
        TreeMap<Integer, Integer> cnt = new TreeMap<>();
        for (int x : hand) cnt.merge(x, 1, Integer::sum);
        while (!cnt.isEmpty()) {
            int start = cnt.firstKey();
            for (int v = start; v < start + w; v++) {
                Integer c = cnt.get(v);
                if (c == null) return false;
                if (c == 1) cnt.remove(v); else cnt.put(v, c - 1);
            }
        }
        return true;
    }

    static List<List<Integer>> kSmallestPairsBrute(int[] a, int[] b, int k) {
        List<List<Integer>> all = new ArrayList<>();
        for (int x : a) for (int y : b) all.add(List.of(x, y));
        all.sort(Comparator.comparingInt(p -> p.get(0) + p.get(1)));
        return all.subList(0, Math.min(k, all.size()));
    }

    static List<List<Integer>> kSmallestPairs(int[] a, int[] b, int k) {
        PriorityQueue<int[]> pq = new PriorityQueue<>(Comparator.comparingInt(p -> a[p[0]] + b[p[1]]));
        for (int i = 0; i < Math.min(a.length, k); i++) pq.offer(new int[]{i, 0});
        List<List<Integer>> res = new ArrayList<>();
        while (k-- > 0 && !pq.isEmpty()) {
            int[] p = pq.poll();
            res.add(List.of(a[p[0]], b[p[1]]));
            if (p[1] + 1 < b.length) pq.offer(new int[]{p[0], p[1] + 1});
        }
        return res;
    }

    static List<Integer> maxSumCombinationsBrute(int[] a, int[] b, int k) {
        List<Integer> s = new ArrayList<>();
        for (int x : a) for (int y : b) s.add(x + y);
        s.sort(Collections.reverseOrder());
        return s.subList(0, k);
    }

    static List<Integer> maxSumCombinations(int[] a0, int[] b0, int k) {
        Integer[] a = Arrays.stream(a0).boxed().sorted(Collections.reverseOrder()).toArray(Integer[]::new);
        Integer[] b = Arrays.stream(b0).boxed().sorted(Collections.reverseOrder()).toArray(Integer[]::new);
        PriorityQueue<int[]> pq = new PriorityQueue<>((x, y) -> (a[y[0]] + b[y[1]]) - (a[x[0]] + b[x[1]]));
        Set<Long> seen = new HashSet<>();
        pq.offer(new int[]{0, 0}); seen.add(0L);
        List<Integer> res = new ArrayList<>();
        while (res.size() < k) {
            int[] p = pq.poll();
            res.add(a[p[0]] + b[p[1]]);
            int[][] nexts = {{p[0] + 1, p[1]}, {p[0], p[1] + 1}};
            for (int[] nx : nexts)
                if (nx[0] < a.length && nx[1] < b.length && seen.add((long) nx[0] * b.length + nx[1])) pq.offer(nx);
        }
        return res;
    }

    static class Twitter {
        int time = 0;
        final Map<Integer, List<int[]>> tweets = new HashMap<>();   // user -> {time, tweetId}
        final Map<Integer, Set<Integer>> follows = new HashMap<>();
        void postTweet(int user, int id) { tweets.computeIfAbsent(user, k -> new ArrayList<>()).add(new int[]{time++, id}); }
        List<Integer> getNewsFeed(int user) {
            PriorityQueue<int[]> pq = new PriorityQueue<>((x, y) -> y[0] - x[0]);   // {time, id, user, index}
            Set<Integer> users = new HashSet<>(follows.getOrDefault(user, Set.of()));
            users.add(user);
            for (int u : users) {
                List<int[]> l = tweets.get(u);
                if (l != null && !l.isEmpty()) { int i = l.size() - 1; pq.offer(new int[]{l.get(i)[0], l.get(i)[1], u, i}); }
            }
            List<Integer> feed = new ArrayList<>();
            while (!pq.isEmpty() && feed.size() < 10) {
                int[] t = pq.poll();
                feed.add(t[1]);
                if (t[3] > 0) { int[] nx = tweets.get(t[2]).get(t[3] - 1); pq.offer(new int[]{nx[0], nx[1], t[2], t[3] - 1}); }
            }
            return feed;
        }
        void follow(int a, int b) { if (a != b) follows.computeIfAbsent(a, k -> new HashSet<>()).add(b); }
        void unfollow(int a, int b) { Set<Integer> s = follows.get(a); if (s != null) s.remove(b); }
    }

    static int ipoBrute(int k, int w, int[] profits, int[] capital) {
        boolean[] used = new boolean[profits.length];
        while (k-- > 0) {
            int best = -1;
            for (int i = 0; i < profits.length; i++)
                if (!used[i] && capital[i] <= w && (best == -1 || profits[i] > profits[best])) best = i;
            if (best == -1) break;
            used[best] = true; w += profits[best];
        }
        return w;
    }

    static int findMaximizedCapital(int k, int w, int[] profits, int[] capital) {
        int n = profits.length;
        Integer[] idx = new Integer[n];
        for (int i = 0; i < n; i++) idx[i] = i;
        Arrays.sort(idx, Comparator.comparingInt(i -> capital[i]));
        PriorityQueue<Integer> pq = new PriorityQueue<>(Collections.reverseOrder());
        int p = 0;
        while (k-- > 0) {
            while (p < n && capital[idx[p]] <= w) pq.offer(profits[idx[p++]]);   // newly affordable projects
            if (pq.isEmpty()) break;
            w += pq.poll();
        }
        return w;
    }

    static double[] medianSlidingWindowBrute(int[] a, int k) {
        double[] r = new double[a.length - k + 1];
        for (int i = 0; i + k <= a.length; i++) {
            int[] w = Arrays.copyOfRange(a, i, i + k); Arrays.sort(w);
            r[i] = k % 2 == 1 ? w[k / 2] : ((double) w[k / 2 - 1] + w[k / 2]) / 2;
        }
        return r;
    }

    /** Two heaps with lazy deletion: `low` (max-heap) holds the smaller half, sizes balanced by valid counts. */
    static double[] medianSlidingWindow(int[] a, int k) {
        PriorityQueue<Integer> low = new PriorityQueue<>(Collections.reverseOrder()), high = new PriorityQueue<>();
        Map<Integer, Integer> delayed = new HashMap<>();
        int[] size = new int[2];   // valid elements in low, high
        double[] res = new double[a.length - k + 1];
        for (int i = 0; i < a.length; i++) {
            if (low.isEmpty() || a[i] <= low.peek()) { low.offer(a[i]); size[0]++; } else { high.offer(a[i]); size[1]++; }
            if (i >= k) {                                     // remove a[i-k] lazily
                int out = a[i - k];
                delayed.merge(out, 1, Integer::sum);
                if (out <= low.peek()) size[0]--; else size[1]--;
                prune(low, delayed); prune(high, delayed);
            }
            // rebalance: low has size[1] or size[1] + 1 valid elements
            if (size[0] > size[1] + 1) { high.offer(low.poll()); size[0]--; size[1]++; prune(low, delayed); }
            else if (size[0] < size[1]) { low.offer(high.poll()); size[1]--; size[0]++; prune(high, delayed); }
            if (i >= k - 1) res[i - k + 1] = k % 2 == 1 ? low.peek() : ((double) low.peek() + high.peek()) / 2;
        }
        return res;
    }

    static void prune(PriorityQueue<Integer> h, Map<Integer, Integer> delayed) {
        while (!h.isEmpty() && delayed.getOrDefault(h.peek(), 0) > 0) {
            int x = h.poll();
            delayed.merge(x, -1, Integer::sum);
        }
    }

    static int[] smallestRangeSweep(List<List<Integer>> lists) {
        List<int[]> all = new ArrayList<>();
        for (int i = 0; i < lists.size(); i++) for (int v : lists.get(i)) all.add(new int[]{v, i});
        all.sort(Comparator.comparingInt(x -> x[0]));
        int[] cnt = new int[lists.size()];
        int covered = 0, l = 0;
        int[] best = {-100000, 100000};
        for (int r = 0; r < all.size(); r++) {
            if (cnt[all.get(r)[1]]++ == 0) covered++;
            while (covered == lists.size()) {
                int lo = all.get(l)[0], hi = all.get(r)[0];
                if (hi - lo < best[1] - best[0]) best = new int[]{lo, hi};
                if (--cnt[all.get(l)[1]] == 0) covered--;
                l++;
            }
        }
        return best;
    }

    static int[] smallestRange(List<List<Integer>> lists) {
        PriorityQueue<int[]> pq = new PriorityQueue<>(Comparator.comparingInt(x -> x[0]));   // {value, list, index}
        int max = Integer.MIN_VALUE;
        for (int i = 0; i < lists.size(); i++) { int v = lists.get(i).get(0); pq.offer(new int[]{v, i, 0}); max = Math.max(max, v); }
        int[] best = {pq.peek()[0], max};
        while (true) {
            int[] c = pq.poll();
            if (max - c[0] < best[1] - best[0]) best = new int[]{c[0], max};
            List<Integer> l = lists.get(c[1]);
            if (c[2] + 1 == l.size()) return best;     // one list exhausted: can't cover all anymore
            int nv = l.get(c[2] + 1);
            pq.offer(new int[]{nv, c[1], c[2] + 1});
            max = Math.max(max, nv);
        }
    }

    static double mincostToHireWorkersBrute(int[] q, int[] w, int k) {
        double best = Double.MAX_VALUE;
        for (int c = 0; c < q.length; c++) {
            double ratio = (double) w[c] / q[c];
            List<Integer> ok = new ArrayList<>();
            for (int i = 0; i < q.length; i++) if ((double) w[i] / q[i] <= ratio) ok.add(q[i]);
            if (ok.size() < k) continue;
            Collections.sort(ok);
            double s = 0; for (int i = 0; i < k; i++) s += ok.get(i);
            best = Math.min(best, s * ratio);
        }
        return best;
    }

    static double mincostToHireWorkers(int[] q, int[] w, int k) {
        int n = q.length;
        Integer[] idx = new Integer[n];
        for (int i = 0; i < n; i++) idx[i] = i;
        Arrays.sort(idx, Comparator.comparingDouble(i -> (double) w[i] / q[i]));
        PriorityQueue<Integer> maxQ = new PriorityQueue<>(Collections.reverseOrder());
        double best = Double.MAX_VALUE; int sumQ = 0;
        for (int i : idx) {
            maxQ.offer(q[i]); sumQ += q[i];
            if (maxQ.size() > k) sumQ -= maxQ.poll();          // drop the largest quality
            if (maxQ.size() == k) best = Math.min(best, sumQ * ((double) w[i] / q[i]));
        }
        return best;
    }

    public static void main(String[] args) {
        check(connectSticks(new int[]{2, 4, 3}), 14); check(connectRopesBrute(new int[]{1, 8, 3, 5}), 30); check(connectSticks(new int[]{1, 8, 3, 5}), 30);
        int[] hand = {1, 2, 3, 6, 2, 3, 4, 7, 8};
        check(isNStraightHand(hand, 3), true); check(isNStraightHandBrute(hand, 3), true); check(isNStraightHand(new int[]{1, 2, 3, 4, 5}, 4), false);
        int[] p1 = {1, 7, 11}, p2 = {2, 4, 6};
        check(kSmallestPairs(p1, p2, 3), List.of(List.of(1, 2), List.of(1, 4), List.of(1, 6)));
        check(kSmallestPairsBrute(p1, p2, 3), kSmallestPairs(p1, p2, 3));
        check(maxSumCombinations(new int[]{3, 2}, new int[]{1, 4}, 2), List.of(7, 6));
        check(maxSumCombinations(new int[]{1, 4, 2, 3}, new int[]{2, 5, 1, 6}, 4), maxSumCombinationsBrute(new int[]{1, 4, 2, 3}, new int[]{2, 5, 1, 6}, 4));
        Twitter tw = new Twitter();
        tw.postTweet(1, 5); check(tw.getNewsFeed(1), List.of(5));
        tw.follow(1, 2); tw.postTweet(2, 6); check(tw.getNewsFeed(1), List.of(6, 5));
        tw.unfollow(1, 2); check(tw.getNewsFeed(1), List.of(5));
        int[] pr = {1, 2, 3}, cap = {0, 1, 1};
        check(findMaximizedCapital(2, 0, pr, cap), 4); check(ipoBrute(2, 0, pr, cap), 4); check(findMaximizedCapital(3, 0, pr, new int[]{0, 1, 2}), 6);
        Random rnd = new Random(9);
        for (int t = 0; t < 100; t++) {
            int n = rnd.nextInt(15) + 1, k = rnd.nextInt(n) + 1;
            int[] a = new int[n];
            for (int i = 0; i < n; i++) a[i] = rnd.nextInt(7) - 3;
            check(Arrays.toString(medianSlidingWindow(a, k)), Arrays.toString(medianSlidingWindowBrute(a, k)));
        }
        check(Arrays.toString(medianSlidingWindow(new int[]{1, 3, -1, -3, 5, 3, 6, 7}, 3)), "[1.0, -1.0, -1.0, 3.0, 5.0, 6.0]");
        List<List<Integer>> lists = List.of(List.of(4, 10, 15, 24, 26), List.of(0, 9, 12, 20), List.of(5, 18, 22, 30));
        check(Arrays.toString(smallestRange(lists)), "[20, 24]"); check(Arrays.toString(smallestRangeSweep(lists)), "[20, 24]");
        check(Math.abs(mincostToHireWorkers(new int[]{10, 20, 5}, new int[]{70, 50, 30}, 2) - 105.0) < 1e-6, true);
        int[] qq = {3, 1, 10, 10, 1}, ww = {4, 8, 2, 2, 7};
        check(Math.abs(mincostToHireWorkers(qq, ww, 3) - mincostToHireWorkersBrute(qq, ww, 3)) < 1e-6, true);
        System.out.println("HeapProblemsII: all tests passed");
    }

    static void check(Object got, Object exp) {
        if (!got.equals(exp)) throw new AssertionError("expected " + exp + " but got " + got);
    }
}
