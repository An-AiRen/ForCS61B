public class LinkedListDeque<T> {
    public class ItemNode {
        public ItemNode prev;
        public T item;
        public ItemNode next;

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
        size = 0;
    }

    /** Adds an item of type T to the front of the deque */
    public void addFirst(T item) {
        ItemNode p = new ItemNode(sentinel, item, sentinel.next);
        if (size == 0) {
            sentinel.next = p;
            sentinel.prev = p;
        } else {
            sentinel.next.prev = p;
            sentinel.next = sentinel.next.prev;
        }
        size += 1;
    }

    /** Adds an item of type T to the back of the deque */
    public void addLast(T item) {
        ItemNode p = new ItemNode(sentinel.prev, item, sentinel);
        if (size == 0) {
            sentinel.prev = p;
            sentinel.next = p;
        } else {
            sentinel.prev.next = p;
            sentinel.prev = sentinel.prev.next;
        }
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
        size -= 1;

        ItemNode first = sentinel.next;
        if (first == null) {
            return null;
        } else {
            sentinel.next = first.next;
            return first.item;
        }
    }

    /** Removes and returns the item at the back of the deque. if no such item, return null */
    public T removeLast() {
        size += 1;

        ItemNode last = sentinel.prev;
        if (last == null) {
            return null;
        } else {
            sentinel.prev = last.prev;
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
