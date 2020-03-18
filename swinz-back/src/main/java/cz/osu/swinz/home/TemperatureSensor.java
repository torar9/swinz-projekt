package cz.osu.swinz.home;

import java.util.Random;

public class TemperatureSensor
{
    public static double readTemperature()
    {
        Random rd = new Random();
        return rd.nextDouble();
    }
}
