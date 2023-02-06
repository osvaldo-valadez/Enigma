package enigma;


import java.util.HashMap;
import java.util.Collection;
import java.util.HashSet;

/** Class that represents a complete enigma machine.
 *  @author Osvaldo Valadez
 */
class Machine {
    /** Create a copy of the number of rotors. */
    private int _numRotors;

    /** Create a copy of the number of pawls. */
    private int _pawls;

    /** Create a copy of the collection of rotors. */
    private Collection<Rotor> _allRotors;

    /** Create a copy of the array of rotors. */
    private Rotor[] _rotors;

    /** Create a copy of the trackmap. */
    private HashMap<String, Rotor> _trackmap;

    /**
     * A new Enigma machine with alphabet ALPHA, 1 < NUMROTORS rotor slots,
     * and 0 <= PAWLS < NUMROTORS pawls.  ALLROTORS contains all the
     * available rotors.
     */
    Machine(Alphabet alpha, int numRotors, int pawls,
            Collection<Rotor> allRotors) {
        _alphabet = alpha;
        _numRotors = numRotors;
        _pawls = pawls;
        _allRotors = allRotors;

    }

    /**
     * Return the number of rotor slots I have.
     */
    int numRotors() {
        return _numRotors;
    }

    /**
     * Return the number pawls (and thus rotating rotors) I have.
     */
    int numPawls() {
        return _pawls;
    }

    /**
     * Set my rotor slots to the rotors named ROTORS from my set of
     * available rotors (ROTORS[0] names the reflector).
     * Initially, all rotors are set at their 0 setting.
     */
    void insertRotors(String[] rotors) {
        _rotors = new Rotor[_numRotors];
        _trackmap = new HashMap<>();
        for (Rotor x : _allRotors) {
            String name = x.name();
            _trackmap.put(name, x);
        }
        for (int x = 0; x < numRotors(); x++) {
            String val = rotors[x];
            if (_trackmap.containsKey(val.toUpperCase())) {
                _rotors[x] = _trackmap.get(val);
            }
        }
    }

    /**
     * Set my rotors according to SETTING, which must be a string of
     * numRotors()-1 characters in my alphabet. The first letter refers
     * to the leftmost rotor setting (not counting the reflector).
     */
    void setRotors(String setting) {
        if (setting.length() != numRotors() - 1) {
            throw new AssertionError("Not proper setting");
        }
        for (int i = 1; i < numRotors(); i++) {
            _rotors[i].set(setting.charAt(i - 1));
        }
    }
    /** Copy of plugboard. */
    private Permutation _plugboard;

    /**
     * Set the plugboard to PLUGBOARD.
     */
    void setPlugboard(Permutation plugboard) {
        _plugboard = plugboard;

    }

    /**
     * Returns the result of converting the input character C (as an
     * index in the range 0..alphabet size - 1), after first advancing
     * the machine.
     */
    int convert(int c) {
        /** Advancing the machine */

        /** Start from the right and move the setting forward and move left */
        move();
        for (Rotor i : iNeedAderall) {
            i.advance();
        }
        if (_plugboard != null) {
            c = _plugboard.permute(_plugboard.wrap(c));
        }
        for (int x = _rotors.length - 1; x >= 0; x--) {
            c = _rotors[x].convertForward(c);
        }
        for (int i = 1; i < _rotors.length; i += 1) {
            c = _rotors[i].convertBackward(c);
        }
        if (_plugboard != null) {
            c = _plugboard.permute(_plugboard.wrap(c));
        }
        return c;
    }

    /**
     * Returns the encoding/decoding of MSG, updating the state of
     * the rotors accordingly.
     */
    String convert(String msg) {
        char[] message = new char[msg.length()];
        for (int i = 0; i < msg.length(); i++) {
            char letter = msg.charAt(i);
            int after = convert(_alphabet.toInt(letter));
            message[i] =  _alphabet.toChar(after);
        }
        return new String(message);
    }

    /**
     * Common alphabet of my rotors.
     */
    private final Alphabet _alphabet;

    /** Return a copy of my hashset. */
    private HashSet<Rotor> iNeedAderall;

    /** Move the rotors. */
    private void move() {
        iNeedAderall = new HashSet<Rotor>();
        for (int x = numRotors() - 1; x > 0; x--) {
            if (x == numRotors() - 1) {
                iNeedAderall.add(_rotors[x]);
            } else if (x == numRotors() - 2
                    && _rotors[x].rotates() && _rotors[x + 1].atNotch()) {
                iNeedAderall.add(_rotors[x]);
            } else {
                if (_rotors[x].rotates() && _rotors[x + 1].atNotch()) {
                    iNeedAderall.add(_rotors[x]);
                    iNeedAderall.add(_rotors[x + 1]);
                }
            }
        }
    }
    /** Return the number of rotors @return int. */
    int getNumRotors() {
        return _numRotors;
    }
    /** Return the number of pawls @return int. */
    int getNumPawls() {
        return _pawls;
    }

    /** Return the number of pawls @return Collection<Rotors>. */
    Collection<Rotor> getAllRotors() {
        return _allRotors;
    }
    /** Return rotors @return Rotor[]. */
    Rotor[] getRotors() {
        return _rotors;
    }
    /** Return the number of pawls @return Hashmap<String,Rotor>. */
    HashMap<String, Rotor> getTrackmap() {
        return _trackmap;
    }
    /** Return the plugboard @return Permutation. */
    Permutation getPlugboard() {
        return _plugboard;
    }
    /** Return the @return Hashset<Rotor>. */
    HashSet<Rotor> getHashset() {
        return iNeedAderall;
    }
}
