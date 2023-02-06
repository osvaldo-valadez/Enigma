package enigma;

import static enigma.EnigmaException.*;

/** Class that represents a rotating rotor in the enigma machine.
 *  @author Osvaldo Valadez
 */
class MovingRotor extends Rotor {
    /** Copy of notches. */
    private String _notches;

    /** Get _notches @return String. */
    String getNotches() {
        return _notches;
    }

    /** A rotor named NAME whose permutation in its default setting is
     *  PERM, and whose notches are at the positions indicated in NOTCHES.
     *  The Rotor is initally in its 0 setting (first character of its
     *  alphabet).
     */
    MovingRotor(String name, Permutation perm, String notches) {
        super(name, perm);
        _notches = notches;

    }

    @Override
    boolean rotates() {
        return true;
    }

    @Override
    boolean atNotch() {
        for (int x = 0; x < _notches.length(); x++) {
            if (alphabet().toInt(_notches.charAt(x)) == this.setting()) {
                return true;
            }
        }
        return false;

    }

    @Override
    void advance() {
        if (rotates()) {
            this.set(getPermutation().wrap(this.setting() + 1));
        }
    }


}
