package cz.osu.swinz.home;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Random;

public class PowerConsumptionSensor
{
    private static double MIN_CONSUMPTION = 0.0;
    private static double MAX_CONSUMPTION = 80.0;

    public static double readPowerConsumption()
    {
        Random rd = new Random();

        double pwr = MIN_CONSUMPTION + (MAX_CONSUMPTION - MIN_CONSUMPTION) * rd.nextDouble();
        BigDecimal bd = BigDecimal.valueOf(pwr);
        bd = bd.setScale(2, RoundingMode.HALF_UP);

        return bd.doubleValue();
    }
}
