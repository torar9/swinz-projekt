package cz.osu.swinz;

import cz.osu.swinz.database.HouseRepository;
import cz.osu.swinz.database.Room;
import cz.osu.swinz.database.RoomReportRepository;
import cz.osu.swinz.database.RoomRepository;
import cz.osu.swinz.home.RoomStats;
import cz.osu.swinz.home.RoomStatsGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@EnableScheduling
@RestController
public class StatisticsController
{
    @PersistenceContext
    EntityManager ent;
    @Autowired
    private RoomRepository roomRepo;
    @Autowired
    private RoomReportRepository reportRepo;
    @Autowired
    private HouseRepository houseRepo;

    @GetMapping(path="/groups/stats")
    public @ResponseBody Iterable<RoomStats> getRoomStats()
    {
        RoomStatsGenerator gen = new RoomStatsGenerator(ent);
        return gen.getMonthStats(roomRepo.findAll());
    }

    @GetMapping(path="/groups/{id}/stats/lightWeeks")
    public @ResponseBody double getMonthStats(@PathVariable int id)
    {
        if(roomRepo.findById(id).isPresent())
        {
            Room room = roomRepo.findById(id).get();
            RoomStatsGenerator gen = new RoomStatsGenerator(ent);

            return gen.getAverageLightTwoWeeks(room);
        }
        else return -1;
    }

    @GetMapping(path="/groups/stats/heater")
    public @ResponseBody int getHeaterInYear()
    {
        RoomStatsGenerator gen = new RoomStatsGenerator(ent);

        return gen.getHeatDaysInYear();
    }

    @ExceptionHandler({Exception.class})
    public void handleException()
    {
        System.out.println("Exception!");
    }
}
