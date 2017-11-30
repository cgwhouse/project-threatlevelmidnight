package edu.wofford;

/** 
* Thrown to indicate that ArgumentParser has parsed over two arguments that are mutually exclusive. 
*/
public class MutuallyExclusiveArgumentException extends ArgumentException {

    /** 
    * Constructs an UnrecognizedArgumentException with the specified detail message.
    *
    * @param message   the detail message
    */
    public MutuallyExclusiveArgumentException(String message) {
        super(message);
    }
}
