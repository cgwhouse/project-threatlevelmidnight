package edu.wofford;

import org.junit.*;

import static org.junit.Assert.*;

public class ArgumentParserTest {
    private ArgumentParser parser;

    @Test(expected = ArgumentException.class)
    public void testInitialParserIsCorrect() {
        parser = new ArgumentParser();

        parser.getValue("height");
    }

    @Test()
    public void testGetValueWorks() {
        String[] argumentNames = { "length", "width", "height" };

        String[] argumentValues = { "7", "5", "2" };

        parser = new ArgumentParser();

        parser.setProgramNames(argumentNames);

        parser.setProgramValues(argumentValues);

        for (int i = 0; i < 3; i++) {
            assertEquals(argumentValues[i], parser.getValue(argumentNames[i]));
        }
    }

    @Test()
    public void testTooManyArguments() {
        try {
            String[] argumentNames = { "length", "width", "height" };

            String[] argumentValues = { "7", "5", "2", "43" };

            parser = new ArgumentParser();

            parser.setProgramName("VolumeCalculator");

            parser.setProgramNames(argumentNames);

            parser.setProgramValues(argumentValues);

            parser.getValue("height");
        } catch (final ArgumentException error) {
            final String message = "usage: java VolumeCalculator length width height\nVolumeCalculator.java: error: unrecognized arguments: 43";

            assertEquals(message, error.getMessage());
        }
    }

    @Test()
    public void testTooFewArguments() {
        try {
            String[] argumentNames = { "length", "width", "height" };

            String[] argumentValues = { "7", "5" };

            parser = new ArgumentParser();

            parser.setProgramName("VolumeCalculator");

            parser.setProgramNames(argumentNames);

            parser.setProgramValues(argumentValues);

            parser.getValue("height");
        } catch (final ArgumentException error) {
            final String message = "usage: java VolumeCalculator length width height\nVolumeCalculator.java: error: the following arguments are required: height";
            assertEquals(message, error.getMessage());
        }
    }

    @Test()
    public void testHelpArgument() {
        try {
            String[] argumentNames = { "length", "width", "height" };

            String[] argumentDescriptions = { "length the length of the box (float)",
                    "width the width of the box (float)", "height the height of the box (float)" };

            String[] argumentValues = { "-h" };

            parser = new ArgumentParser();

            parser.setProgramName("VolumeCalculator");

            parser.setProgramDescription("Calculate the volume of a box.");

            parser.setArgumentDescriptions(argumentDescriptions);

            parser.setProgramNames(argumentNames);

            parser.setProgramValues(argumentValues);
        } catch (final ArgumentException error) {
            final String message = "usage: java VolumeCalculator length width height\nCalculate the volume of a box.\npositional arguments:\nlength the length of the box (float)\nwidth the width of the box (float)\nheight the height of the box (float)";
            assertEquals(message, error.getMessage());
        }
    }
}
