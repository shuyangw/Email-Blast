package structures;

import java.util.Comparator;

final public class AlphabetComparator implements Comparator<String> {
    public int compare(String str1, String str2){
        return str1.compareTo(str2);
    }
}