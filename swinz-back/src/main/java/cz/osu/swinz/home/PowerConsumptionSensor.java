package cz.osu.swinz.home;

import java.util.Random;

public class PowerConsumptionSensor
{
    public static double readPowerConsumption()
    {
        Random rd = new Random();
        return rd.nextDouble();
    }
}
