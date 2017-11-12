package edu.wofford;

import java.util.*;

public class ArgumentParser {

    private List<String> argumentNames;
    private Map<String, Argument> argumentMap;
    private String programName;
    private String programDescription;
    private Map<String, String> shortFormMap;

    public ArgumentParser(String programName) {
        argumentNames = new ArrayList<String>();
        argumentMap = new HashMap<String, Argument>();
        shortFormMap = new HashMap<String, String>();
        this.programName = programName;
        programDescription = "";
    }

    public void setProgramName(String name) {
        programName = name;
    }

    public void setProgramDescription(String description) {
        programDescription = description;
    }

    public void setArguments(String[] names) {
        for (String name : names) {
            argumentNames.add(name);
            Argument arg = new Argument(name);
            argumentMap.put(name, arg);
        }
    }

    public void setArgument(String name) {
        argumentNames.add(name);
        Argument arg = new Argument(name);
        argumentMap.put(name, arg);
    }

    public void setArgument(Argument arg) {
        String name = arg.getName();
        if (!name.startsWith("--")) {
            argumentNames.add(name);
        }
        argumentMap.put(name, arg);
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

    public void setNamedArgument(Argument arg) {
        argumentMap.put(arg.getName(), arg);
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
                Argument shortFormFlag = new Argument(name);
                shortFormFlag.setValue("false");
                shortFormFlag.setType("boolean");
                argumentMap.put(name, shortFormFlag);
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
                            throw new ArgumentException(msg);
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
                    throw new ArgumentException(msg);
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
            throw new ArgumentException(msg);
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
        throw new ArgumentException(message);
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
            throw new ArgumentException(msg);
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
