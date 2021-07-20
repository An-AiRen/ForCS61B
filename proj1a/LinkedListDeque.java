public class LinkedListDeque<T> {
    public class ItemNode {
        private ItemNode prev;
        private T item;
        private ItemNode next;

        public ItemNode(ItemNode p, T i, ItemNode n) {
            prev = p;
            item = i;
            next = n;
        }
    }

    private ItemNode sentinel;
    private int size;

    public LinkedListDeque() {
        sentinel = new ItemNode(null, null, null);
        sentinel.prev = sentinel;
        sentinel.next = sentinel;

        size = 0;
    }

    /** Adds an item of type T to the front of the deque */
    public void addFirst(T item) {
        ItemNode p = new ItemNode(sentinel, item, sentinel.next);

        sentinel.next.prev = p;
        sentinel.next = sentinel.next.prev;

        size += 1;
    }

    /** Adds an item of type T to the back of the deque */
    public void addLast(T item) {
        ItemNode p = new ItemNode(sentinel.prev, item, sentinel);

        sentinel.prev.next = p;
        sentinel.prev = sentinel.prev.next;

        size += 1;
    }

    /** Returns true if deque is empty, false otherwise */
    public boolean isEmpty() {
        return size == 0;
    }

    /** Returns the number of items in the deque */
    public int size() {
        return size;
    }

    /** Prints the items in the deque from first to last, seperated by space */
    public void printDeque() {
        ItemNode p = sentinel.next;

        /** cannot use while! because this is a circular deque! */
        for (int i = 0; i < size; i++) {
            System.out.print(p.item + " ");
            p = p.next;
        }
    }

    /** Removes and returns the item at the front of the deque. if no such item, return null */
    public T removeFirst() {
        ItemNode first = sentinel.next;
        if (first == sentinel) {
            return null;
        } else {
            first.next.prev = sentinel;
            sentinel.next = first.next;
            size -= 1;
            return first.item;
        }
    }

    /** Removes and returns the item at the back of the deque. if no such item, return null */
    public T removeLast() {
        ItemNode last = sentinel.prev;
        if (last == sentinel) {
            return null;
        } else {
            last.prev.next = sentinel;
            sentinel.prev = last.prev;
            size -= 1;
            return last.item;
        }
    }

    /** Gets the item at the given index. if no such item, return null. Cannot alter the deque! */
    public T get(int index) {
        ItemNode p = sentinel;

        for (int i = 0; i <= index; i++) {
            p = p.next;
            if (p == null) {
                return null;
            }
        }
        return p.item;
    }

    private T getNode(ItemNode p, int curr, int dest) {
        if (p == null) {
            return null;
        }
        if (curr == dest) {
            return p.item;
        }
        return getNode(p.next, curr + 1, dest);
    }
    /** Same as get, but use recursion */
    public T getRecursive(int index) {
        return getNode(sentinel.next, 0, index);
    }
}

