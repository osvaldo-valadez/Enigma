package enigma;

/** An alphabet of encodable characters.  Provides a mapping from characters
 *  to and from indices into the alphabet.
 *  @author Osvaldo Valadez
 */

class Alphabet {
    /** Creates a copy of the alphabet. */
    private String _chars;

    /** A new alphabet containing CHARS.  Character number #k has index
     *  K (numbering from 0). No character may be duplicated. */
    Alphabet(String chars) {
        for (int i = 0; i < chars.length(); i++) {
            for (int j = 0; j < chars.length(); j++) {
                if (chars.charAt(i) == chars.charAt(j) && i != j) {
                    throw new EnigmaException("Characters duplicated");
                }
            }
        }
        this._chars = chars;
    }

    /** A default alphabet of all upper-case characters. */
    Alphabet() {
        this("ABCDEFGHIJKLMNOPQRSTUVWXYZ");
    }

    /** Returns the size of the alphabet. */
    int size() {
        return this._chars.length();
    }

    /** Returns true if preprocess(CH) is in this alphabet. */
    boolean contains(char ch) {
        for (int i = 0; i < size(); i++) {
            if (this._chars.charAt(i) == ch) {
                return true;
            }
        }
        return false;
    }

    /** Returns character number INDEX in the alphabet, where
     *  0 <= INDEX < size(). */
    char toChar(int index) {
        if (index < 0 || index >= size()) {
            throw new EnigmaException("Index is out of range.");
        }
        return _chars.charAt(index);
    }

    /** Returns the index of character preprocess(CH), which must be in
     *  the alphabet. This is the inverse of toChar(). */
    int toInt(char ch) {
        int index = 0;
        for (int i = 0; i < size(); i++) {
            if (toChar(i) == ch) {
                index = i;
            }
        }
        return index;
    }
    /** Returns the char of what you are on @return String. */
    String chars() {
        return _chars;
    }

}
