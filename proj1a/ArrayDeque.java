public class ArrayDeque<T> {
    private T[] items;
    private int size;
    private int start; // to store start position
    private int end;  //to store last position

    public ArrayDeque() {
        items = (T[]) new Object[8]; //a bit weird i have to say
        size = 0;
        start = 4;
        end = 3;
    }

    /** to compute array indices according to given index */
    private int minusOne(int index) {
        int pos;
        if (start + index >= items.length) {
            pos = index + start - items.length;
        } else {
            pos = start + index;
        }
        return pos;
    }

    /** Resizes the underlying array to the target capacity. That's tricky! */
    private void resize(int capacity) {
        T[] a = (T[]) new Object[capacity];

        if (size <= capacity - start) {
            System.arraycopy(items, start, a, start, items.length - start);
            System.arraycopy(items, 0, a, items.length, end + 1);
            end = start + size - 1;
        } else {
            System.arraycopy(items, start, a, start, items.length - start);
            System.arraycopy(items, 0, a, items.length, capacity - items.length);
            System.arraycopy(items, capacity - items.length, a, 0, end - capacity + items.length + 1);
            end = size + start - capacity;
        }

        items = a;
    }

    /** to adjust the usage factor of the array */
    private  void  adjustUsageFactor() {
        /** there is no need to adjust if length is less than 16 */
        if (items.length < 16) {
            return;
        } else {
            double f = (double)size / items.length;
            if (f < 0.25) {
                resize(2 * size);                    //need to be adjusted
            }
        }
    }

    /** Adds an item of type T to the front of the deque */
    public void addFirst(T item) {
        if (size == items.length) {
            resize(size * 2);
        }

        if (start == 0) {
            start = items.length - 1;
        } else {
            start -= 1;
        }
        items[start] = item;
        size += 1;
    }

    /** Adds an item of type T to the back of the deque */
    public void addLast(T item) {
        if (size == items.length) {
            resize(size * 2);
        }

        if (end == items.length - 1) {
            end = 0;
        } else {
            end += 1;
        }
        items[end] = item;
        size += 1;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }
    public void printDeque() {
        if (start <= end) {
            for (int i = start; i <= end; i++) {
                System.out.print(items[i] + " ");
            }
        } else {
            for (int i = start; i < items.length; i++) {
                System.out.print(items[i] + " ");
            }
            for (int i = 0; i <= end; i++) {
                System.out.print(items[i] + " ");
            }
        }
    }

    public T removeFirst() {
        T first = items[start];
        items[start] = null;

        if (start == items.length - 1) {
            start = 0;
        } else {
            start += 1;
        }
        size -= 1;
        adjustUsageFactor();
        return first;
    }

    public T removeLast() {
        T last = items[end];
        items[end] = null;

        if (end == 0) {
            end = items.length - 1;
        } else {
            end -= 1;
        }
        size -= 1;
        adjustUsageFactor();
        return last;
    }

    public T get(int index) {
        if (index >= size) {
            return null;
        }

        int pos = minusOne(index);
        if (pos > end) {
            return null;
        } else {
            return items[pos];
        }
    }
}

