package keywords;

import edu.wofford.*;

public class Feature03 {
        ArgumentParser parser;

        public void startProgramWithArguments(String[] args) {
                String[] argumentNames = { "length", "width", "height" };

                String[] argumentTypes = { "float", "float", "float" };

                parser = new ArgumentParser();

                parser.setArguments(argumentNames);

                parser.setProgramName("VolumeCalculator");

                for (int i = 0; i < 3; i++) {
                        parser.setArgumentType(argumentNames[i], argumentTypes[i]);
                }
        }

        public String getProgramOutput() {
                try {
                        String[] argumentValues = { "7", "something", "2" };
                        parser.setArgumentValues(argumentValues);
                        return "Not passing robot test 2.";
                } catch (ArgumentException e) {
                        return e.getMessage();
                }
        }
}
