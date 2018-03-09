import java.util.Comparator;

/**
 * Comparator for a Dataset, uses the Euklidian method
 */
public class DatasetEuklidianComparator implements Comparator<Dataset> {
    private Dataset candidate;

    /**
     * Creates a new instance of a Euklidian Comparator for Datasets
     * @param candidate a Dataset to which the distance should be measured
     */
    public DatasetEuklidianComparator(Dataset candidate){
        this.candidate = candidate;
    }

    /**
     * Compares the two given objects by their distance to the candidate
     * @param object1 first Dataset
     * @param object2 second Dataset
     * @return 0 if both Datasets have the same distance to the candidate,
     *          1 if the first Dateset is farther than the second Dataset,
     *         -1 if the first Dateset is nearer than the second Dataset
     */
    public int compare(Dataset object1, Dataset object2){
        if(object1.getAttributeCount() != candidate.getAttributeCount() ||
                object2.getAttributeCount() != candidate.getAttributeCount()){
            return 0;
        }

        double dist1 = 0.0, dist2 = 0.0;
        double tmp1 = 0.0, tmp2 = 0.0;

        for(int i = 0; i < candidate.getAttributeCount(); i++){
            Attribute ac = candidate.getAttribute(i);
            Attribute a1 = object1.getAttribute(i);
            Attribute a2 = object2.getAttribute(i);

            if(ac.getType() == AttributeTypes.TEXT){
                dist1 += DatasetEuklidianComparator.unlimitedCompare((String)ac.getValue(), (String)a1.getValue());
                dist2 += DatasetEuklidianComparator.unlimitedCompare((String)ac.getValue(), (String)a2.getValue());
            }else{
                double acDouble = 0.0;
                double a1Double = 0.0;
                double a2Double = 0.0;
                switch(ac.getType()){
                    case INTEGER: acDouble = (double)((Integer)ac.getValue()).intValue(); break;
                    case DECIMAL: acDouble = (double)ac.getValue();
                }
                switch(a1.getType()){
                    case INTEGER: a1Double = (double)((Integer)a1.getValue()).intValue(); break;
                    case DECIMAL: a1Double = (double)a1.getValue();
                }
                switch(a2.getType()){
                    case INTEGER: a2Double = (double)((Integer)a2.getValue()).intValue(); break;
                    case DECIMAL: a2Double = (double)a2.getValue();
                }

                tmp1 += Math.pow(a1Double-acDouble, 2);
                tmp2 += Math.pow(a2Double-acDouble, 2);
            }
        }

        dist1 += Math.sqrt(tmp1);
        dist2 += Math.sqrt(tmp2);

        if (dist1 > dist2) {
            return 1;
        }
        if (dist1 < dist2) {
            return -1;
        }
        return 0;
    }

    /**
     * COPIED FROM https://commons.apache.org/sandbox/commons-text/jacoco/org.apache.commons.text.similarity/LevenshteinDistance.java.html#L337
     *
     * <p>Find the Levenshtein distance between two Strings.</p>
     *
     * <p>A higher score indicates a greater distance.</p>
     *
     * <p>The previous implementation of the Levenshtein distance algorithm
     * was from <a href="https://web.archive.org/web/20120526085419/http://www.merriampark.com/ldjava.htm">
     * https://web.archive.org/web/20120526085419/http://www.merriampark.com/ldjava.htm</a></p>
     *
     * <p>This implementation only need one single-dimensional arrays of length s.length() + 1</p>
     *
     * <pre>
     * unlimitedCompare(null, *)             = IllegalArgumentException
     * unlimitedCompare(*, null)             = IllegalArgumentException
     * unlimitedCompare("","")               = 0
     * unlimitedCompare("","a")              = 1
     * unlimitedCompare("aaapppp", "")       = 7
     * unlimitedCompare("frog", "fog")       = 1
     * unlimitedCompare("fly", "ant")        = 3
     * unlimitedCompare("elephant", "hippo") = 7
     * unlimitedCompare("hippo", "elephant") = 7
     * unlimitedCompare("hippo", "zzzzzzzz") = 8
     * unlimitedCompare("hello", "hallo")    = 1
     * </pre>
     *
     * @param left the first String, must not be null
     * @param right the second String, must not be null
     * @return result distance, or -1
     * @throws IllegalArgumentException if either String input {@code null}
     */
    private static int unlimitedCompare(CharSequence left, CharSequence right) {
        if (left == null || right == null) {
            throw new IllegalArgumentException("Strings must not be null");
        }

        /*
           This implementation use two variable to record the previous cost counts,
           So this implementation use less memory than previous impl.
         */

        int n = left.length(); // length of left
        int m = right.length(); // length of right

        if (n == 0) {
            return m;
        } else if (m == 0) {
            return n;
        }

        if (n > m) {
            // swap the input strings to consume less memory
            final CharSequence tmp = left;
            left = right;
            right = tmp;
            n = m;
            m = right.length();
        }

        int[] p = new int[n + 1];

        // indexes into strings left and right
        int i; // iterates through left
        int j; // iterates through right
        int upper_left;
        int upper;

        char rightJ; // jth character of right
        int cost; // cost

        for (i = 0; i <= n; i++) {
            p[i] = i;
        }

        for (j = 1; j <= m; j++) {
            upper_left = p[0];
            rightJ = right.charAt(j - 1);
            p[0] = j;

            for (i = 1; i <= n; i++) {
                upper = p[i];
                cost = left.charAt(i - 1) == rightJ ? 0 : 1;
                // minimum of cell to the left+1, to the top+1, diagonally left and up +cost
                p[i] = Math.min(Math.min(p[i - 1] + 1, p[i] + 1), upper_left + cost);
                upper_left = upper;
            }
        }

        return p[n];
    }
}
