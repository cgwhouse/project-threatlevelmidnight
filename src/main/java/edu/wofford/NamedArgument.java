package edu.wofford;

public class NamedArgument extends Argument {
    public NamedArgument(String name, String value) {
        super(name);
        this.setValue(value);
    }
}
