package edu.wofford;

import java.util.HashSet;
import java.util.Set;

/**
 * Argument is a structure used by ArgumentParser.
 * <p>
 * Argument objects have a name, a value, a description, and a type.
 * <p>
 * Example:
 * <code>Argument arg = new Argument("color");</code>
 * <code>parser.setArgument(arg);</code>
 * <p>
 * In the example, we create an Argument object for an argument called "color".
 * We then can pass our Argument into our ArgumentParser.
 */
public class Argument {

    private String name;
    private String value;
    private String description;
    private String type;
    private Set<String> accepted;

    /** 
     * Constructs an Argument object which requires the Argument name as a string.
     * 
     * @param name the name of the Argument
     */
    public Argument(String name) {
        this.name = name;
        value = "";
        description = "";
        type = "";
        accepted = new HashSet<String>();
    }

    /** 
     * Sets the value of the Argument.
     * 
     * @param value the value of the Argument as a string
     */
    public void setValue(String value) {
        this.value = value;
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
     * Sets the values that an Argument can be set to. If no values are set, then the argument accepts all 
     * values that are valid for its type.
     * 
     * @param values string array of values that the Argument can be set to
     */
    public void setAcceptedValues(String[] values) {
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
        return value;
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
}
