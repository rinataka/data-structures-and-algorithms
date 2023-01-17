
import java.util.Comparator;

public class RangeBinarySearch {

    // Returns the index of the *first* element in `a` that equals the search key,
    // according to the given comparator, or -1 if there is no matching element.
    // Precondition: `a` is sorted according to the given comparator.
    // Complexity: O(log N) comparisons where N is the length of `a`
    public static<T> int firstIndexOf(T[] a, T key, Comparator<T> comparator) {
        // TODO

        int left = 0;
        int right = a.length-1;

        //initialise return value: if no word witch such prefix is found, will return -1
        int result = -1;

        //loop over array until no elements left
        while (left <= right) {

            //get middle element to compare with key
            int mid = (left+right)/2;

            //compare value lets us know if target element matches with key
            int compareValue = comparator.compare(key, a[mid]);
            //"Returns:
            //a negative integer, zero, or a positive integer as the first argument is less than, equal to, or greater
            //than the second."

            // if compareValue = 0 key and prefix of word match
            if (compareValue == 0){
                result = mid;

                // if there still are elements between left and right left, will see if word at previous index also has
                // matching word, if not will leave while loop
                right = mid - 1;

            //if 1: words with prefix that match key as prefix right to middle element
            } else if (compareValue > 0) {
                left = mid + 1;

            //if -1: words with prefix that match with key is to the left of the middle element
            } else {
                right = mid - 1;
            }

        }

        return result;//throw new UnsupportedOperationException();
    }




    // Returns the index of the *last* element in `a` that equals the search key,
    // according to the given comparator, or -1 if there are is matching element.
    // Precondition: `a` is sorted according to the given comparator.
    // Complexity: O(log N) comparisons where N is the length of `a`
    public static<T> int lastIndexOf(T[] a, T key, Comparator<T> comparator) {
        // TODO

        int left = 0;
        int right = a.length-1;

        //if no word witch such prefix is found, will return -1
        int result = -1;

        //loop over array until no elements left
        while (left <= right) {

            //get middle element to compare with key
            int mid = (left+right)/2;

            //compare value lets us know if target element matches with key
            int compareValue = comparator.compare(key, a[mid]);

            // if its 0 key and prefix of word match
            if (compareValue == 0){
                result = mid;

                // if there still are elements between left and right left, will see if word at next index also has
                // matching word, if not will leave while loop
                left = mid + 1;

            //if compare value = 1: words with prefix that match key as prefix right to middle element
            } else if (compareValue > 0){
                left = mid + 1;

            //if -1: words with prefix that match with key is to the left of the middle element
            } else {
                right = mid - 1;
            }
        }

        return result; //throw new UnsupportedOperationException();
    }

}
