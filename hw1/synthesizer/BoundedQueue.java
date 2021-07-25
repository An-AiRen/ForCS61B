package synthesizer;
import java.util.Iterator;

public interface BoundedQueue<T> extends Iterable<T> {
    /** Return size of buffer */
    int capacity();

    /** Rrturn number of items currently in the buffer */
    int fillCount();

    /** add item x to the end */
    void enqueue(T x);

    /** delete and return item from the front */
    T dequeue();

    /** Return(but do not delete) item from the front */
    T peek();

    /** Is the buffer empty? */
    default boolean isEmpty() {
        return fillCount() == 0;
    }

    /** Is the buffer full? */
    default boolean isFull() {
        return fillCount() == capacity();
    }

    public Iterator<T> iterator();
}
