package edu.wofford;

import java.io.File;
import java.util.List;
import org.junit.*;
import static org.junit.Assert.*;

public class ArgumentParserTest {
    private ArgumentParser parser;
    String[] argumentNames = { "length", "width", "height" };

    @Before
    public void setup() {
        parser = new ArgumentParser("VolumeCalculator");
    }

    @Test
    public void testProgramNameIsSet() {
        assertEquals("VolumeCalculator", parser.getProgramName());
    }

    @Test
    public void testProgramNameCanBeOverriden() {
        parser.setProgramName("new name");
        assertEquals("new name", parser.getProgramName());
    }

    @Test(expected = ArgumentException.class)
    public void testEmptyParserCannotSetValues() {
        String[] args = { "7" };
        parser.setArgumentValues(args);
    }

    @Test
    public void testGetValue() {
        String[] argumentValues = { "7", "5", "2" };

        parser.setArguments(argumentNames);
        parser.setArgumentValues(argumentValues);

        for (int i = 0; i < 3; i++) {
            assertEquals(argumentValues[i], parser.getValue(argumentNames[i]));
        }
    }

    @Test
    public void testGetType() {
        Argument floatArg = new Argument("test");
        floatArg.setType("float");
        parser.setArgument(floatArg);

        assertEquals("float", parser.getType("test"));
    }

    @Test
    public void testOptionsForSetArgument() {
        String[] argumentValues = { "7", "5", "2" };

        Argument arg = new Argument(argumentNames[0]);
        parser.setArgument(arg);
        parser.setArgument(argumentNames[1]);
        parser.setArgument(argumentNames[2]);

        parser.setArgumentValues(argumentValues);

        for (int i = 0; i < 3; i++) {
            assertEquals(argumentValues[i], parser.getValue(argumentNames[i]));
        }
    }

    @Test
    public void testTooManyArguments() {
        try {
            String[] argumentValues = { "7", "5", "2", "43" };

            parser.setArguments(argumentNames);
            parser.setArgumentValues(argumentValues);

            parser.getValue("height");
        } catch (ArgumentException error) {
            String message = "usage: java VolumeCalculator length width height\nVolumeCalculator.java: error: unrecognized arguments: 43";

            assertEquals(message, error.getMessage());
        }
    }

    @Test
    public void testTooFewArguments() {
        try {
            String[] argumentValues = { "7", "5" };

            parser.setArguments(argumentNames);
            parser.setArgumentValues(argumentValues);

            parser.getValue("height");
        } catch (ArgumentException error) {
            String message = "usage: java VolumeCalculator length width height\nVolumeCalculator.java: error: the following arguments are required: height";
            assertEquals(message, error.getMessage());
        }
    }

    @Test
    public void testHelpArgument() {
        try {
            String[] argumentDescriptions = { "the length of the box (float)", "the width of the box (float)",
                    "the height of the box (float)" };
            String[] argumentValues = { "-h" };

            parser.setProgramDescription("Calculate the volume of a box.");
            parser.setArguments(argumentNames);

            for (int i = 0; i < argumentNames.length; i++) {
                parser.setArgumentDescription(argumentNames[i], argumentDescriptions[i]);
            }

            parser.setArgumentValues(argumentValues);
        } catch (ArgumentException error) {
            String message = "usage: java VolumeCalculator length width height\nCalculate the volume of a box.\npositional arguments:\nlength the length of the box (float)\nwidth the width of the box (float)\nheight the height of the box (float)";
            assertEquals(message, error.getMessage());
        }
    }

    @Test
    public void testGetDescription() {
        Argument arg = new Argument("test");
        arg.setDescription("test description");
        parser.setArgument(arg);
        assertEquals("test description", parser.getDescription("test"));
    }

    @Test
    public void testSetTypeWorks() {
        String[] argumentValues = { "7", "5", "2" };
        String[] argumentTypes = { "string", "string", "string" };

        parser.setArguments(argumentNames);

        for (int i = 0; i < 3; i++) {
            parser.setArgumentType(argumentNames[i], argumentTypes[i]);
        }

        parser.setArgumentValues(argumentValues);

        assertEquals(argumentValues[0], parser.getValue("length"));
    }

    @Test
    public void testSetTypeInvalidType() {
        try {
            String[] argumentValues = { "true", "something", "2" };
            String[] argumentTypes = { "boolean", "float", "int" };

            parser.setArguments(argumentNames);

            for (int i = 0; i < 3; i++) {
                parser.setArgumentType(argumentNames[i], argumentTypes[i]);
            }

            parser.setArgumentValues(argumentValues);

        } catch (ArgumentException error) {
            String message = "usage: java VolumeCalculator length width height\nVolumeCalculator.java: error: argument width: invalid float value: something";
            assertEquals(message, error.getMessage());
        }
    }

    @Test
    public void testDefaultArgumentsAcquiresNewValue() {
        String[] argumentValues = { "7", "5", "2", "--type", "square" };

        NamedArgument arg = new NamedArgument("--type", "ellipsoid");

        parser.setArguments(argumentNames);
        parser.setArgument(arg);
        parser.setArgumentValues(argumentValues);

        assertEquals("square", parser.getValue("--type"));
    }

    @Test
    public void testDefaultArgumentRetainsDefaultValue() {
        String[] argumentValues = { "7", "5", "2" };

        NamedArgument arg = new NamedArgument("--type", "ellipsoid");

        parser.setArguments(argumentNames);
        parser.setArgument(arg);
        parser.setArgumentValues(argumentValues);

        assertEquals("ellipsoid", parser.getValue("--type"));
    }

    @Test
    public void testDefaultArgumentsOutOfOrder() {
        String[] argumentValues = { "7", "5", "--type", "square", "2" };

        NamedArgument arg = new NamedArgument("--type", "ellipsoid");

        parser.setArguments(argumentNames);
        parser.setArgument(arg);

        parser.setArgumentValues(argumentValues);

        assertEquals("square", parser.getValue("--type"));
    }

    @Test
    public void testDefaultArgumentInvalidType() {
        String[] argumentValues = { "7", "5", "--digits", "square", "2" };

        NamedArgument arg = new NamedArgument("--digits", "1");
        arg.setType("int");
        parser.setArgument(arg);
        parser.setArguments(argumentNames);

        try {
            parser.setArgumentValues(argumentValues);
        } catch (ArgumentException e) {
            String message = "usage: java VolumeCalculator length width height\nVolumeCalculator.java: error: argument --digits: invalid int value: square";
            assertEquals(e.getMessage(), message);
        }
    }

    @Test
    public void testFlagIsSetWhenPresent() {
        String[] argumentValues = { "7", "5", "--test", "2" };

        NamedArgument arg = new NamedArgument("--test", "false");
        arg.setType("boolean");

        parser.setArguments(argumentNames);
        parser.setArgument(arg);

        parser.setArgumentValues(argumentValues);

        assertEquals("true", parser.getValue("--test"));
    }

    @Test
    public void testShortFormNames() {
        String[] argumentValues = { "7", "5", "-e", "2", "2" };

        NamedArgument arg = new NamedArgument("--test", "1");
        arg.setType("int");

        parser.setArguments(argumentNames);
        parser.setNickname(arg, "-te");

        parser.setArgumentValues(argumentValues);

        assertEquals("2", parser.getValue("--test"));
        assertEquals("2", parser.getValue("-t"));
        assertEquals("2", parser.getValue("-e"));
    }

    @Test
    public void testInvalidShortFormNameDashH() {
        try {
            String[] argumentNames = { "length", "width", "height" };
            String[] argumentValues = { "7", "5", "-h", "2", "2" };

            String[] argumentDescriptions = { "the length of the box (float)", "the width of the box (float)",
                    "the height of the box (float)" };

            parser.setProgramDescription("Calculate the volume of a box.");
            parser.setArguments(argumentNames);

            for (int i = 0; i < argumentNames.length; i++) {
                parser.setArgumentDescription(argumentNames[i], argumentDescriptions[i]);
            }

            NamedArgument arg = new NamedArgument("--hue", "3");
            arg.setType("int");
            arg.setDescription("test description (int)");
            parser.setNickname(arg, "-h");
            parser.setArgumentValues(argumentValues);
        } catch (ArgumentException error) {
            String message = "usage: java VolumeCalculator length width height\nCalculate the volume of a box.";
            message += "\npositional arguments:\nlength the length of the box (float)\n";
            message += "width the width of the box (float)\nheight the height of the box (float)\n";
            message += "--hue test description (int)";
            assertEquals(message, error.getMessage());
        }
    }

    @Test
    public void testFlags() {
        String[] argumentValues = { "7", "5", "2", "-abc" };

        parser.setFlags("-abc");

        parser.setArguments(argumentNames);

        parser.setArgumentValues(argumentValues);

        assertEquals("true", parser.getValue("-a"));
        assertEquals("true", parser.getValue("-b"));
        assertEquals("true", parser.getValue("-c"));
    }

    @Test
    public void testSetSingleFlag() {
        String[] values = { "7", "5", "2", "-t" };
        parser.setFlags("-t");
        parser.setArguments(argumentNames);
        parser.setArgumentValues(values);
        assertEquals("true", parser.getValue("-t"));
    }

    @Test
    public void testNonDeclaredFlags() {
        try {
            String[] argumentValues = { "7", "5", "2", "-abc" };

            parser.setArguments(argumentNames);

            parser.setArgumentValues(argumentValues);

            assertEquals("true", parser.getValue("-b"));
        } catch (ArgumentException error) {
            String message = "usage: java VolumeCalculator length width height\nVolumeCalculator.java: error: unrecognized flag: -a";
            assertEquals(message, error.getMessage());
        }
    }

    @Test
    public void testBooleanSetTheLongWay() {
        String[] argumentValues = { "7", "5", "-t", "2" };
        parser.setArguments(argumentNames);
        NamedArgument boolArg = new NamedArgument("--test", "false");
        boolArg.setType("boolean");
        parser.setNickname(boolArg, "-t");
        parser.setArgumentValues(argumentValues);
        assertEquals("true", parser.getValue("-t"));
        assertEquals("true", parser.getValue("--test"));
    }

    @Test
    public void testXMLParser() {
        File file = new File("src/test/resources/testXMLParser.xml");
        String[] argumentValues = { "7", "5", "-t", "ellipsoid", "2" };
        parser.parseXML(file.getAbsolutePath());
        parser.setArgumentValues(argumentValues);
        assertEquals("7", parser.getValue("length"));
        assertEquals("float", parser.getType("width"));
        assertEquals("ellipsoid", parser.getValue("--type"));
        assertEquals("4", parser.getValue("-d"));
    }

    @Test
    public void testXMLCreator() {
        String expected = "<arguments>";
        expected += "<positional><name>length</name><type>float</type><position>1</position></positional>";
        expected += "<positional><name>width</name><type>float</type><position>2</position></positional>";
        expected += "<positional><name>height</name><type>float</type><position>3</position></positional>";
        expected += "<named><name>type</name><shortname>t</shortname><type>string</type><default>box</default></named>";
        expected += "<named><name>digits</name><type>integer</type><default>4</default></named>";
        expected += "</arguments>";
        for (int i = 0; i < argumentNames.length; i++) {
            Argument arg = new Argument(argumentNames[i]);
            arg.setType("float");
            parser.setArgument(arg);
        }
        NamedArgument typeArg = new NamedArgument("--type", "box");
        typeArg.setType("string");
        parser.setNickname(typeArg, "-t");
        NamedArgument digitsArg = new NamedArgument("--digits", "4");
        digitsArg.setType("integer");
        parser.setArgument(digitsArg);
        String result = parser.createXML(false);
        assertEquals(expected, result);
    }

    @Test
    public void testXMLAcceptedValuesOnly() {
        File file = new File("src/test/resources/testXMLParser.xml");
        String[] argumentValues = { "7", "5", "-t", "ellipsoid", "2", "-d", "5" };
        parser.parseXML(file.getAbsolutePath());
        parser.setArgumentValues(argumentValues);
        assertEquals("7", parser.getValue("length"));
        assertEquals("float", parser.getType("width"));
        assertEquals("ellipsoid", parser.getValue("--type"));
        assertEquals("5", parser.getValue("-d"));
    }

    @Test
    public void testXMLAcceptedValuesError() {
        try {
            File file = new File("src/test/resources/testXMLParser.xml");
            String[] argumentValues = { "7", "5", "-t", "ellipsoid", "2", "-d", "6" };
            parser.parseXML(file.getAbsolutePath());
            parser.setArgumentValues(argumentValues);
        } catch (UnacceptedValueException e) {
            String message = "usage: java VolumeCalculator length width height\nVolumeCalculator.java: error: argument --digits: unaccepted value: 6";
            assertEquals(message, e.getMessage());
        }
    }

    @Test
    public void testXMLCreatorAcceptedValues() {
        String expected = "<arguments>";
        expected += "<positional><name>length</name><type>float</type><position>1</position></positional>";
        expected += "<positional><name>width</name><type>float</type><position>2</position></positional>";
        expected += "<positional><name>height</name><type>float</type><position>3</position></positional>";
        expected += "<named><name>type</name><shortname>t</shortname><type>string</type><default>box</default>";
        expected += "<accepted>pyramid</accepted><accepted>box</accepted><accepted>ellipsoid</accepted></named>";
        expected += "<named><name>digits</name><type>integer</type><default>4</default></named>";
        expected += "</arguments>";
        for (int i = 0; i < argumentNames.length; i++) {
            Argument arg = new Argument(argumentNames[i]);
            arg.setType("float");
            parser.setArgument(arg);
        }
        NamedArgument typeArg = new NamedArgument("--type", "box");
        typeArg.setType("string");
        String[] accepted = { "box", "ellipsoid", "pyramid" };
        typeArg.addAcceptedValues(accepted);
        parser.setNickname(typeArg, "-t");
        NamedArgument digitsArg = new NamedArgument("--digits", "4");
        digitsArg.setType("integer");
        parser.setArgument(digitsArg);
        String result = parser.createXML(false);
        assertEquals(expected, result);
    }

    @Test
    public void testXMLCreatorPositionalAcceptedValues() {
        String expected = "<arguments>";
        expected += "<positional><name>length</name><type>float</type><position>1</position></positional>";
        expected += "<positional><name>width</name><type>float</type><position>2</position></positional>";
        expected += "<positional><name>height</name><type>float</type><position>3</position></positional>";
        expected += "<positional><name>test</name><type>string</type><position>4</position>";
        expected += "<accepted>valid</accepted></positional>";
        expected += "</arguments>";
        for (int i = 0; i < argumentNames.length; i++) {
            Argument arg = new Argument(argumentNames[i]);
            arg.setType("float");
            parser.setArgument(arg);
        }
        Argument testArg = new Argument("test");
        testArg.setType("string");
        testArg.addAcceptedValue("valid");
        parser.setArgument(testArg);
        String result = parser.createXML(false);
        assertEquals(expected, result);
    }

    @Test
    public void testCreateXMLRequiredNamedArgs() {
        String expected = "<arguments>";
        expected += "<positional><name>length</name><type>float</type><position>1</position></positional>";
        expected += "<positional><name>width</name><type>float</type><position>2</position></positional>";
        expected += "<positional><name>height</name><type>float</type><position>3</position></positional>";
        expected += "<named><name>test</name><shortname>t</shortname><type>string</type><required></required></named>";
        expected += "</arguments>";
        for (int i = 0; i < argumentNames.length; i++) {
            Argument arg = new Argument(argumentNames[i]);
            arg.setType("float");
            parser.setArgument(arg);
        }
        NamedArgument testArg = new NamedArgument("--test");
        testArg.setType("string");
        parser.setNickname(testArg, "-t");
        String result = parser.createXML(false);
        assertEquals(expected, result);
    }

    @Test
    public void testParseXMLRequiredNamedArgs() {
        File file = new File("src/test/resources/testRequiredMutexXML.xml");
        String[] argumentValues = { "7", "5", "-t", "box", "2", "--required", "test" };
        parser.parseXML(file.getAbsolutePath());
        parser.setArgumentValues(argumentValues);
        assertEquals("7", parser.getValue("length"));
        assertEquals("float", parser.getType("width"));
        assertEquals("box", parser.getValue("--type"));
        assertEquals("4", parser.getValue("--digits"));
        NamedArgument reqArg = (NamedArgument) parser.getArg("--required");
        assertTrue(reqArg.isRequired());
    }

    @Test
    public void testParseXMLMutex() {
        File file = new File("src/test/resources/testRequiredMutexXML.xml");
        String[] argumentValues = { "7", "5", "-t", "box", "2", "--required", "test", "--firstMutex", "test" };
        parser.parseXML(file.getAbsolutePath());
        parser.setArgumentValues(argumentValues);
        assertEquals("7", parser.getValue("length"));
        assertEquals("float", parser.getType("width"));
        assertEquals("box", parser.getValue("--type"));
        assertEquals("4", parser.getValue("--digits"));
        NamedArgument reqArg = (NamedArgument) parser.getArg("--required");
        assertTrue(reqArg.isRequired());
        NamedArgument mutexArg = (NamedArgument) parser.getArg("--firstMutex");
        assertTrue(mutexArg.getMutexArgs().contains("--secondMutex"));
        assertEquals("testSecond", parser.getValue("--secondMutex"));
        assertEquals("test", parser.getValue("--firstMutex"));
    }

    @Test
    public void testParseXMLMutexError() {
        File file = new File("src/test/resources/testRequiredMutexXML.xml");
        String[] argumentValues = { "7", "5", "-t", "box", "2", "--required", "test", "--firstMutex", "test",
                "--secondMutex", "test" };
        parser.parseXML(file.getAbsolutePath());
        try {
            parser.setArgumentValues(argumentValues);
        } catch (MutuallyExclusiveArgumentException e) {
            String message = "usage: java VolumeCalculator length width height\nVolumeCalculator.java: error: the following arguments are mutually exclusive: --secondMutex and --firstMutex";
            assertEquals(message, e.getMessage());
        }
    }

    @Test
    public void testCreateXMLMutex() {
        String expected = "<arguments>";
        expected += "<positional><name>length</name><type>float</type><position>1</position></positional>";
        expected += "<positional><name>width</name><type>float</type><position>2</position></positional>";
        expected += "<positional><name>height</name><type>float</type><position>3</position></positional>";
        expected += "<named><name>firstMutex</name><shortname>f</shortname><type>string</type><default>testFirst</default><mutex>secondMutex</mutex></named>";
        expected += "<named><name>secondMutex</name><type>string</type><default>testSecond</default><mutex>firstMutex</mutex></named>";
        expected += "</arguments>";
        for (int i = 0; i < argumentNames.length; i++) {
            Argument arg = new Argument(argumentNames[i]);
            arg.setType("float");
            parser.setArgument(arg);
        }
        NamedArgument testArg = new NamedArgument("--firstMutex", "testFirst");
        testArg.setType("string");
        NamedArgument testArg2 = new NamedArgument("--secondMutex", "testSecond");
        testArg2.setType("string");
        testArg.addMutuallyExclusiveArg(testArg2);
        testArg2.addMutuallyExclusiveArg(testArg);
        parser.setNickname(testArg, "-f");
        parser.setArgument(testArg2);
        String result = parser.createXML(false);
        assertEquals(expected, result);
    }

    @Test
    public void testParseXMLMultipleValues() {
        File file = new File("src/test/resources/testMultipleValues.xml");
        String[] argumentValues = { "8", "4", "3", "5", "-t", "ellipsoid", "2" };
        parser.parseXML(file.getAbsolutePath());
        parser.setArgumentValues(argumentValues);
        assertEquals("8", parser.getValue("length"));
        assertEquals("float", parser.getType("width"));
        assertEquals("ellipsoid", parser.getValue("--type"));
        assertEquals("4", parser.getValue("-d"));
        String[] values = { "4", "3", "5" };
        Argument arg = parser.getArg("width");
        assertEquals(3, arg.getNumberOfValuesExpected());
        int i = 0;
        for (String value : arg.getMultipleValues()) {
            assertEquals(value, values[i]);
            i++;
        }
    }

    @Test
    public void testCreateXMLMultipleValues() {
        String expected = "<arguments>";
        expected += "<positional><name>length</name><type>float</type><position>1</position></positional>";
        expected += "<positional><name>width</name><type>float</type><position>2</position><values>3</values></positional>";
        expected += "<positional><name>height</name><type>float</type><position>3</position></positional>";
        expected += "</arguments>";
        for (int i = 0; i < argumentNames.length; i++) {
            if (argumentNames[i].equals("width")) {
                Argument arg = new Argument(argumentNames[i]);
                arg.setType("float");
                arg.setNumberOfValuesExpected(3);
                parser.setArgument(arg);
            } else {
                Argument arg = new Argument(argumentNames[i]);
                arg.setType("float");
                parser.setArgument(arg);
            }
        }
        String result = parser.createXML(false);
        assertEquals(expected, result);
    }

    @Test
    public void TestAcceptedValuesSet() {
        String[] argumentValues = { "7", "5", "-t", "ellipsoid", "2" };
        parser.setArguments(argumentNames);
        NamedArgument typeArg = new NamedArgument("--type", "box");
        String[] accepted = { "box", "ellipsoid", "pyramid" };
        typeArg.addAcceptedValues(accepted);
        parser.setNickname(typeArg, "-t");
        parser.setArgumentValues(argumentValues);
        assertEquals("ellipsoid", parser.getValue("--type"));
    }

    @Test
    public void testAcceptedValuesError() {
        try {
            String[] argumentValues = { "7", "5", "-t", "vape", "2" };
            parser.setArguments(argumentNames);
            NamedArgument typeArg = new NamedArgument("--type", "box");
            String[] accepted = { "box", "ellipsoid", "pyramid" };
            typeArg.addAcceptedValues(accepted);
            parser.setNickname(typeArg, "-t");
            parser.setArgumentValues(argumentValues);
        } catch (UnacceptedValueException e) {
            String message = "usage: java VolumeCalculator length width height\nVolumeCalculator.java: error: argument --type: unaccepted value: vape";
            assertEquals(message, e.getMessage());
        }
    }

    @Test
    public void testRequiredNamedArgsRetrieve() {
        String[] argumentValues = { "7", "5", "2", "--type", "square" };

        NamedArgument arg = new NamedArgument("--type");
        arg.setType("string");

        parser.setArguments(argumentNames);
        parser.setArgument(arg);
        parser.setArgumentValues(argumentValues);

        assertEquals("square", parser.getValue("--type"));
    }

    @Test
    public void testRequiredNamedArgsShortform() {
        try {
            String[] values = { "7", "5", "2" };
            parser.setArguments(argumentNames);
            NamedArgument typeArg = new NamedArgument("--type");
            typeArg.setType("string");
            parser.setNickname(typeArg, "-t");
            parser.setArgumentValues(values);
        } catch (MissingRequiredArgumentException e) {
            String message = "usage: java VolumeCalculator length width height\nVolumeCalculator.java: error: the following arguments are required: --type";
            assertEquals(message, e.getMessage());
        }
    }

    @Test
    public void testRequiredNamedArgsError() {
        try {
            String[] argumentValues = { "7", "5", "2", "--type", "square" };

            NamedArgument arg1 = new NamedArgument("--type");
            NamedArgument arg2 = new NamedArgument("--color");

            parser.setArguments(argumentNames);
            parser.setArgument(arg1);
            parser.setArgument(arg2);
            parser.setArgumentValues(argumentValues);
        } catch (ArgumentException error) {
            String message = "usage: java VolumeCalculator length width height\nVolumeCalculator.java: error: the following arguments are required: --color";
            assertEquals(message, error.getMessage());
        }
    }

    @Test
    public void testMutuallyExclusiveArgsError() {
        try {
            String[] argumentValues = { "7", "5", "2", "--type", "square", "--shape", "circle" };

            NamedArgument arg1 = new NamedArgument("--type", "circle");
            NamedArgument arg2 = new NamedArgument("--shape", "circle");
            arg1.addMutuallyExclusiveArg(arg2);
            arg2.addMutuallyExclusiveArg(arg1);
            parser.setArguments(argumentNames);
            parser.setArgument(arg1);
            parser.setArgument(arg2);
            parser.setArgumentValues(argumentValues);
        } catch (ArgumentException error) {
            String message = "usage: java VolumeCalculator length width height\nVolumeCalculator.java: error: the following arguments are mutually exclusive: --shape and --type";
            assertEquals(message, error.getMessage());
        }
    }

    @Test
    public void testDifferentSetsOfMutuallyExclusiveArgsError() {
        try {
            String[] argumentValues = { "7", "5", "2", "--color", "blue", "--shape", "circle", "--hue", "red",
                    "--test1", "1" };

            NamedArgument arg1 = new NamedArgument("--type", "oval");
            NamedArgument arg2 = new NamedArgument("--shape", "circle");
            arg1.addMutuallyExclusiveArg(arg2);
            arg2.addMutuallyExclusiveArg(arg1);

            NamedArgument arg3 = new NamedArgument("--color", "green");
            NamedArgument arg4 = new NamedArgument("--hue", "red");
            arg3.addMutuallyExclusiveArg(arg3);
            arg4.addMutuallyExclusiveArg(arg4);

            NamedArgument arg5 = new NamedArgument("--test1", "1");
            NamedArgument arg6 = new NamedArgument("--test2", "2");
            arg3.addMutuallyExclusiveArg(arg5);
            arg4.addMutuallyExclusiveArg(arg6);

            parser.setArguments(argumentNames);
            parser.setArgument(arg1);
            parser.setArgument(arg2);
            parser.setArgument(arg3);
            parser.setArgument(arg4);
            parser.setArgument(arg5);
            parser.setArgument(arg6);
            parser.setArgumentValues(argumentValues);
        } catch (ArgumentException error) {
            String message = "usage: java VolumeCalculator length width height\nVolumeCalculator.java: error: the following arguments are mutually exclusive: --hue and --color";
            assertEquals(message, error.getMessage());
        }
    }

    @Test
    public void testShortformMutuallyExclusiveError() {
        try {
            String[] values = { "7", "5", "2", "-c", "blue", "--hue", "green" };
            parser.setArguments(argumentNames);
            NamedArgument colorArg = new NamedArgument("--color", "red");
            colorArg.setType("string");
            NamedArgument hueArg = new NamedArgument("--hue", "red");
            hueArg.setType("string");
            colorArg.addMutuallyExclusiveArg(hueArg);
            hueArg.addMutuallyExclusiveArg(colorArg);
            parser.setNickname(colorArg, "-c");
            parser.setArgument(hueArg);
            parser.setArgumentValues(values);
        } catch (MutuallyExclusiveArgumentException e) {
            String message = "usage: java VolumeCalculator length width height\nVolumeCalculator.java: error: the following arguments are mutually exclusive: --hue and --color";
            assertEquals(message, e.getMessage());
        }
    }

    @Test
    public void testMultipleFlagsAtOnceMutex() {
        String[] values = { "7", "5", "2", "-as" };
        for (String name : argumentNames) {
            Argument arg = new Argument(name);
            arg.setType("float");
            parser.setArgument(arg);
        }
        NamedArgument addArg = new NamedArgument("--add", "false");
        addArg.addAcceptedValue("true");
        addArg.addAcceptedValue("false");
        addArg.setType("boolean");
        addArg.setDescription("If true, adds 5 to the answer (false by default).");
        addArg.addMutuallyExclusiveArg("--subtract");
        parser.setNickname(addArg, "-a");
        NamedArgument subArg = new NamedArgument("--subtract", "false");
        subArg.addAcceptedValue("true");
        subArg.addAcceptedValue("false");
        subArg.setType("boolean");
        subArg.setDescription("If true, subtracts 5 from the answer (false by default).");
        subArg.addMutuallyExclusiveArg("--add");
        parser.setNickname(subArg, "-s");
        try {
            parser.setArgumentValues(values);
        } catch (MutuallyExclusiveArgumentException e) {
            String message = "usage: java VolumeCalculator length width height\nVolumeCalculator.java: error: the following arguments are mutually exclusive: --subtract and --add";
            assertEquals(message, e.getMessage());
        }
    }

    @Test
    public void testPositionalHasMultipleValues() {
        String[] values = { "1", "2", "3", "5", "2" };
        Argument length = new Argument("length");
        length.setNumberOfValuesExpected(3);
        length.setType("float");
        parser.setArgument(length);
        for (int i = 1; i < argumentNames.length; i++) {
            Argument arg = new Argument(argumentNames[i]);
            arg.setType("float");
            parser.setArgument(arg);
        }
        parser.setArgumentValues(values);
        List<String> lengthVals = parser.getValues("length");
        for (int i = 0; i < 3; i++) {
            assertEquals(lengthVals.get(i), values[i]);
        }
    }

    @Test
    public void testNotEnoughValuesForPositional() {
        String[] values = { "4", "3", "2" };
        Argument test = new Argument("test");
        test.setType("float");
        test.setNumberOfValuesExpected(4);
        parser.setArgument(test);
        try {
            parser.setArgumentValues(values);
        } catch (NotEnoughValuesException e) {
            String message = "usage: java VolumeCalculator test\nVolumeCalculator.java: error: argument test requires 4 values";
            assertEquals(message, e.getMessage());
        }
    }

    @Test
    public void testRequiredNamedArgsAndMutexArgsRetrieve() {
        String[] argumentValues = { "7", "5", "2", "--type", "square", "--color", "blue" };

        NamedArgument argType = new NamedArgument("--type");
        argType.setType("string");

        NamedArgument argHue = new NamedArgument("--hue", "red");
        argHue.setType("string");

        NamedArgument argColor = new NamedArgument("--color", "green");
        argColor.setType("string");
        argColor.addMutuallyExclusiveArg(argHue);

        parser.setArguments(argumentNames);
        parser.setArgument(argType);
        parser.setArgument(argHue);
        parser.setArgument(argColor);
        parser.setArgumentValues(argumentValues);

        assertEquals("blue", parser.getValue("--color"));
    }
}
