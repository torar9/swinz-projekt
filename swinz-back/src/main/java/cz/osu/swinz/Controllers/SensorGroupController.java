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
import javax.persistence.*;
import java.util.Optional;

@EnableScheduling
@RestController
public class SensorGroupController
{
    @PersistenceContext
    EntityManager ent;
    @Autowired
    private RoomRepository roomRepo;
    @Autowired
    private RoomReportRepository reportRepo;
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
    public @ResponseBody boolean removeRoom(@PathVariable int id)//curl localhost:8080/groups/remove -d id=1
    {
        Optional<Room> r = roomRepo.findById(id);
        if(r.isPresent())
        {
            roomRepo.delete(r.get());
            return true;
        }

        return false;
    }

    @GetMapping(path="/groups/{id}")
    public @ResponseBody Room getRoom(@PathVariable int id) throws Exception
    {
        if(roomRepo.findById(id).isPresent())
        {
            return roomRepo.findById(id).get();
        }

        throw new Exception("Unable to find room");
    }

    @GetMapping(path="/groups")
    public @ResponseBody Iterable<Room> getAllRooms()
    {
        return roomRepo.findAll();
    }

    @GetMapping(path="/groups/{id}/report")
    public @ResponseBody GroupReport getRoomReport(@PathVariable int id) throws Exception
    {
        if(roomRepo.findById(id).isPresent())
        {
            Room r = roomRepo.findById(id).get();

            return new GroupReport(TemperatureSensor.readTemperature(), PowerConsumptionSensor.readPowerConsumption(), LightSensor.isLightOn(), r.getHeaterState());
        }

        throw new Exception("Unable to find room");
    }

    @GetMapping(path="/groups/{id}/temp")
    public @ResponseBody double getRoomTemperature(@PathVariable int id) throws Exception
    {
        if(roomRepo.findById(id).isPresent())
        {
            return TemperatureSensor.readTemperature();
        }

        throw new Exception("Unable to find room");
    }

    @GetMapping(path="/groups/{id}/targetTemp")
    public @ResponseBody double getRoomTargetTemperature(@PathVariable int id) throws Exception
    {
        if(roomRepo.findById(id).isPresent())
        {
            return roomRepo.findById(id).get().getTargetTemperature();
        }

        throw new Exception("Unable to find room");
    }

    @PostMapping(path="/groups/{id}/targetTemp")
    public @ResponseBody boolean setRoomTargetTemperature(@PathVariable int id, @RequestParam double temp) throws Exception
    {
        if(roomRepo.findById(id).isPresent())
        {
            roomRepo.findById(id).get().setTargetTemperature(temp);
            roomRepo.save(roomRepo.findById(id).get());
            return true;
        }

        throw new Exception("Unable to find room");
    }

    @GetMapping(path="/groups/{id}/heater")
    public @ResponseBody boolean getRoomHeater(@PathVariable int id) throws Exception
    {
        if(roomRepo.findById(id).isPresent())
        {
            return roomRepo.findById(id).get().getHeaterState();
        }

        throw new Exception("Unable to find room");
    }

    @PostMapping(path="/groups/{id}/heater")
    public @ResponseBody boolean setRoomHeater(@PathVariable int id, @RequestParam boolean state) throws Exception
    {
        if(roomRepo.findById(id).isPresent())
        {
            roomRepo.findById(id).get().setHeaterState(state);
            roomRepo.save(roomRepo.findById(id).get());
            return true;
        }

        throw new Exception("Unable to find room");
    }

    @PostMapping(path="/groups/{id}/heaterForce")
    public @ResponseBody boolean setRoomHeaterForced(@PathVariable int id, @RequestParam boolean state) throws Exception
    {
        if(roomRepo.findById(id).isPresent())
        {
            roomRepo.findById(id).get().setForceHeater(state);
            roomRepo.save(roomRepo.findById(id).get());
            return true;
        }

        throw new Exception("Unable to find room");
    }

    @GetMapping(path="/groups/{id}/heaterForce")
    public @ResponseBody boolean getRoomHeaterForce(@PathVariable int id) throws Exception
    {
        if(roomRepo.findById(id).isPresent())
        {
            return roomRepo.findById(id).get().isForceHeater();
        }

        throw new Exception("Unable to find room");
    }

    @GetMapping(path="/groups/{id}/power")
    public @ResponseBody
    double getRoomPowerConsumption(@PathVariable int id) throws Exception
    {
        if(roomRepo.findById(id).isPresent())
        {
            return PowerConsumptionSensor.readPowerConsumption();
        }

        throw new Exception("Unable to find room");
    }

    @ExceptionHandler({Exception.class})
    public void handleException()
    {
        System.out.println("Exception!");
    }
}
