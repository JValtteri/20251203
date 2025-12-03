package com.ojala;

import java.util.ArrayList;
import java.util.Arrays;

public class App
{
    public static void main( String[] args )
    {
        int startAltitude = 0;
        int endAltitude   = 0;
        int consumption   = 0;

        if (args.length != 2) {
            System.out.println( "Usage: app [start altitude] [end altitude]" );
            return;
        }

        try {
            startAltitude = Integer.parseInt(args[0]);
            endAltitude = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            System.out.println( "Inputs must be numbers" );
        }

        if (startAltitude < endAltitude) {
            consumption = calculateAscent(startAltitude, endAltitude);
        } else if (startAltitude > endAltitude) {
            consumption = calculateDescent(startAltitude, endAltitude);
        }
        System.out.println( String.format("Consumption:", consumption, "lb")  );
    }

    private static int calculateAscent(int startAltitude, int endAltitude) {

        return -1;
    }

    private static int calculateDescent(int startAltitude, int endAltitude) {

        return -1;
    }

    private static class FuelData {
        private int altitude;       // feet
        private int consumption;    // lb
        public FuelData(int altitude, int consumption) {
            this.altitude = altitude;
            this.consumption = consumption;
        }
    }

    java.util.ArrayList<FuelData> ascentData = new ArrayList<>(Arrays.asList(
        new FuelData(0, 640),
        new FuelData(5, 280),
        new FuelData(10, 370),
        new FuelData(15, 460),
        new FuelData(20, 550),
        new FuelData(25, 640)
    ));

    java.util.ArrayList<FuelData> descentData = new ArrayList<>(Arrays.asList(
        new FuelData(0, 0),
        new FuelData(5, 5),
        new FuelData(10, 20),
        new FuelData(15, 40),
        new FuelData(20, 70),
        new FuelData(25, 90)
    ));

}
