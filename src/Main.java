public class Main {

    public static void main(String[] args) {
        KnnClassifier classifier = new KnnClassifier();
        classifier.setK(10);

        /** IRIS **
        classifier.setDelimiter(",");
        classifier.setOutputColumnCount(5);
        classifier.readData("datafiles/iris.data");

        /** WHITE WINE **/
        classifier.setDelimiter(";");
        //classifier.ignoreColumns(new int[]{1,2});
        classifier.setOutputColumnCount(12);
        classifier.setDataBeginRowCount(2);
        classifier.readData("datafiles/winequality-white-data.csv");


        /** CLASSIFY **/
        classifier.doKFoldCross();
    }
}
