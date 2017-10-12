//package edu.wofford;

public class Calculator {

    private double length;
    private double width;
    private double height;


    public Calculator(String len, String wid, String hei){
        length = convertToDouble(len);
        width = convertToDouble(wid);
        height = convertToDouble(hei);
    }


    private double convertToDouble(String stringNumber){
        return Double.parseDouble(stringNumber);
    }

    public double calculateVolume(){
        return length*width*height;
    }


}
