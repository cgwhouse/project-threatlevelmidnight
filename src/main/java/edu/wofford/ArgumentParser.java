package edu.wofford;

import java.util.*;

public class ArgumentParser {

    private List<String> argumentNames;
    private Map<String, Argument> argumentMap;
    private String programName;
    private String programDescription;

    public ArgumentParser() {
        argumentNames = new ArrayList<String>();
        argumentMap = new HashMap<String, Argument>();
        programName = "Program name not specified.";
        programDescription = "Program description not specified.";
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
        argumentNames.add(name);
        argumentMap.put(name, arg);
    }

    public void setArgumentDescription(String name, String description) {
        Argument arg = argumentMap.get(name);
        arg.setDescription(description);
        argumentMap.replace(name, arg);
    }

    public void setArgumentType(String name, String typeName) {
        Argument arg = argumentMap.get(name);
        arg.setType(typeName);
        argumentMap.replace(name, arg);
    }

    public void setArgumentValues(String[] values) {
        for (int i = 0; i < values.length; i++) {
            if (values[i].equals("-h")) {
                help();
            }
        }
        String error = "";
        if (values.length != argumentNames.size()) {
            if (values.length < argumentNames.size()) {
                handleError(true, makeString(argumentNames.subList(values.length, argumentNames.size())));
            } else {
                for (int i = argumentNames.size(); i < values.length; i++) {
                    error += values[i] + " ";
                }
                handleError(false, error.trim());
            }
        } else {
            int index = 0;
            for (String name : argumentNames) {
                Argument arg = argumentMap.get(name);
                    if (legitamiteValue(arg.getType(), values[index])) {
                        arg.setValue(values[index]);
                        argumentMap.replace(name, arg);
                        index++;
                    }
                    else {
                        error = "argument " + name + ": invalid " + arg.getType() + " value: " + values[index];
                        handleTypeError(error.trim());
                    }
                }
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

    private void handleError(boolean errorType, String error) {
        String message = makeUsageMessage();
        message += programName + ".java: error: ";
        if (errorType) {
            message += "the following arguments are required: ";
        } else {
            message += "unrecognized arguments: ";
        }
        message += error;
        throw new ArgumentException(message);
    }

    private void handleTypeError(String error) {
        String message = makeUsageMessage();
        message += programName + ".java: error: ";
        message += error;
        throw new ArgumentException(message);
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

    private boolean legitamiteValue(String typeName, String value) {
        try {
            if (typeName.equals("int")) {
                Integer.valueOf(value);
            }
            else if (typeName.equals("float")) {
                Float.valueOf(value);
            }
            else if (typeName.equals("boolean")) {
                Boolean.valueOf(value);
            }
            else if (typeName.equals("string")) {
                String.valueOf(value);
            }
        }
        catch (Exception ex){
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
