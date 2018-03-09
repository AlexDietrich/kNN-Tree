import java.util.ArrayList;

/**
 * Represents a single possible category in the output column
 */
public class OutputCategory {
    private ArrayList<Dataset> datasets = new ArrayList<>();
    private Attribute categoryValue;

    /**
     * Creates a new OutputCategory instance and sets the value of this category to a given attribute
     * @param value the output attribute value of this category
     */
    public OutputCategory(Attribute value){
        this.categoryValue = value;
    }

    /**
     * Adds a new Dataset to this category
     * @param dataset the Dataset instance which should be added
     */
    public void addDataset(Dataset dataset){
        if(!datasets.contains(dataset)) {
            datasets.add(dataset);
        }
    }

    /**
     * Returns the value of the category
     * @return an Attribute object representing the category value
     */
    public Attribute getCategoryValue() {
        return categoryValue;
    }

    /**
     * Returns the size of the Dataset List of this category
     * @return an int representing the number of datasets in this category
     */
    public int getDatasetNumber(){
        return datasets.size();
    }

    /**
     * Returns a single dataset in this category by index
     * @param index the index of the desired dataset
     * @return a Dataset instance representing the desired dataset
     */
    public Dataset getDataset(int index){
        if(datasets.size() > index && index >= 0) {
            return datasets.get(index);
        }
        return null;
    }
}
