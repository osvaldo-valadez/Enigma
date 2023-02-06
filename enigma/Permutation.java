package enigma;

import java.util.HashMap;

import static enigma.EnigmaException.*;

/** Represents a permutation of a range of integers starting at 0 corresponding
 *  to the characters of an alphabet.
 *  @author Osvaldo Valadez
 */
public class Permutation {

    /** Set this to the cycles when you initialize. */
    private String _cycles;

    /** Set this to the permutation map you create. */
    private HashMap _permutationMap;

    /** Set this Permutation to that specified by CYCLES, a string in the
     *  form "(cccc) (cc) ..." where the c's are characters in ALPHABET, which
     *  is interpreted as a permutation in cycle notation.  Characters in the
     *  alphabet that are not included in any cycle map to themselves.
     *  Whitespace is ignored. */
    Permutation(String cycles, Alphabet alphabet) {
        _cycles = cycles;
        _alphabet = alphabet;
        if (!checkParenthesis(cycles)) {
            throw new EnigmaException("Not a proper cycle");
        }
        char start = ' ';
        HashMap<Character, Character> permutationMap = new HashMap<>();
        _permutationMap = permutationMap;
        for (int i = 0; i < cycles.length(); i++) {
            if (cycles.charAt(i) != '('
                    && cycles.charAt(i) != ')' && cycles.charAt(i) != ' ') {
                if (cycles.charAt(i - 1) == '(') {
                    start = cycles.charAt(i);
                }
                if (cycles.charAt(i + 1) != ')') {
                    permutationMap.put(cycles.charAt(i),
                            cycles.charAt(i + 1));
                } else {
                    permutationMap.put(cycles.charAt(i), start);
                }
            }
        }
        for (int i = 0; i < alphabet.size(); i++) {
            permutationMap.putIfAbsent(alphabet.toChar(i), alphabet.toChar(i));
        }
    }

    /** Add the cycle c0->c1->...->cm->c0 to the permutation, where CYCLE is
     *  c0c1...cm. */
    private void addCycle(String cycle) {
        this._cycles += cycle;
    }

    /** Return the value of P modulo the size of this permutation. */
    final int wrap(int p) {
        int r = p % size();
        if (r < 0) {
            r += size();
        }
        return r;
    }

    /** Returns the size of the alphabet I permute. */
    int size() {
        return _alphabet.size();
    }

    /** Return the result of applying this permutation to P modulo the
     *  alphabet size. */
    int permute(int p) {
        int index = wrap(p);
        char a = _alphabet.toChar(index);
        char returnvalue = (char) _permutationMap.get(a);
        return _alphabet.toInt(returnvalue);
    }

    /** Return the result of applying the inverse of this permutation
     *  to  C modulo the alphabet size. */
    int invert(int c) {
        Object tocharresult = (Object) _alphabet.toChar(c);
        for (Object i: _permutationMap.keySet()) {
            char copy = (char) i;
            if (_permutationMap.get(i) == tocharresult) {
                return _alphabet.toInt(copy);
            }
        }
        return c;
    }

    /** Return the result of applying this permutation to the index of P
     *  in ALPHABET, and converting the result to a character of ALPHABET. */
    char permute(char p) {
        int convert = _alphabet.toInt(p);
        int index = wrap(convert);
        char a = _alphabet.toChar(index);
        return (char) _permutationMap.get(a);
    }

    /** Return the result of applying the inverse of this permutation to C. */
    char invert(char c) {
        for (Object i: _permutationMap.keySet()) {
            char copy = (char) i;
            if (_permutationMap.get(i) == (Object) c) {
                return copy;
            }
        }
        return c;
    }

    /** Return the alphabet used to initialize this Permutation. */
    Alphabet alphabet() {
        return _alphabet;
    }

    /** Return the cycles of this permutation. */
    String getCycles() {
        return _cycles;
    }

    /** Return the permutation map of this permutation. */
    HashMap getPermutationMap() {
        return _permutationMap;
    }

    /** Return true iff this permutation is a derangement (i.e., a
     *  permutation for which no value maps to itself). */
    boolean derangement() {
        if (_permutationMap.get(this) == this) {
            return false;
        }
        return true;
    }

    /** Alphabet of this permutation. */
    private Alphabet _alphabet;

    /** Check that parenthesis @param s match @return boolean. */
    boolean checkParenthesis(String s) {
        boolean check;
        int open = 0;
        int closed = 0;
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == '(') {
                open += 1;
            }
            if (s.charAt(i) == ')') {
                closed += 1;
            }
        }
        if (open == closed) {
            check = true;
        } else {
            check = false;
        }
        return check;
    }

    /** Check that elements in @param cycle
     * and @param alphabet are alphabet @return boolean. */
    public boolean cycleInAlphabet(String cycle, Alphabet alphabet) {
        for (int i = 0; i < cycle.length(); i++) {
            Boolean check = false;
            for (int j = 0; j < alphabet.size(); j++) {
                if (cycle.charAt(i) != ' ' && cycle.charAt(i) != '('
                        && cycle.charAt(i) != ')'
                        && cycle.charAt(i) == alphabet.toChar(j)) {
                    check = true;
                }
            }
            if (!check) {
                return false;
            }
        }
        return true;
    }
    /** Make sure that the elements in @param cycle
     * don't repeat @return boolean. */
    public boolean checkRepeats(String cycle) {
        boolean repeated = false;
        for (int i = 0; i < cycle.length(); i++) {
            for (int j = 0; j < cycle.length(); i++) {
                if (cycle.charAt(i) != ' ' && cycle.charAt(i) != '('
                        && cycle.charAt(i) != ')' && cycle.charAt(j) != ' '
                        && cycle.charAt(j) != '('
                        && cycle.charAt(j) != ')') {
                    if (cycle.charAt(i) == cycle.charAt(j) && i != j) {
                        repeated = true;
                    }
                }
            }
            if (repeated) {
                return true;
            }
        }
        return false;
    }
}
