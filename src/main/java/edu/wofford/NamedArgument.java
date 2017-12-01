package edu.wofford;

import java.util.*;

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
 * In the example, we create a NamedArgument object for an argument called "--type" with the default value of "ellipsoid".
 * We then can pass our NamedArgument into our ArgumentParser.
 * </pre>
 */
public class NamedArgument extends Argument {
    private String nicknames;
    private Boolean required;
    private List<String> mutuallyExclusiveNamedArgs;

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
        mutuallyExclusiveNamedArgs = new ArrayList<String>();
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
        mutuallyExclusiveNamedArgs = new ArrayList<String>();
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
     * Adds the name of the Argument to the list that holds mutually exclusive Argument names.
     * 
     * @param argName the name of the Argument as a string
     */
    public void addMutuallyExclusiveArg(String argName) {
        if (!mutuallyExclusiveNamedArgs.contains(argName)) {
            mutuallyExclusiveNamedArgs.add(argName);
        }
    }

    /** 
     * Adds the name of the Argument to the list that holds mutually exclusive Argument names.
     * 
     * @param arg the NamedArgument
     */
    public void addMutuallyExclusiveArg(NamedArgument arg) {
        if (!mutuallyExclusiveNamedArgs.contains(arg.getName())) {
            mutuallyExclusiveNamedArgs.add(arg.getName());
        }
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

    /** 
     * Gets whether the NamedArgument is mutually exclusive or not.
     * 
     * @return either true or false
     */
    public Boolean hasMutualExclusiveArgs() {
        if (mutuallyExclusiveNamedArgs.size() > 0) {
            return true;
        }
        else {
            return false;
        }
    }

    /** 
     * Gets whether the NamedArgument is mutually exclusive or not to the provided NamedArgument.
     * 
     * @return either true or false
     */
    public Boolean isMutuallyExclusive(NamedArgument arg) {
        if (mutuallyExclusiveNamedArgs.contains(arg.getName())) {
            return true;
        }
        else {
            return false;
        }
    }

    /** 
     * Gets whether the NamedArgument is mutually exclusive or not to the provided NamedArgument name as a string.
     * 
     * @return either true or false
     */
    public Boolean isMutuallyExclusive(String argName) {
        if (mutuallyExclusiveNamedArgs.contains(argName)) {
            return true;
        }
        else {
            return false;
        }
    }
}
