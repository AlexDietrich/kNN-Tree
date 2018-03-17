import java.time.Duration;
import java.time.temporal.TemporalUnit;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * A basic framework for classifying any set of data
 */
public class KnnClassifier {

    private DataReader dataReader = new DataReader();
    private ConfusionMatrix confusionMatrix = new ConfusionMatrix();
    private ArrayList<OutputCategory> categories = new ArrayList<>();

    private int k = 10;
    private int effectiveOutputColumnCount = -1;
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
        if(dataReader.getOutputColumnCount() > 0) {
            System.out.print("Reading data...");
            ArrayList<Dataset> datasets = this.dataReader.readData(filename);
            System.out.println("Done!");

            // Calculate effective output column (without ignored columns)
            effectiveOutputColumnCount = dataReader.getOutputColumnCount();
            for(int ignoredColumn : dataReader.getIgnoredColumns()){
                if(ignoredColumn < dataReader.getOutputColumnCount()){
                    effectiveOutputColumnCount--;
                }
            }

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
     * Sets the row number where the data in the file begins (first line = 1)
     * @param row an int representing the row number
     */
    public void setDataBeginRowCount(int row){
        if(row > 0) {
            this.dataReader.setDataBeginRowCount(row);
        }
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
        System.out.print("Removing outliers...");
        //TODO calculate standard deviation for every attribute which is an int or double
        //TODO remove datasets from ArrayList if attribute value is more than 2 times standard deviation
        System.out.println("Done!");
        return datasets;
    }

    /**
     * Creates a new OutputCategory for each output category available in the dataset list and moves every dataset
     * from this list to the corresponding category
     */
    private void categorizeList(ArrayList<Dataset> datasets){
        System.out.print("Categorizing all datasets...");
        if(datasets.size() <= 0 || effectiveOutputColumnCount <= 0){
            return;
        }

        for(Dataset dataset : datasets){
            int index = -1;
            for(OutputCategory cat : categories){
                if(cat.getCategoryValue().getValue().equals(dataset.getAttribute(effectiveOutputColumnCount-1).getValue())){
                    index = categories.indexOf(cat);
                    break;
                }
            }
            if(index == -1){
                OutputCategory newCat = new OutputCategory(dataset.getAttribute(effectiveOutputColumnCount-1));
                categories.add(newCat);
                index = categories.indexOf(newCat);
            }

            categories.get(index).addDataset(dataset);
        }
        System.out.println("Done!");
    }

    /**
     * Classifies all datasets once by using k fold cross validation
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
            doPass(i, packs);
        }

        // Print confusion matrix
        System.out.println("Printing results...\n");
        confusionMatrix.printMatrix();
    }

    /**
     * Does a single pass from k fold cross validation
     * @param passIndex the index of the current pass (first pass = 0, last pass = k-1)
     * @param packs an ArrayList which represents all k packs used for this pass
     */
    private void doPass(int passIndex, ArrayList<ArrayList<Dataset>> packs){
        System.out.print("Doing pass "+(passIndex+1)+"...");

        // Create List for training datasets
        ArrayList<Dataset> train = new ArrayList<>();
        for(int j = 0; j < k; j++){
            if(j == passIndex) continue;
            train.addAll(packs.get(j));
        }

        // Go through each entry of test pack
        for(Dataset test : packs.get(passIndex)){
            // Predict output
            Attribute prediction = classifyDataset(test, train);
            // Add entry to confusion matrix
            confusionMatrix.increment(prediction, test.getAttribute(effectiveOutputColumnCount-1));
        }

        System.out.println("Done!");
    }

    /**
     * Classifies a single dataset by using a list of training data
     * @param candidate the dataset to be classified
     * @param train an ArrayList of Dataset instances which should be used for training
     * @return an Attribute representing the predicted output
     */
    private Attribute classifyDataset(Dataset candidate, ArrayList<Dataset> train){
        DatasetEuklidianComparator cmp = new DatasetEuklidianComparator(candidate);
        Collections.sort(train, cmp);

        // Check which category value is found the most in first k elements of sorted list
        Hashtable<Attribute, Integer> numbers = new Hashtable<>();
        for(int j = 0; j < k; j++){
            Attribute a = train.get(j).getAttribute(effectiveOutputColumnCount-1);
            if(numbers.containsKey(a)){
                numbers.replace(a,numbers.get(a)+1);
            }else {
                numbers.put(a,1);
            }
        }

        // Prediction index
        Attribute predicted = null;
        int mostValue = 0;

        // Calculate actual and prediction index
        for(Attribute a : numbers.keySet()){
            if(numbers.get(a) > mostValue){
                mostValue = numbers.get(a);
                predicted = a;
            }
        }

        return predicted;
    }

    /**
     * Measures the time used for classifying a set number of datasets
     * @param number the number of datasets for which the time should be measured
     */
    public void measureClassifyingTime(int number){
        // Creating packs
        ArrayList<ArrayList<Dataset>> packs = createPacks();

        // Create List for training datasets
        ArrayList<Dataset> train = new ArrayList<>();
        for(int j = 1; j < k; j++){
            train.addAll(packs.get(j));
        }

        // Get single dataset for classifying
        Dataset test = packs.get(0).get(0);

        System.out.print("Measuring time...");

        long startNanos = System.nanoTime();
        for(int i = 0; i < number; i++){
            classifyDataset(test, train);
        }
        long timeUsed = System.nanoTime() - startNanos;

        System.out.println("Done!");

        System.out.println("Printing results...");
        Duration d = Duration.ofNanos(timeUsed);
        String formatted = d.toString().substring(2).replaceAll("(\\d[HMS])(?!$)", "$1 ").toLowerCase();
        System.out.println("Classified "+number+" datasets in "+formatted+"!");
    }

    /**
     * Creates k number of packs where the datasets of each output category are evenly spread across all packs
     * @return an ArrayList of ArrayLists of Datasets where each entry of the outer most ArrayList represents a pack
     */
    private ArrayList<ArrayList<Dataset>> createPacks(){
        System.out.print("Creating packs...");
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
                    startIndex = cat.getDatasetNumber()%k*(int)Math.ceil((double)cat.getDatasetNumber()/k)+
                            (i-cat.getDatasetNumber()%k)*(int)Math.floor((double)cat.getDatasetNumber()/k);
                    endIndex = startIndex+(int)Math.floor((double)cat.getDatasetNumber()/k);
                }else{
                    startIndex = i*(int)Math.ceil((double)cat.getDatasetNumber()/k);
                    endIndex = startIndex+(int)Math.ceil((double)cat.getDatasetNumber()/k);
                }

                // Loop through datasets in current category which should be added in current pack
                for(int n = startIndex; n < endIndex; n++){
                    pack.add(cat.getDataset(n));
                }
            }
            packs.add(pack);
        }
        System.out.println("Done!");
        return packs;
    }
}
