import edu.wofford.*;

public class VolumeCalculator {

    public static void main(String[] args) {
        String[] measurementNames = { "length", "width", "height" };

        ArgumentParser parser = new ArgumentParser();
        parser.setProgramNames(measurementNames);
        parser.setProgramValues(args);

        double d = Double.parseDouble(parser.getValue("length")) * Double.parseDouble(parser.getValue("width"))
                * Double.parseDouble(parser.getValue("height"));

        System.out.println(d);
    }

}
