package keywords;

import edu.wofford.*;

public class Feature02 {
        ArgumentParser parser;
        ArgumentParser absurdParser;

        public void startProgramWithArguments(String[] args) {
                String[] argumentNames = { "length", "width", "height" };
                String[] argumentDescriptions = { "length the length of the box (float)",
                                "width the width of the box (float)", "height the height of the box (float)" };
                parser = new ArgumentParser();
                parser.setProgramName("VolumeCalculator");
                parser.setProgramDescription("Calculate the volume of a box.");
                parser.setArgumentDescriptions(argumentDescriptions);
                parser.setProgramNames(argumentNames);
        }

        public String getProgramOutput() {
                try {
                        String[] argumentValues = { "-h" };
                        parser.setProgramValues(argumentValues);
                        return "Not passing robot test 2.";
                } catch (ArgumentException e) {
                        return e.getMessage();
                }
        }
}
