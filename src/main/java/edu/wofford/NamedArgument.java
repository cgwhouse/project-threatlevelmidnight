package edu.wofford;

/**
 * NamedArgument is a structure used by ArgumentParser that extends Argument.
 * <p>
 * NamedArgument objects are Arguments with nicknames.
 * <pre>
 * Example:
 * {@code
 * NamedArgument arg = new NamedArgument("--type", "ellipsoid");
 * parser.setArgument(arg);
 * }
 * In the example, we create a NamedArgument object for an argument called "--type" with the default value of "ellipsoide".
 * We then can pass our NamedArgument into our ArgumentParser.
 * </pre>
 */
public class NamedArgument extends Argument {
    private String nicknames;
    private Boolean required;

    /** 
     * Constructs an Argument object which requires the Argument name as a string and sets the value of the Argument.
     * 
     * @param name the name of the Argument
     * @param value the value of the Argument
     */
    public NamedArgument(String name, String value) {
        super(name);
        this.setValue(value);
        nicknames = "-";
        required = false;
    }

    /** 
     * Constructs an Argument object which requires the Argument name as a string, required as a boolean and sets the value of the Argument.
     * 
     * @param name the name of the Argument
     * @param value the value of the Argument
     * @param required denotes whether the Argument is required
     */
    public NamedArgument(String name) {
        super(name);
        this.setValue("");
        nicknames = "-";
        required = true;
    }

    /** 
     * Adds a short-form name to the Argument.
     * 
     * @param nickname the nickname of the Argument as a string
     */
    public void addNickname(String nickname) {
        nicknames += nickname;
    }

    /** 
     * Gets each short-form name for the Argument.
     * 
     * @return a single string containing a "-" followed by each of the short-form names for the Argument
     */
    public String getNicknames() {
        return nicknames;
    }

    /** 
     * Gets whether the Argument is required or not.
     * 
     * @return either true or false
     */
    public Boolean isRequired() {
        return required;
    }
}
