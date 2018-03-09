import java.util.ArrayList;

/**
 * Represents a confusion matrix
 */
public class ConfusionMatrix {

    private ArrayList<Attribute> outputCategories = new ArrayList<>();
    private ArrayList<ArrayList<Integer>> matrix;

    /**
     * Creates a new empty confusion matrix
     */
    public ConfusionMatrix(){
    }

    /**
     * Sets the possible categories of the output to create the corresponding confusion matrix
     * @param outputCategories an ArrayList of Attribute instances representing the possible output categories
     */
    public void setOutputCategories(ArrayList<Attribute> outputCategories){
        this.outputCategories = outputCategories;
        resetMatrix();
    }

    /**
     * Increments a field in the confusion matrix
     * @param predictionIndex the index of the predicted category
     * @param referenceIndex the index of the actual reference category
     */
    public void increment(int predictionIndex, int referenceIndex){
        if(matrix != null && matrix.size() > referenceIndex && matrix.get(0).size() > predictionIndex) {
            matrix.get(referenceIndex).set(predictionIndex, matrix.get(referenceIndex).get(predictionIndex) + 1);
        }
    }

    /**
     * Resets the confusion matrix values
     */
    public void resetMatrix(){
        matrix = new ArrayList<>();
        for(int i = 0; i < outputCategories.size(); i++){
            matrix.add(new ArrayList<>());
            for(int j = 0; j < outputCategories.size(); j++){
                matrix.get(i).add(0);
            }
        }
    }

    /**
     * Prints the confusion matrix to stdout
     */
    public void printMatrix(){
        //TODO print matrix to console
        for(ArrayList<Integer> list : matrix){
            for(Integer i : list){
                System.out.print(i+"   ");
            }
            System.out.print("\n");
        }
    }

}
