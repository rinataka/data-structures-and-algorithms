
import java.util.Comparator;

public class Term {
    private String word;
    private long weight;

    // Initializes a term with a given word and weight.
    public Term(String word, long weight) {
        this.word = word;
        this.weight = weight;
    }

    // Gets the word.
    public String getWord() {
        return word;
    }

    // Gets the weight.
    public long getWeight() {
        return weight;
    }

    // Extracts a prefix from the word.
    // If `len` is larger than the word length, the prefix is the entire word.
    public String getPrefix(int len) {
        // TODO
        String prefix;
        if (len > word.length()){
            prefix = word;
        } else {
            prefix = word.substring(0, len);
        }
        return prefix;
        //throw new UnsupportedOperationException();
    }

    // Compares two terms in case-insensitive lexicographic order.
    // TODO
    public static final Comparator<Term> byLexicographicOrder = new Comparator<Term>() {
        @Override
        public int compare(Term o1, Term o2) {
            String word1 = o1.getWord();
            String word2 = o2.getWord();

            return word1.compareToIgnoreCase(word2);
        }
    };


    // Compares two terms in descending order by weight.
    // TODO
    public static final Comparator<Term> byReverseWeightOrder = new Comparator<Term>() {
        @Override
        public int compare(Term o1, Term o2) {
            long first = o1.getWeight();
            long second = o2.getWeight();

            if (first<second){
                return 1;
            } else if (first == second){
                return 0;
            } else {
                return -1;
            }
        }
    };

    // This method returns a comparator that compares the two terms in case-insensitive
    // lexicographic order, but using only the first k characters of each word.
    public static Comparator<Term> byPrefixOrder(int k) {
        // TODO
        // Hint: use getPrefix and byLexicographicOrder.
        return new Comparator<Term>() {
            @Override
            public int compare(Term o1, Term o2) {
                //String prefix1 = o1.getPrefix(k);
                //String prefix2 = o2.getPrefix(k);

                Term a = new Term(o1.getPrefix(k), o1.getWeight());
                Term b = new Term(o2.getPrefix(k), o2.getWeight());

                return byLexicographicOrder.compare(a, b);
            }
        };
    }



/*
    // If you are stuck with creating comparators, here is a silly example that considers all integers equal:
    public static final Comparator<Integer> example0 = new Comparator<Integer>() {
        public int compare(Integer a, Integer b) {
            return 0;
        }
    };

    // And here is the same example using functional syntax:
    public static final Comparator<Integer> example1 = (a, b) -> {
        return 0;
    };

    // This is the same as the following:
    public static final Comparator<Integer> example2 = (a, b) -> 0;
*/

    // Returns a string representation of this term in the following format:
    // the weight, followed by whitespace, followed by the word.
    public String toString() {
        return String.format("%12d    %s", this.getWeight(), this.getWord());
    }

}
