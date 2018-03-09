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
        //TODO calculate distance between candidate and object1 and between candidate and object2
        //TODO return value according to documentation
        return 0;
    }
}
