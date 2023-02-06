package enigma;

import static enigma.EnigmaException.*;

/** Superclass that represents a rotor in the enigma machine.
 *  @author Osvaldo Valadez
 */

class Rotor {
    /** Copy of position. */
    private int position;

    /** A rotor named NAME whose permutation is given by PERM. */
    Rotor(String name, Permutation perm) {
        _name = name;
        _permutation = perm;
        position = 0;
    }

    /** Return my name. */
    String name() {
        return _name;
    }

    /** Return my alphabet. */
    Alphabet alphabet() {
        return _permutation.alphabet();
    }

    /** Return my permutation. */
    Permutation permutation() {
        return _permutation;
    }

    /** Return the size of my alphabet. */
    int size() {
        return _permutation.alphabet().size();
    }

    /** Return true iff I have a ratchet and can move. */
    boolean rotates() {
        return false;
    }

    /** Return true iff I reflect. */
    boolean reflecting() {
        return false;
    }

    /** Return my current setting. */
    int setting() {
        return position;
    }

    /** Set setting() to POSN.  */
    void set(int posn) {
        position = posn;
    }

    /** Set setting() to character CPOSN. */
    void set(char cposn) {
        position = alphabet().toInt(cposn);
    }

    /** Return the conversion of P (an integer in the range 0..size()-1)
     *  according to my permutation. */
    int convertForward(int p) {
        int preperm = permutation().wrap(position + p);
        int permss =  alphabet().toInt((char) permutation()
                .getPermutationMap().get(alphabet().toChar(preperm)));
        return permutation().wrap(permss - position);

    }
    /**Copy of perms. */
    private int perms;
    /** Return the conversion of E (an integer in the range 0..size()-1)
     *  according to the inverse of my permutation. */
    int convertBackward(int e) {
        char preperm = alphabet().toChar(permutation().wrap(position + e));
        for (Object i: permutation().getPermutationMap().keySet()) {
            if ((char) permutation().getPermutationMap().get(i) == preperm) {
                perms = alphabet().toInt((char) i);
            }

        }
        return permutation().wrap(perms - position);

    }

    /** Returns true iff I am positioned to allow the rotor to my left
     *  to advance. */
    boolean atNotch() {
        return false;
    }

    /** Advance me one position, if possible. By default, does nothing. */
    void advance() {
    }

    @Override
    public String toString() {
        return "Rotor " + _name;
    }

    /** My name. */
    private final String _name;

    /** The permutation implemented by this rotor in its 0 position. */
    private Permutation _permutation;

    /** get copy of _permutation @return Permutation. */
    Permutation getPermutation() {
        return _permutation;
    }

    /** Get a copy of position @return int. */
    int getPosition() {
        return position;
    }
    /** Get a copy of perm @return int.*/
    int getPerms() {
        return perms;
    }
}
