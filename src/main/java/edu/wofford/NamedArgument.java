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
    private List<String> mutexArgs;
    private String defaultValue;

    /** 
     * Constructs an Argument object which requires the Argument name as a string and sets the value of the Argument.
     * 
     * @param name the name of the Argument
     * @param defaultValue the value of the Argument
     */
    public NamedArgument(String name, String defaultValue) {
        super(name);
        this.defaultValue = defaultValue;
        this.setValue(defaultValue);
        nicknames = "-";
        required = false;
        mutexArgs = new ArrayList<String>();
    }

    /** 
     * Constructs an Argument object which requires the Argument name as a string, required as a boolean and sets the value of the Argument.
     * 
     * @param name the name of the Argument
     */
    public NamedArgument(String name) {
        super(name);
        defaultValue = "";
        nicknames = "-";
        required = true;
        mutexArgs = new ArrayList<String>();
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
     * Gets the default value of the NamedArgument.
     * 
     * @return the default value of the NamedArgument
     */
    public String getDefault() {
        return defaultValue;
    }

    /** 
     * Gets a list of the mutually exclusive arguments that are mutually exclusive with the NamedArgument.
     * 
     * @return the list of argument names that are mutually exclusive with the NamedArgument
     */
    public List<String> getMutexArgs() {
        return mutexArgs;
    }

    /** 
     * Adds the name of the argument to the list that holds mutually exclusive argument names.
     * 
     * @param argName the name of the argument as a string
     */
    public void addMutuallyExclusiveArg(String argName) {
        if (!mutexArgs.contains(argName)) {
            mutexArgs.add(argName);
        }
    }

    /** 
     * Adds the name of the argument to the list that holds mutually exclusive argument names.
     * 
     * @param arg the NamedArgument
     */
    public void addMutuallyExclusiveArg(NamedArgument arg) {
        if (!mutexArgs.contains(arg.getName())) {
            mutexArgs.add(arg.getName());
        }
    }

    /** 
     * Gets each short-form name for the NamedArgument.
     * 
     * @return a single string containing a "-" followed by each of the short-form names for the NamedArgument
     */
    public String getNicknames() {
        return nicknames;
    }

    /** 
     * Gets whether the NamedArgument is required.
     * 
     * @return true if the NamedArgument is required, false otherwise
     */
    public boolean isRequired() {
        return required;
    }

    /** 
     * Gets whether the NamedArgument is mutually exclusive.
     * 
     * @return true if this NamedArgument has mutually exclusive NamedArguments, false otherwise
     */
    public boolean hasMutualExclusiveArgs() {
        if (mutexArgs.size() > 0) {
            return true;
        }
        return false;
    }

    /** 
     * Gets whether the NamedArgument is mutually exclusive or not to the provided NamedArgument.
     * 
     * @param arg NamedArgument object representing the other NamedArgument we are checking
     * @return    true if the current NamedArgument is mutually exclusive with the provided one, false 
     *            otherwise
     */
    public boolean isMutuallyExclusive(NamedArgument arg) {
        if (mutexArgs.contains(arg.getName())) {
            return true;
        }
        return false;
    }

    /** 
     * Gets whether the NamedArgument is mutually exclusive with the provided NamedArgument.
     * 
     * @param argName string representing the name of the other NamedArgument we are checking
     * @return        either true or false
     */
    public boolean isMutuallyExclusive(String argName) {
        if (mutexArgs.contains(argName)) {
            return true;
        }
        return false;
    }
}
