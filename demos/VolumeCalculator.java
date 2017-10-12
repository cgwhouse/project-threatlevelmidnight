//package edu.wofford;

public class VolumeCalculator {

    public static void main(String[] args) {

        Calculator vc = new Calculator(args[0], args[1], args[2]);

        double volume = vc.calculateVolume();
        System.out.println(volume);
	}

}
