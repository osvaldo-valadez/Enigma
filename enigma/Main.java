package enigma;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.regex.Pattern;

import static enigma.EnigmaException.*;

/** Enigma simulator.
 *  @author Osvaldo Valadez
 */
public final class Main {

    /** Process a sequence of encryptions and decryptions, as
     *  specified by ARGS, where 1 <= ARGS.length <= 3.
     *  ARGS[0] is the name of a configuration file.
     *  ARGS[1] is optional; when present, it names an input file
     *  containing messages.  Otherwise, input comes from the standard
     *  input.  ARGS[2] is optional; when present, it names an output
     *  file for processed messages.  Otherwise, output goes to the
     *  standard output. Exits normally if there are no errors in the input;
     *  otherwise with code 1. */
    public static void main(String... args) {
        try {
            new Main(args).process();
            return;
        } catch (EnigmaException excp) {
            System.err.printf("Error: %s%n", excp.getMessage());
        }
        System.exit(1);
    }

    /** Check ARGS and open the necessary files (see comment on main). */
    Main(String[] args) {
        if (args.length < 1 || args.length > 3) {
            throw error("Only 1, 2, or 3 command-line arguments allowed");
        }

        _config = getInput(args[0]);

        if (args.length > 1) {
            _input = getInput(args[1]);
        } else {
            _input = new Scanner(System.in);
        }

        if (args.length > 2) {
            _output = getOutput(args[2]);
        } else {
            _output = System.out;
        }

    }

    /** Return a Scanner reading from the file named NAME. */
    private Scanner getInput(String name) {
        try {
            return new Scanner(new File(name));
        } catch (IOException excp) {
            throw error("could not open %s", name);
        }
    }

    /** Return a PrintStream writing to the file named NAME. */
    private PrintStream getOutput(String name) {
        try {
            return new PrintStream(new File(name));
        } catch (IOException excp) {
            throw error("could not open %s", name);
        }
    }

    /** Configure an Enigma machine from the contents of configuration
     *  file _config and apply it to the messages in _input, sending the
     *  results to _output. */
    private void process() {
        Machine enigma = readConfig();
        String next = _input.nextLine();
        while (_input.hasNext()) {
            String setting = next;
            if (!setting.contains("*")) {
                throw new EnigmaException("Wrong setting format");
            }
            setUp(enigma, setting);
            next = (_input.nextLine());
            while (next.isEmpty()) {
                next = " ";
            }
            while (!(next.contains("*"))) {
                String result = enigma.convert(next.replaceAll(" ", "")
                        .toUpperCase());
                if (next.isEmpty()) {
                    _output.println();
                } else {
                    printMessageLine(result);
                }
                if (!_input.hasNext()) {
                    next = "*";
                } else {
                    next = (_input.nextLine()).toUpperCase();
                }
            }
        }
    }



    /** Copy of numrotors. */
    private int numRotorss;

    /** Copy of numpawlss. */
    private int numPawlss;

    /** Return an Enigma machine configured from the contents of configuration
     *  file _config. */
    private Machine readConfig() {
        try {
            String letters = _config.next();
            _alphabet = new Alphabet(letters);
            numRotorss = _config.nextInt();
            numPawlss = _config.nextInt();
            ArrayList<Rotor> rotorArrayList = new ArrayList<>();
            while (_config.hasNext()) {
                rotorArrayList.add(readRotor());
            }
            return new Machine(_alphabet, numRotorss,
                    numPawlss, rotorArrayList);
        } catch (NoSuchElementException excp) {
            throw error("configuration file truncated");
        }
    }
    /** Copy of returnRotor. */
    private Rotor returnRotor;

    /** Copy of first. */
    private char first;
    /** Return a rotor, reading its description from _config. */
    private Rotor readRotor() {
        try {
            String rotorName = _config.next().toUpperCase();
            String settings = _config.next().toUpperCase();
            String notchess = "";
            for (int i = 0; i < settings.length(); i++) {
                if (i == 0) {
                    first = settings.charAt(0);
                } else {
                    notchess += settings.charAt(i);
                }
            }
            String cycles = "";

            while (_config.hasNext("[(].*") && _config.hasNext()) {
                cycles += _config.next();
            }
            Permutation rotorPerm = new Permutation(cycles, _alphabet);
            if (first == 'M') {
                returnRotor = new MovingRotor(rotorName, rotorPerm, notchess);
            } else if (first == 'N') {
                returnRotor = new FixedRotor(rotorName, rotorPerm);
            } else if (first == 'R') {
                returnRotor = new Reflector(rotorName, rotorPerm);
            }
            return returnRotor;
        } catch (NoSuchElementException excp) {
            throw error("bad rotor description");
        }

    }

    /** Set M according to the specification given on SETTINGS,
     *  which must have the format specified in the assignment. */
    private void setUp(Machine M, String settings) {
        Scanner settingsFirst = new Scanner(settings);
        Pattern r = Pattern.compile("[*]");
        if (!settingsFirst.hasNext(r)) {
            throw new AssertionError("Not a correct setting statement");
        }
        settingsFirst.next();
        List<String> futureRotorArray = new ArrayList<>();
        for (int i = 0; i < M.numRotors(); i++) {
            futureRotorArray.add(settingsFirst.next().toUpperCase());
        }
        String[] rotorArray = new String[futureRotorArray.size()];
        for (int i = 0; i < futureRotorArray.size(); i++) {
            rotorArray[i] = futureRotorArray.get(i);
        }
        String set = settingsFirst.next();
        String futurePlug = "";
        while (settingsFirst.hasNext()) {
            futurePlug += settingsFirst.next();
        }
        if (!futurePlug.equals("")) {
            Permutation plug = new Permutation(futurePlug, _alphabet);
            M.insertRotors(rotorArray);
            M.setRotors(set);
            M.setPlugboard(plug);
        } else {
            M.insertRotors(rotorArray);
            M.setRotors(set);
        }


    }

    /** Print MSG in groups of five (except that the last group may
     *  have fewer letters). */
    private void printMessageLine(String msg) {
        for (int i = 0; i < msg.length(); i += 5) {
            if (msg.length() - i <= 5) {
                _output.println(msg.substring(i, msg.length()) + " ");
            } else {
                _output.print(msg.substring(i, i + 5) + " ");
            }
        }
    }

    /** Alphabet used in this machine. */
    private Alphabet _alphabet;

    /** Source of input messages. */
    private Scanner _input;

    /** Source of machine configuration. */
    private Scanner _config;

    /** File for encoded/decoded messages. */
    private PrintStream _output;

    /** Function to return numrotorss @return int. */
    int getNumRotorss() {
        return numRotorss;
    }

    /** Function to return numpawlss. @return int. */
    int getNumPawlss() {
        return numPawlss;
    }

    /** returns Copy of returnRotor @return Rotor. */
    Rotor getReturnRotor() {
        return returnRotor;
    }

    /** Copy of first @return char. */
    char getFirst() {
        return first;
    }

    /** A String containing cycles which readRotor() appends to. */
    private String perm;

    /** Temporary string that is set to NEXT token of _config. */
    private String temp;

    /** Type and notches of current rotor. */
    private String notches;
}
