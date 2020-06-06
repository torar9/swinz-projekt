package cz.osu.swinz.Controllers;

import cz.osu.swinz.database.Room;
import cz.osu.swinz.database.RoomRepository;
import cz.osu.swinz.home.statistics.RoomStats;
import cz.osu.swinz.home.statistics.RoomStatsGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@EnableScheduling
@RestController
public class StatisticsController
{
    @PersistenceContext
    private EntityManager ent;
    @Autowired
    private RoomRepository roomRepo;

    private RoomStatsGenerator gen;
    private List<RoomStats> statList;

    @PostConstruct
    private void init()
    {
        gen = new RoomStatsGenerator(ent);
        reloadCachedStatistics();
    }

    @GetMapping(path="/groups/stats")
    public @ResponseBody ResponseEntity<Iterable<RoomStats>> getRoomStats()
    {
        return ResponseEntity.ok(statList);
    }

    @GetMapping(path="/groups/{id}/stats/lightWeeks")
    public @ResponseBody ResponseEntity<Double> getMonthLightStats(@PathVariable int id)
    {
        if(roomRepo.findById(id).isPresent())
        {
            Room room = roomRepo.findById(id).get();
            return ResponseEntity.ok(gen.getAverageLightTwoWeeks(room));
        }
        else return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @GetMapping(path="/groups/stats/heater")
    public @ResponseBody ResponseEntity<Integer> getHeaterInYear()
    {
        return ResponseEntity.ok(gen.getHeatDaysInYear());
    }

    @Scheduled(cron = "0 0 0 * * *")//Cron, uložení statistik každý den
    private void reloadCachedStatistics()
    {
        statList = gen.getMonthStats(roomRepo.findAll());
    }

    @ExceptionHandler({Exception.class})
    public ResponseEntity<Void> handleException()
    {
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
