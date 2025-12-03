package com.ojala;

import java.util.Arrays;
import java.util.Comparator;

public class App
{
    public static void main( String[] args )
    {
        int startAltitude = 0;
        int endAltitude   = 0;

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
        calculateConsumption(startAltitude, endAltitude);
    }

    private static void calculateConsumption(int startAltitude, int endAltitude) {
        int consumption   = 0;
        if (startAltitude < endAltitude) {
            consumption = calculateAscent(startAltitude, endAltitude);
        } else if (startAltitude > endAltitude) {
            consumption = calculateDescent(startAltitude, endAltitude);
        }
        System.out.println( String.format("Consumption:", consumption, "lb")  );
    }

    private static int calculateAscent(int startAltitude, int endAltitude) {
        int startConsumption = interpolateConsumption(startAltitude, ascentData);
        int endConsumption = interpolateConsumption(endAltitude, ascentData);
        return endConsumption-startConsumption;
    }

    private static int calculateDescent(int startAltitude, int endAltitude) {
        int startConsumption = interpolateConsumption(startAltitude, descentData);
        int endConsumption = interpolateConsumption(endAltitude, descentData);
        int consumption = endConsumption-startConsumption;
        if (consumption < 0) {
            return 0;
        }
        return consumption;
    }

    private static int interpolateConsumption(int value, FuelData[] data) {
        int index = searchValueIndex(value, data);
        if (index > -1) {
            return data[index].consumption;
        }
        int insersionPoint = (-index-1);
        if (insersionPoint == data.length) {
            int consumptionHigh = data[insersionPoint].consumption;
            int consumptionLow  = data[insersionPoint-1]  .consumption;
            int altitudeHigh    = data[insersionPoint].altitude;
            int altitudeLow     = data[insersionPoint-1]  .altitude;

            int coefficient = (consumptionHigh-consumptionLow)/(altitudeHigh-altitudeLow);
            int baseConsumption = consumptionHigh;
            int interpotationAdjustment = (value-altitudeLow)*coefficient;

            return baseConsumption + interpotationAdjustment;
        } else {
            int consumptionHigh = data[insersionPoint+1].consumption;
            int consumptionLow  = data[insersionPoint]  .consumption;
            int altitudeHigh    = data[insersionPoint+1].altitude;
            int altitudeLow     = data[insersionPoint]  .altitude;

            int coefficient = (consumptionHigh-consumptionLow)/(altitudeHigh-altitudeLow);
            int baseConsumption = consumptionLow;
            int interpotationAdjustment = (value-altitudeLow)*coefficient;

            return baseConsumption + interpotationAdjustment;
        }
    }

    private static int searchValueIndex(int value, FuelData[] data) {
        FuelData key = new FuelData(value, 0);
        return Arrays.binarySearch(data, key, altitudeComparator);
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

    static FuelData[] ascentData = {
        new FuelData(0, 640),
        new FuelData(5, 280),
        new FuelData(10, 370),
        new FuelData(15, 460),
        new FuelData(20, 550),
        new FuelData(25, 640)
    };

    static FuelData[] descentData = {
        new FuelData(0, 0),
        new FuelData(5, 5),
        new FuelData(10, 20),
        new FuelData(15, 40),
        new FuelData(20, 70),
        new FuelData(25, 90)
    };
}
