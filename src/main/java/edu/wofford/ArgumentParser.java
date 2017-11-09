package edu.wofford;

import java.util.*;

public class ArgumentParser {

    private List<String> argumentNames;
    private Map<String, Argument> argumentMap;
    private String programName;
    private String programDescription;

    public ArgumentParser(String programName) {
        argumentNames = new ArrayList<String>();
        argumentMap = new HashMap<String, Argument>();
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

    public void setArgumentDescription(String name, String description) {
        Argument arg = argumentMap.get(name);
        arg.setDescription(description);
    }

    public void setArgumentType(String name, String typeName) {
        Argument arg = argumentMap.get(name);
        arg.setType(typeName);
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
                        if(legitimateValue(current.getType(), value)) {
                            current.setValue(value);
                        }
                        else {
                            msg += programName + ".java: error: argument " + current.getName() + ": invalid " + current.getType() + " value: " + value;
                            throw new ArgumentException(msg);
                        }
                    }
                }
            } else {
                if(positionalIndex >= argumentNames.size()) {
                    msg += programName + ".java: error: unrecognized arguments: " + arg;
                    throw new ArgumentException(msg);
                }
                else {
                    String posName = argumentNames.get(positionalIndex);
                    positionalIndex++;
                    Argument current = argumentMap.get(posName);
                    if(legitimateValue(current.getType(), arg)) {
                        current.setValue(arg);                        
                    }
                    else {
                        msg += programName + ".java: error: argument " + current.getName() + ": invalid " + current.getType() + " value: " + arg;
                        throw new ArgumentException(msg);                       
                    }
                }
            }
        }
        if(positionalIndex < argumentNames.size()) {
            msg += programName + ".java: error: the following arguments are required: " + argumentNames.get(positionalIndex);
            throw new ArgumentException(msg);
        }
    }

    public String getValue(String name) {
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

    // private <T> T castValue(String typeName, String value) {
    //     switch (typeName) {
    //         case typeName.equals("int"):
    //             return (T) Integer.valueOf(value);
    //         case typeName.equals("float"):
    //             return (T) float.valueOf(value);
    //         case typeName.equals("boolean"):
    //             return (T) boolean.valueOf(value);
    //         default:
    //             return value;
    //     }
    // }
}
