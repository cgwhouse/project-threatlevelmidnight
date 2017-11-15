package edu.wofford;

import java.util.*;

public class ArgumentParser {

    private List<String> argumentNames;
    private Map<String, Argument> argumentMap;
    private String programName;
    private String programDescription;
    private Map<String, String> shortFormMap;

	/** Constructs an ArgumentParser object 
	 * which requires the program name as a string.
	 * @param programName The name of the program.
	 */
    public ArgumentParser(String programName) {
        argumentNames = new ArrayList<String>();
        argumentMap = new HashMap<String, Argument>();
        shortFormMap = new HashMap<String, String>();
        this.programName = programName;
        programDescription = "";
    }

	/** Sets the name of the program. 
	 * This will change the program name from the default that is set
	 * with the constructor.
	 * @param name The new name of the program as a string.
	 */
    public void setProgramName(String name) {
        programName = name;
    }

	/** Sets the description of the program.
	 * @param description The description of the program as a string.
	 */
    public void setProgramDescription(String description) {
        programDescription = description;
    }

	/** Sets the names of the arguments
	 * For each name in the array, an argument is created.
	 * Note the argument <strong>"-h"</strong> is not allowed.
	 * @param names An array of names as strings.
	 */
    public void setArguments(String[] names) {
        for (String name : names) {
            if (!name.equals("-h")) {
                argumentNames.add(name);
                Argument arg = new Argument(name);
                argumentMap.put(name, arg);
            }
        }
    }

	/** Sets one argument.
	 * Given a name, an argument is created and set.
	 * Note the argument <strong>"-h"</strong> is not allowed.
	 * @param name A name of the argument as string.
	 */
    public void setArgument(String name) {
        if (!name.equals("-h")) {
            argumentNames.add(name);
            Argument arg = new Argument(name);
            argumentMap.put(name, arg);
        }
    }

	/** Sets one argument.
	 * The given argument is set for the parser.
	 * Note the argument with name <strong>"-h"</strong> is not allowed.
	 * @param arg An Argument object.
	 * @see Argument
	 */
    public void setArgument(Argument arg) {
        String name = arg.getName();
        if (!name.equals("-h")) {
            if (!name.startsWith("--")) {
                argumentNames.add(name);
            }
            argumentMap.put(name, arg);
        }
    }

    public void setNamedArgument(Argument arg, String shortFormNames) {
        argumentMap.put(arg.getName(), arg);
        for (int i = 1; i < shortFormNames.length(); i++) {
            String name = "-" + Character.toString(shortFormNames.charAt(i));
            if (!name.equals("-h")) {
                shortFormMap.put(name, arg.getName());
            }
        }
    }

    public void setArgumentDescription(String name, String description) {
        Argument arg = argumentMap.get(name);
        arg.setDescription(description);
    }

    public void setArgumentType(String name, String typeName) {
        Argument arg = argumentMap.get(name);
        arg.setType(typeName);
    }

    public void setFlags(String flag) {
        if (flag.startsWith("-") && !flag.startsWith("--")) {
            for (int i = 1; i < flag.length(); i++) {
                String name = "-" + Character.toString(flag.charAt(i));
                if (!name.equals("-h")){
                    Argument shortFormFlag = new Argument(name);
                    shortFormFlag.setValue("false");
                    shortFormFlag.setType("boolean");
                    argumentMap.put(name, shortFormFlag);
                }
            }
        }
    }

    public void setArgumentValues(String[] values) {
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
                        //ADD WHILE LOGIC TO CHOMP MORE VALUES IN FUTURE FEATURES
                        if (longForm.getType().equals("boolean")) {
                            longForm.setValue("true");
                        } else {
                            String value = queue.remove();
                            checkAndSet(longForm, value);
                        }
                    }
                }
            } else {
                if (positionalIndex >= argumentNames.size()) {
                    msg += programName + ".java: error: unrecognized arguments: " + arg;
                    throw new UnrecognizedArgumentException(msg);
                } else {
                    String posName = argumentNames.get(positionalIndex);
                    positionalIndex++;
                    Argument current = argumentMap.get(posName);
                    checkAndSet(current, arg);
                }
            }
        }
        if (positionalIndex < argumentNames.size()) {
            msg += programName + ".java: error: the following arguments are required: "
                    + argumentNames.get(positionalIndex);
            throw new MissingRequiredArgumentException(msg);
        }
    }

    public String getProgramName() {
        return programName;
    }

    public String getValue(String name) {
        if (name.startsWith("-")) {
            if (shortFormMap.containsKey(name)) {
                name = shortFormMap.get(name);
            }
        }
        Argument arg = argumentMap.get(name);
        return arg.getValue();
    }

    public String getDescription(String name) {
        Argument arg = argumentMap.get(name);
        return arg.getDescription();
    }

    public String getType(String name) {
        Argument arg = argumentMap.get(name);
        return arg.getType();
    }

    private void help() {
        String message = makeUsageMessage();
        String decrArgs = "positional arguments:\n";
        message += programDescription + "\n";
        for (String name : argumentNames) {
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
        return "usage: java " + programName + " " + makeString(argumentNames) + "\n";
    }

    private void checkAndSet(Argument current, String value) {
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
}
