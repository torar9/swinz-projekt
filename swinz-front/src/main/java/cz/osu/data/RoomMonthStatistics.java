package cz.osu.data;

import java.math.BigDecimal;
import java.math.BigInteger;

public class RoomMonthStatistics
{
    private String roomName;
    private BigInteger averageDayLight;
    private BigDecimal daysHeaterOn;
    private BigDecimal powerConsumption;

    public RoomMonthStatistics(String roomName, BigInteger averageDayLight, BigDecimal daysHeaterOn, BigDecimal powerConsumption)
    {
        this.roomName = roomName;
        this.averageDayLight = averageDayLight;
        this.daysHeaterOn = daysHeaterOn;
        this.powerConsumption = powerConsumption;
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
                ", averageDayLight=" + averageDayLight +
                ", daysHeaterOn=" + daysHeaterOn +
                ", powerConsumption=" + powerConsumption +
                '}';
    }
}
