package edu.wofford;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Argument is a structure used by ArgumentParser.
 * <p>
 * Argument objects have a name, a value, a description, and a type.
 * <pre>
 * Example:
 * {@code
 * Argument arg = new Argument("color");
 * parser.setArgument(arg);
 * }
 * In the example, we create an Argument object for an argument called "color".
 * We then can pass our Argument into our ArgumentParser.
 * </pre>
 */
public class Argument {

    private String name;
    private String description;
    private String type;
    private Set<String> accepted;
    private int numberOfValuesExpected;
    private List<String> multipleValues;

    /** 
     * Constructs an Argument object which requires the Argument name as a string.
     * 
     * @param name the name of the Argument
     */
    public Argument(String name) {
        this.name = name;
        description = "";
        type = "";
        accepted = new HashSet<String>();
        multipleValues = new ArrayList<String>();
        numberOfValuesExpected = 1;
    }

    /** 
     * Sets the value of the Argument.
     * 
     * @param value the value of the Argument as a string
     */
    public void setValue(String value) {
        multipleValues.add(value);
    }

    /**
     * Sets the number of values that the Argument is expecting, default is 1.
     * 
     * @param n an integer representing the number of values expected by the Argument
     */
    public void setNumberOfValuesExpected(int n) {
        numberOfValuesExpected = n;
    }

    /** 
     * Sets the description of the Argument.
     * 
     * @param description the description of the Argument as a string
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /** 
     * Sets the type of the Argument.
     * 
     * @param type the type of the Argument as a string
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Sets the values that the Argument can be set to. If no values are set, then the argument accepts all 
     * values that are valid for its type.
     * 
     * @param values string array of values that the Argument can be set to
     */
    public void addAcceptedValues(String[] values) {
        for (int i = 0; i < values.length; i++) {
            accepted.add(values[i]);
        }
    }

    /**
     * Adds a value to the set of accepted values that the Argument can be. If no values are set, then the
     * argument accepts all values that are valid for its type.
     * 
     * @param value string representation of the value that the Argument can be set to
     */
    public void addAcceptedValue(String value) {
        accepted.add(value);
    }

    /** 
     * Gets the name of the Argument.
     * 
     * @return string representing the name of the Argument
     */
    public String getName() {
        return name;
    }

    /** 
     * Gets the value of the Argument.
     * 
     * @return string representing the value of the Argument
     */
    public String getValue() {
        return multipleValues.get(0);
    }

    /**
     * Gets the values of the Argument.
     * 
     * @return a list of strings representing the multiple values that the Argument has been assigned.
     */
    public List<String> getMultipleValues() {
        return multipleValues;
    }

    /** 
     * Gets the description of the Argument.
     * 
     * @return string representing the description of the Argument
     */
    public String getDescription() {
        return description;
    }

    /** 
     * Gets the type of the Argument.
     * 
     * @return string representing the type of the Argument
     */
    public String getType() {
        return type;
    }

    /**
     * Returns the number of values that the argument is expecting.
     * 
     * @return an integer representing the number of values expected
     */
    public int getNumberOfValuesExpected() {
        return numberOfValuesExpected;
    }

    /**
     * Gets the Argument's accepted values.
     * 
     * @return set of of the accepted values for the Argument, as strings
     */
    public Set<String> getAcceptedValues() {
        return accepted;
    }
}
