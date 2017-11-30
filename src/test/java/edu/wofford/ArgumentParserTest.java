package edu.wofford;

import java.io.File;
import java.net.PasswordAuthentication;

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
            parser.setNickname(arg, "hue");

            parser.setArgumentValues(argumentValues);
        } catch (ArgumentException error) {
            String message = "usage: java VolumeCalculator length width height\nCalculate the volume of a box.\npositional arguments:\nlength the length of the box (float)\nwidth the width of the box (float)\nheight the height of the box (float)";
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
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource("testXMLParser.xml").getFile());
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
    public void testXMLAcceptedValuesOnly() {
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource("testXMLParser.xml").getFile());
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
            ClassLoader classLoader = getClass().getClassLoader();
            File file = new File(classLoader.getResource("testXMLParser.xml").getFile());
            String[] argumentValues = { "7", "5", "-t", "ellipsoid", "2", "-d", "6" };
            System.out.println(file.getAbsolutePath());
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
    public void testRequiredNamedArgsRetrieve() {
        String[] argumentValues = { "7", "5", "2", "--type", "square" };

        NamedArgument arg = new NamedArgument("--type");

        parser.setArguments(argumentNames);
        parser.setArgument(arg);
        parser.setArgumentValues(argumentValues);

        assertEquals("square", parser.getValue("--type"));
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
    public void testMutuallyExclusiveRequiredNamedArgsError() {
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
    public void testDifferentSetsOfMutuallyExclusiveRequiredNamedArgsError() {
        try {
            String[] argumentValues = { "7", "5", "2", "--color", "blue", "--shape", "circle", "--hue", "red", "--test1", "1" };

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
}
