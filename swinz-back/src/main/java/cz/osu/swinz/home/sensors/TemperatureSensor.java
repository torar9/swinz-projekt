package cz.osu.swinz.home.sensors;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Random;

public class TemperatureSensor
{
    private static double MIN_TEMP = 17.0;
    private static double MAX_TEMP = 30.0;

    public static double readTemperature()
    {
        Random rd = new Random();

        double tmp = MIN_TEMP + (MAX_TEMP - MIN_TEMP) * rd.nextDouble();
        BigDecimal bd = BigDecimal.valueOf(tmp);
        bd = bd.setScale(2, RoundingMode.HALF_UP);

        return bd.doubleValue();
    }
}
