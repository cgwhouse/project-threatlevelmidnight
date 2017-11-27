package edu.wofford;

/**
 * Argument is a structure used by ArgumentParser.
 * <p>
 * Argument objects have a name, a value, a description, and a type.
 * <p>
 * TODO: Include Examples Demonstrating the Above
 */
public class Argument {

    private String name;
    private String value;
    private String description;
    private String type;

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
