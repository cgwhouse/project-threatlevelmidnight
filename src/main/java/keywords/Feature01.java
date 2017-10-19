package keywords;

import edu.wofford.*;

public class Feature01 {
	ArgumentParser parser;
	ArgumentParser absurdParser;

	public void startProgramWithArguments(String[] args) {
		ArgumentParser parser = new ArgumentParser();
		String[] names = { "length", "width", "height" };
		// parser.setProgramName("VolumeCalculator");
		parser.setProgramNames(names);
		parser.setProgramValues(args);
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

	public String getProgramOutput() {
		double d = Double.parseDouble(parser.getValue("length")) * Double.parseDouble(parser.getValue("width"))
				* Double.parseDouble(parser.getValue("height"));
		return Double.toString(d);
	}

	public void startAbsurdProgramWithArguments(String[] args) {
		absurdParser = new ArgumentParser();
		// String[] names = { "length", "width", "height" };
		// parser.setProgramName("VolumeCalculator");
		// parser.setProgramNames(names);
		// parser.setProgramValues(args);
		String[] names = { "pet", "number", "rainy", "bathrooms" };
		absurdParser.setProgramNames(names);
		absurdParser.setProgramValues(args);
	}

	public String getPet() {
		return absurdParser.getValue("pet");
	}

	public String getNumber() {
		return absurdParser.getValue("number");
	}

	public String getRainy() {
		return absurdParser.getValue("rainy");
	}

	public String getBathrooms() {
		return absurdParser.getValue("bathrooms");
	}
}
