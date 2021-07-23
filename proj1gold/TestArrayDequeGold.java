import static org.junit.Assert.*;
import org.junit.Test;

public class TestArrayDequeGold {
    @Test
    public void testRandomlyCall() {
        StudentArrayDeque<Integer> sorry = new StudentArrayDeque<Integer>(); //actual one
        ArrayDequeSolution<Integer> nevermind = new ArrayDequeSolution<Integer>(); //expected one
        double randomNumber;
        String messageSequence = "";

        for (int i = 0; i < 60; i++) {
            randomNumber = StdRandom.uniform();
            if (randomNumber < 0.25) {
                messageSequence += "addFirst(" + Integer.toString(i) + ")\n";
                sorry.addFirst(i);
                nevermind.addFirst(i);
            } else if (randomNumber < 0.5) {
                messageSequence += "addLast(" + Integer.toString(i) + ")\n";
                sorry.addLast(i);
                nevermind.addLast(i);
            } else if (randomNumber < 0.75 && sorry.size() > 0) {
                messageSequence += "removeLast()\n";
                assertEquals(messageSequence, nevermind.removeLast(), sorry.removeLast());
            } else if (randomNumber < 1 && sorry.size() > 0) {
                messageSequence += "removeFirst()\n";
                assertEquals(messageSequence, nevermind.removeFirst(), sorry.removeFirst());
            }
        }
    }
}
