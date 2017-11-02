import edu.wofford.*;

public class VolumeCalculator {

    public static void main(String[] args) {
        System.out.println("Example 1: VolumeCalculator 5 --type square 4 3\n");

        ArgumentParser parser = new ArgumentParser();
        String[] requiredArguments = { "width", "height" };

        Argument lengthArg = new Argument("length");
        lengthArg.setType("float");
        lengthArg.setDescription("the length of the box");

        String programName = "Volume Calculator";
        String programDescription = "Calculates the volume of a box";
        String[] argumentsValues = { "5", "--type", "square", "4", "3" };
        String[] helpValue = { "-h" };
        String[] requiredArgumentsDescriptions = { "the width of the box", "the height of the box" };
        String[] requiredArgumentsTypes = { "float", "float"};
        
        Argument arg = new Argument("--type");
        arg.setValue("ellipsoid");
        
        parser.setProgramName(programName);
        parser.setProgramDescription(programDescription);
        parser.setArgument(lengthArg);
        parser.setArguments(requiredArguments);
        parser.setArgument(arg);
        for (int i = 0; i < 2; i++) {
            parser.setArgumentType(requiredArguments[i], requiredArgumentsTypes[i]);
            parser.setArgumentDescription(requiredArguments[i], requiredArgumentsDescriptions[i]);
        }

        parser.setArgumentValues(argumentsValues);

        System.out.println("The value of width should be 4... it is... " + parser.getValue("width"));
        System.out.println("The type of length should be float... it is... " + lengthArg.getType());
        System.out.println("The value of --type should be square... it is... " + parser.getValue("--type"));

        System.out.println("\nExample 2: VolumeCalculator -h\n");
        System.out.println("The following help message is displayed when passing -h...");

        try {
            parser.setArgumentValues(helpValue);
        } catch (final ArgumentException error) {
            System.out.println(error.getMessage());
        }
    }
}
