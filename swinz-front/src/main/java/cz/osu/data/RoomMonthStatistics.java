package cz.osu.data;

import java.math.BigDecimal;
import java.math.BigInteger;

public class RoomMonthStatistics
{
    private String roomName;
    private String month;
    private BigInteger averageDayLight;
    private BigDecimal daysHeaterOn;
    private BigDecimal powerConsumption;

    public RoomMonthStatistics(String roomName, String month, BigInteger averageDayLight, BigDecimal daysHeaterOn, BigDecimal powerConsumption)
    {
        this.roomName = roomName;
        this.month = month;
        this.averageDayLight = averageDayLight;
        this.daysHeaterOn = daysHeaterOn;
        this.powerConsumption = powerConsumption;
    }

    public String getMonth()
    {
        return month;
    }

    public BigInteger getAverageDayLight()
    {
        return averageDayLight;
    }

    public BigDecimal getDaysHeaterOn()
    {
        return daysHeaterOn;
    }
    

    public BigDecimal getPowerConsumption()
    {
        return powerConsumption;
    }

    public String getRoomName()
    {
        return roomName;
    }

    @Override
    public String toString()
    {
        return "RoomMonthStatistics{" +
                "roomName='" + roomName + '\'' +
                ", Month='" + month + '\'' +
                ", averageDayLight=" + averageDayLight +
                ", daysHeaterOn=" + daysHeaterOn +
                ", powerConsumption=" + powerConsumption +
                '}';
    }
}
