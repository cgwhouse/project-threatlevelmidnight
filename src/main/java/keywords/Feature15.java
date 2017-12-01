package keywords;

import edu.wofford.*;
import java.util.List;

public class Feature15 {
    ArgumentParser parser;
    List<String> result;

    public void startSumNumbersWithArguments(String[] args) {
        parser = new ArgumentParser("VolumeCalculator");
        Argument arg = new Argument("nums");
        arg.setType("int");
        arg.setNumberOfValuesExpected(5);
        parser.setArgument(arg);
        parser.setArgumentValues(args);
    }

    public List<String> getNumbers() {
        result = parser.getValues("nums");
        return result;
    }
}
