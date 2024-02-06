import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents a vector containing Double values, columns and the name. Implements the DoubleDataVector interfaces
 *
 * @author 659358id Ihor Dumanskyi
 */

public class DoubleDataVector implements DataVector<Double> {
    private final List<String> valuesNames;
    private final List<Double> values;
    private final String name;

    /**
     * Creates a DoubleDataVector instanse with the given name, list of entry names, and list of Double values
     *
     * @param name the name of the DoubleDataVector
     * @param valuesNames a list of entry names
     * @param values a list of Double values
     */
    public DoubleDataVector(String name, List<String> valuesNames, List<Double> values) {
        this.valuesNames = valuesNames;
        this.values = values;
        this.name = name;
    }

    /**
     * Returns the name of the DoubleDataVector
     *
     * @return the name of the DoubleDataVector
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * Returns a list of entry names associated with the DoubleDataVector
     *
     * @return a list of entry names
     */
    @Override
    public List<String> getEntryNames() {
        return valuesNames;
    }

    /**
     * Returns the Double value associated with the given entryName
     *
     * @param entryName the name of the entry
     * @return the Double value corresponding to the entryName
     */
    @Override
    public Double getValue(String entryName) {
        int index;
        if (!name.startsWith("row_")) {
            index = Integer.parseInt(entryName.substring(4));
        } else {
            index = valuesNames.indexOf(entryName);
        }
        return values.get(index);
    }

    /**
     * Returns a list of Double values held by the DoubleDataVector
     *
     * @return a list of Double values
     */
    @Override
    public List<Double> getValues() {
        return values;
    }

    /**
     * Returns a map with entry names as keys and their corresponding Double values
     *
     * @return map
     */
    @Override
    public Map<String, Double> asMap() {
        Map<String, Double> resultMap = new HashMap<>();
        for (int i = 0; i < valuesNames.size(); i++) {
            resultMap.put(valuesNames.get(i), values.get(i));
        }
        return resultMap;
    }
}
