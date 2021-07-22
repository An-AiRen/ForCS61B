public class OffByN implements CharacterComparator{
    private int offset;

    public offByN(int N) {
        offset = N;
    }

    @Override
    public boolean equalChars(char x, char y) {
        return x - y == offset || y - x == offset;
    }
}
