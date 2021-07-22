public class Palindrome {
    public Deque<Character> wordToDeque(String word) {
        Deque<Character> wordstring = new LinkedListDeque<Character>(); //a bit weird

        for (int i = 0; i < word.length(); i += 1) {
            wordstring.addLast(word.charAt(i));
        }
        return wordstring;
    }

    /** Return true if the given word is a palindrome, and false otherwise */
    public boolean isPalindrome(String word) {
        int mid = word.length() / 2;
        for (int i = 0; i < mid; i += 1) {
            if (word.charAt(i) != word.charAt(word.length() - i - 1)) {
                return false;
            }
        }
        return true;
    }

    /** modify rules of equalChars */
    public boolean isPalindrome(String word, CharacterComparator cc) {
        int mid = word.length() / 2;
        for (int i = 0; i < mid; i += 1) {
            if (!cc.equalChars(word.charAt(i), word.charAt(word.length() - 1 - i))) {
                return false;
            }
        }
        return true;
    }
}
