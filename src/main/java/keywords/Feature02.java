package keywords;

import edu.wofford.*;

public class Feature02 {
        ArgumentParser parser;
        ArgumentParser absurdParser;

        public void startProgramWithArguments(String[] args) {
                String[] argumentNames = { "length", "width", "height" };
                String[] argumentDescriptions = { "the length of the box (float)", "the width of the box (float)",
                                "the height of the box (float)" };
                parser = new ArgumentParser();
                parser.setProgramName("VolumeCalculator");
                parser.setProgramDescription("Calculate the volume of a box.");
                parser.setArguments(argumentNames);
                for (int i = 0; i < argumentNames.length; i++) {
                        parser.setArgumentDescription(argumentNames[i], argumentDescriptions[i]);
                }
        }

        public String getProgramOutput() {
                try {
                        String[] argumentValues = { "-h" };
                        parser.setArgumentValues(argumentValues);
                        return "Not passing robot test 2.";
                } catch (ArgumentException e) {
                        return e.getMessage();
                }
        }
}
