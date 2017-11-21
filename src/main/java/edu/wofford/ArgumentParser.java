package edu.wofford;

import java.util.*;
import java.io.FileNotFoundException;
import java.io.FileReader;
import javax.xml.stream.*;
import javax.xml.stream.events.*;

/**
 * ArgumentParser is a command-line parsing utility that is able to structure and create various options for various programs.
 * 
 * ArgumentParser is able to handle required arguments, named arguments and boolean flags.
 * 
 * Required arguments and named arguments can be of type string, integer, float, or boolean.
 * 
 * TODO: Include Examples Demonstrating the Above
 */
public class ArgumentParser {

    private List<String> argumentNames;
    private Map<String, Argument> argumentMap;
    private String programName;
    private String programDescription;
    private Map<String, String> shortFormMap;

    /** 
     * Constructs an ArgumentParser object which requires the program name as a string.
     * 
     * @param programName the name of the program
     */
    public ArgumentParser(String programName) {
        argumentNames = new ArrayList<String>();
        argumentMap = new HashMap<String, Argument>();
        shortFormMap = new HashMap<String, String>();
        this.programName = programName;
        programDescription = "";
    }

    /** 
     * Sets the name of the program. This will change the program name from the default that is set
     * with the constructor.
     * 
     * @param name the new name of the program as a string
     */
    public void setProgramName(String name) {
        programName = name;
    }

    /** 
     * Sets the description of the program.
     * 
     * @param description the description of the program as a string
     */
    public void setProgramDescription(String description) {
        programDescription = description;
    }

    /** 
     * Sets the names of the arguments. For each name in the array, an argument is created.
     * Note the argument <strong>"-h"</strong> is not allowed.
     * 
     * @param names an array of names as strings
     */
    public void setArguments(String[] names) {
        for (String name : names) {
            if (!name.equals("-h")) {
                argumentNames.add(name);
                Argument arg = new Argument(name);
                argumentMap.put(name, arg);
            }
        }
    }

    /** 
     * Sets one argument. Given a name, an argument is created and set. Note the argument 
     * <strong>"-h"</strong> is not allowed.
     * 
     * @param name the desired name of the argument as string
     */
    public void setArgument(String name) {
        if (!name.equals("-h")) {
            argumentNames.add(name);
            Argument arg = new Argument(name);
            argumentMap.put(name, arg);
        }
    }

    /** 
     * Sets one argument. The given argument is set for the parser. Note the argument with name 
     * <strong>"-h"</strong> is not allowed.
     * 
     * @param arg the Argument object to be set
     * @see Argument
     */
    public void setArgument(Argument arg) {
        String name = arg.getName();
        if (!name.equals("-h")) {
            if (!name.startsWith("--")) {
                argumentNames.add(name);
            }
            argumentMap.put(name, arg);
        }
    }

    /**
     * Assigns a short-form name to an Argument object. This method assumes that the argument object has 
     * already been created with its attributes (e.g. type, default value, description) set as desired. 
     * The method should be used when an Argument object has been fully defined and is ready to be given a
     * nickname.
     * 
     * @param arg            Argument object to be given an alias and then set as an argument for the parser
     * @param shortFormNames string starting with "-" that contains the desired short form name for the 
     *                       Argument object. If the string contains multiple letters, each letter will be 
     *                       assigned as an individual alias for the Argument object.
     */
    public void setNickname(Argument arg, String shortFormNames) {
        argumentMap.put(arg.getName(), arg);
        for (int i = 1; i < shortFormNames.length(); i++) {
            String name = "-" + Character.toString(shortFormNames.charAt(i));
            if (!name.equals("-h")) {
                shortFormMap.put(name, arg.getName());
            }
        }
    }

    /**
     * Sets an argument's description. The argument to be modified must already be known by the
     * ArgumentParser.
     * 
     * @param name        the name of the argument whose description we want to set
     * @param description the description to set
     */
    public void setArgumentDescription(String name, String description) {
        Argument arg = argumentMap.get(name);
        arg.setDescription(description);
    }

    /**
     * Sets an argument's type. The argument to be modified must already be known by the ArgumentParser.
     * 
     * @param name     the name of the argument whose type we want to set
     * @param typeName the type to set, options are int, float, boolean, and string
     */
    public void setArgumentType(String name, String typeName) {
        Argument arg = argumentMap.get(name);
        arg.setType(typeName);
    }

    /**
     * Set any number of short-form named flag arguments, specified in a single string starting with "-".
     * Each flag's initial value will be set to false, and flags that are set using this method will only
     * be accessible via the short-form name (e.g. "-a") that is provided in the parameter.
     * 
     * @param flag a string starting with "-" and followed by any number of characters, each of which will
     *             be a new flag to be recognized by the ArgumentParser.
     */
    public void setFlags(String flag) {
        if (flag.startsWith("-") && !flag.startsWith("--")) {
            for (int i = 1; i < flag.length(); i++) {
                String name = "-" + Character.toString(flag.charAt(i));
                if (!name.equals("-h")) {
                    NamedArgument shortFormFlag = new NamedArgument(name, "false");
                    shortFormFlag.setType("boolean");
                    argumentMap.put(name, shortFormFlag);
                }
            }
        }
    }

    /**
     * Parses the values taken from the command line and sets them to their respective arguments. If the
     * value "-h" or "--help" is detected, a help message will display to the user.
     * 
     * @param values                            string of values from the command line
     * @throws UnrecognizedArgumentException    if an argument is provided that the ArgumentParser does not
     *                                          recognize
     * @throws MissingRequiredArgumentException if a required argument's value is not provided
     * @throws InvalidTypeException             if an argument's value does not match its expected type
     * @throws HelpException                    if the value "-h" or "--help" is provided
     */
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
                            throw new UnrecognizedArgumentException(msg);
                        }
                    }
                } else {
                    if (argumentMap.containsKey(arg)) {
                        Argument flag = argumentMap.get(arg);
                        flag.setValue("true");
                    } else if (shortFormMap.containsKey(arg)) {
                        Argument longForm = argumentMap.get(shortFormMap.get(arg));
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
                    throw new UnrecognizedArgumentException(msg);
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
            throw new MissingRequiredArgumentException(msg);
        }
    }

    /**
     * Gets the name of the program whose arguments the ArgumentParser is parsing.
     * 
     * @return the name of the program
     */
    public String getProgramName() {
        return programName;
    }

    /** 
     * Gets the value of the argument with the associated name.
     *
     * @param name      the name of the variable whose value is wanted
     * @return          string representation of the variable's value
     */
    public String getValue(String name) {
        if (name.startsWith("-")) {
            if (shortFormMap.containsKey(name)) {
                name = shortFormMap.get(name);
            }
        }
        Argument arg = argumentMap.get(name);
        return arg.getValue();
    }

    /** 
     * Gets the description of the argument with the associated name.
     *
     * @param name      the name of the variable whose description is wanted
     * @return          string representation of the variable's description
     */
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
        throw new HelpException(message);
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
            throw new InvalidTypeException(msg);
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

    public void parseXML(String filename) {
        boolean bName = false;
        boolean bType = false;
        boolean bPosition = false;
        boolean bShortname = false;
        boolean bDefault = false;
        int positionalCount = 0;
        String name = "";
        String type = "";
        String position = "";
        String shortname = "";
        String mydefault = "";
        Map<Integer, Argument> posMap = new HashMap<Integer, Argument>();
        try {
            XMLInputFactory factory = XMLInputFactory.newInstance();
            XMLEventReader eventReader = factory.createXMLEventReader(new FileReader(filename));
            while (eventReader.hasNext()) {
                XMLEvent event = eventReader.nextEvent();
                switch (event.getEventType()) {
                case XMLStreamConstants.START_ELEMENT:
                    StartElement startElement = event.asStartElement();
                    String qName = startElement.getName().getLocalPart();
                    if (qName.equalsIgnoreCase("name")) {
                        bName = true;
                    } else if (qName.equalsIgnoreCase("type")) {
                        bType = true;
                    } else if (qName.equalsIgnoreCase("position")) {
                        bPosition = true;
                    } else if (qName.equalsIgnoreCase("shortname")) {
                        bShortname = true;
                    } else if (qName.equalsIgnoreCase("default")) {
                        bDefault = true;
                    }
                    break;
                case XMLStreamConstants.CHARACTERS:
                    Characters characters = event.asCharacters();
                    if (bName) {
                        name = characters.getData();
                        bName = false;
                    }
                    if (bShortname) {
                        shortname = characters.getData();
                        bShortname = false;
                    }
                    if (bType) {
                        type = characters.getData();
                        bType = false;
                    }
                    if (bPosition) {
                        position = characters.getData();
                        bPosition = false;
                    }
                    if (bDefault) {
                        mydefault = characters.getData();
                        bDefault = false;
                    }
                    break;
                case XMLStreamConstants.END_ELEMENT:
                    EndElement endElement = event.asEndElement();
                    if (endElement.getName().getLocalPart().equalsIgnoreCase("positional")
                            || endElement.getName().getLocalPart().equalsIgnoreCase("named")) {
                        if (endElement.getName().getLocalPart().equalsIgnoreCase("positional")) {
                            positionalCount++;
                            Argument arg = new Argument(name);
                            arg.setType(type);
                            posMap.put(Integer.parseInt(position), arg);
                            position = "";
                        } else if (endElement.getName().getLocalPart().equalsIgnoreCase("named")) {
                            NamedArgument arg = new NamedArgument("--" + name, mydefault);
                            arg.setType(type);
                            if (!shortname.equals("")) {
                                this.setNickname(arg, "-" + shortname);
                                shortname = "";
                            } else {
                                this.setArgument(arg);
                            }
                            mydefault = "";
                        }
                        name = "";
                        type = "";
                    }
                    break;
                }
            }
            for (int i = 1; i < positionalCount + 1; i++) {
                Argument arg = posMap.get(i);
                this.setArgument(arg);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (XMLStreamException e) {
            e.printStackTrace();
        }
    }
}
