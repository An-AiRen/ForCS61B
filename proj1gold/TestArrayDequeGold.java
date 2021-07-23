import static org.junit.Assert.*;
import org.junit.Test;

public class TestArrayDequeGold {
    @Test
    public void testRandomlyCall() {
        StudentArrayDeque<Integer> sorry = new StudentArrayDeque<Integer>(); //actual one
        ArrayDequeSolution<Integer> nevermind = new ArrayDequeSolution<Integer>(); //expected one
        int randomNumber;
        String messageSequence = "";

        for (int i = 0; i < 500; i++) {
            randomNumber = StdRandom.uniform(6);
            if (randomNumber == 0) {
                messageSequence += "addFirst(" + Integer.toString(i) + ")\n";
                sorry.addFirst(i);
                nevermind.addFirst(i);
            } else if (randomNumber == 1) {
                messageSequence += "addLast(" + Integer.toString(i) + ")\n";
                sorry.addLast(i);
                nevermind.addLast(i);
            } else if (randomNumber == 2 && sorry.size() > 0) {
                messageSequence += "removeLast()\n";
                assertEquals(messageSequence, nevermind.removeLast(), sorry.removeLast());
            } else if (randomNumber == 3 && sorry.size() > 0) {
                messageSequence += "removeFirst()\n";
                assertEquals(messageSequence, nevermind.removeFirst(), sorry.removeFirst());
            } else if (randomNumber == 4 && sorry.size() > 0) {
                int ranIndex = StdRandom.uniform(sorry.size());
                messageSequence += "get(" + Integer.toString(ranIndex) + ")\n";
                assertEquals(messageSequence, nevermind.get(ranIndex), sorry.get(ranIndex));
            } else {
                messageSequence += "size()\n";
                assertEquals(messageSequence, nevermind.size(), sorry.size());
            }
        }
    }
}
