package edu.wofford;

/** 
* Thrown to indicate that ArgumentParser has parsed over a value for a specified argument with an incompatible type. 
*/
public class InvalidTypeException extends ArgumentException {

    /** 
    * Constructs an InvalidTypeException with the specified detail message.
    *
    * @param message   the detail message
    */
    public InvalidTypeException(String message) {
        super(message);
    }
}
