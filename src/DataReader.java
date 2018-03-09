import java.util.ArrayList;

/**
 * A class for reading datasets from a file
 */
public class DataReader {

    private int dataBeginRowCount = 1;
    private ArrayList<Integer> ignoredColumns = new ArrayList<>();
    private String delimiter = ";";
    private int outputColumnCount = -1;

    private ArrayList<Attribute> outputAttributes = new ArrayList<>();

    /**
     * Creates a new instance of a DataReader
     */
    public DataReader(){
    }

    /**
     * Sets the row number where the data in the file begins (first line = 1)
     * @param row an int representing the row number
     */
    public void setDataBeginRowCount(int row){
        this.dataBeginRowCount = row;
    }

    /**
     * Sets a column to ignored state (first column = 1)
     * @param column an int representing the column number
     */
    public void addIgnoredColumn(int column){
        this.ignoredColumns.add(column);
    }

    /**
     * Sets the delimiter of the attributes in the data file
     * @param delimiter a String containing the delimiter
     */
    public void setDelimiter(String delimiter) {
        this.delimiter = delimiter;
    }

    /**
     * Sets the column which represents the output value (first column = 1)
     * @param column an int representing the column number
     */
    public void setOutputColumnCount(int column){
        if(column > 0){
            this.outputColumnCount = column;
        }
    }

    /**
     * Reads all data from a data file and returns an ArrayList containing all datasets
     * @param filename a String representing the filename of the data file
     * @return an ArrayList of Dataset entries
     */
    public ArrayList<Dataset> readData(String filename){
        ArrayList<Dataset> datasets = new ArrayList<>();

        //TODO read data from file line by line (starting at dataBeginRowCount), split each line at delimiter,
        //TODO remove all ignored columns, create new DataSet instance and give it splitted array,
        //TODO add instance to ArrayList

        //TODO for each line add the attribute for the output column to the ArrayList outputAttributes

        return datasets;
    }

    /**
     * Returns a list of all Attribute instances of the output column
     * @return an ArrayList of Attribute instances
     */
    public ArrayList<Attribute> getOutputAttributes() {
        return outputAttributes;
    }
}
