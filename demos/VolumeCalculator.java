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

        Argument typeArg = new Argument("--type");
        typeArg.setType("string");
        typeArg.setValue("square");
        parser.setNamedArgument(typeArg, "-t");

        parser.setFlags("-as");
        try {
            if (args.length == 0) {
                String helpMessage = "\n";
                helpMessage += "java VolumeCalculator length width height\n\n";
                helpMessage += "Example 1: VolumeCalculator 5 --type ellipsoid 4 3\n";
                helpMessage += "The value of width is 4.\n";
                helpMessage += "The type of length is float.\n";
                helpMessage += "The value of --type is ellipsoid (default is square).\n";
                helpMessage += "The short form of --type is -t.\n";
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
                System.out.println(result);
            }
        } catch (ArgumentException e) {
            System.out.println(e.getMessage());
        }
    }
}
