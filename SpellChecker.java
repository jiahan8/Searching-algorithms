package edu.iastate.cs228.hw4;
/**
 * @author Jia Han Tan
 */
import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class SpellChecker {

    /**
     * Displays usage information.
     *
     * There's no reason that you should need to modify this.
     */
    private static void doUsage() {
        System.out.println("Usage: SpellChecker [-i] <dictionary> <document>\n"
                         + "                    -d <dictionary>\n"
                         + "                    -h");
    }

    /**
     * Displays detailed usage information and exits.
     *
     * There's no reason that you should need to modify this.
     */
    private static void doHelp() {
        doUsage();
        System.out.println("\n"                                                                                +
                           "When passed a dictionary and a document, spell check the document.  Optionally,\n" +
                           "the switch -n toggles non-interactive mode; by default, the tool operates in\n"   +
                           "interactive mode.  Interactive mode will write the corrected document to disk,\n"  +
                           "backing up the uncorrected document by concatenating a tilde onto its name.\n\n"   +
                           "The optional -d switch with a dictionary parameter enters dictionary edit mode.\n" +
                           "Dictionary edit mode allows the user to query and update a dictionary.  Upon\n"    +
                           "completion, the updated dictionary is written to disk, while the original is\n"    +
                           "backed up by concatenating a tilde onto its name.\n\n"                             +
                           "The switch -h displays this help and exits.");
        System.exit(0);
    }

    /**
     * Runs the three modes of the SpellChecker based on the input arguments. DO
     * NOT change this method in any way other than to set the name and sect
     * variables.
     */
    public static void main(String[] args) {
        if (args.length == 0) {
            doUsage();
            System.exit(-1);
        }

        /* In order to be considered for the competition, set these variables. */
        String name = "Jia Han Tan"; // First and Last
        String sect = "C"; // "A" or "B"

        Timer timer = new Timer();

        timer.start();

        if (args[0].equals("-h"))
            doHelp();
        else if (args[0].equals("-n"))
            doNonInteractiveMode(args);
        else if (args[0].equals("-d"))
            doDictionaryEditMode(args);
        else
            doInteractiveMode(args);

        timer.stop();

        System.out.println("Student name:   " + name);
        System.out.println("Student sect:   " + sect);
        System.out.println("Execution time: " + timer.runtime() + " ms");
    }

    /**
     * Carries out the Interactive mode of the Spell Checker.
     * 
     * @param args
     *            the arguments given to the main. The correct number of
     *            arguments may or may not be contained in it.
     *            Call doUsage() and exit if the parameter count is incorrect.
     */
    public static void doInteractiveMode(String[] args) { // interactive
		Scanner input = new Scanner(System.in);
    	if (args.length != 3) {
			doUsage();
			return;
		}
		Dictionary dictionary = loadDictionary(args[1]);
		String[] file = loadFile(args[2]);
		
		ErrorMap map = new ErrorMap(file.length);
		
		for (int line = 0; line < file.length; line++) {
			new ErrorFinder(line, line + 1, file, dictionary, map);
			System.out.print(getErrors(file, line, map));

			while (map.getErrors(line) != null) {
				int character = map.getErrors(line).get(0);
				String word = ErrorFinder.getWord(file[line], character);

				switch (getInput(word + ": [r]eplace/[a]ccept? ", input, 'r', 'a')) {
					case 'r':
						System.out.print("Replacement text: ");

						file[line] = file[line].substring(0, character) + input.nextLine() +
								file[line].substring(character + word.length());
						break;

					case 'a':
						dictionary.addEntry(word.toLowerCase());

						break;
					default:
						throw new RuntimeException("getInput(String, char ...) return unhandled results");
				} // switch
				new ErrorFinder(line, line + 1, file, dictionary, map);
				System.out.print(getErrors(file, line, map));
			}// while
		} // for
		input.close();
    }

    
    // additional method
    /**
	 * getInput(String, Scanner, char ...) continually prompts and polls the user
	 * a valid character is input.
	 *
	 * @param message
	 * 		The message to print
	 * @param cin
	 * 		The Scanner to read input from
	 * @param expected
	 * 		A list of character's of expected input
	 *
	 * @return A char input by the user and that is a subset of expected
	 */
	private static char getInput(String message, Scanner cin, char... expected) {
		do {
			System.out.print(message);

			String line = cin.nextLine();

			if (line.length() > 0) {
				char input = line.charAt(0);

				for (char valid : expected)
					if (input == valid)
						return input;
			}

			System.out.println("Please enter input as prompted");
		} while (true);
	}
    
	
    /**
     * Carries out the Non-Interactive mode of the Spell Checker.
     * 
     * @param args
     *            the arguments given to the main. The correct number of
     *            arguments may or may not be contained in it.
     *            Call doUsage() and exit if the parameter count is incorrect.
     */
    public static void doNonInteractiveMode(String[] args) { // non interactive
    	if (args.length != 3) {
			doUsage();
			return;
		}
		Dictionary dictionary = loadDictionary(args[1]);
		String[] file = loadFile(args[2]);
		
		ErrorMap map = new ErrorMap(file.length);
		
		int cores = 2;
		for (int i = 0; i < cores; i++)
			new ErrorFinder((i * file.length) / cores, ((i + 1) * file.length) / cores, file, dictionary, map);

		for (int i = 0; i < file.length; i++)
			System.out.print(getErrors(file, i, map));
    }

    // additional 2 methods
    private static String getErrors(String[] lines, int line, ErrorMap map) {
		StringBuilder result = new StringBuilder(lines[line].length() * 2 + 2);
		result.append(lines[line]);
		result.append('\n');

		map.waitForCompletion(line);

		ArrayList<Integer> errors = map.getErrors(line);
		if (errors != null) {
			int cursor = 0;
			for (Integer I : errors) {
				int value = I;
				while (cursor++ < value)
					result.append(' ');
				result.append('^');
			}

			result.append('\n');
		}

		return result.toString();
	}

	private static Dictionary loadDictionary(String filename) {
		try {
			return new Dictionary(filename);
		} catch (FileNotFoundException e) {
			System.out.println("Dictionary file not found");
			doUsage();
			throw new RuntimeException(e);
		}
	}
    
    /**
     * Carries out the Dictionary Edit mode of the Spell Checker.
     * 
     * @param args
     *            the arguments given to the main. The correct number of
     *            arguments may or may not be contained in it.
     *            Call doUsage() and exit if the parameter count is incorrect.
     */
    public static void doDictionaryEditMode(String[] args) {
    	if (args.length != 3) { // or use < > 
			doUsage();
			return;
		}
		System.out.println("Editing " + new File(args[2]).getName());

		Dictionary dictionary = loadDictionary(args[1]);

		Scanner input = new Scanner(System.in);
		System.out.print("Word: ");

		for (String words = input.nextLine(); !words.equals("!quit"); words = input.nextLine()) {
			boolean isWord = true;
			for (int i = 0; i < words.length() && isWord; i++)
				if (!ErrorFinder.isValid(words, i))
					isWord = false;

			if (isWord) {
				if (dictionary.hasWord(words)) {
					System.out.printf("'%s' was found.%n", words);

					switch (getInput(words + ": [n]o operation / [r]emove / [g]et definition / [c]hange definition: ",
							input, 'n', 'r', 'g', 'c')) {
						case 'r':
							dictionary.removeEntry(words);
							break;
						case 'g':
							String definition = dictionary.getDefinitionOf(words);

							if (definition == null)
								System.out.println("<undefined>");
							else
								System.out.println(dictionary.getDefinitionOf(words));
							break;
						case 'c':
							System.out.print("Definition: ");

							String newDefinition = input.nextLine();
							if (newDefinition.matches("\\s*"))
								newDefinition = null;

							dictionary.updateEntry(words, newDefinition);
							break;
					}
				}
				else {
					System.out.printf("'%s' not found.%n", words);

					String definition = null;
					switch (getInput("[n]o operation / [a]dd / [d]efine and add: ", input, 'n', 'a', 'd')) {
						case 'd':
							System.out.print("Definition: ");
							definition = input.nextLine();

							if (definition.matches("\\s*"))
								definition = null;
						case 'a':
							dictionary.addEntry(words, definition);
							break;
					}
				}
			}
			else
				System.out.printf("'%s' is invalid. Please enter a word.\n", words);

			System.out.print("Word: ");
		}

		try {
			dictionary.printToFile(args[1]);
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		}
    }

    // additional method
    private static String[] loadFile(String filename) {
		try {
			ArrayList<String> data = new ArrayList<String>();

			BufferedReader file = new BufferedReader(new FileReader(filename));
			while (file.ready())
				data.add(file.readLine());
			file.close();

			String[] lines = new String[data.size()];
			data.toArray(lines);

			return lines;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
    
    /**
     * Timer class used for this project's competition. DO NOT modify this class
     * in any way or you will be ineligible for Eternal Glory.
     */
    private static class Timer {
        private long startTime;
        private long endTime;

        public void start() {
            startTime = System.nanoTime();
        }

        public void stop() {
            endTime = System.nanoTime();
        }

        public long runtime() {
            return endTime - startTime;
        }
    }
    
    // addiitional classes + methods
    private static class ErrorFinder implements Runnable {
		private final int start, end;
		private final String[] lines;
		private final Dictionary dictionary;
		private final ErrorMap map;

		public ErrorFinder(int start, int end, String[] lines, Dictionary dictionary, ErrorMap map) {
			this.start = start;
			this.end = end;
			this.lines = lines;
			this.dictionary = dictionary;
			this.map = map;

			start();
		}

		public void start() {
			for (int i = start; i < end; i++)
				map.decomplete(i);

			new Thread(this).start();
		}

		public void run() {
			for (int lineNo = start; lineNo < end; lineNo++) {
				String line = lines[lineNo].toLowerCase() + '\n';

				for (int i = 0; i < line.length(); i++) {
					if (!isValid(line, i))
						continue;

					String word = getWord(line, i);

					if (!dictionary.hasWord(word))
						map.addError(lineNo, i);

					i += word.length();
				}

				map.complete(lineNo);
			}
		}

		public static String getWord(String line, int start) {
			StringBuilder builder = new StringBuilder(15);

			while (start < line.length() && isValid(line, start))
				builder.append(line.charAt(start++));

			return builder.toString();
		}

		private static boolean isValid(String line, int character) {
			char c = Character.toLowerCase(line.charAt(character));

			return (c >= 'a' && c <= 'z') || c == '\'' || c == '-';
		}
	}

	private static class ErrorMap {
		private final ArrayList<Integer>[] errors;
		@SuppressWarnings("CanBeFinal")//
		private volatile boolean[] completed;

		@SuppressWarnings("unchecked")
		public ErrorMap(int numLines) {
			errors = (ArrayList<Integer>[]) new ArrayList[numLines];
			completed = new boolean[numLines];
		}

		public void addError(int line, int character) {
			if (errors[line] == null)
				errors[line] = new ArrayList<Integer>();

			errors[line].add(character);
		}

		public void complete(int line) {
			completed[line] = true;
		}

		public void decomplete(int line) {
			errors[line] = null;
			completed[line] = false;
		}

		public void waitForCompletion(int line) {
			//noinspection StatementWithEmptyBody
			while (!completed[line]);
		}

		public ArrayList<Integer> getErrors(int line) {
			return errors[line];
		}
	}
	
}
