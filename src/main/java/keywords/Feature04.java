package keywords;

import edu.wofford.*;

public class Feature04 {
    ArgumentParser parser;

    public void startProgramWithArguments(String[] args) {
        parser = new ArgumentParser();
        parser.setProgramName("VolumeCalculator");
        Argument typeArg = new Argument("--type");
        Argument digitsArg = new Argument("--digits");
        typeArg.setValue("box");
        digitsArg.setValue("4");
        parser.setArgument(typeArg);
        parser.setArgument(digitsArg);

        String[] names = { "length", "width", "height" };
        for (int i = 0; i < names.length; i++) {
            parser.setArgument(names[i]);
        }
        parser.setArgumentValues(args);
    }

    public String getLength() {
        return parser.getValue("length");
    }

    public String getWidth() {
        return parser.getValue("width");
    }

    public String getHeight() {
        return parser.getValue("height");
    }

    public String getType() {
        return parser.getValue("--type");
    }

    public String getDigits() {
        return parser.getValue("--digits");
    }
}
