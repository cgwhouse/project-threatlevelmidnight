package keywords;

import edu.wofford.*;

public class Feature07 {
    ArgumentParser parser;

    public void startProgramWithArguments(String[] args) {
        parser = new ArgumentParser("VolumeCalculator");
        Argument typeArg = new NamedArgument("--type", "box");
        Argument digitsArg = new NamedArgument("--digits", "4");

        parser.setNickname(typeArg, "-t");
        parser.setNickname(digitsArg, "-d");

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
