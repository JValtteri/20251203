package com.ojala;

import static org.junit.Assert.assertEquals;

import org.junit.Test;


public class AppTest
{
    /*
     * Esimerkki 1
     */
    @Test
    public void esimerkki1() {
        int startAltitude = 5000;
        int endAltitude = 15000;
        int expect = 180;
        int consumption = App.calculateConsumption(startAltitude, endAltitude);
        assertEquals(expect, consumption);
    }

    /*
     * Esimerkki 2
     */
    @Test
    public void esimerkki2() {
        int startAltitude = 1000;
        int endAltitude = 0;
        int expect = 1;
        int consumption = App.calculateConsumption(startAltitude, endAltitude);
        assertEquals(expect, consumption);
    }

    /*
     * Esimerkki 3
     */
    @Test
    public void esimerkki3() {
        int startAltitude = 1000;
        int endAltitude = -1000;
        int expect = 2;
        int consumption = App.calculateConsumption(startAltitude, endAltitude);
        assertEquals(expect, consumption);
    }

    /*
     * Esimerkki 4
     */
    @Test
    public void esimerkki4() {
        int startAltitude = 0;
        int endAltitude = 30000;
        int expect = 730;
        int consumption = App.calculateConsumption(startAltitude, endAltitude);
        assertEquals(expect, consumption);
    }

    /*
     * Extra tests
     */

    @Test
    public void climbInterpolation() {
        int startAltitude = 12000;
        int endAltitude = 24000;
        int expect = 216;
        int consumption = App.calculateConsumption(startAltitude, endAltitude);
        assertEquals(expect, consumption);
    }

    @Test
    public void climbMinRangeExtrapolation() {
        int startAltitude = -1000;
        int endAltitude = 10000;
        int expect = 426;
        int consumption = App.calculateConsumption(startAltitude, endAltitude);
        assertEquals(expect, consumption);
    }

    @Test
    public void descentMaxRangeExtrapolation() {
        int startAltitude = 27000;
        int endAltitude = 10000;
        int expect = 78;
        int consumption = App.calculateConsumption(startAltitude, endAltitude);
        assertEquals(expect, consumption);
    }
}
