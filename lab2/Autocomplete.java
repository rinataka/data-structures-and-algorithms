import java.util.Arrays;

public class Autocomplete {
    private Term[] dictionary;

    // Initializes the dictionary from the given array of terms.
    public Autocomplete(Term[] dictionary) {
        this.dictionary = dictionary;
        sortDictionary();
    }

    // Sorts the dictionary in *case-insensitive* lexicographic order.
    // Complexity: O(N log N) where N is the number of dictionary terms
    private void sortDictionary() {
        // TODO
        Arrays.sort(dictionary, Term.byLexicographicOrder);

        //throw new UnsupportedOperationException();
    }

    // Returns all terms that start with the given prefix, in descending order of weight.
    // Precondition: the internal dictionary is in lexicographic order.
    // Complexity: O(log N + M log M) where M is the number of matching terms
    public Term[] allMatches(String prefix) {
        // TODO

        Term key = new Term(prefix, 0);


        // find indexes with elements matching prefix
        int firstIndex = RangeBinarySearch.firstIndexOf(dictionary, key, Term.byPrefixOrder(prefix.length()));
        int lastIndex = RangeBinarySearch.lastIndexOf(dictionary, key, Term.byPrefixOrder(prefix.length()));

        int size = lastIndex - firstIndex + 1;

        Term [] matches = new Term[size];

        if (firstIndex == -1 || lastIndex == -1){
            System.out.println("\nNo terms in the dictionary match this key");
            matches = new Term [0];
            return matches;
        } else {

            for (int i=firstIndex; i<=lastIndex; i++){
                matches[i-firstIndex] = dictionary[i];
            }

            Arrays.sort(matches, Term.byReverseWeightOrder);

            return matches; //throw new UnsupportedOperationException();
        }
    }

    // Returns the number of terms that start with the given prefix.
    // Precondition: the internal dictionary is in lexicographic order.
    // Complexity: O(log N) where N is the number of dictionary terms
    public int numberOfMatches(String prefix) {
        // TODO

        Term key = new Term (prefix, 0);

        int firstIndex = RangeBinarySearch.firstIndexOf(dictionary, key, Term.byPrefixOrder(prefix.length()));
        int lastIndex = RangeBinarySearch.lastIndexOf(dictionary, key, Term.byPrefixOrder(prefix.length()));

        if (firstIndex == -1 || lastIndex == -1){
            return 0;
        } else {
            return lastIndex - firstIndex + 1;
        }

        //throw new UnsupportedOperationException();
    }

}
