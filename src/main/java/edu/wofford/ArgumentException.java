package edu.wofford;

/** 
* Thrown to indicate that an invalid action has taken place in regards to the arguments within ArgumentParser. 
*/
public class ArgumentException extends RuntimeException {

    /** 
    * Constructs an ArgumentException with the specified detail message.
    *
    * @param message   the detail message
    */
    public ArgumentException(String message) {
        super(message);
    }
}
