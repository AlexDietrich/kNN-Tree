import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A basic framework for classifying any set of data
 */
public class KnnClassifier {

    private DataReader dataReader = new DataReader();
    private ConfusionMatrix confusionMatrix = new ConfusionMatrix();
    private ArrayList<OutputCategory> categories = new ArrayList<>();

    private int k = 10;
    private int outputColumnCount = -1;
    private boolean removeOutliers = true;

    /**
     * Creates a new kNN-Classifier instance
     */
    public KnnClassifier(){
    }

    /**
     * Sets the number of packs
     * @param k an int representing the number of packs
     */
    public void setK(int k){
        if(k > 1) {
            this.k = k;
        }
    }

    /**
     * Reads the datasets from a file, removes outliers from this dataset if it wasn't set otherwise and
     * categorizes the dataset list into multiple categories defined by the output column
     * @param filename a String representing the filename which contains the data
     */
    public void readData(String filename){
        if(outputColumnCount > 0) {
            ArrayList<Dataset> datasets = this.dataReader.readData(filename);

            if(removeOutliers){
                datasets = removeOutliers(datasets);
            }
            categorizeList(datasets);

            ArrayList<Attribute> outputCategories = new ArrayList<>();
            for(int i = 0; i < categories.size(); i++){
                outputCategories.add(categories.get(i).getCategoryValue());
            }
            confusionMatrix.setOutputCategories(outputCategories);
        }
    }

    /**
     * Sets a column to ignored state so it won't be used for classifying (first column = 1)
     * @param col an int representing the column number
     */
    public void ignoreColumn(int col){
        this.dataReader.addIgnoredColumn(col);
    }

    /**
     * Sets multiple columns to ignored state so they won't be used for classifying (first column = 1)
     * @param cols an int-array containing multiple column numbers
     */
    public void ignoreColumns(int[] cols){
        for(int col : cols){
            this.dataReader.addIgnoredColumn(col);
        }
    }

    /**
     * Sets the column which represents the output value (first column = 1)
     * @param column an int representing the column number
     */
    public void setOutputColumnCount(int column){
        if(column > 0){
            this.outputColumnCount = column;
            this.dataReader.setOutputColumnCount(column);
        }
    }

    /**
     * Sets the delimiter of the attributes in the data file
     * @param delimiter a String containing the delimiter
     */
    public void setDelimiter(String delimiter){
        this.dataReader.setDelimiter(delimiter);
    }

    /**
     * Sets if outliers should be removed before classifying
     * @param removeOutliers a boolean representing the desired action
     */
    public void setRemoveOutliers(boolean removeOutliers){
        this.removeOutliers = removeOutliers;
    }

    /**
     * Removes all outliers (more than two times standard deviation for numbers) from the datasets
     */
    private ArrayList<Dataset> removeOutliers(ArrayList<Dataset> datasets){
        //TODO calculate standard deviation for every attribute which is an int or double
        //TODO remove datasets from ArrayList if attribute value is more than 2 times standard deviation
        return datasets;
    }

    /**
     * Creates a new OutputCategory for each output category available in the dataset list and moves every dataset
     * from this list to the corresponding category
     * If the output column is a numeric value, it will be stripped of after the comma for categorizing
     */
    private void categorizeList(ArrayList<Dataset> datasets){
        if(datasets.size() <= 0 || this.outputColumnCount <= 0){
            return;
        }
        //TODO go through each dataset, create new OutputCategory in Arraylist if it doesn't exist already and move
        //TODO the entries to the correct OutputCategory


    }

    /**
     *
     */
    public void doKFoldCross(){
        // Reset confusion matrix
        confusionMatrix.resetMatrix();

        // Create k packs
        ArrayList<ArrayList<Dataset>> packs = createPacks();

        // Check if enough packs are available
        if(packs.size() != k){
            return;
        }

        // Do k passes and change pack for testing each time
        for(int i = 0; i < k; i++){
            // Create List for training datasets
            ArrayList<Dataset> train = new ArrayList<>();
            for(int j = 0; j < k; j++){
                if(j == i) continue;
                train.addAll(packs.get(j));
            }

            // Go through each entry of test pack
            for(Dataset test : packs.get(i)){
                DatasetEuklidianComparator cmp = new DatasetEuklidianComparator(test);
                Collections.sort(train, cmp);

                // Check which category value is found the most in first k elements of sorted list
                ArrayList<Integer> numbers = new ArrayList<>();
                for(int j = 0; j < categories.size(); j++){
                    numbers.add(0);
                }
                for(int j = 0; j < k; j++){
                    Attribute attr = train.get(j).getAttribute(outputColumnCount-1);
                    for(int n = 0; n < categories.size(); n++) {
                        if (attr.getType() == AttributeTypes.TEXT){
                            if(attr.getValue().equals(categories.get(n).getCategoryValue().getValue())) {
                                numbers.set(n, numbers.get(n) + 1);
                            }
                        }else if(attr.getValue() == categories.get(n).getCategoryValue().getValue()){
                            numbers.set(n, numbers.get(n) + 1);
                        }
                    }
                }

                // Actual index
                int actualIndex = 0;
                // Prediction index
                int mostIndex = 0;
                int mostValue = 0;

                // Calculate actual and prediction index
                for(int j = 0; j < categories.size(); j++){
                    if(actualIndex == 0){
                        if(test.getAttribute(outputColumnCount-1).getType() == AttributeTypes.TEXT){
                            if(test.getAttribute(outputColumnCount-1).getValue().equals(categories.get(j).getCategoryValue().getValue())){
                                actualIndex = j;
                            }
                        }else if(test.getAttribute(outputColumnCount-1).getValue() == categories.get(j).getCategoryValue().getValue()){
                            actualIndex = j;
                        }
                    }
                    if(numbers.get(j) > mostValue){
                        mostIndex = j;
                        mostValue = numbers.get(j);
                    }
                }

                // Add entry to confusion matrix
                confusionMatrix.increment(mostIndex, actualIndex);
            }
        }

        // Print confusion matrix
        confusionMatrix.printMatrix();
    }

    /**
     * Creates k number of packs where the datasets of each output category are evenly spread across all packs
     * @return an ArrayList of ArrayLists of Datasets where each entry of the outer most ArrayList represents a pack
     */
    private ArrayList<ArrayList<Dataset>> createPacks(){
        ArrayList<ArrayList<Dataset>> packs = new ArrayList<>();

        for(int i = 0; i < k; i++){ // Loop through every pack (k packs)
            ArrayList<Dataset> pack = new ArrayList<>();
            for(int j = 0; j < categories.size(); j++){ // Loop through every category
                OutputCategory cat = categories.get(j);

                // Calculate next start and end index in this category for current pack
                int startIndex = 0;
                int endIndex = 0;
                if(cat.getDatasetNumber()%k == 0){
                    startIndex = cat.getDatasetNumber()/k*i;
                    endIndex = startIndex+cat.getDatasetNumber()/k;
                }else if(cat.getDatasetNumber()%k <= i){
                    startIndex = cat.getDatasetNumber()%k*(int)Math.ceil(cat.getDatasetNumber()/k)+
                            (i-cat.getDatasetNumber()%k)*(int)Math.floor(cat.getDatasetNumber()/k);
                    if(cat.getDatasetNumber()%k == i){
                        endIndex = startIndex+(int)Math.floor(cat.getDatasetNumber()/k);
                    }else{
                        endIndex = startIndex+(int)Math.ceil(cat.getDatasetNumber()/k);
                    }
                }else{
                    startIndex = i*(int)Math.ceil(cat.getDatasetNumber()/k);
                    endIndex = startIndex+(int)Math.ceil(cat.getDatasetNumber()/k);
                }

                // Loop through datasets in current category which should be added in current pack
                for(int n = startIndex; n < endIndex; n++){
                    pack.add(cat.getDataset(n));
                }
            }
            packs.add(pack);
        }
        return packs;
    }
}
