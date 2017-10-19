package edu.wofford;

import java.util.*;

public class ArgumentParser {

    private List<String> argumentNames;
    private List<String> argumentValues;
    private List<String> argumentDescriptions;
    private String programName = "Program name not specified.";
    private String programDescription = "Program description not specified.";

    public ArgumentParser() {
        argumentNames = new ArrayList<String>();
        argumentValues = new ArrayList<String>();
    }

    public void setProgramName(String name) {
        programName = name;
    }
    
    public void setProgramDescription(String description) {
        programDescription = description;
    }

    public void setProgramNames(String[] names) {
        for (String name : names) {
            argumentNames.add(name);
        }
    }
    
    public void setArgumentDescriptions(String[] descriptions) {
        for (String description: descriptions) {
            if (description.equals("-h")) {
                handleError();
            }
            
            argumentDescriptions.add(description);
        }
    }

    public void setProgramValues(String[] values) {
        for (String value : values) {
            argumentValues.add(value);
        }
    }

    public String getValue(String valueName) {
        if ((argumentNames.size() == 0 || argumentValues.size() == 0) || (argumentValues.size() != argumentNames.size())) {
            handleError();
        }
        
        return argumentValues.get(argumentNames.indexOf(valueName));
    }

    private void handleError() {
        String message = "usage: java " + programName + " " + makeString(argumentNames) + "\n";

        if (argumentValues.contains("-h")) {
            String decrArgs = "positional arguments:\n";
            message += programDescription + "\n";
            for (String description : argumentDescriptions) {
                decrArgs += "\t" + description + "\n";
            }
            message += decrArgs.trim();
        }
        else {
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
