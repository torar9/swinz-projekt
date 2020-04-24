package cz.osu.data;

import java.util.List;

public class RoomStats
{
    private String monthName;
    private List<RoomMonthStatistics> list;

    public RoomStats(String monthName, List<RoomMonthStatistics> list)
    {
        this.monthName = monthName;
        this.list = list;
    }

    public String getMonthName()
    {
        return monthName;
    }

    public List<RoomMonthStatistics> getList()
    {
        return list;
    }

    @Override
    public String toString()
    {
        return "RoomStats{" +
                "monthName='" + monthName + '\'' +
                ", list=" + list +
                '}';
    }
}
