import edu.wofford.*;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class SumNumbers {

    public static void main(String[] args) {
        ArgumentParser parser = new ArgumentParser("SumNumbers");
        parser.setProgramDescription("Calculates the sum of 5 numbers.");

        Argument arg = new Argument("numbers");
        arg.setType("float");
        arg.setDescription("numbers Each value in numbers is a piece of the total sum to be computed.");
        arg.setNumberOfValuesExpected(5);
        parser.setArgument(arg);

        NamedArgument testRequiredArg = new NamedArgument("--moreInfo");
        testRequiredArg.setType("string");
        testRequiredArg.addAcceptedValue("yes");
        testRequiredArg.addAcceptedValue("no");
        testRequiredArg.setDescription("Indicate whether you want additional information printed, yes or no.");
        parser.setNickname(testRequiredArg, "-m");

        NamedArgument addArg = new NamedArgument("--add", "false");
        addArg.addAcceptedValue("true");
        addArg.addAcceptedValue("false");
        addArg.setType("boolean");
        addArg.setDescription("If true, adds 5 to the answer (false by default).");
        addArg.addMutuallyExclusiveArg("--subtract");
        parser.setNickname(addArg, "-a");
        NamedArgument subArg = new NamedArgument("--subtract", "false");
        subArg.addAcceptedValue("true");
        subArg.addAcceptedValue("false");
        subArg.setType("boolean");
        subArg.setDescription("If true, subtracts 5 from the answer (false by default).");
        subArg.addMutuallyExclusiveArg("--add");
        parser.setNickname(subArg, "-s");

        NamedArgument xmlArg = new NamedArgument("--createXML", "false");
        xmlArg.addAcceptedValue("true");
        xmlArg.addAcceptedValue("false");
        xmlArg.setType("boolean");
        xmlArg.setDescription(
                "If true, creates a document containing argument information in XML format (false by default).");
        parser.setNickname(xmlArg, "-x");

        double result = 0;
        try {
            if (args.length == 0) {
                String helpMessage = "\n";
                helpMessage += "java SumNumbers numbers --moreInfo\n\n";
                helpMessage += "Example: SumNumbers 1 2 3 4 5 -m yes -a\n";
                helpMessage += "--moreInfo (shortform -m) is a required argument.\n";
                helpMessage += "Accepted values for --moreInfo are \"yes\" and \"no\".";
                helpMessage += "--add (-a) and subtract (-s) are flags which add or subtract 5 from the final sum.\n";
                helpMessage += "You cannot add and subtract at the same time.\n";
                System.out.println(helpMessage);
                String[] help = { "-h" };
                parser.setArgumentValues(help);
            } else {
                parser.setArgumentValues(args);
                List<String> lengthVals = parser.getValues("numbers");
                for (int i = 0; i < 5; i++) {
                    result += Double.parseDouble(lengthVals.get(i));
                }
                if (parser.getValue("--add").equals("true")) {
                    result += 5;
                } else if (parser.getValue("--subtract").equals("true")) {
                    result -= 5;
                }
                if (parser.getValue("-x").equals("true")) {
                    parser.createXML(true);
                }
            }
            System.out.println(result);
            if (parser.getValue("--moreInfo").equals("yes")) {
                String[] msgs = { "How are you today?", "Happy Wednesday.",
                        "Only smart people can understand Rick and Morty." };
                int random = ThreadLocalRandom.current().nextInt(0, msgs.length);
                System.out.println(msgs[random]);
            }
        } catch (ArgumentException e) {
            System.out.println(e.getMessage());
        }
    }
}
