package com.ojala;

import java.util.Arrays;
import java.util.Comparator;

/**
 * Program for calculating airplane fuel usage based on start and end altitudes
 */
public class App{

    public static void main( String[] args ) {
        int startAltitude = 0;
        int endAltitude   = 0;

        if (args.length != 2) {
            System.out.println( "Usage: app [start altitude] [end altitude]" );
            return;
        }
        try {
            startAltitude = Integer.parseInt(args[0]);
            endAltitude = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            System.out.println( "Inputs must be numbers" );
        }

        int consumption = calculateConsumption(startAltitude, endAltitude);
        System.out.println( String.format("Consumption: %d lb", consumption)  );
    }

    /**
     * Calculates fuel consumption (lb) based on start and end altitudes (ft)
     * @param startAltitude
     * @param endAltitude
     * @return fuel consumption (lb)
     */
    public static int calculateConsumption(int startAltitude, int endAltitude) {
        int consumption = 0;
        if (startAltitude < endAltitude) {
            consumption = calculateAscent(startAltitude, endAltitude);
        } else if (startAltitude > endAltitude) {
            consumption = calculateDescent(startAltitude, endAltitude);
        }
        return consumption;
    }

    private static int calculateAscent(int startAltitude, int endAltitude) {
        int startConsumption = interpolateConsumption(startAltitude, ascentData);
        int endConsumption = interpolateConsumption(endAltitude, ascentData);
        return endConsumption - startConsumption;
    }

    private static int calculateDescent(int startAltitude, int endAltitude) {
        int startConsumption = interpolateConsumption(startAltitude, descentData);
        int endConsumption = interpolateConsumption(endAltitude, descentData);
        int consumption = startConsumption - endConsumption;
        return consumption;
    }

    private static int interpolateConsumption(int value, FuelData[] data) {
        int index = searchValueIndex(value, data);
        boolean valueIsOnDatapoint = index > -1;
        if (valueIsOnDatapoint) {                   // Value is exactly on a datapoint
            return data[index].consumption;
        }
        int insersionPoint  = (-index-1);           // Converts binarySearch insertion index
        int baseConsumption = 0;
        int adjustment      = 0;
        boolean valuePastUpperBound = insersionPoint == data.length;
        boolean valuePastLowerBound = insersionPoint == 0;
        if (valuePastUpperBound) {
            baseConsumption = data[insersionPoint-1].consumption;
            adjustment = extrapolateHigh(value, insersionPoint, data);
        } else if (valuePastLowerBound) {
            baseConsumption = data[insersionPoint].consumption;
            adjustment = extrapolateLow(value, insersionPoint, data);
        } else { // Value is between values
            baseConsumption = data[insersionPoint-1].consumption;
            adjustment = interpolate(value, insersionPoint, data);
        }
        return baseConsumption + adjustment;
    }

    private static int searchValueIndex(int value, FuelData[] data) {
        FuelData key = new FuelData(value, 0);
        return Arrays.binarySearch(data, key, altitudeComparator);
    }

    /**
     * For extrapolating past upper bound
     */
    private static int extrapolateHigh(int value, int insersionPoint, FuelData[] data) {
        int consumptionHigh = data[insersionPoint-1]  .consumption;
        int consumptionLow  = data[insersionPoint-2]  .consumption;
        int altitudeHigh    = data[insersionPoint-1]  .altitude;
        int altitudeLow     = data[insersionPoint-2]  .altitude;

        float coefficient = (float) (consumptionHigh-consumptionLow)/(altitudeHigh-altitudeLow);
        int adjustment    = Math.round((value-altitudeHigh)*coefficient);
        return adjustment;
    }

    /**
     * For extrapolating past lower bound
     */
    private static int extrapolateLow(int value, int insersionPoint, FuelData[] data) {
        int consumptionHigh = data[insersionPoint+1].consumption;
        int consumptionLow  = data[insersionPoint]  .consumption;
        int altitudeHigh    = data[insersionPoint+1].altitude;
        int altitudeLow     = data[insersionPoint]  .altitude;

        float coefficient = (float) (consumptionHigh-consumptionLow)/(altitudeHigh-altitudeLow);
        int adjustment    = Math.round((value-altitudeLow)*coefficient);
        return adjustment;
    }

    /**
     * For interpolating between values
     */
    private static int interpolate(int value, int insersionPoint, FuelData[] data) {
        int consumptionHigh = data[insersionPoint]  .consumption;
        int consumptionLow  = data[insersionPoint-1].consumption;
        int altitudeHigh    = data[insersionPoint]  .altitude;
        int altitudeLow     = data[insersionPoint-1].altitude;

        float coefficient = (float) (consumptionHigh-consumptionLow)/(altitudeHigh-altitudeLow);
        int adjustment    = Math.round((value-altitudeLow)*coefficient);
        return adjustment;
    }

    private static class FuelData {
        private int altitude;       // feet
        private int consumption;    // lb
        public FuelData(int altitude, int consumption) {
            this.altitude = altitude;
            this.consumption = consumption;
        }
    }

    private static Comparator<FuelData> altitudeComparator = new Comparator<FuelData>() {
        @Override
        public int compare(FuelData f1, FuelData f2) {
            return Integer.compare(f1.altitude, f2.altitude);
        }
    };

    static public FuelData[] ascentData = {
        new FuelData(0, 0),
        new FuelData(5000, 280),
        new FuelData(10000, 370),
        new FuelData(15000, 460),
        new FuelData(20000, 550),
        new FuelData(25000, 640)
    };

    static public FuelData[] descentData = {
        new FuelData(0, 0),
        new FuelData(5000, 5),
        new FuelData(10000, 20),
        new FuelData(15000, 40),
        new FuelData(20000, 70),
        new FuelData(25000, 90)
    };
}
