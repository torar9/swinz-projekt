package cz.osu.swinz.database;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "rooms")
public class Room
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private boolean heaterState = false;
    private boolean forceHeater = false;
    private double targetTemperature = 20.0;

    @OneToMany(mappedBy = "room", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<RoomReport> report;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "house_id", nullable = false)
    private House house;

    public Room()
    {
    }

    public Room(String name, House house)
    {
        this.name = name;
        this.house = house;
    }

    public Room(String name, boolean heaterState, double targetTemperature, House house)
    {
        this.name = name;
        this.heaterState = heaterState;
        this.targetTemperature = targetTemperature;
        this.house = house;
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public boolean getHeaterState()
    {
        return heaterState;
    }

    public void setHeaterState(boolean heaterState)
    {
        this.heaterState = heaterState;
    }

    public double getTargetTemperature()
    {
        return targetTemperature;
    }

    public void setTargetTemperature(double targetTemperature)
    {
        this.targetTemperature = targetTemperature;
    }

    public House getHouse()
    {
        return house;
    }

    public boolean isForceHeater()
    {
        return forceHeater;
    }

    public void setForceHeater(boolean forceHeater)
    {
        this.forceHeater = forceHeater;
    }

    /*
    @Override
    public String toString()
    {
        return "Room{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", heaterState=" + heaterState +
                ", targetTemperature=" + targetTemperature +
                ", house=" + house +
                '}';
    }*/

    @Override
    public String toString()
    {
        return "Room{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", heaterState=" + heaterState +
                ", forceHeater=" + forceHeater +
                ", targetTemperature=" + targetTemperature +
                ", house=" + house +
                '}';
    }
}
