package cz.osu.data;

import java.util.List;

public class RoomStats
{
    private String roomName;
    private List<RoomMonthStatistics> list;

    public RoomStats(String roomName, List<RoomMonthStatistics> list)
    {
        this.roomName = roomName;
        this.list = list;
    }

    public String getRoomName()
    {
        return roomName;
    }

    public List<RoomMonthStatistics> getList()
    {
        return list;
    }

    @Override
    public String toString()
    {
        return "RoomStats{" +
                "roomName='" + roomName + '\'' +
                ", list=" + list +
                '}';
    }
}
