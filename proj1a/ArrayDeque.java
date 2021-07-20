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

    /** to minus the index of array. if current index is 0, index minusOne should be items.length - 1. */
    private int minusOne(int index) {
        if (index == 0) {
            index = items.length - 1;
        } else {
            index -= 1;
        }
        return index;
    }

    private int minusOne(int index, int length) {
        if (index == 0) {
            index = length - 1;
        } else {
            index -= 1;
        }
        return index;
    }
    /** to add the index of array. if current index is length - 1, index plusOne should be 0. */
    private int plusOne(int index) {
        if (index == items.length - 1) {
            index = 0;
        } else {
            index += 1;
        }
        return index;
    }

    private  int plusOne(int index, int length) {
        if (index == length - 1) {
            index = 0;
        } else {
            index += 1;
        }
        return index;
    }

    /** Resizes the underlying array to the target capacity. That's tricky! */
    private void resize(int capacity) {
        T[] a = (T[]) new Object[capacity];
        int nindex = start;

        for (int i = start; i != end; i = plusOne(i)) {
            a[nindex] = items[i];
            nindex = plusOne(nindex, capacity);
        }
        a[nindex] = items[end];
        end = nindex;
        items = a;
    }

    /** to adjust the usage factor of the array */
    private  void  adjustUsageFactor() {
        /** there is no need to adjust if length is less than 16 */
        if (items.length < 16) {
            return;
        } else {
            if (size < items.length / 4) {
                resize(2 * size);
            }
        }
    }

    /** Adds an item of type T to the front of the deque */
    public void addFirst(T item) {
        if (size == items.length) {
            resize(size * 2);
        }

        start = minusOne(start);
        items[start] = item;
        size += 1;
    }

    /** Adds an item of type T to the back of the deque */
    public void addLast(T item) {
        if (size == items.length) {
            resize(size * 2);
        }

        end = plusOne(end);
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
        for (int i = start; i != end; i = plusOne(i)) {
            System.out.print(items[i] + " ");
        }
        System.out.println();
    }

    public T removeFirst() {
        if (size == 0) {
            return null;
        }

        T first = items[start];

        items[start] = null;
        start = plusOne(start);
        size -= 1;

        adjustUsageFactor();
        return first;
    }

    public T removeLast() {
        if (size == 0) {
            return null;
        }

        T last = items[end];

        items[end] = null;
        end = minusOne(end);
        size -= 1;

        adjustUsageFactor();
        return last;
    }

    public T get(int index) {
        if (index >= size || index < 0) {
            return null;
        }

        int i;
        int count = 0;

        for (i = start; i != end && count < index; i = plusOne(i)) {
            count += 1;
        }

        return items[i];
    }
}

