package cz.osu.swinz;

import cz.osu.swinz.database.*;
import cz.osu.swinz.home.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
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

    @GetMapping(path="/test")
    public @ResponseBody String getStatistics()
    {
        //Object averageConsumption = ent.createNativeQuery("Select avg(power_consumption) from room_reports").getResultList().get(0);

        return "F";
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

    @PostMapping(path="/groups/globalTemp")
    public @ResponseBody boolean setGlobalTemp(@RequestParam double temp)
    {
        for(Room e : roomRepo.findAll())
        {
            e.setTargetTemperature(temp);
            roomRepo.save(e);
            houseRepo.save(e.getHouse());
            return true;
        }

        return false;
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
    {//int id, String name, double temp, double powerConsumption, boolean lightOn
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
    {//int id, String name, double temp, double powerConsumption, boolean lightOn
        if(roomRepo.findById(id).isPresent())
        {
            Room r = roomRepo.findById(id).get();

            return new GroupReport(TemperatureSensor.readTemperature(), PowerConsumptionSensor.readPowerConsumption(), LightSensor.isLightOn(), r.getHeaterState());
        }

        throw new Exception("Unable to find room");
    }

    @GetMapping(path="/groups/{id}/temp")
    public @ResponseBody double getRoomTemperature(@PathVariable int id) throws Exception
    {//int id, String name, double temp, double powerConsumption, boolean lightOn
        if(roomRepo.findById(id).isPresent())
        {
            return TemperatureSensor.readTemperature();
        }

        throw new Exception("Unable to find room");
    }

    @GetMapping(path="/groups/{id}/targetTemp")
    public @ResponseBody double getRoomTargetTemperature(@PathVariable int id) throws Exception
    {//int id, String name, double temp, double powerConsumption, boolean lightOn
        if(roomRepo.findById(id).isPresent())
        {
            return roomRepo.findById(id).get().getTargetTemperature();
        }

        throw new Exception("Unable to find room");
    }

    @PostMapping(path="/groups/{id}/targetTemp")
    public @ResponseBody boolean setRoomTargetTemperature(@PathVariable int id, @RequestParam double temp) throws Exception
    {//int id, String name, double temp, double powerConsumption, boolean lightOn
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
    {//int id, String name, double temp, double powerConsumption, boolean lightOn
        if(roomRepo.findById(id).isPresent())
        {
            return roomRepo.findById(id).get().getHeaterState();
        }

        throw new Exception("Unable to find room");
    }

    @PostMapping(path="/groups/{id}/heater")
    public @ResponseBody boolean setRoomHeater(@PathVariable int id, @RequestParam boolean state) throws Exception
    {//int id, String name, double temp, double powerConsumption, boolean lightOn
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
    {//int id, String name, double temp, double powerConsumption, boolean lightOn
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
    {//int id, String name, double temp, double powerConsumption, boolean lightOn
        if(roomRepo.findById(id).isPresent())
        {
            return roomRepo.findById(id).get().isForceHeater();
        }

        throw new Exception("Unable to find room");
    }

    @GetMapping(path="/groups/{id}/power")
    public @ResponseBody double getRoomPowerConsumption(@PathVariable int id) throws Exception
    {//int id, String name, double temp, double powerConsumption, boolean lightOn
        if(roomRepo.findById(id).isPresent())
        {
            return PowerConsumptionSensor.readPowerConsumption();
        }

        throw new Exception("Unable to find room");
    }

    @GetMapping(path="/groups/globalHeater")
    public @ResponseBody boolean getGlovalHeaterState() throws Exception
    {//int id, String name, double temp, double powerConsumption, boolean lightOn
        for(House h : houseRepo.findAll())
        {
            return h.isHeaterOn();
        }

        throw new Exception("Unable to find room");
    }

    @PostMapping(path="/groups/globalHeater")
    public @ResponseBody boolean setGlovalHeaterState(@RequestParam boolean state) throws Exception
    {//int id, String name, double temp, double powerConsumption, boolean lightOn
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

    @Scheduled(fixedDelay=60000)//Automaticke ukladani databaze po x milisekundách
    private void saveReports()
    {
        for(Room e : roomRepo.findAll())
        {//double temperature, double powerConsumption, boolean isLightOn, Room room
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

    /*
    @ExceptionHandler({Exception.class})
    public void handleException()
    {
        System.out.println("Exception!");
    }*/
}
