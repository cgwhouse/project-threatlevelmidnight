package edu.wofford;

/** 
* Thrown to indicate that ArgumentParser has parsed over the help flag (-h). 
*/
public class HelpException extends ArgumentException {

    /** 
    * Constructs an HelpException with the specified detail message.
    *
    * @param message   the detail message
    */
    public HelpException(String message) {
        super(message);
    }
}
