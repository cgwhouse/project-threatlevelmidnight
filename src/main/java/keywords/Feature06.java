package keywords;

import edu.wofford.*;

public class Feature06 {
    private ArgumentParser parser;
    private String output;

    public void startProgramWithArguments(String[] args) {
        String[] argumentNames = { "length", "width", "height" };
        String[] argumentDescriptions = { "the length of the box (float)", "the width of the box (float)",
                "the height of the box (float)" };

        parser = new ArgumentParser("VolumeCalculator");
        parser.setProgramDescription("Calculate the volume of a box.");

        parser.setArguments(argumentNames);
        for (int i = 0; i < argumentNames.length; i++) {
            parser.setArgumentDescription(argumentNames[i], argumentDescriptions[i]);
        }
        try {
            parser.setArgumentValues(args);
        } catch (ArgumentException e) {
            output = e.getMessage();
        }
    }

    public String getProgramOutput() {
        return output;
    }
}
