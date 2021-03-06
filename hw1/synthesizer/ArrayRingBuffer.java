package synthesizer;
import java.lang.reflect.Array;
import java.util.Iterator;

//Make sure to make this class and all of its methods public
//Make sure to make this class extend AbstractBoundedQueue<t>
public class ArrayRingBuffer<T> extends AbstractBoundedQueue<T> {
    /* Index for the next dequeue or peek. */
    private int first;            // index for the next dequeue or peek
    /* Index for the next enqueue. */
    private int last;
    /* Array for storing the buffer data. */
    private T[] rb;

    /**
     * Create a new ArrayRingBuffer with the given capacity.
     */
    public ArrayRingBuffer(int capacity) {
        // Create new array with capacity elements.
        // first, last, and fillCount should all be set to 0.
        // this.capacity should be set appropriately. Note that the local variable
        // here shadows the field we inherit from AbstractBoundedQueue, so
        // you'll need to use this.capacity to set the capacity.
        rb = (T[]) new Object[capacity];
        first = 0;
        last = 0;
        this.fillCount = 0;
        this.capacity = capacity;
    }

    /**
     * Adds x to the end of the ring buffer. If there is no room, then
     * throw new RuntimeException("Ring buffer overflow"). Exceptions
     * covered Monday.
     */
    public void enqueue(T x) {
        //Enqueue the item. Don't forget to increase fillCount and update last.
        if (isFull()) {
            throw new RuntimeException("Ring Buffer Overflow");
        }

        rb[last] = x;
        if (last == capacity - 1) {
            last = 0;
        } else {
            last += 1;
        }
        fillCount += 1;
    }

    /**
     * Dequeue oldest item in the ring buffer. If the buffer is empty, then
     * throw new RuntimeException("Ring buffer underflow"). Exceptions
     * covered Monday.
     */
    public T dequeue() {
        //Dequeue the first item. Don't forget to decrease fillCount and update
        if (isEmpty()) {
            throw new RuntimeException("Ring Buffer Underflow");
        }

        T firstitem = rb[first];
        if (first == capacity - 1) {
            first = 0;
        } else {
            first += 1;
        }
        fillCount -= 1;

        return firstitem;
    }

    /**
     * Return oldest item, but don't remove it.
     */
    public T peek() {
        //Return the first item. None of your instance variables should change.
        if (isEmpty()) {
            throw new RuntimeException("Ring Buffer Underflow");
        }
       return rb[first];
    }

    //When you get to part 5, implement the needed code to support iteration.
    public class Arrayiterator<T> implements Iterator<T> {
        private int wizPos;
        private int count;

        public Arrayiterator() {
            wizPos = first;
            count = 0;
        }

        @Override
        public boolean hasNext() {
            return count < fillCount;
        }

        @Override
        public T next() {
            T returnItem = (T) rb[wizPos]; //why cast?
            count += 1;
            if (wizPos == capacity - 1) {
                wizPos = 0;
            } else {
                wizPos += 1;
            }
            return returnItem;
        }
    }

    public Iterator<T> iterator() {
        return new Arrayiterator();
    }
}
