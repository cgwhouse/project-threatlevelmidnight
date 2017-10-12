package edu.wofford;

import java.util.*;

public class ArgumentParser {
    private List<String> argumentNames;
    
    private List<String> argumentValues;
    
    public ArgumentParser(String[] names, String[] values) {
        argumentNames = new ArrayList<String>();
        
        argumentValues = new ArrayList<String>();
        
        for (String name: names) {
            argumentNames.add(name);
        }
        
        for (String value: values) {
            argumentValues.add(value);
        }
    }
    
    public String getValue(String valueName) {
        return argumentValues.get(argumentNames.indexOf(valueName));
    }
    
    public String getValue(int valuePosition) {
        return argumentValues.get(valuePosition);
    }
}