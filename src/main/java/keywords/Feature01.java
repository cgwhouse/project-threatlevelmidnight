package keywords;

import edu.wofford.*;

public class Feature01 {
	ArgumentParser parser;
	ArgumentParser absurdParser;
	String message = "";

	public void startProgramWithArguments(String[] args) {
		parser = new ArgumentParser("VolumeCalculator");
		String[] names = { "length", "width", "height" };
		parser.setArguments(names);
		try {
			parser.setArgumentValues(args);
		} catch (ArgumentException e) {
			message = e.getMessage();
		}
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
		if (!message.equals("")) {
			return message;
		} else {
			Double d = Double.parseDouble(parser.getValue("length")) * Double.parseDouble(parser.getValue("width"))
					* Double.parseDouble(parser.getValue("height"));
			return Integer.toString(d.intValue());
		}
	}

	public void startAbsurdProgramWithArguments(String[] args) {
		absurdParser = new ArgumentParser("");
		String[] names = { "pet", "number", "rainy", "bathrooms" };
		absurdParser.setArguments(names);
		absurdParser.setArgumentValues(args);
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
