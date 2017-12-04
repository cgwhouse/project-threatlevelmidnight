package edu.wofford;

/** 
* Thrown to indicate that one of the following errors arose while handling XML parsing/creating:
* FileNotFoundException or XMLStreamException or IOException or TransformerException. 
*/
public class BadXMLException extends RuntimeException {
    /** 
    * Constructs a BadXMLException exception.
    */
    public BadXMLException() {
        super();
    }
}