package edu.wofford;

/** 
* Thrown to indicate that ArgumentParser has parsed a positional argument that requires more values than those
* provided. 
*/
public class NotEnoughValuesException extends ArgumentException {

    /** 
    * Constructs a NotEnoughValuesException with the specified detail message.
    *
    * @param msg the detail message
    */
    public NotEnoughValuesException(String msg) {
        super(msg);
    }
}
