package edu.wofford;

public class VolumeCalculator {

    public static void main(String[] args) {

        Calculator volume = new Calculator(args);

        System.out.println(volume.calculate());
	}

}
