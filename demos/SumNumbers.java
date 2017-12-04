import edu.wofford.*;
import java.util.*;

public class SumNumbers {
	

    public static void main(String[] args) {
        ArgumentParser parser = new ArgumentParser("SumNumbers");
        parser.setProgramDescription("Calculates the sum of 5 numbers");

        String[] positionals = { "numbers"};
        for (String name : positionals) {
            Argument arg = new Argument(name);
            arg.setType("float");
            arg.setDescription("The " + name + " are the pieces of the total sum");
            arg.setNumberOfValuesExpected(5);
            parser.setArgument(arg);
        }

        double result = 0;
        try {
            if (args.length == 0) {
                String helpMessage = "\n";
                helpMessage += "java SumNumbers numbers\n\n";
                helpMessage += "Example: SumNumbers 1 2 3 4 5\n";
                System.out.println(helpMessage);
                String[] help = { "-h" };
                parser.setArgumentValues(help);
            } else {
                parser.setArgumentValues(args);
             
				List<String> lengthVals = parser.getValues("numbers");
				for (int i = 0; i < 5; i++) {
					result += Double.parseDouble(lengthVals.get(i));
				}
            }
                System.out.println(result);
            
        } catch (ArgumentException e) {
            System.out.println(e.getMessage());
        }
	}
}
