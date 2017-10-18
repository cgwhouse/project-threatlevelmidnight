package edu.wofford;

import java.util.*;

public class ArgumentParser {
    
    private List<String> argumentNames;

    private List<String> argumentValues;
    
    private String programName;
    
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

    public ArgumentParser() {
        argumentNames = new ArrayList<String>();
        
        argumentValues = new ArrayList<String>();
    }
    
    public void setProgramName(String name) {
        programName = name;
    }
    
    public void setProgramValues(String[] values) {
        for (String value : values) {
            argumentValues.add(value);
        }
        
        if (argumentNames.size() != 0 && argumentNames.size() != argumentValues.size()) {
            handleError();
        }
    }
    
    public void setProgramNames(String[] names) {
        for (String name : names) {
            argumentNames.add(name);
        }
        
        if (argumentValues.size() != 0 && argumentValues.size() != argumentNames.size()) {
            handleError();
        }
    }

    public String getValue(String valueName) {
        return argumentValues.get(argumentNames.indexOf(valueName));
    }
}