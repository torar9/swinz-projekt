package cz.osu.swinz.home;

public class RoomHistoryByMonth
{
    private String Month;
    private double averageDayLight;
    private double daysHeaterOn;
    private double averageDayLightTwoWeeks;
    private double powerConsumption;

    public RoomHistoryByMonth(String month, double averageDayLight, double daysHeaterOn, double averageDayLightTwoWeeks, double powerConsumption)
    {
        Month = month;
        this.averageDayLight = averageDayLight;
        this.daysHeaterOn = daysHeaterOn;
        this.averageDayLightTwoWeeks = averageDayLightTwoWeeks;
        this.powerConsumption = powerConsumption;
    }

    public String getMonth()
    {
        return Month;
    }

    public double getAverageDayLight()
    {
        return averageDayLight;
    }

    public double getDaysHeaterOn()
    {
        return daysHeaterOn;
    }

    public double getAverageDayLightTwoWeeks()
    {
        return averageDayLightTwoWeeks;
    }

    public double getPowerConsumption()
    {
        return powerConsumption;
    }
}
