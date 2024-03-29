public class Main {

    public static void main(String[] args) {
        KnnClassifier classifier = new KnnClassifier();
        classifier.setK(10);

        /* IRIS */
        classifier.setDelimiter(",");
        classifier.setOutputColumnCount(5);
        classifier.readData("datafiles/iris.data");

        /* WHITE WINE *
        classifier.setDelimiter(";");
        classifier.ignoreColumns(new int[]{1,3,4,5,6,9,10});
        classifier.setOutputColumnCount(12);
        classifier.setDataBeginRowCount(2);
        classifier.readData("datafiles/winequality-white-data.csv");


        /* CLASSIFY NORMAL *
        classifier.doKFoldCross();

        /* CLASSIFY TIMED */
        classifier.measureClassifyingTime(100000);

        /**/
    }
}
