
import org.w3c.dom.Node;

import java.util.List;
import java.util.LinkedList;
import java.util.Set;
import java.util.HashSet;

import java.util.stream.Collectors;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * A graph that encodes word ladders.
 *
 * The class does not store the full graph in memory, just a dictionary of words. 
 * The edges are then computed on demand. 
 */
public class WordLadder implements DirectedGraph<String> {

    private Set<String> dictionary;
    private Set<Character> charset;

    /**
     * Creates a new empty graph.
     */
    public WordLadder() {
        dictionary = new HashSet<>();
        charset = new HashSet<>();
    }

    /**
     * Creates a new word ladder graph from the given dictionary file.
     * The file should contain one word per line, except lines starting with "#".
     * @param file  path to a text file
     */
    public WordLadder(String file) throws IOException {
        dictionary = new HashSet<>();
        charset = new HashSet<>();
        Files.lines(Paths.get(file))
                .filter(line -> !line.startsWith("#"))
                .forEach(word -> addWord(word.trim()));
    }

    /**
     * Adds the {@code word} to the dictionary, if it only contains letters.
     * The word is converted to lowercase.
     * @param word  the word
     */
    public void addWord(String word) {
        if (word.matches("\\p{L}+")) {
            word = word.toLowerCase();
            dictionary.add(word);
            for (char c : word.toCharArray())
                charset.add(c);
        }
    }

    /**
     * @return the number of words in the dictionary
     */
    public int nNodes() {
        return dictionary.size();
    }

    /**
     * @param  w  a graph node (a word)
     * @return a list of the graph edges that originate from {@code w}
     */
    public List<DirectedEdge<String>> outgoingEdges(String w) {
        /******************************
         * TODO: Task 2               *
         * Change below this comment  *
         ******************************/

        List<DirectedEdge<String>> words = new LinkedList<>();
        //One action in a word ladder is to change exactly one letter in a word, and make sure that the resulting string
        //is also a word (according to the dictionary). Every step should cost 1.0.

        //turn word into array of chars
        char[] chars = w.toCharArray();

        //initialise word to add to list
        String currentWord = "";

        //iterate over all characters in word
        for (int i=0; i<chars.length; i++){

            //iterate over all letters in the alphabet
            for (char letter : charset){

                //initialise array copy of initial word
                char[] newWord = chars.clone();
                //switch character of word at index i with letter from alphabet
                newWord[i] = letter;
                //turn altered word into a string
                currentWord = new String(newWord);

                //test current word
                if (dictionary.contains(currentWord) && !w.equals(currentWord)){
                    //if new add
                    DirectedEdge<String> edge = new DirectedEdge<String>(w, currentWord);
                    words.add(edge);
                }
            }
        }


        return words;
    }

    /*public DirectedEdge<String> wordtoEdge(String w){

        char[] chars = w.toCharArray();

        String currentWord = "";

        for (int i=0; i<chars.length; i++){

            //iterate over all letters in the alphabet
            for (char letter : charset){

                //initialise array copy of initial word
                char[] newWord = chars;
                //switch character of word at index i with letter from alphabet
                newWord[i] = letter;
                //turn altered word into a string
                currentWord = new String(newWord);

                //test current word
                if (dictionary.contains(currentWord) && !w.equals(currentWord)){
                    //if new add
                    DirectedEdge<String> edge = new DirectedEdge<String>(w, currentWord);

                }
            }
        }

        return;
    }
*/
    /**
     * @param  w  one node/word
     * @param  u  another node/word
     * @return the guessed best cost for getting from {@code w} to {@code u}
     * (the number of differing character positions)
     */
    public double guessCost(String w, String u) {
        /******************************
         * TODO: Task 4               *
         * Change below this comment  *
         ******************************/

        if (w.length() != u.length()){ //error handling
            return 0;
        }

        char[] wWord = w.toCharArray();
        char[] uWord = u.toCharArray();

        int difference = 0;

        for (int i = 0; i < uWord.length; i++) {

            if (uWord[i] != wWord[i]){
                difference++;
            }
        }

        return difference;
    }

    /**
     * @return a string representation of the graph
     */
    @Override
    public String toString() {
        StringWriter buffer = new StringWriter();
        PrintWriter w = new PrintWriter(buffer);
        w.println("Word ladder with " + nNodes() + " words, charset: \"" + charset.stream().map(Object::toString).collect(Collectors.joining()) + "\"");
        w.println();

        int ctr = 0;
        w.println("Example words and ladder steps:");
        for (String v : dictionary) {
            if (v.length() != 5)
                continue;
            List<DirectedEdge<String>> edges = outgoingEdges(v);
            if (edges.isEmpty())
                continue;
            w.println(v + " --> " + edges.stream().map(e -> e.to()).collect(Collectors.joining(", ")));
            if (ctr++ >= 10)
                break;
        }
        return buffer.toString();
    }

    /**
     * Unit tests the class
     * @param args  the command-line arguments
     */
    public static void main(String[] args) {
        try {
            System.out.println(new WordLadder(args[0]));
        } catch (Exception e) {
            // If there is an error, print it and a little command-line help
            e.printStackTrace();
            System.err.println();
            System.err.println("Usage: java WordLadder dictionary-file");
            System.exit(1);
        }
    }

}
