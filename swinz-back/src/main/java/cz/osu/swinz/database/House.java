package cz.osu.swinz.database;

import javax.persistence.*;
import java.util.Set;

@Entity
public class House
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private double targetTemperature = 20.0;
    private boolean isHeaterOn= false;

    @OneToMany(mappedBy = "house", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Room> rooms;

    public House()
    {
    }

    public int getId()
    {
        return id;
    }

    public double getTargetTemperature()
    {
        return targetTemperature;
    }

    public void setTargetTemperature(double targetTemperature)
    {
        this.targetTemperature = targetTemperature;
    }

    public boolean isHeaterOn()
    {
        return isHeaterOn;
    }

    public boolean setHeaterOn(boolean heaterOn)
    {
        isHeaterOn = heaterOn;
        return heaterOn;
    }

    @Override
    public String toString()
    {
        return "House{" +
                "id=" + id +
                ", targetTemperature=" + targetTemperature +
                ", isHeaterOn=" + isHeaterOn +
                '}';
    }
}
