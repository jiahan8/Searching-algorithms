package edu.iastate.cs228.hw4;
/**
 * @author Jia Han Tan
 */
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.io.*;

public class Dictionary {
    /**
     * An instance of a BinarySearchTree which stores this Dictionary's list of
     * words.
     */
	private final BinarySearchTree<Entry> words;
    /**
     * Constructs a new Dictionary which is empty.
     */
    public Dictionary() {
        // TODO
    	words = new BinarySearchTree<Entry>();
    }

    /**
     * Constructs a new Dictionary whose word list is exactly (a deep copy
     * of) the list stored in the given tree. (For testing purposes, you can
     * set this Dictionary's BST to the given BST, rather clone it, but your
     * final method must do the deep copy)
     * 
     * @param tree
     *            - The tree of the existing word list
     */
    public Dictionary(BinarySearchTree<Entry> tree) {
        // TODO
    	words=tree.deepClone();
    }

    /**
     * Constructs a new Dictionary from the file specified by the given file
     * name. Each line of the file will contain at least one word with an
     * optional definition. Each line will have no leading or trailing
     * whitespace. For each line of the file, create a new Entry containing the
     * word and definition (if given) and add it to the BST.
     * 
     * @param filename
     *            - The file containing the wordlist
     * @throws FileNotFoundException
     *             - If the given file does not exist
     */
    public Dictionary(String filename) throws FileNotFoundException {
        // TODO
    	String[] lines = loadFile(filename);

		words = new BinarySearchTree<Entry>();
		for (String line : lines) {
			if (line.length() > 0) {
				if (line.contains(" ")) {
					String word = line.substring(0, line.indexOf(" "));
					String definition = line.substring(line.indexOf(" ") + 1);

					words.add(new Entry(word, definition));
				}
				else
					words.add(new Entry(line));
			}
		}
    }
    
    // additional method
    private static String[] loadFile(String filename) throws FileNotFoundException {
		try {
			ArrayList<String> data = new ArrayList<String>();

			BufferedReader file = new BufferedReader(new FileReader(filename));
			while (file.ready())
				data.add(file.readLine());
			file.close();

			String[] lines = new String[data.size()];
			data.toArray(lines);

			return lines;
		} catch (FileNotFoundException e) {
			throw e;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

    /**
     * Adds a new Entry to the Dictionary for the given word with no definition.
     * 
     * @param word
     *            - The word to add to the Dictionary
     * @return true only if the Entry was successfully added to the Dictionary,
     *         false otherwise.
     */
    public boolean addEntry(String word) {
        // TODO
		return word != null && words.add(new Entry(word));
    }

    /**
     * Adds a new Entry to the Dictionary for the given word and definition.
     * 
     * @param word
     *            - The word to add to the Dictionary
     * @param definition
     *            - The definition of the given word
     * @return true only if the Entry was successfully added to the Dictionary,
     *         false otherwise.
     */
    public boolean addEntry(String word, String definition) {
        // TODO
		return word != null && words.add(new Entry(word, definition));
    }

    /**
     * Tests whether or not word exists in this Dictionary.
     * 
     * @param word
     *            - The word to test.
     * @return true is word exists in this Dictionary, false otherwise.
     */
    public boolean hasWord(String word) {
        // TODO
		return word != null && words.contains(new Entry(word));
    }

    /**
     * Returns the definition of the given word in the Dictionary, if it is
     * there.
     * 
     * @param word
     *            - The word to retrieve the definition of.
     * @return the definition of the word.
     * @throws IllegalArgumentExeception
     *             - If word does not exist in the Dictionary.
     */
    public String getDefinitionOf(String word) throws IllegalArgumentException {
        // TODO
		if (word == null)
			throw new IllegalArgumentException("word cannot be null");

		Entry entry = words.get(new Entry(word));

		if (entry == null)
			throw new IllegalArgumentException(word + " does not exist in the dictionary");

		return entry.d;
	}

    /**
     * Removes the given word from the word dictionary if it is there.
     * 
     * @param word
     *            - The word to remove from Dictionary.
     * @return true only if the word is successfully removed from the
     *         BinarySearchTree, false otherwise.
     */
    public boolean removeEntry(String word) {
        // TODO
		return word != null && words.remove(new Entry(word));
    }

    /**
     * Changes the definition of given word if it is there.
     * 
     * @param word
     *            - The word to change the definition of
     * @param newDef
     *            - The new definition of the word
     * @return true if the definition was successfully updated, false otherwise.
     */
    public boolean updateEntry(String word, String newDef) {
        // TODO
		if (word == null)
			return false;

		Entry entry = words.get(new Entry(word));

		if (entry == null)
			return false;

		entry.d = newDef;
		return true;
	}

    /**
     * Outputs this Dictionary to the given file. The file should be formatted
     * as follows: 1) One word and definition should appear per line separated
     * by exactly one space. 2) Lines should not have any leading or trailing
     * whitespace except for a single newline. 3) Each line of the file should
     * have text. There should be no empty lines. 4) The words should be sorted
     * alphabetically (i.e. using the BST's inorder traversal)
     * 
     * @param filename
     * @throws FileNotFoundException
     */
    public void printToFile(String filename) throws FileNotFoundException {
        // TODO
    	File file = new File(filename);
		System.out.printf("Wrote %s.", file.getName());

		if (file.exists()) {
			file.renameTo(new File(file.getAbsolutePath() + "~"));
			System.out.printf("  Original backed up to %s~.", file.getName());
		}

		System.out.println();

		try {
			FileOutputStream fos = new FileOutputStream(file, true);

			for (Entry entry : words.getInorderTraversal()) {
				fos.write(entry.w.getBytes());

				if (entry.d != null && !entry.d.matches("\\w*"))
					fos.write((" " + entry.d).getBytes());

				fos.write("\n".getBytes());
			}

			fos.close();
		} catch (FileNotFoundException e) {
			throw e;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
    }

    /**
     * Returns the number of items stored in the Dictionary.
     */
    public int entryCount() {
        // TODO
        return words.size();
    }

    /**
     * Returns a sorted list of Entries (as returned by an inorder traversal of
     * the BST)
     * 
     * @return an ArrayList of sorted Entries
     */
    public ArrayList<Entry> getSortedEntries() {
        // TODO
    	return words.getInorderTraversal();
    }

    /**
     * A Key-Value Pair class which represents an entry in a Dictionary.
     * 
     * @author Jia Han Tan
     */
    public static class Entry implements Comparable<Entry> {

        /**
         * Instance variables storing the word and definition of this Entry
         */
    	private final String w;
    	private String d;
        /**
         * Constructs a new Entry with the given word with no definition
         * 
         * @param w
         *            - The word to create an entry for.
         */
        public Entry(String w) {
            // TODO
        	this.w=w;
        }

        /**
         * Constructs a new Entry with the given word and definition
         * 
         * @param w
         *            - The word to create an entry for.
         * @param d
         *            - The definition of the given word.
         */
        public Entry(String w, String d) {
            // TODO
        	this.w = w;
        	this.d = d;
        }

        /**
         * Compares the word contained in this entry to the word in other.
         * Returns a value < 0 if the word in this Entry is alphabetically
         * before the other word, = 0 if the words are the same, and > 0
         * otherwise.
         */
        @Override
        public int compareTo(Entry other) {
            // TODO
            return w.compareTo(other.w);
        }

        /**
         * Tests for equality of this Entry with the given Object. Two entries
         * are considered equal if the words they contain are equal regardless
         * of their definitions.
         */
        @Override
        public boolean equals(Object o) {
            // TODO
        	if (o == null)
				return false;
			else if (o.getClass() == String.class)
				return w.equals(o);
			else
				return o.getClass() == getClass() && w.equals(((Entry) o).w);
        }
    }
}
