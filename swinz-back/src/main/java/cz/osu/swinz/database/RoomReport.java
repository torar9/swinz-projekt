package cz.osu.swinz.database;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "room_reports")
public class RoomReport
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private double temperature;
    private double powerConsumption;
    private boolean isLightOn;

    @CreationTimestamp
    private LocalDateTime reportDate;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "room_id", nullable = false)
    private Room room;

    public RoomReport()
    {
    }

    public RoomReport(double temperature, double powerConsumption, boolean isLightOn, Room room)
    {
        this.temperature = temperature;
        this.powerConsumption = powerConsumption;
        this.isLightOn = isLightOn;
        this.room = room;
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public Room getRoom()
    {
        return room;
    }

    public void setRoom(Room room)
    {
        this.room = room;
    }

    public double getTemperature()
    {
        return temperature;
    }

    public void setTemperature(double temperature)
    {
        this.temperature = temperature;
    }

    public double getPowerConsumption()
    {
        return powerConsumption;
    }

    public void setPowerConsumption(double powerConsumption)
    {
        this.powerConsumption = powerConsumption;
    }

    public boolean isLightOn()
    {
        return isLightOn;
    }

    public void setLightOn(boolean lightOn)
    {
        isLightOn = lightOn;
    }

    public LocalDateTime getReportDate()
    {
        return reportDate;
    }

    public void setReportDate(LocalDateTime reportDate)
    {
        this.reportDate = reportDate;
    }

    @Override
    public String toString()
    {
        return "RoomReport{" +
                "id=" + id +
                ", temperature=" + temperature +
                ", powerConsumption=" + powerConsumption +
                ", isLightOn=" + isLightOn +
                ", reportDate=" + reportDate +
                ", room=" + room +
                '}';
    }
}
