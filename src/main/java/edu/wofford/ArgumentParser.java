package edu.wofford;

import java.util.*;
// test

/**
 * ArgumentParser is a command-line parsing utility that is able to structure and create various options for various programs.
 * <p>
 * ArgumentParser is able to handle required arguments, named arguments and boolean flags.
 * Required arguments and named arguments can be of type string, integer, float, or boolean.
 * <p><strong>
 * Example 1:
 * </strong>
 * <pre>
 * {@code
 * private ArgumentParser parser;
 * parser = new ArgumentParser("Example 1 Program");
 * String[] argumentNames = { "requiredArg1", "requiredArg2", "requiredArg3" };
 * parser.setArguments(argumentNames);
 * parser.setArgumentValues(args);
 * }
 * </pre>
 * In Example 1, we create an ArgumentParser for our program, "Example 1 Program".
 * We then specify the name of the arguments our program requires: "requiredArg1", "requiredArg2", "requriedArg3".
 * We then pass in the argument values from the user.
 * The user's values come via the execution of your program from the command line (args).
 * <p><strong>
 * Example 2:
 * </strong>
 * <pre>
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
 * </pre>
 * In Example 2, we create an ArgumentParser for our program, "Example 2 Program".
 * We then specify the name of the arguments our program requires in two ways.
 * The first way is using an Argument object; we set one of the required arguments by creating an Argument object and passing it to setArgument.
 * The second way is using setArgument but we provide the string name of the argument.
 * We then pass in the argument values from the user.
 * The user's values come via the execution of your program from the command line (args).
 * <p><strong>
 * Example 3:
 * </strong>
 * <pre>
 * {@code
 * private ArgumentParser parser;
 * parser = new ArgumentParser("Example 3 Program");
 * String[] argumentNames = { "requiredArg1", "requiredArg2", "requiredArg3" };
 * NamedArgument arg = new NamedArgument("--type", "ellipsoid");
 * parser.setArguments(argumentNames);
 * parser.setArgument(arg);
 * parser.setArgumentValues(args);
 * }
 * </pre>
 * In Example 3, we create an ArgumentParser for our program, "Example 3 Program".
 * We then specify the name of the arguments our program requires by using setArguments.
 * We then pass in the NamedArgument object, "--type" with the default value of "ellipsoid".
 * The user's values come via the execution of your program from the command line (args).
 * If the user's values coming from the command line (args) do not specify the --type argument, then the default "ellipsoid" will be used in our program.
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
     * Gets the values of the argument with the associated name.
     * 
     * @param name the name of the argument whose values are wanted
     * @return     a list of string representations of the argument's values
     */
    public List<String> getValues(String name) {
        Argument arg = argumentMap.get(name);
        return arg.getMultipleValues();
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

    public Argument getArg(String name) {
        return argumentMap.get(name);
    }
    //endregion

    /**
     * Parses the values taken from the command line and sets them to their respective arguments. If the
     * value "-h" or "--help" is detected, a help message will be displayed to the user.
     * 
     * @param values                              string of values from the command line
     * @throws UnrecognizedArgumentException      if an argument is provided that the ArgumentParser does not
     *                                            recognize
     * @throws MissingRequiredArgumentException   if a required argument's value is not provided
     * @throws InvalidTypeException               if an argument's value does not match its expected type
     * @throws HelpException                      if the value "-h" or "--help" is provided
     * @throws MutuallyExclusiveArgumentException if two provided arguments must not be provided at the same
     *                                            time
     */
    public void setArgumentValues(String[] values) {
        Queue<String> queue = new ArrayDeque<>();
        for (String s : values) {
            queue.add(s);
        }
        String msg = makeUsageMessage();
        int positionalIndex = 0;
        List<String> encounteredMutex = new ArrayList<String>();
        while (!queue.isEmpty()) {
            String arg = queue.remove();
            if (arg.equals("-h") || arg.equals("--help")) {
                help();
            } else if (arg.startsWith("--")) {
                parseNamed(arg, queue, encounteredMutex);
            } else if (arg.startsWith("-") && !arg.startsWith("--")) {
                if (arg.length() > 2) {
                    parseMultipleFlags(arg, msg, encounteredMutex);
                } else {
                    parseShortform(arg, queue, encounteredMutex);
                }
            } else {
                if (positionalIndex >= positionalArgs.size()) {
                    msg += programName + ".java: error: unrecognized arguments: " + arg;
                    throw new UnrecognizedArgumentException(msg);
                } else {
                    positionalIndex = parsePositional(positionalIndex, arg, queue);
                }
            }
        }
        if (positionalIndex < positionalArgs.size()) {
            msg += programName + ".java: error: the following arguments are required: "
                    + positionalArgs.get(positionalIndex);
            throw new MissingRequiredArgumentException(msg);
        }
        parseRequired(msg);
        parseMutuallyExclusive(encounteredMutex, msg);
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
    private void parseNamed(String arg, Queue<String> queue, List<String> encounteredMutex) {
        if (argumentMap.containsKey(arg)) {
            Argument current = argumentMap.get(arg);
            if (current.getType().equals("boolean")) {
                checkAndSet(current, "true");
            } else {
                String value = queue.remove();
                checkAndSet(current, value);
            }
            checkForMutexEncounter(current, encounteredMutex);
        }
    }

    private void parseMultipleFlags(String arg, String msg, List<String> mutex) {
        for (int i = 1; i < arg.length(); i++) {
            String name = "-" + Character.toString(arg.charAt(i));
            if (argumentMap.containsKey(name)) {
                checkAndSet(argumentMap.get(name), "true");
                checkForMutexEncounter(argumentMap.get(name), mutex);
            } else if (shortFormMap.containsKey(name)) {
                checkAndSet(argumentMap.get(shortFormMap.get(name)), "true");
                checkForMutexEncounter(argumentMap.get(shortFormMap.get(name)), mutex);
            } else {
                msg += programName + ".java: error: unrecognized flag: " + name;
                throw new UnrecognizedArgumentException(msg);
            }
        }
    }

    private void parseShortform(String arg, Queue<String> queue, List<String> encounteredMutex) {
        if (argumentMap.containsKey(arg)) {
            Argument flag = argumentMap.get(arg);
            checkAndSet(flag, "true");
        } else if (shortFormMap.containsKey(arg)) {
            String longForm = argumentMap.get(shortFormMap.get(arg)).getName();
            parseNamed(longForm, queue, encounteredMutex);
        }
    }

    private int parsePositional(int positionalIndex, String arg, Queue<String> queue) {
        String posName = positionalArgs.get(positionalIndex);
        positionalIndex++;
        Argument current = argumentMap.get(posName);
        int n = current.getNumberOfValuesExpected();
        checkAndSet(current, arg, n, queue);
        return positionalIndex;
    }

    private void help() {
        String message = makeUsageMessage();
        String decrArgs = "positional arguments:\n";
        message += programDescription + "\n";
        for (String name : positionalArgs) {
            Argument arg = argumentMap.get(name);
            decrArgs += name + " " + arg.getDescription() + "\n";
        }
        for (String name : namedArgs) {
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

    private void checkAndSet(Argument current, String argValue, int valuesCount, Queue<String> remainingValues) {
        while (valuesCount > 0) {
            checkAndSet(current, argValue);
            valuesCount--;
            if (valuesCount > 0) {
                try {
                    argValue = remainingValues.remove();
                } catch (NoSuchElementException e) {
                    String msg = makeUsageMessage();
                    msg += programName + ".java: error: argument " + current.getName() + " requires "
                            + current.getNumberOfValuesExpected() + " values";
                    throw new NotEnoughValuesException(msg);
                }
            }
        }
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

    private String checkMutuallyExclusiveNamedArgs(List<String> mutuallyExclusiveArgs,
            NamedArgument mutuallyExclusiveArg) {
        String retVal = "";
        for (String argName : mutuallyExclusiveArgs) {
            NamedArgument arg = (NamedArgument) argumentMap.get(argName);
            if (mutuallyExclusiveArg.isMutuallyExclusive(argName)
                    || arg.isMutuallyExclusive(mutuallyExclusiveArg.getName())) {
                return argName;
            }
        }
        return retVal;
    }

    private void parseMutuallyExclusive(List<String> encounteredMutuallyExclusiveNamedArgs, String msg) {
        List<String> mutuallyExclusiveNamedArgs = new ArrayList<String>();
        for (String name : encounteredMutuallyExclusiveNamedArgs) {
            NamedArgument arg = (NamedArgument) argumentMap.get(name);
            if (arg.hasMutualExclusiveArgs()) {
                if (mutuallyExclusiveNamedArgs.size() == 0) {
                    mutuallyExclusiveNamedArgs.add(arg.getName());
                } else {
                    String mutuallyExclusiveArgConflict = checkMutuallyExclusiveNamedArgs(mutuallyExclusiveNamedArgs,
                            arg);
                    if (!mutuallyExclusiveArgConflict.equals("")) {
                        msg += programName + ".java: error: the following arguments are mutually exclusive: " + name
                                + " and " + mutuallyExclusiveArgConflict;
                        throw new MutuallyExclusiveArgumentException(msg);
                    }
                }
            }
        }
    }

    private void parseRequired(String msg) {
        for (String name : namedArgs) {
            NamedArgument arg = (NamedArgument) argumentMap.get(name);
            List<String> values = arg.getMultipleValues();
            if (arg.isRequired() && values.isEmpty()) {
                msg += programName + ".java: error: the following arguments are required: " + name;
                throw new MissingRequiredArgumentException(msg);
            }
        }
    }

    private void checkForMutexEncounter(Argument current, List<String> encounteredMutuallyExclusiveNamedArgs) {
        if (namedArgs.contains(current.getName())) {
            NamedArgument currentNamedArgument = (NamedArgument) current;
            if (currentNamedArgument.hasMutualExclusiveArgs()) {
                encounteredMutuallyExclusiveNamedArgs.add(currentNamedArgument.getName());
            }
        }
    }
    //endregion

}
