import java.util.*;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.apache.commons.math3.stat.inference.TTest;
import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;
/**
 * This class represents a data frame containing double values. It implements the DataFrame interface for Doubles.
 *
 * @author 659358id Ihor Dumasnkyi
 */
public class DoubleDataFrame implements DataFrame<Double>, DataFrameStatistics {
    private final List<String> columnNames;
    private final double [][] data;
    private final Map<String, List<Double>> dataFrame;

    /**
     * Constructs a DoubleDataFrame with the given column names and data
     *
     * @param columnNames the names of the columns
     * @param data the two-dimensional array of Double data
     */
    public DoubleDataFrame(List<String> columnNames , double [][] data ) {
        dataFrame = new HashMap<>();
        this.columnNames = columnNames;
        this.data = data;
        for(int i = 0; i < columnNames.size(); i++) {
            List<Double> xColumn = new ArrayList<>();
            for (double[] datum : data) {
                xColumn.add(datum[i]);
            }
            dataFrame.put(columnNames.get(i), xColumn);
        }
    }

    /**
     * Gets the number of rows in the DataFrame
     *
     * @return the number of rows
     */
    public int getRowCount() {
        return data.length;
    }

    /**
     * Gets the number of columns in the DataFrame
     *
     * @return the number of columns
     */
    public int getColumnCount() {
        return columnNames.size();
    }

    /**
     * Gets the list of column names
     *
     * @return An unmodifiable list of column names
     */
    public List<String> getColumnNames() {
        return Collections.unmodifiableList(columnNames);
    }

    /**
     * Sets the value of an element in the data frame specified by the name of the column and row index provided as arguments
     * The new value is also given as an argument to the method
     *
     * @param rowIndex the row index of the entr
     * @param colName  the name of the column in which the entry is stored
     * @param value    the new value of the entry
     * @throws IndexOutOfBoundsException when the row index does not match any existing row
     * @throws IllegalArgumentException  when colName does not match any existing column
     */
    public void setValue(int rowIndex, String colName, Double value) {
        if (rowIndex < 0 || rowIndex >= getRowCount()) {
            throw new IndexOutOfBoundsException("Invalid row index: " + rowIndex);
        }

        if (!dataFrame.containsKey(colName)) {
            throw new IllegalArgumentException("Column name not found: " + colName);
        }

        dataFrame.get(colName).set(rowIndex, value);
    }

    /**
     * Retrieves the value stored under a specific rowIndex and in a specific column, both provided as arguments to the method
     *
     * @param rowIndex the row index of the entry
     * @param colName  the name of the column in which the entry is stored
     * @return the double value stored in the data frame at the given row index and column name
     * @throws IndexOutOfBoundsException when the row index does not match any existing row
     * @throws IllegalArgumentException  when colName does not match any existing column
     */
    public Double getValue(int rowIndex, String colName) {
        if (rowIndex < 0 || rowIndex >= getRowCount()) {
            throw new IndexOutOfBoundsException();
        }

        if (!dataFrame.containsKey(colName)) {
            throw new IllegalArgumentException();
        }

        return dataFrame.get(colName).get(rowIndex);
    }

    /**
     * Returns a data vector object representing a row of the data frame, as specified in the argument to the method
     *
     * @param rowIndex the row index o entry
     * @return a DoubleDataVector object representing the specified row
     * @throws IndexOutOfBoundsException when the row index does not match any existing row
     */
    @Override
    public DataVector<Double> getRow(int rowIndex) throws IndexOutOfBoundsException {
        if (rowIndex < 0 || rowIndex >= this.getRowCount()) {
            throw new IndexOutOfBoundsException();
        }
        List<Double> rows = new ArrayList<>();
        for (int j = 0; j < data[rowIndex].length; j++) {
            rows.add(data[rowIndex][j]);
        }

        return new DoubleDataVector("row_" + rowIndex, columnNames, rows);
    }

    /**
     * Returns a data vector object representing a column of the data frame, as specified in the argument to the method
     *
     * @param colName the name of the column
     * @return a DoubleDataVector object representing the specified column
     * @throws IllegalArgumentException when colName does not match any existing column
     */
    @Override
    public DataVector<Double> getColumn(String colName) throws IllegalArgumentException {
        if(!columnNames.contains(colName)) {
            throw new IllegalArgumentException();
        }
        List<String> namesOfEntries = new ArrayList<>();
        for (int j = 0; j < this.getRowCount(); j++) {
            namesOfEntries.add("row_" + j);
        }
        return new DoubleDataVector(colName, namesOfEntries, dataFrame.get(colName));
    }

    /**
     * Returns all the columns of the data frame as a list of DoubleDataVector objects, with each list entry representing one column
     *
     * @return a list of columns
     */
    @Override
    public List<DataVector<Double>> getColumns() {
        List<DataVector<Double>> resultCols = new ArrayList<>();

        for(String colName: columnNames) {
            resultCols.add(this.getColumn(colName));
        }
        return resultCols;
    }

    /**
     * Returns all the rows of the data frame as a list of DoubleDataVector objects, with each list entry representing one row
     *
     * @return a list of rows
     */
    @Override
    public List<DataVector<Double>> getRows() {
        List<DataVector<Double>> resultRows = new ArrayList<>();

        for(int j = 0; j < this.getRowCount(); j++) {
            resultRows.add(this.getRow(j));
        }
        return resultRows;
    }

    /**
     * Creates a new expanded data fame, enlarged by a number of rows specified as int argument of the method,
     * and the columns given in the form of a list as an argument
     *
     * @param additionalRows the number of rows to add to the new data frame
     * @param newCols the names of the columns to add to the new data frame
     * @return a new expanded data frame
     */
    @Override
    public DataFrame<Double> expand(int additionalRows, List<String> newCols) throws IllegalArgumentException {
        List<String> newColNames = new ArrayList<>(columnNames);
        newColNames.addAll(newCols);


        double[][] newData = new double[additionalRows + this.getRowCount()][newCols.size() + this.getColumnCount()];
        //copy data
        for(int i = 0; i < this.getRowCount(); i++) {
            System.arraycopy(data[i], 0, newData[i], 0, this.getColumnCount());
        }
        //fill in 0's
        for(int i = this.getRowCount(); i < this.getRowCount() + additionalRows; i++) {
            for(int j = this.getColumnCount(); j < this.getColumnCount() + newCols.size(); j++) {
                newData[i][j] = 0;
            }
        }
        return new DoubleDataFrame(newColNames, newData);
    }
    /**
     Makes a projection of the original data frame but with reduced number of colums
     @param retainColumns the names of columns to be retained in the projection
     @return a new DoubleDataFrame object containing only the columns specified for retention
     @throws IllegalArgumentException if any column name in the list of columns to be retained does not exist in the data frame
     */
    @Override
    public DataFrame<Double> project(Collection<String> retainColumns) throws IllegalArgumentException {
        List<String> newColNames = columnNames.stream()
                .filter(retainColumns::contains)
                .collect(Collectors.toList());
        double[][] newData = new double[this.getRowCount()][retainColumns.size()];

        for (int j = 0; j < newColNames.size(); j++) {
            List<Double> colJ = dataFrame.get(columnNames.get(j));

            for (int i = 0; i < this.getRowCount(); i++) {
                newData[i][j] = colJ.get(i);
            }
        }
        return new DoubleDataFrame(newColNames, newData);

    }

    /**
     * Returns a new DoubleDataFrame object with filtered rows based on the provided rowFilter Predicate object
     *
     * @param rowFilter a predicate indicating whether a row should be retained
     * @return a new DoubleDataFrame object with filtered rows
     */
    @Override
    public DataFrame<Double> select(Predicate<DataVector<Double>> rowFilter) {
        List<DataVector<Double>> selectedRows = getRows().stream()
                .filter(rowFilter)
                .collect(Collectors.toList());

        double[][] newData = new double[selectedRows.size()][this.getColumnCount()];

        for (int i = 0; i < selectedRows.size(); i++) {
            List<Double> rowI = selectedRows.get(i).getValues();
            for (int j = 0; j < this.getColumnCount(); j++) {
                newData[i][j] = rowI.get(j);
            }
        }

        return new DoubleDataFrame(columnNames, newData);
    }

    /**
     * Creates a new DoubleDataFrame with an additional column where each entry represents a summary of
     * entries in the correspondig row
     *
     * @param columnName name the name of the new column
     * @param function   the function to apply to each row
     * @return a new DoubleDataFrame object with an additional column
     */
    @Override
    public DataFrame<Double> computeColumn(String columnName, Function<DataVector<Double>, Double> function) {
        DataFrame<Double> newDataFrame = this.expand(0, columnName);

        List<DataVector<Double>> rowsList = this.getRows();

        for (int i = 0; i < this.getRowCount(); i++) {
            Double computeValue = function.apply(rowsList.get(i));
            newDataFrame.setValue(i, columnName, computeValue);
        }

        return newDataFrame;
    }

    /**
     * Creates a DoubleDataVector object holding summary values for each column, with the summary specified
     * by the summaryFunction provided as an argument
     *
     * @param name the name of the resulting data vector
     * @param summaryFunction the binary operator used to reduce the values in each column
     * @return a DoubleDataVector object holding summary values
     */
    @Override
    public DataVector<Double> summarize(String name, BinaryOperator<Double> summaryFunction) {
        /*
        I have some mistake in this method, but after spending a considerable
        amount of time I could not find it
         */
        List<Double> sumValues = new ArrayList<>();

        for (String columnName : this.getColumnNames()) {
            Double columnSum = dataFrame.get(columnName).stream()
                    .reduce(0.0, summaryFunction);
            sumValues.add(columnSum);
        }

        return new DoubleDataVector(name, this.getColumnNames(), sumValues);
    }

    @Override
    public DataFrameStatistics statistics() {
        return this;
    }

    @Override
    public double tTest(String var, double mu) {
        List<Double> dataColumn = dataFrame.get(var);
        double[] dataArr = new double[dataColumn.size()];
        for (int i = 0; i < dataColumn.size(); i++) {
            dataArr[i] = dataColumn.get(i);
        }

        return new TTest().tTest(mu, dataArr);
    }

    @Override
    public double tTest(String var1, String var2) {
        List<Double> dataColumn1 = dataFrame.get(var1);
        List<Double> dataColumn2 = dataFrame.get(var2);

        double[] dataArr1 = new double[dataColumn1.size()];
        double[] dataArr2 = new double[dataColumn2.size()];

        for (int i = 0; i < dataColumn1.size(); i++) {
            dataArr1[i] = dataColumn1.get(i);
            dataArr2[i] = dataColumn2.get(i);
        }

        return new TTest().tTest(dataArr1, dataArr2);
    }

    @Override
    public double pearsonsCorrelation(String var1, String var2) {
        List<Double> dataColumn1 = dataFrame.get(var1);
        List<Double> dataColumn2 = dataFrame.get(var2);
        double[] dataArr1 = new double[dataColumn1.size()];
        double[] dataArr2 = new double[dataColumn2.size()];

        for (int i = 0; i < dataColumn1.size(); i++) {
            dataArr1[i] = dataColumn1.get(i);
            dataArr2[i] = dataColumn2.get(i);
        }
        return new PearsonsCorrelation().correlation(dataArr1, dataArr2);
    }

    @Override
    public DescriptiveStatistics describe(String var) {
        List<Double> dataColumn = dataFrame.get(var);
        double[] dataArr = new double[dataColumn.size()];
        for (int i = 0; i < dataColumn.size(); i++) {
            dataArr[i] = dataColumn.get(i);
        }
        return new DescriptiveStatistics(dataArr);
    }

    @Override
    public Map<String, Double> estimateLinearModel(String dep, List<String> indep) {
        return null;
    }
}