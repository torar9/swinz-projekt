package cz.osu.swinz.home.sensors;

import java.util.Random;

public class LightSensor
{
    public static boolean isLightOn()
    {
        Random rd = new Random();
        return rd.nextBoolean();
    }
}
