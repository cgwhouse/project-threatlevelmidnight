package edu.wofford;

/** 
* Thrown to indicate that ArgumentParser has finished parsing without finding enough values for the required arguments. 
*/
public class MissingRequiredArgumentException extends ArgumentException {

    /** 
    * Constructs an MissingRequiredArgumentException with the specified detail message.
    *
    * @param message   the detail message
    */
    public MissingRequiredArgumentException(String message) {
        super(message);
    }
}
