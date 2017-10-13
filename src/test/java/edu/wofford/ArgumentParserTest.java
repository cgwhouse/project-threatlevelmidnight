package edu.wofford;

import org.junit.*;

import static org.junit.Assert.*;

public class ArgumentParserTest {
	private ArgumentParser parser;

	private	String[] argumentNames = {"length", "width", "height"};

	private	String[] argumentValues = {"7", "5", "2"};
    
    @Before
    public void setUp() {
		parser = new ArgumentParser(argumentNames, argumentValues);
    }
    

	@Test
	public void testInitialParserIsCorrect() {
		for (int i = 0; i < 3; i++) {
			assertEquals(argumentValues[i], parser.getValue(i));
		}
		
		for (int i = 0; i < 3; i++) {
			assertEquals(argumentValues[i], parser.getValue(argumentNames[i]));
		}
	}
    
}