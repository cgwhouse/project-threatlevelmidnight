package edu.wofford;

import org.junit.*;

import static org.junit.Assert.*;

public class ArgumentParserTest {
    private ArgumentParser parser;

    @Before
    public void setup() {
        parser = new ArgumentParser("VolumeCalculator");
    }

    @Test(expected = ArgumentException.class)
    public void testInitialParserIsCorrect() {
        String[] args = { "height" };
        parser.setArgumentValues(args);
    }

    @Test
    public void testGetValueWorks() {
        String[] argumentNames = { "length", "width", "height" };
        String[] argumentValues = { "7", "5", "2" };

        parser.setArguments(argumentNames);
        parser.setArgumentValues(argumentValues);

        for (int i = 0; i < 3; i++) {
            assertEquals(argumentValues[i], parser.getValue(argumentNames[i]));
        }
    }

    @Test
    public void testOptionsForSetArgument() {
        String[] argumentNames = { "length", "width", "height" };
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
            String[] argumentNames = { "length", "width", "height" };
            String[] argumentValues = { "7", "5", "2", "43" };

            parser.setArguments(argumentNames);
            parser.setArgumentValues(argumentValues);

            parser.getValue("height");
        } catch (final ArgumentException error) {
            final String message = "usage: java VolumeCalculator length width height\nVolumeCalculator.java: error: unrecognized arguments: 43";

            assertEquals(message, error.getMessage());
        }
    }

    @Test
    public void testTooFewArguments() {
        try {
            String[] argumentNames = { "length", "width", "height" };
            String[] argumentValues = { "7", "5" };

            parser.setArguments(argumentNames);
            parser.setArgumentValues(argumentValues);

            parser.getValue("height");
        } catch (final ArgumentException error) {
            final String message = "usage: java VolumeCalculator length width height\nVolumeCalculator.java: error: the following arguments are required: height";
            assertEquals(message, error.getMessage());
        }
    }

    @Test
    public void testHelpArgument() {
        try {
            String[] argumentNames = { "length", "width", "height" };
            String[] argumentDescriptions = { "the length of the box (float)", "the width of the box (float)",
                    "the height of the box (float)" };
            String[] argumentValues = { "-h" };

            parser.setProgramDescription("Calculate the volume of a box.");
            parser.setArguments(argumentNames);

            for (int i = 0; i < argumentNames.length; i++) {
                parser.setArgumentDescription(argumentNames[i], argumentDescriptions[i]);
            }

            parser.setArgumentValues(argumentValues);
        } catch (final ArgumentException error) {
            final String message = "usage: java VolumeCalculator length width height\nCalculate the volume of a box.\npositional arguments:\nlength the length of the box (float)\nwidth the width of the box (float)\nheight the height of the box (float)";
            assertEquals(message, error.getMessage());
        }
    }

    @Test
    public void testSetTypeWorks() {
        String[] argumentNames = { "length", "width", "height" };
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
            String[] argumentNames = { "length", "width", "height" };
            String[] argumentValues = { "true", "something", "2" };
            String[] argumentTypes = { "boolean", "float", "int" };

            parser.setArguments(argumentNames);

            for (int i = 0; i < 3; i++) {
                parser.setArgumentType(argumentNames[i], argumentTypes[i]);
            }

            parser.setArgumentValues(argumentValues);

        } catch (final ArgumentException error) {
            final String message = "usage: java VolumeCalculator length width height\nVolumeCalculator.java: error: argument width: invalid float value: something";
            assertEquals(message, error.getMessage());
        }
    }

    @Test
    public void testDefaultArgumentsAcquiresNewValue() {
        String[] argumentNames = { "length", "width", "height" };
        String[] argumentValues = { "7", "5", "2", "--type", "square" };

        Argument arg = new Argument("--type");
        arg.setValue("ellipsoid");

        parser.setArguments(argumentNames);
        parser.setArgument(arg);
        parser.setArgumentValues(argumentValues);

        assertEquals("square", parser.getValue("--type"));
    }

    @Test
    public void testDefaultArgumentRetainsDefaultValue() {
        String[] argumentNames = { "length", "width", "height" };
        String[] argumentValues = { "7", "5", "2" };

        Argument arg = new Argument("--type");
        arg.setValue("ellipsoid");

        parser.setArguments(argumentNames);
        parser.setArgument(arg);
        parser.setArgumentValues(argumentValues);

        assertEquals("ellipsoid", parser.getValue("--type"));
    }

    @Test
    public void testDefaultArgumentsOutOfOrder() {
        String[] argumentNames = { "length", "width", "height" };
        String[] argumentValues = { "7", "5", "--type", "square", "2" };

        Argument arg = new Argument("--type");
        arg.setValue("ellipsoid");

        parser.setArguments(argumentNames);
        parser.setArgument(arg);

        parser.setArgumentValues(argumentValues);

        assertEquals("square", parser.getValue("--type"));
    }

    // @Test()
    // public void testFlagIsSet() {
    //     String[] argumentNames = { "length", "width", "height" };
    //     String[] argumentValues = { "7", "5", "--test", "2" };

    //     Argument arg = new Argument("--test");
    //     arg.setValue("false");

    //     parser = new ArgumentParser();
    //     parser.setArguments(argumentNames);
    //     parser.setArgument(arg);

    //     parser.setArgumentValues(argumentValues);

    //     assertEquals("true", parser.getValue("--test"));
    // }
}
