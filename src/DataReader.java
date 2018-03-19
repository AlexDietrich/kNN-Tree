import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * A class for reading datasets from a file
 */
public class DataReader {

    private int dataBeginRowCount = 1;
    private ArrayList<Integer> ignoredColumns = new ArrayList<>();
    private String delimiter = ";";
    private int outputColumnCount = -1;

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
     * Returns all ignored columns
     * @return an ArrayList of Integers representing all columns in ignored state
     */
    public ArrayList<Integer> getIgnoredColumns() {
        return ignoredColumns;
    }

    /**
     * Returns the column number where the output is located
     * @return an int containing the output column number
     */
    public int getOutputColumnCount() {
        return outputColumnCount;
    }

    /**
     * Reads all data from a data file and returns an ArrayList containing all datasets
     * If the output column is a numeric value, it will be stripped of at the comma for categorizing
     * @param filename a String representing the filename of the data file
     * @return an ArrayList of Dataset entries
     */
    public ArrayList<Dataset> readData(String filename){
        ArrayList<Dataset> datasets = new ArrayList<>();

        String line = "";
        int skipped = 0;

        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            while ((line = br.readLine()) != null) {
                if(line.trim().isEmpty()){continue;}
                if(skipped+1 >= dataBeginRowCount) {
                    String[] splitted = line.split(delimiter);

                    // Remove ignored columns
                    for(int ignore : ignoredColumns){
                        if(ignore <= splitted.length){
                            splitted[ignore-1] = null;
                        }
                    }

                    // Check if output column contains a numeric value and convert it to an integer for categorizing
                    if(outputColumnCount <= splitted.length){
                        String outputValue = splitted[outputColumnCount-1];
                        try{
                            double d = Double.parseDouble(outputValue);
                            splitted[outputColumnCount-1] = Integer.toString((int)Math.floor(d));
                        }catch(Exception e){}
                    }

                    // Create new dataset from read attributes and add it to list
                    Dataset d = new Dataset(splitted, outputColumnCount);
                    datasets.add(d);
                }else{
                    skipped++;
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return datasets;
    }
}
