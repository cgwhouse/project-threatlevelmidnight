package edu.wofford;

/** 
* Thrown to indicate that ArgumentParser has parsed a value for an argument that is
* not compatible with the argument's type.
*/
public class UnacceptedValueException extends ArgumentException {
    /** 
    * Constructs a UnacceptedValueException with the specified detail message.
    *
    * @param msg the detail message
    */
    public UnacceptedValueException(String msg) {
        super(msg);
    }
}
