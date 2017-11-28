package edu.wofford;

/**
 * NamedArgument is a structure used by ArgumentParser that extends Argument.
 * <p>
 * NamedArgument objects are Arguments with nicknames.
 * <p>
 * Example:
 * <code>NamedArgument arg = new NamedArgument("--type", "ellipsoid");</code>
 * <code>parser.setArgument(arg);</code>
 * <p>
 * In the example, we create a NamedArgument object for an argument called "--type" with the default value of "ellipsoide".
 * We then can pass our NamedArgument into our ArgumentParser.
 */
public class NamedArgument extends Argument {
    private String nicknames;

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
}
