import edu.wofford.*;

public class VolumeCalculator {

    public static void main(String[] args) {
        ArgumentParser parser = new ArgumentParser("VolumeCalculator");
        parser.setProgramDescription("Calculates the volume of a box, ellipsoid, or pyramid.");

        String[] positionals = { "length", "width", "height" };
        for (String name : positionals) {
            Argument arg = new Argument(name);
            arg.setType("float");
            arg.setDescription("The " + name + " of the shape");
            parser.setArgument(arg);
        }

        NamedArgument typeArg = new NamedArgument("--type", "box");
        typeArg.setType("string");
        String[] accepted = { "box", "ellipsoid", "pyramid" };
        typeArg.addAcceptedValues(accepted);
        parser.setNickname(typeArg, "-t");
        NamedArgument digitsArg = new NamedArgument("--digits", "4");
        digitsArg.setType("integer");
        parser.setNickname(digitsArg, "-d");

        parser.setFlags("-asx");

        NamedArgument testRequiredArg = new NamedArgument("--required");
        testRequiredArg.setType("string");
        parser.setArgument(testRequiredArg);

        NamedArgument testMutexFirst = new NamedArgument("--multiply", "false");
        NamedArgument testMutexSecond = new NamedArgument("--divide", "false");
        testMutexFirst.addMutuallyExclusiveArg(testMutexSecond);
        testMutexSecond.addMutuallyExclusiveArg(testMutexFirst);
        testMutexFirst.setType("boolean");
        testMutexSecond.setType("boolean");
        parser.setArgument(testMutexFirst);
        parser.setArgument(testMutexSecond);
        try {
            if (args.length == 0) {
                String helpMessage = "\n";
                helpMessage += "java VolumeCalculator length width height\n\n";
                helpMessage += "Example 1: VolumeCalculator 5 -t ellipsoid 4 3 -as\n";
                helpMessage += "The value of width is 4.\n";
                helpMessage += "The type of length is float.\n";
                helpMessage += "The value of --type is ellipsoid (default is box).\n";
                helpMessage += "The short form of --type is -t.\n";
                helpMessage += "Possible values for --type are: box, pyramid, ellipsoid\n";
                helpMessage += "-a adds 50 to the result and -s subtracts 75.\n";
                helpMessage += "-x creates a file containing the argument information in xml format.\n";
                helpMessage += "It is required to set --required to a string value, but its value has no effect.\n";
                helpMessage += "--multiply and --divide are mutually exclusive.\n";
                System.out.println(helpMessage);
                String[] help = { "-h" };
                parser.setArgumentValues(help);
            } else {
                parser.setArgumentValues(args);
                double result = 1;
                for (String name : positionals) {
                    result *= Double.parseDouble(parser.getValue(name));
                }
                if (parser.getValue("--type").equals("pyramid")) {
                    result /= 3;
                } else if (parser.getValue("--type").equals("ellipsoid")) {
                    result *= 4 * Math.PI;
                    result /= 3;
                }
                if (parser.getValue("-a").equals("true")) {
                    result += 50;
                }
                if (parser.getValue("-s").equals("true")) {
                    result -= 75;
                }
                if (parser.getValue("--multiply").equals("true")) {
                    result *= 2;
                }
                if (parser.getValue("--divide").equals("true")) {
                    result /= 2;
                }
                if (parser.getValue("-x").equals("true")) {
                    parser.createXML(true);
                }
                System.out.println(result);
            }
        } catch (ArgumentException e) {
            System.out.println(e.getMessage());
        }
    }
}
