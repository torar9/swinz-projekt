package cz.osu.data;

public class GroupReport
{
    private double temp;
    private double powerConsumption;
    private boolean lightStatus;
    private boolean heaterState;

    public GroupReport(double temp, double powerConsumption, boolean lightStatus, boolean heaterState)
    {
        this.temp = temp;
        this.powerConsumption = powerConsumption;
        this.lightStatus = lightStatus;
        this.heaterState = heaterState;
    }

    public double getTemp()
    {
        return temp;
    }

    public double getPowerConsumption()
    {
        return powerConsumption;
    }

    public boolean isHeaterState()
    {
        return heaterState;
    }

    public boolean isLightStatus()
    {
        return lightStatus;
    }

    @Override
    public String toString()
    {
        return "GroupReport{" +
                "temp=" + temp +
                ", powerConsumption=" + powerConsumption +
                ", lightStatus=" + lightStatus +
                ", heaterState=" + heaterState +
                '}';
    }
}
