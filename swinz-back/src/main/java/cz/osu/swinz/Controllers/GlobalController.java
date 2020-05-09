package cz.osu.swinz.Controllers;

import cz.osu.swinz.database.*;
import cz.osu.swinz.home.sensors.LightSensor;
import cz.osu.swinz.home.sensors.PowerConsumptionSensor;
import cz.osu.swinz.home.sensors.TemperatureSensor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;

@EnableScheduling
@RestController
public class GlobalController
{
    @Autowired
    private RoomRepository roomRepo;
    @Autowired
    private RoomReportRepository reportRepo;
    @Autowired
    private HouseRepository houseRepo;

    @GetMapping(path="/groups/globalHeater")
    public @ResponseBody ResponseEntity<Boolean> getGlovalHeaterState()
    {
        for(House h : houseRepo.findAll())
        {
            return ResponseEntity.ok(h.isHeaterOn());
        }

        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PostMapping(path="/groups/globalHeater")
    public @ResponseBody ResponseEntity<Boolean> setGlovalHeaterState(@RequestParam boolean state)
    {
        for(House h : houseRepo.findAll())
        {
            h.setHeaterOn(state);
            houseRepo.save(h);
            setHeaters(state);

            return ResponseEntity.ok(true);
        }

        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
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
    public @ResponseBody ResponseEntity<Void> setGlobalTemp(@RequestParam double temp)
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

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(path="/groups/globalTemp")
    public @ResponseBody ResponseEntity<Double> getGlobalTemp()
    {
        for(House e : houseRepo.findAll())
        {
            return ResponseEntity.ok(e.getTargetTemperature());
        }

        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @Scheduled(fixedDelay=60000)//Automaticke ukladani databaze po x milisekundách
    private ResponseEntity<Void> saveReports()
    {
        for(Room e : roomRepo.findAll())
        {
            RoomReport rep = new RoomReport(TemperatureSensor.readTemperature(), PowerConsumptionSensor.readPowerConsumption(), e.getHeaterState(), LightSensor.isLightOn(), e);
            reportRepo.save(rep);
        }

        return new ResponseEntity<>(HttpStatus.OK);
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

    @ExceptionHandler({Exception.class})
    public ResponseEntity<Void> handleException()
    {
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
