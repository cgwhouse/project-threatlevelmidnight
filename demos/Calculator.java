package edu.wofford;

public class Calculator {
	private String[] measurementNames;

    private ArgumentParser parser;

    public Calculator(String[] measurements) {
		measurementNames = ["length", "width", "height"];

        parser = new ArgumentParser(measurementNames, measurements);
    }
	
	public double calculate() {
        return Double.parseDouble(parser.getValue("length")) * Double.parseDouble(parser.getValue("width")) * Double.parseDouble(parser.getValue("height"));
    }
}
