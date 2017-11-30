package edu.wofford;

import java.util.*;

/**
 * ArgumentParser is a command-line parsing utility that is able to structure and create various options for various programs.
 * <p>
 * ArgumentParser is able to handle required arguments, named arguments and boolean flags.
 * Required arguments and named arguments can be of type string, integer, float, or boolean.
 * <pre>
 * Example 1:
 * {@code
 * private ArgumentParser parser;
 * parser = new ArgumentParser("Example 1 Program");
 * String[] argumentNames = { "requiredArg1", "requiredArg2", "requiredArg3" };
 * parser.setArguments(argumentNames);
 * parser.setArgumentValues(args);
 * }
 * In Example 1, we create an ArgumentParser for our program, "Example 1 Program".
 * We then specify the name of the arguments our program requires: "requiredArg1", "requiredArg2", "requriedArg3".
 * We then pass in the argument values from the user.
 * The user's values come via the execution of your program from the command line (args).
 * </pre>
 * <pre>
 * Example 2:
 * {@code
 * private ArgumentParser parser;
 * parser = new ArgumentParser("Example 2 Program");
 * String[] argumentNames = { "requiredArg1", "requiredArg2", "requiredArg3" };
 * Argument arg = new Argument(argumentNames[0]);
 * parser.setArgument(arg);
 * parser.setArgument(argumentNames[1]);
 * parser.setArgument(argumentNames[2]);
 * parser.setArgumentValues(args);
 * }
 * In Example 2, we create an ArgumentParser for our program, "Example 2 Program".
 * We then specify the name of the arguments our program requires in two ways.
 * The first way is using an Argument object; we set one of the required arguments by creating an Argument object and passing it to setArgument.
 * The second way is using setArgument but we provide the string name of the argument.
 * We then pass in the argument values from the user.
 * The user's values come via the execution of your program from the command line (args).
 * </pre>
 * <pre>
 * Example 3:
 * {@code
 * private ArgumentParser parser;
 * parser = new ArgumentParser("Example 3 Program");
 * String[] argumentNames = { "requiredArg1", "requiredArg2", "requiredArg3" };
 * NamedArgument arg = new NamedArgument("--type", "ellipsoid");
 * parser.setArguments(argumentNames);
 * parser.setArgument(arg);
 * parser.setArgumentValues(args);
 * }
 * In Example 3, we create an ArgumentParser for our program, "Example 3 Program".
 * We then specify the name of the arguments our program requires by using setArguments.
 * We then pass in the NamedArgument object, "--type" with the default value of "ellipsoid".
 * The user's values come via the execution of your program from the command line (args).
 * If the user's values coming from the command line (args) do not specify the --type argument, then the default "ellipsoid" will be used in our program.
 * </pre>
 */
public class ArgumentParser {

    private List<String> positionalArgs;
    private List<String> namedArgs;
    private Map<String, Argument> argumentMap;
    private Map<String, String> shortFormMap;
    private String programName;
    private String programDescription;

    /** 
     * Constructs an ArgumentParser object which requires the program name as a string.
     * 
     * @param programName the name of the program
     */
    public ArgumentParser(String programName) {
        positionalArgs = new ArrayList<String>();
        namedArgs = new ArrayList<String>();
        argumentMap = new HashMap<String, Argument>();
        shortFormMap = new HashMap<String, String>();
        this.programName = programName;
        programDescription = "";
    }

    //region Gets and Sets
    /** 
     * Sets the name of the program. This will change the program name from the default that is set
     * with the constructor.
     * 
     * @param name the new name of the program as a string
     */
    public void setProgramName(String name) {
        programName = name;
    }

    /** 
     * Sets the description of the program.
     * 
     * @param description the description of the program as a string
     */
    public void setProgramDescription(String description) {
        programDescription = description;
    }

    /** 
     * Sets the names of the arguments. For each name in the array, an argument is created.
     * Note the argument <strong>"-h"</strong> is not allowed.
     * 
     * @param names an array of names as strings
     */
    public void setArguments(String[] names) {
        for (String name : names) {
            if (!name.equals("-h")) {
                positionalArgs.add(name);
                Argument arg = new Argument(name);
                argumentMap.put(name, arg);
            }
        }
    }

    /** 
     * Sets one argument. Given a name, an argument is created and set. Note the argument 
     * <strong>"-h"</strong> is not allowed.
     * 
     * @param name the desired name of the argument as string
     */
    public void setArgument(String name) {
        if (!name.equals("-h")) {
            positionalArgs.add(name);
            Argument arg = new Argument(name);
            argumentMap.put(name, arg);
        }
    }

    /** 
     * Sets one argument. The given argument is set for the parser. Note the argument with name 
     * <strong>"-h"</strong> is not allowed.
     * 
     * @param arg the Argument object to be set
     * @see Argument
     */
    public void setArgument(Argument arg) {
        String name = arg.getName();
        if (!name.equals("-h")) {
            if (!name.startsWith("--")) {
                positionalArgs.add(name);
            } else {
                namedArgs.add(name);
            }
            argumentMap.put(name, arg);
        }
    }

    /**
     * Assigns a short-form name to an Argument object. This method assumes that the argument object has 
     * already been created with its attributes (e.g. type, default value, description) set as desired. 
     * The method should be used when an Argument object has been fully defined and is ready to be given a
     * nickname.
     * 
     * @param arg            Argument object to be given an alias and then set as an argument for the parser
     * @param shortFormNames string starting with "-" that contains the desired short form name for the 
     *                       Argument object. If the string contains multiple letters, each letter will be 
     *                       assigned as an individual alias for the Argument object.
     */
    public void setNickname(NamedArgument arg, String shortFormNames) {
        argumentMap.put(arg.getName(), arg);
        namedArgs.add(arg.getName());
        for (int i = 1; i < shortFormNames.length(); i++) {
            String name = "-" + Character.toString(shortFormNames.charAt(i));
            if (!name.equals("-h")) {
                shortFormMap.put(name, arg.getName());
                arg.addNickname(Character.toString(shortFormNames.charAt(i)));
            }
        }
    }

    /**
     * Sets an argument's description. The argument to be modified must already be known by the
     * ArgumentParser.
     * 
     * @param name        the name of the argument whose description we want to set
     * @param description the description to set
     */
    public void setArgumentDescription(String name, String description) {
        Argument arg = argumentMap.get(name);
        arg.setDescription(description);
    }

    /**
     * Sets an argument's type. The argument to be modified must already be known by the ArgumentParser.
     * 
     * @param name     the name of the argument whose type we want to set
     * @param typeName the type to set, options are int, float, boolean, and string
     */
    public void setArgumentType(String name, String typeName) {
        Argument arg = argumentMap.get(name);
        arg.setType(typeName);
    }

    /**
     * Set any number of short-form named flag arguments, specified in a single string starting with "-".
     * Each flag's initial value will be set to false, and flags that are set using this method will only
     * be accessible via the short-form name (e.g. "-a") that is provided in the parameter.
     * 
     * @param flag a string starting with "-" and followed by any number of characters, each of which will
     *             be a new flag to be recognized by the ArgumentParser.
     */
    public void setFlags(String flag) {
        if (flag.startsWith("-") && !flag.startsWith("--")) {
            for (int i = 1; i < flag.length(); i++) {
                String name = "-" + Character.toString(flag.charAt(i));
                if (!name.equals("-h")) {
                    NamedArgument shortFormFlag = new NamedArgument(name, "false");
                    shortFormFlag.setType("boolean");
                    argumentMap.put(name, shortFormFlag);
                }
            }
        }
    }

    /**
    * Gets the name of the program whose arguments the ArgumentParser is parsing.
    * 
    * @return the name of the program
    */
    public String getProgramName() {
        return programName;
    }

    /** 
     * Gets the value of the argument with the associated name.
     *
     * @param name      the name of the argument whose value is wanted
     * @return          string representation of the argument's value
     */
    public String getValue(String name) {
        if (name.startsWith("-")) {
            if (shortFormMap.containsKey(name)) {
                name = shortFormMap.get(name);
            }
        }
        Argument arg = argumentMap.get(name);
        return arg.getValue();
    }

    /** 
     * Gets the description of the argument with the associated name.
     *
     * @param name      the name of the argument whose description is wanted
     * @return          string representation of the argument's description
     */
    public String getDescription(String name) {
        Argument arg = argumentMap.get(name);
        return arg.getDescription();
    }

    /**
     * Gets the type of the argument with the associated name.
     * 
     * @param name the name of the argument whose type is wanted
     * @return     string representation of the argument's type
     */
    public String getType(String name) {
        Argument arg = argumentMap.get(name);
        return arg.getType();
    }
    //endregion

    /**
     * Parses the values taken from the command line and sets them to their respective arguments. If the
     * value "-h" or "--help" is detected, a help message will display to the user.
     * 
     * @param values                            string of values from the command line
     * @throws UnrecognizedArgumentException    if an argument is provided that the ArgumentParser does not
     *                                          recognize
     * @throws MissingRequiredArgumentException if a required argument's value is not provided
     * @throws InvalidTypeException             if an argument's value does not match its expected type
     * @throws HelpException                    if the value "-h" or "--help" is provided
     */
    public void setArgumentValues(String[] values) {
        List<String> encounteredMutuallyExclusiveNamedArgs = new ArrayList<String>();
        Queue<String> queue = new ArrayDeque<>();
        for (String s : values) {
            queue.add(s);
        }
        String msg = makeUsageMessage();
        int positionalIndex = 0;
        while (!queue.isEmpty()) {
            String arg = queue.remove();
            if (arg.equals("-h") || arg.equals("--help")) {
                help();
            } else if (arg.startsWith("--")) {
                if (argumentMap.containsKey(arg)) {
                    Argument current = argumentMap.get(arg);
                    if (current.getType().equals("boolean")) {
                        current.setValue("true");
                    } else {
                        String value = queue.remove();
                        checkAndSet(current, value);
                    }
                    if (namedArgs.contains(current.getName())) {
                        NamedArgument currentNamedArgument = (NamedArgument)current;
                        if (currentNamedArgument.hasMutualExclusiveArgs()){
                            encounteredMutuallyExclusiveNamedArgs.add(currentNamedArgument.getName());
                        }
                    }
                }
            } else if (arg.startsWith("-") && !arg.startsWith("--")) {
                if (arg.length() > 2) {
                    for (int i = 1; i < arg.length(); i++) {
                        String name = "-" + Character.toString(arg.charAt(i));
                        if (argumentMap.containsKey(name)) {
                            Argument flag = argumentMap.get(name);
                            flag.setValue("true");
                        } else if (shortFormMap.containsKey(name)) {
                            Argument longForm = argumentMap.get(shortFormMap.get(name));
                            longForm.setValue("true");
                        } else {
                            msg += programName + ".java: error: unrecognized flag: " + name;
                            throw new UnrecognizedArgumentException(msg);
                        }
                    }
                } else {
                    if (argumentMap.containsKey(arg)) {
                        Argument flag = argumentMap.get(arg);
                        flag.setValue("true");
                    } else if (shortFormMap.containsKey(arg)) {
                        Argument longForm = argumentMap.get(shortFormMap.get(arg));
                        if (longForm.getType().equals("boolean")) {
                            longForm.setValue("true");
                        } else {
                            String value = queue.remove();
                            checkAndSet(longForm, value);
                        }
                    }
                }
            } else {
                if (positionalIndex >= positionalArgs.size()) {
                    msg += programName + ".java: error: unrecognized arguments: " + arg;
                    throw new UnrecognizedArgumentException(msg);
                } else {
                    String posName = positionalArgs.get(positionalIndex);
                    positionalIndex++;
                    Argument current = argumentMap.get(posName);
                    checkAndSet(current, arg);
                }
            }
        }
        if (positionalIndex < positionalArgs.size()) {
            msg += programName + ".java: error: the following arguments are required: "
                    + positionalArgs.get(positionalIndex);
            throw new MissingRequiredArgumentException(msg);
        }
        
        List<String> mutuallyExclusiveNamedArgs = new ArrayList<String>();

        for (String name : encounteredMutuallyExclusiveNamedArgs) {
            NamedArgument arg = (NamedArgument)argumentMap.get(name);

            if (arg.isRequired() && arg.getValue().equals("")) {
                msg += programName + ".java: error: the following arguments are required: " + name;
                throw new MissingRequiredArgumentException(msg);
            }

            if (arg.hasMutualExclusiveArgs()) {
                if (mutuallyExclusiveNamedArgs.size() == 0) {
                    mutuallyExclusiveNamedArgs.add(arg.getName());
                }
                else {
                    String mutuallyExclusiveArgConflict = checkMutuallyExclusiveNamedArgs(mutuallyExclusiveNamedArgs, arg);
                    if (!mutuallyExclusiveArgConflict.equals("")) {
                        msg += programName + ".java: error: the following arguments are mutually exclusive: " + name + " and " + mutuallyExclusiveArgConflict;
                        throw new MutuallyExclusiveArgumentException(msg);
                    }
                }
            }
        }
    }

    /**
     * Reads argument information from an XML file and adds them to the ArgumentParser object's known arguments.
     * 
     * @param filename the name of the file to read from
     */
    public void parseXML(String filename) {
        XML.parseXML(filename, this);
    }

    /**
     * Creates a string that contains all of the parser's argument information, formatted as XML.
     * 
     * @param createFile if true, XML string is written to a newly created file in the current directory
     * @return           string of parser's argument information formatted as XML
     */
    public String createXML(boolean createFile) {
        return XML.createXML(createFile, positionalArgs, namedArgs, argumentMap);
    }

    //region Private Methods
    private void help() {
        String message = makeUsageMessage();
        String decrArgs = "positional arguments:\n";
        message += programDescription + "\n";
        for (String name : positionalArgs) {
            Argument arg = argumentMap.get(name);
            decrArgs += name + " " + arg.getDescription() + "\n";
        }
        message += decrArgs.trim();
        throw new HelpException(message);
    }

    private String makeString(List<String> list) {
        String result = "";
        for (String item : list) {
            result += item + " ";
        }
        return result.trim();
    }

    private String makeUsageMessage() {
        return "usage: java " + programName + " " + makeString(positionalArgs) + "\n";
    }

    private void checkAndSet(Argument current, String value) {
        Set<String> accepted = current.getAcceptedValues();
        if (!accepted.isEmpty()) {
            if (!accepted.contains(value)) {
                String msg = makeUsageMessage();
                msg += programName + ".java: error: argument " + current.getName() + ": unaccepted value: " + value;
                throw new UnacceptedValueException(msg);
            }
        }
        if (legitimateValue(current.getType(), value)) {
            current.setValue(value);
        } else {
            String msg = makeUsageMessage();
            msg += programName + ".java: error: argument " + current.getName() + ": invalid " + current.getType()
                    + " value: " + value;
            throw new InvalidTypeException(msg);
        }
    }

    private boolean legitimateValue(String typeName, String value) {
        try {
            if (typeName.equals("int")) {
                Integer.valueOf(value);
            } else if (typeName.equals("float")) {
                Float.valueOf(value);
            } else if (typeName.equals("boolean")) {
                Boolean.valueOf(value);
            } else if (typeName.equals("string")) {
                String.valueOf(value);
            }
        } catch (Exception ex) {
            return false;
        }
        return true;
    }

    private String checkMutuallyExclusiveNamedArgs(List<String> mutuallyExclusiveArgs, NamedArgument mutuallyExclusiveArg) {
        String retVal = "";

        for (String argName : mutuallyExclusiveArgs) {
            NamedArgument arg = (NamedArgument)argumentMap.get(argName);

            if (mutuallyExclusiveArg.isMutuallyExclusive(argName) || arg.isMutuallyExclusive(mutuallyExclusiveArg.getName())){
                return argName;
            }
        }

        return retVal;
    }
    //endregion

}
