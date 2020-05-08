package cz.osu.swinz.Controllers;

import cz.osu.swinz.database.*;
import cz.osu.swinz.home.*;
import cz.osu.swinz.home.sensors.LightSensor;
import cz.osu.swinz.home.sensors.PowerConsumptionSensor;
import cz.osu.swinz.home.sensors.TemperatureSensor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.util.Optional;

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

    @GetMapping(path="/status")
    public @ResponseBody boolean isAlive()
    {
        return true;
    }

    @PostMapping(path="/groups/add")
    public @ResponseBody boolean addNewRoom(@RequestParam String name) throws Exception//curl localhost:8080/groups/add -d name=JmenoMistnosti
    {
        for (House e : houseRepo.findAll())
        {
            Room r = new Room(name, e);
            roomRepo.save(r);

            return true;
        }

        throw new Exception("Unable to find house");
    }

    @PostMapping(path="/groups/{id}/remove")
    public @ResponseBody boolean removeRoom(@PathVariable int id) throws Exception//curl localhost:8080/groups/remove -d id=1
    {
        Room r = roomRepo.findById(id).orElseThrow(() -> new Exception("Unable to find room"));
        roomRepo.delete(r);

        return false;
    }

    @GetMapping(path="/groups/{id}")
    public @ResponseBody Room getRoom(@PathVariable int id) throws Exception
    {
        return roomRepo.findById(id).orElseThrow(() -> new Exception("Unable to find room"));
    }

    @GetMapping(path="/groups")
    public @ResponseBody Iterable<Room> getAllRooms()
    {
        return roomRepo.findAll();
    }

    @GetMapping(path="/groups/{id}/report")
    public @ResponseBody GroupReport getRoomReport(@PathVariable int id) throws Exception
    {
        Room r = roomRepo.findById(id).orElseThrow(() -> new Exception("Unable to find room"));;

        return new GroupReport(TemperatureSensor.readTemperature(), PowerConsumptionSensor.readPowerConsumption(), LightSensor.isLightOn(), r.getHeaterState());
    }

    @GetMapping(path="/groups/{id}/temp")
    public @ResponseBody double getRoomTemperature(@PathVariable int id) throws Exception
    {
        roomRepo.findById(id).orElseThrow(() -> new Exception("Unable to find room"));

        return TemperatureSensor.readTemperature();
    }

    @GetMapping(path="/groups/{id}/targetTemp")
    public @ResponseBody double getRoomTargetTemperature(@PathVariable int id) throws Exception
    {
        roomRepo.findById(id).orElseThrow(() -> new Exception("Unable to find room"));

        return roomRepo.findById(id).get().getTargetTemperature();
    }

    @PostMapping(path="/groups/{id}/targetTemp")
    public @ResponseBody boolean setRoomTargetTemperature(@PathVariable int id, @RequestParam double temp) throws Exception
    {
        Room r = roomRepo.findById(id).orElseThrow(() -> new Exception("Unable to find room"));
        r.setTargetTemperature(temp);
        roomRepo.save(r);

        return true;
    }

    @GetMapping(path="/groups/{id}/heater")
    public @ResponseBody boolean getRoomHeater(@PathVariable int id) throws Exception
    {
        return roomRepo.findById(id).orElseThrow(() -> new Exception("Unable to find room")).getHeaterState();
    }

    @PostMapping(path="/groups/{id}/heater")
    public @ResponseBody boolean setRoomHeater(@PathVariable int id, @RequestParam boolean state) throws Exception
    {
        Room r = roomRepo.findById(id).orElseThrow(() -> new Exception("Unable to find room"));
        r.setHeaterState(state);
        roomRepo.save(r);

        return true;
    }

    @PostMapping(path="/groups/{id}/heaterForce")
    public @ResponseBody boolean setRoomHeaterForced(@PathVariable int id, @RequestParam boolean state) throws Exception
    {
        Room r = roomRepo.findById(id).orElseThrow(() -> new Exception("Unable to find room"));
        r.setForceHeater(state);
        roomRepo.save(r);

        return true;
    }

    @GetMapping(path="/groups/{id}/heaterForce")
    public @ResponseBody boolean getRoomHeaterForce(@PathVariable int id) throws Exception
    {
        return roomRepo.findById(id).orElseThrow(() -> new Exception("Unable to find room")).isForceHeater();
    }

    @GetMapping(path="/groups/{id}/power")
    public @ResponseBody double getRoomPowerConsumption(@PathVariable int id) throws Exception
    {
        roomRepo.findById(id).orElseThrow(() -> new Exception("Unable to find room")).isForceHeater();

        return PowerConsumptionSensor.readPowerConsumption();
    }

    @ExceptionHandler({Exception.class})
    public void handleException()
    {
        System.out.println("Exception!");
    }
}
