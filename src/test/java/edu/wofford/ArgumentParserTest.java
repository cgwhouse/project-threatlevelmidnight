package edu.wofford;

import org.junit.*;

import static org.junit.Assert.*;

public class ArgumentParserTest {
	private ArgumentParser parser;

	@Test(expected = ArgumentException.class)
	public void testInitialParserIsCorrect() {
        parser = new ArgumentParser();
    
		parser.getValue("height");
	}
    
    @Test()
    public void testGetValueWorks() {
        String[] argumentNames = {"length", "width", "height"};

        String[] argumentValues = {"7", "5", "2"};
        
        parser = new ArgumentParser();
        
        parser.setProgramNames(argumentNames);
    
		parser.setProgramValues(argumentValues);
		
		for (int i = 0; i < 3; i++) {
			assertEquals(argumentValues[i], parser.getValue(argumentNames[i]));
		}
    }
}