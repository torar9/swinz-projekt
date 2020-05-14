package cz.osu.swinz.Controllers;

import cz.osu.swinz.database.*;
import cz.osu.swinz.home.*;
import cz.osu.swinz.home.sensors.LightSensor;
import cz.osu.swinz.home.sensors.PowerConsumptionSensor;
import cz.osu.swinz.home.sensors.TemperatureSensor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;

@EnableScheduling
@RestController
public class SensorGroupController
{
    @Autowired
    private RoomRepository roomRepo;
    @Autowired
    private HouseRepository houseRepo;

    @PostConstruct
    private void initDatabase()
    {
        if(houseRepo.count() == 0)
            houseRepo.save(new House());
    }

    @PostMapping(path="/groups/add")
    public @ResponseBody ResponseEntity<Boolean> addNewRoom(@RequestParam String name) throws Exception//curl localhost:8080/groups/add -d name=JmenoMistnosti
    {
        for (House e : houseRepo.findAll())
        {
            Room r = new Room(name, e);
            roomRepo.save(r);

            return ResponseEntity.ok(true);
        }

        return new ResponseEntity<>(false, HttpStatus.BAD_REQUEST);
    }

    @PostMapping(path="/groups/{id}/remove")
    public @ResponseBody ResponseEntity<Boolean> removeRoom(@PathVariable int id)//curl localhost:8080/groups/remove -d id=1
    {
        try
        {
            Room r = roomRepo.findById(id).orElseThrow(() -> new Exception("Unable to find room"));
            roomRepo.delete(r);

            return ResponseEntity.ok(true);
        }
        catch (Exception e)
        {
            return new ResponseEntity<>(false, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(path="/groups/{id}")
    public @ResponseBody ResponseEntity<Room> getRoom(@PathVariable int id)
    {
        try
        {
            return ResponseEntity.ok(roomRepo.findById(id).orElseThrow(() -> new Exception("Unable to find room")));
        }
        catch (Exception e)
        {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(path="/groups")
    public @ResponseBody ResponseEntity<Iterable<Room>> getAllRooms()
    {
        return ResponseEntity.ok(roomRepo.findAll());
    }

    @GetMapping(path="/groups/{id}/report")
    public ResponseEntity<GroupReport> getRoomReport(@PathVariable int id)
    {
        try
        {
            Room r = roomRepo.findById(id).orElseThrow(() -> new Exception());
            return ResponseEntity.ok(new GroupReport(TemperatureSensor.readTemperature(), PowerConsumptionSensor.readPowerConsumption(), LightSensor.isLightOn(), r.getHeaterState()));
        }
        catch (Exception e)
        {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(path="/groups/{id}/temp")
    public @ResponseBody ResponseEntity<Double> getRoomTemperature(@PathVariable int id)
    {
        try
        {
            roomRepo.findById(id).orElseThrow(() -> new Exception("Unable to find room"));

            return ResponseEntity.ok(TemperatureSensor.readTemperature());
        }
        catch (Exception e)
        {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(path="/groups/{id}/targetTemp")
    public @ResponseBody ResponseEntity<Double> getRoomTargetTemperature(@PathVariable int id)
    {
        try
        {
            roomRepo.findById(id).orElseThrow(() -> new Exception("Unable to find room"));

            return ResponseEntity.ok(roomRepo.findById(id).get().getTargetTemperature());
        }
        catch (Exception e)
        {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(path="/groups/{id}/targetTemp")
    public @ResponseBody ResponseEntity<Boolean> setRoomTargetTemperature(@PathVariable int id, @RequestParam double temp)
    {
        try
        {
            Room r = roomRepo.findById(id).orElseThrow(() -> new Exception("Unable to find room"));
            r.setTargetTemperature(temp);
            roomRepo.save(r);

            return ResponseEntity.ok(true);
        }
        catch (Exception e)
        {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(path="/groups/{id}/heater")
    public @ResponseBody ResponseEntity<Boolean> getRoomHeaterState(@PathVariable int id)
    {
        try
        {
            return ResponseEntity.ok(roomRepo.findById(id).orElseThrow(() -> new Exception("Unable to find room")).getHeaterState());
        }
        catch (Exception e)
        {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(path="/groups/{id}/heaterForced")
    public @ResponseBody ResponseEntity<Boolean> setRoomHeaterForcedState(@PathVariable int id, @RequestParam boolean state)
    {
        try
        {
            Room r = roomRepo.findById(id).orElseThrow(() -> new Exception("Unable to find room"));
            r.setForceHeater(state);
            roomRepo.save(r);

            return ResponseEntity.ok(true);
        }
        catch (Exception e)
        {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(path="/groups/{id}/heaterForced")
    public @ResponseBody ResponseEntity<Boolean> getRoomHeaterForcedState(@PathVariable int id)
    {
        try
        {
            return ResponseEntity.ok(roomRepo.findById(id).orElseThrow(() -> new Exception("Unable to find room")).isForceHeater());
        }
        catch (Exception e)
        {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(path="/groups/{id}/power")
    public @ResponseBody ResponseEntity<Double> getRoomPowerConsumption(@PathVariable int id)
    {
        try
        {
            roomRepo.findById(id).orElseThrow(() -> new Exception("Unable to find room")).isForceHeater();

            return ResponseEntity.ok(PowerConsumptionSensor.readPowerConsumption());
        }
        catch (Exception e)
        {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @ExceptionHandler({Exception.class})
    public ResponseEntity<Void> handleException()
    {
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
