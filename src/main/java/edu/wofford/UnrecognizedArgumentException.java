package edu.wofford;

/** 
* Thrown to indicate that ArgumentParser has parsed over a value for an unspecified argument. 
*/
public class UnrecognizedArgumentException extends ArgumentException {

    /** 
    * Constructs an UnrecognizedArgumentException with the specified detail message.
    *
    * @param message   the detail message
    */
    public UnrecognizedArgumentException(String message) {
        super(message);
    }
}
