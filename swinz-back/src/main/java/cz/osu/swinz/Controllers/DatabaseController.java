package cz.osu.swinz.Controllers;

import cz.osu.swinz.database.*;
import cz.osu.swinz.home.sensors.LightSensor;
import cz.osu.swinz.home.sensors.PowerConsumptionSensor;
import cz.osu.swinz.home.sensors.TemperatureSensor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;

@ConditionalOnProperty(value = "app.scheduling.enable", havingValue = "true", matchIfMissing = true)
@EnableScheduling
@RestController
public class DatabaseController
{
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

    @Scheduled(fixedDelay=60000)//Automaticke ukladani databaze po x milisekundách
    private void saveReports()
    {
        for(Room e : roomRepo.findAll())
        {
            RoomReport rep = new RoomReport(TemperatureSensor.readTemperature(), PowerConsumptionSensor.readPowerConsumption(), e.getHeaterState(), LightSensor.isLightOn(), e);
            reportRepo.save(rep);
        }
    }

    @ExceptionHandler({Exception.class})
    public ResponseEntity<Void> handleException()
    {
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
