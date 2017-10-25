import edu.wofford.*;

public class VolumeCalculator {

    public static void main(String[] args) {
        String[] measurementNames = { "length", "width", "height" };
        String[] argDescriptions = { "the length of the box", 
									"the width of the box", 
									"the height of the box"};

        ArgumentParser parser = new ArgumentParser();
        
        parser.setProgramName("Volume Calculator");
        parser.setProgramDescription("Calculates the volume of a box");
        parser.setArgumentDescriptions(argDescriptions);
        
        parser.setProgramNames(measurementNames);
        parser.setProgramValues(args);

        double d = Double.parseDouble(parser.getValue("length")) * Double.parseDouble(parser.getValue("width"))
                * Double.parseDouble(parser.getValue("height"));

        System.out.println(d);
    }

}
