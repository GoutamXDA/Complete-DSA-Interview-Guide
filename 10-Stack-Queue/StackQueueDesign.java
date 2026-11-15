import java.util.*;

/**
 * Design problems.
 * ArrayStack          : int[] + top index, grow ×2.
 * LC 232 Queue via 2 stacks: push to in; pop from out (refill from in only when out is empty). Amortised O(1).
 * LC 225 Stack via 1 queue : after offer, rotate size-1 elements to the back. push O(n), pop O(1).
 * LC 622 Circular queue  : array + head + size; tail = (head + size) % cap.
 * LC 155 Min stack
 *   A) stack of {val, minSoFar}     O(n) extra
 *   B) single stack of longs with encoding: push 2*val - min when val < min. O(1) extra besides the stack.
 */
public class StackQueueDesign {

    static class ArrayStack {
        int[] data = new int[2]; int top = -1;
        void push(int x) { if (top + 1 == data.length) data = Arrays.copyOf(data, data.length * 2); data[++top] = x; }
        int pop() { if (top < 0) throw new RuntimeException("empty"); return data[top--]; }
        int peek() { return data[top]; }
        boolean isEmpty() { return top < 0; }
    }

    static class MyQueue {
        Deque<Integer> in = new ArrayDeque<>(), out = new ArrayDeque<>();
        void push(int x) { in.push(x); }
        int pop() { peek(); return out.pop(); }
        int peek() { if (out.isEmpty()) while (!in.isEmpty()) out.push(in.pop()); return out.peek(); }
        boolean empty() { return in.isEmpty() && out.isEmpty(); }
    }

    static class MyStack {
        Queue<Integer> q = new ArrayDeque<>();
        void push(int x) { q.offer(x); for (int i = 0; i < q.size() - 1; i++) q.offer(q.poll()); }
        int pop() { return q.poll(); }
        int top() { return q.peek(); }
        boolean empty() { return q.isEmpty(); }
    }

    static class MyCircularQueue {
        int[] a; int head = 0, size = 0;
        MyCircularQueue(int k) { a = new int[k]; }
        boolean enQueue(int v) { if (isFull()) return false; a[(head + size++) % a.length] = v; return true; }
        boolean deQueue() { if (isEmpty()) return false; head = (head + 1) % a.length; size--; return true; }
        int Front() { return isEmpty() ? -1 : a[head]; }
        int Rear() { return isEmpty() ? -1 : a[(head + size - 1) % a.length]; }
        boolean isEmpty() { return size == 0; }
        boolean isFull() { return size == a.length; }
    }

    static class MinStackPairs {
        Deque<int[]> st = new ArrayDeque<>();
        void push(int x) { st.push(new int[]{x, st.isEmpty() ? x : Math.min(x, st.peek()[1])}); }
        void pop() { st.pop(); }
        int top() { return st.peek()[0]; }
        int getMin() { return st.peek()[1]; }
    }

    static class MinStackEncoded {
        Deque<Long> st = new ArrayDeque<>(); long min;
        void push(int x) {
            if (st.isEmpty()) { st.push((long) x); min = x; }
            else if (x < min) { st.push(2L * x - min); min = x; }  // encoded value < new min flags it
            else st.push((long) x);
        }
        void pop() { long t = st.pop(); if (t < min) min = 2 * min - t; } // recover previous min
        int top() { long t = st.peek(); return (int) (t < min ? min : t); }
        int getMin() { return (int) min; }
    }

    public static void main(String[] args) {
        ArrayStack s = new ArrayStack();
        for (int i = 1; i <= 5; i++) s.push(i);
        check(s.pop(), 5); check(s.peek(), 4);

        MyQueue q = new MyQueue(); q.push(1); q.push(2);
        check(q.peek(), 1); check(q.pop(), 1); check(q.empty(), false);

        MyStack ms = new MyStack(); ms.push(1); ms.push(2);
        check(ms.top(), 2); check(ms.pop(), 2); check(ms.empty(), false);

        MyCircularQueue cq = new MyCircularQueue(3);
        check(cq.enQueue(1), true); cq.enQueue(2); cq.enQueue(3); check(cq.enQueue(4), false);
        check(cq.Rear(), 3); check(cq.isFull(), true); cq.deQueue(); cq.enQueue(4); check(cq.Rear(), 4); check(cq.Front(), 2);

        MinStackPairs m1 = new MinStackPairs(); MinStackEncoded m2 = new MinStackEncoded();
        for (int x : new int[]{-2, 0, -3}) { m1.push(x); m2.push(x); }
        check(m1.getMin(), -3); check(m2.getMin(), -3);
        m1.pop(); m2.pop();
        check(m1.top(), 0); check(m2.top(), 0); check(m1.getMin(), -2); check(m2.getMin(), -2);
        System.out.println("StackQueueDesign: all tests passed");
    }

    static void check(Object got, Object exp) {
        if (!got.equals(exp)) throw new AssertionError("expected " + exp + " but got " + got);
    }
}
