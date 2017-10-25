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

    public void addArgument(String name) {
        argumentNames.add(name);
        Argument arg = new Argument(name);
        argumentMap.put(name, arg);
    }

    public void addArgument(Argument arg) {
        String name = arg.getName();
        argumentNames.add(name);
        argumentMap.put(name, arg);
    }

    public void setArgumentDescription(String name, String description) {
        Argument arg = argumentMap.get(name);
        arg.setDescription(description);
        argumentMap.replace(name, arg);
    }

    public void setProgramValues(String[] values) {
        int index = 0;
        for (String name: argumentNames) {
            Argument arg = argumentMap.get(name);
            if (value.equals("-h")) {
                handleError("Help");
            }

            argumentValues.add(value);
        }
    }

    public String getValue(String valueName) {
        if ((argumentNames.size() == 0 || argumentValues.size() == 0)
                || (argumentValues.size() != argumentNames.size())) {
            handleError("Error");
        }

        return argumentValues.get(argumentNames.indexOf(valueName));
    }

    private void handleError(String messageType) {
        String message = "usage: java " + programName + " " + makeString(argumentNames) + "\n";

        if (messageType.equals("Help")) {
            String decrArgs = "positional arguments:\n";
            message += programDescription + "\n";
            for (String description : argumentDescriptions) {
                decrArgs += description + "\n";
            }
            message += decrArgs.trim();
        } else {
            String badArgs = "";
            message += programName + ".java: error: ";
            if (argumentNames.size() < argumentValues.size()) {
                message += "unrecognized arguments: ";
                for (String value : argumentValues.subList(argumentNames.size(), argumentValues.size())) {
                    badArgs += value + " ";
                }
            } else if (argumentNames.size() > argumentValues.size()) {
                message += "the following arguments are required: ";
                for (String name : argumentNames.subList(argumentValues.size(), argumentNames.size())) {
                    badArgs += name + " ";
                }
            }
            message += badArgs.trim();
        }

        throw new ArgumentException(message);
    }

    private String makeString(List<String> list) {
        String result = "";
        for (String item : list) {
            result += item + " ";
        }
        return result.trim();
    }
}
