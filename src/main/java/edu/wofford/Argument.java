package edu.wofford;

import java.util.*;

public class Argument {

    private String name;
    private String value;
    private String description;
    private String type;

    public Argument(String name) {
        this.name = name;
        value = "";
        description = "";
        type = "";
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    public String getDescription() {
        return description;
    }

    public String getType() {
        return type;
    }
}
