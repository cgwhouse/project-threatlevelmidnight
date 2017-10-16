package edu.wofford;

import java.util.*;

public class ArgumentParser {
    private List<String> argumentNames;

    private List<String> argumentValues;

    private void handleError() {
        //    usage: java VolumeCalculator length width height
        //VolumeCalculator.java: error: unrecognized arguments: 43
        String className = new Exception().getStackTrace()[1].getClassName();
        String badArgs = "";
        
        if (argumentNames.size() < argumentValues.size()) {
            for (String value : argumentValues.subList(argumentNames.size(), argumentValues.size())) {
                badArgs += value + " ";
            }
            System.out.println("usage: java " + className + argumentNames.toString());
            System.out.println(className + ".java: error: unrecognized arguments: " + badArgs);
        }
    }

    public ArgumentParser(String[] names, String[] values) {
        argumentNames = new ArrayList<String>();
        argumentValues = new ArrayList<String>();

        for (String name : names) {
            argumentNames.add(name);
        }
        for (String value : values) {
            argumentValues.add(value);
        }

        if (argumentNames.size() != argumentValues.size()) {
            handleError();
        }
    }

    public String getValue(String valueName) {
        return argumentValues.get(argumentNames.indexOf(valueName));
    }

    public String getValue(int valuePosition) {
        return argumentValues.get(valuePosition);
    }
}