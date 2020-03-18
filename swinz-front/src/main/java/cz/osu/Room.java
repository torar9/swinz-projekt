package cz.osu;

public class Room
{
    private int id;
    private String name;
    private boolean heaterState;
    private GroupReport report;
    private double targetTemperature;

    public Room(int id, String name, boolean heaterState, double targetTemperature)
    {
        this.id = id;
        this.name = name;
        this.heaterState = heaterState;
        this.targetTemperature = targetTemperature;
        report = null;
    }

    public int getId()
    {
        return id;
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

    public void setId(int id)
    {
        this.id = id;
    }

    public GroupReport getReport()
    {
        return report;
    }

    public void setReport(GroupReport report)
    {
        this.report = report;
    }

    public double getTargetTemperature()
    {
        return targetTemperature;
    }

    public void setTargetTemperature(double targetTemperature)
    {
        this.targetTemperature = targetTemperature;
    }

    @Override
    public String toString()
    {
        return "Room{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", heaterState=" + heaterState +
                ", report=" + report +
                ", targetTemperature=" + targetTemperature +
                '}';
    }
}
