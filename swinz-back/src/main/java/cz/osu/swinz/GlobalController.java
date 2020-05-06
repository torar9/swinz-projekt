package cz.osu.swinz;

import cz.osu.swinz.database.*;
import cz.osu.swinz.home.sensors.LightSensor;
import cz.osu.swinz.home.sensors.PowerConsumptionSensor;
import cz.osu.swinz.home.sensors.TemperatureSensor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@EnableScheduling
@RestController
public class GlobalController
{
    @PersistenceContext
    EntityManager ent;
    @Autowired
    private RoomRepository roomRepo;
    @Autowired
    private RoomReportRepository reportRepo;
    @Autowired
    private HouseRepository houseRepo;

    @GetMapping(path="/groups/globalHeater")
    public @ResponseBody
    boolean getGlovalHeaterState() throws Exception
    {
        for(House h : houseRepo.findAll())
        {
            return h.isHeaterOn();
        }

        throw new Exception("Unable to find room");
    }

    @PostMapping(path="/groups/globalHeater")
    public @ResponseBody boolean setGlovalHeaterState(@RequestParam boolean state) throws Exception
    {
        for(House h : houseRepo.findAll())
        {
            h.setHeaterOn(state);
            houseRepo.save(h);
            setHeaters(state);

            return true;
        }

        throw new Exception("Unable to find room");
    }

    private void setHeaters(boolean state)
    {
        for (Room r : roomRepo.findAll())
        {
            r.setHeaterState(state);
            roomRepo.save(r);
        }
    }

    @PostMapping(path="/groups/globalTemp")
    public @ResponseBody void setGlobalTemp(@RequestParam double temp)
    {
        for(House h : houseRepo.findAll())
        {
            h.setTargetTemperature(temp);

            for(Room e : roomRepo.findAll())
            {
                e.setTargetTemperature(temp);
                roomRepo.save(e);
                houseRepo.save(e.getHouse());
            }
        }
    }

    @GetMapping(path="/groups/globalTemp")
    public @ResponseBody double getGlobalTemp() throws Exception
    {
        for(House e : houseRepo.findAll())
        {
            return e.getTargetTemperature();
        }

        throw new Exception("Unable to find house");
    }

    @ExceptionHandler({Exception.class})
    public void handleException()
    {
        System.out.println("Exception!");
    }

    @Scheduled(fixedDelay=60000)//Automaticke ukladani databaze po x milisekundách
    private void saveReports()
    {
        for(Room e : roomRepo.findAll())
        {
            RoomReport rep = new RoomReport(TemperatureSensor.readTemperature(), PowerConsumptionSensor.readPowerConsumption(), e.getHeaterState(), LightSensor.isLightOn(), e);
            reportRepo.save(rep);
        }
    }

    @Scheduled(fixedDelay=1000)
    private void checkTemps()
    {
        for(House h : houseRepo.findAll())
        {
            for(Room e : roomRepo.findAll())
            {
                if(!h.isHeaterOn())
                {
                    double tmp = TemperatureSensor.readTemperature();

                    if(e.isForceHeater())
                    {
                        e.setHeaterState(true);
                    }

                    if(tmp < e.getTargetTemperature())
                    {
                        e.setHeaterState(true);
                    }
                    else if(!e.isForceHeater() && (tmp > e.getTargetTemperature()))
                    {
                        e.setHeaterState(false);
                    }
                    roomRepo.save(e);
                }
            }
        }
    }
}
