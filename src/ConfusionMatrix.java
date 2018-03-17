import java.text.DecimalFormat;
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
        //TODO pretty print matrix to console
        for (Attribute attr:outputCategories) {
            System.out.print(attr.getValue()+"   ");
        }
        System.out.println("\n-------------------------------------");
        int count = 0;
        int correct = 0;
        for(int i = 0; i < matrix.size(); i++){
            for(int j = 0; j < matrix.get(i).size(); j++){
                System.out.print(matrix.get(i).get(j)+"   ");
                count += matrix.get(i).get(j);
                if(i == j){
                    correct += matrix.get(i).get(j);
                }
            }
            System.out.print("\n");
        }
        System.out.println("-------------------------------------");
        System.out.println("Vertical axis: Reference");
        System.out.println("Horizontal axis: Prediction");
        System.out.println("-------------------------------------");

        //Print accuracy
        System.out.println("Accuracy: "+new DecimalFormat("#.##").format((double)correct/(double)count*100)+"%");
    }

}
